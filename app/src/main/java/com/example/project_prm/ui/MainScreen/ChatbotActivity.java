

package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.ui.chatAI.ChatMessageAdapter;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView rvChatMessages;
    private TextInputEditText etChatInput;
    private MaterialButton btnSendMessage;
    private ChatMessageAdapter chatAdapter;
    private List<ChatMessageAdapter.ChatMessage> chatMessages;
    private OkHttpClient client;
    private static final String API_KEY = "AIzaSyDBly0AHgIqKPjoOSR91MnbbWZyy5DOliA"; // Kiểm tra lại khóa này
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize UI components
        rvChatMessages = findViewById(R.id.rv_chat_messages);
        etChatInput = findViewById(R.id.et_chat_input);
        btnSendMessage = findViewById(R.id.btn_send_message);
        chatMessages = new ArrayList<>();
        client = new OkHttpClient();

        // Setup RecyclerView
        rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatMessageAdapter(chatMessages);
        rvChatMessages.setAdapter(chatAdapter);

        // Handle send button
        btnSendMessage.setOnClickListener(v -> {
            String message = etChatInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatAdapter.addMessage(new ChatMessageAdapter.ChatMessage(message, true));
                rvChatMessages.scrollToPosition(chatMessages.size() - 1);

                sendMessageToGemini(message);
                etChatInput.setText("");
            }
        });
    }

    private String loadContextFromAssets() {
        try {
            InputStream is = getAssets().open("context.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder context = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                context.append(line).append("\n");
            }
            reader.close();
            return context.toString();
        } catch (IOException e) {
            return "Error loading context: " + e.getMessage();
        }
    }

    private void sendMessageToGemini(String message) {
        String context = loadContextFromAssets();
        // Đảm bảo JSON payload đúng cú pháp
        String jsonPayload = "{\"contents\": [{\"role\": \"user\", \"parts\": [{\"text\": \"" + context.replace("\"", "\\\"") + "\\nCâu hỏi: " + message.replace("\"", "\\\"") + "\"}]}]}";
        RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(GEMINI_API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    chatAdapter.addMessage(new ChatMessageAdapter.ChatMessage("Error: " + e.getMessage(), false));
                    rvChatMessages.scrollToPosition(chatMessages.size() - 1);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    String botResponse = parseGeminiResponse(responseBody);
                    runOnUiThread(() -> {
                        chatAdapter.addMessage(new ChatMessageAdapter.ChatMessage(botResponse, false));
                        rvChatMessages.scrollToPosition(chatMessages.size() - 1);
                    });
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No error details";
                    runOnUiThread(() -> {
                        chatAdapter.addMessage(new ChatMessageAdapter.ChatMessage("Error: API request failed with code " + response.code() + ": " + errorBody, false));
                        rvChatMessages.scrollToPosition(chatMessages.size() - 1);
                    });
                }
            }
        });
    }

    private String parseGeminiResponse(String responseBody) {
        try {
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                JsonObject candidate = candidates.get(0).getAsJsonObject();
                JsonObject content = candidate.getAsJsonObject("content");
                if (content != null) {
                    JsonArray parts = content.getAsJsonArray("parts");
                    if (parts != null && !parts.isEmpty()) {
                        return parts.get(0).getAsJsonObject().get("text").getAsString();
                    }
                }
            }
            return "No response text found";
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}
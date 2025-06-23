package com.example.project_prm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView rvChatMessages;
    private TextInputEditText etChatInput;
    private MaterialButton btnSendMessage;
    private ChatMessageAdapter chatAdapter;
    private List<ChatMessageAdapter.ChatMessage> chatMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize UI components
        rvChatMessages = findViewById(R.id.rv_chat_messages);
        etChatInput = findViewById(R.id.et_chat_input);
        btnSendMessage = findViewById(R.id.btn_send_message);
        chatMessages = new ArrayList<>();

        // Setup RecyclerView
        rvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatMessageAdapter(chatMessages);
        rvChatMessages.setAdapter(chatAdapter);

        // Handle send button
        btnSendMessage.setOnClickListener(v -> {
            String message = etChatInput.getText().toString().trim();
            if (!message.isEmpty()) {
                // Add user message
                chatAdapter.addMessage(new ChatMessageAdapter.ChatMessage(message, true));
                rvChatMessages.scrollToPosition(chatMessages.size() - 1);

                // Get chatbot response
                rvChatMessages.scrollToPosition(chatMessages.size() - 1);

                // Clear input
                etChatInput.setText("");
            }
        });
    }
}
package com.example.project_prm.ChatAIScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<ChatMessage> chatMessages;

    // Simple ChatMessage model class
    public static class ChatMessage {
        String message;
        boolean isUser;

        public ChatMessage(String message, boolean isUser) {
            this.message = message;
            this.isUser = isUser;
        }
    }


    public ChatMessageAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        if (message.isUser) {
            // User message (right)
            holder.tvUserMessage.setText(message.message);
            holder.tvUserMessage.setVisibility(View.VISIBLE);
            holder.tvBotMessage.setVisibility(View.GONE);
        } else {
            // Bot message (left)
            holder.tvBotMessage.setText(message.message);
            holder.tvBotMessage.setVisibility(View.VISIBLE);
            holder.tvUserMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        notifyItemInserted(chatMessages.size() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserMessage, tvBotMessage;

        ViewHolder(View itemView) {
            super(itemView);
            tvUserMessage = itemView.findViewById(R.id.tv_user_message);
            tvBotMessage = itemView.findViewById(R.id.tv_bot_message);
        }
    }
}
package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.ChatMessage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class ChatMessageDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, ChatMessage message) {
        batch.set(db.collection("chat_messages").document(), message.toMap());
    }

    public Task<Void> update(String messageId, ChatMessage message) {
        return db.collection("chat_messages").document(messageId).set(message.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String messageId) {
        return db.collection("chat_messages").document(messageId).delete();
    }

    public Task<DocumentSnapshot> getById(String messageId) {
        return db.collection("chat_messages").document(messageId).get();
    }
}

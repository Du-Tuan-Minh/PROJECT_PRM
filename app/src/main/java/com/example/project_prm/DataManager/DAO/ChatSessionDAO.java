package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.ChatSession;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class ChatSessionDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, String sessionId, ChatSession session) {
        batch.set(db.collection("chat_sessions").document(sessionId), session.toMap());
    }

    public Task<Void> update(String sessionId, ChatSession session) {
        return db.collection("chat_sessions").document(sessionId).set(session.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String sessionId) {
        return db.collection("chat_sessions").document(sessionId).delete();
    }

    public Task<DocumentSnapshot> getById(String sessionId) {
        return db.collection("chat_sessions").document(sessionId).get();
    }
}

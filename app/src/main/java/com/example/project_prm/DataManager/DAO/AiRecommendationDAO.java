package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.AiRecommendation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class AiRecommendationDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, AiRecommendation recommendation) {
        batch.set(db.collection("ai_recommendations").document(), recommendation.toMap());
    }

    public Task<Void> update(String id, AiRecommendation recommendation) {
        return db.collection("ai_recommendations").document(id).set(recommendation.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String id) {
        return db.collection("ai_recommendations").document(id).delete();
    }

    public Task<DocumentSnapshot> getById(String id) {
        return db.collection("ai_recommendations").document(id).get();
    }
}

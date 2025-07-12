package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.HealthLog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class HealthLogDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, HealthLog log) {
        batch.set(db.collection("health_logs").document(), log.toMap());
    }

    public Task<Void> update(String logId, HealthLog log) {
        return db.collection("health_logs").document(logId).set(log.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String logId) {
        return db.collection("health_logs").document(logId).delete();
    }

    public Task<DocumentSnapshot> getById(String logId) {
        return db.collection("health_logs").document(logId).get();
    }
}

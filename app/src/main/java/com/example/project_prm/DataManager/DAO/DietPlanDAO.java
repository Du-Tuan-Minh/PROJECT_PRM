package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.DietPlan;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class DietPlanDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, DietPlan plan) {
        batch.set(db.collection("diet_plans").document(), plan.toMap());
    }

    public Task<Void> update(String planId, DietPlan plan) {
        return db.collection("diet_plans").document(planId).set(plan.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String planId) {
        return db.collection("diet_plans").document(planId).delete();
    }

    public Task<DocumentSnapshot> getById(String planId) {
        return db.collection("diet_plans").document(planId).get();
    }
}

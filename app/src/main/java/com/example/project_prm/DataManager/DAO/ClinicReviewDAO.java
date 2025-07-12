package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.ClinicReview;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class ClinicReviewDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, ClinicReview review) {
        batch.set(db.collection("clinic_reviews").document(), review.toMap());
    }

    public Task<Void> update(String reviewId, ClinicReview review) {
        return db.collection("clinic_reviews").document(reviewId).set(review.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String reviewId) {
        return db.collection("clinic_reviews").document(reviewId).delete();
    }

    public Task<DocumentSnapshot> getById(String reviewId) {
        return db.collection("clinic_reviews").document(reviewId).get();
    }
}

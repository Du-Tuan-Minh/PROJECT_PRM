package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.DiseaseCategory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class DiseaseCategoryDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, String categoryId, DiseaseCategory category) {
        batch.set(db.collection("disease_categories").document(categoryId), category.toMap());
    }

    public Task<Void> add(String categoryId, DiseaseCategory category) {
        return db.collection("disease_categories").document(categoryId).set(category.toMap());
    }

    public Task<Void> update(String categoryId, DiseaseCategory category) {
        return db.collection("disease_categories").document(categoryId).update(category.toMap());
    }

    public Task<Void> delete(String categoryId) {
        return db.collection("disease_categories").document(categoryId).delete();
    }

    public Task<DocumentSnapshot> getById(String categoryId) {
        return db.collection("disease_categories").document(categoryId).get();
    }
}

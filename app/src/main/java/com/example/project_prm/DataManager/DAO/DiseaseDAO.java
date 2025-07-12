// DiseaseDAO.java
package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.Disease;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class DiseaseDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, String diseaseId, Disease disease) {
        batch.set(db.collection("diseases").document(diseaseId), disease.toMap());
    }

    public Task<Void> add(String diseaseId, Disease disease) {
        return db.collection("diseases").document(diseaseId).set(disease.toMap());
    }

    public Task<Void> update(String diseaseId, Disease disease) {
        return db.collection("diseases").document(diseaseId).update(disease.toMap());
    }

    public Task<Void> delete(String diseaseId) {
        return db.collection("diseases").document(diseaseId).delete();
    }

    public Task<DocumentSnapshot> getById(String diseaseId) {
        return db.collection("diseases").document(diseaseId).get();
    }
}

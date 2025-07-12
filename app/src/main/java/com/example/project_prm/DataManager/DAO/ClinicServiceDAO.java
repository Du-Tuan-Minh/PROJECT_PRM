package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.ClinicService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class ClinicServiceDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, ClinicService service) {
        batch.set(db.collection("clinic_services").document(), service.toMap());
    }

    public Task<Void> update(String serviceId, ClinicService service) {
        return db.collection("clinic_services").document(serviceId).set(service.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String serviceId) {
        return db.collection("clinic_services").document(serviceId).delete();
    }

    public Task<DocumentSnapshot> getById(String serviceId) {
        return db.collection("clinic_services").document(serviceId).get();
    }
}

package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.Clinic;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class ClinicDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Thêm bằng batch
    public void addWithBatch(WriteBatch batch, String clinicId, Clinic clinic) {
        batch.set(db.collection("clinics").document(clinicId), clinic.toMap());
    }

    // Cập nhật clinic
    public void update(String clinicId, Clinic clinic) {
        db.collection("clinics").document(clinicId).set(clinic.toMap());
    }

    // Xóa clinic
    public void delete(String clinicId) {
        db.collection("clinics").document(clinicId).delete();
    }

    // Lấy clinic theo ID
    public Task<DocumentSnapshot> getById(String clinicId) {
        return db.collection("clinics").document(clinicId).get();
    }



}

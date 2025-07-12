package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.MedicationSchedule;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class MedicationScheduleDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, MedicationSchedule medication) {
        batch.set(db.collection("medication_schedules").document(), medication.toMap());
    }

    public Task<Void> update(String scheduleId, MedicationSchedule medication) {
        return db.collection("medication_schedules").document(scheduleId).set(medication.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String scheduleId) {
        return db.collection("medication_schedules").document(scheduleId).delete();
    }

    public Task<DocumentSnapshot> getById(String scheduleId) {
        return db.collection("medication_schedules").document(scheduleId).get();
    }
}

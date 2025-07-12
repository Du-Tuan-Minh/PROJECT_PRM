package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class AppointmentDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, String appointmentId, Appointment appointment) {
        batch.set(db.collection("appointments").document(appointmentId), appointment.toMap());
    }

    public Task<Void> update(String appointmentId, Appointment appointment) {
        return db.collection("appointments").document(appointmentId).set(appointment.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String appointmentId) {
        return db.collection("appointments").document(appointmentId).delete();
    }

    public Task<DocumentSnapshot> getById(String appointmentId) {
        return db.collection("appointments").document(appointmentId).get();
    }
}

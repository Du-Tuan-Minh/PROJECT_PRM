package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.AppointmentReminder;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class AppointmentReminderDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, AppointmentReminder reminder) {
        batch.set(db.collection("appointment_reminders").document(), reminder.toMap());
    }

    public Task<Void> update(String reminderId, AppointmentReminder reminder) {
        return db.collection("appointment_reminders").document(reminderId).set(reminder.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String reminderId) {
        return db.collection("appointment_reminders").document(reminderId).delete();
    }

    public Task<DocumentSnapshot> getById(String reminderId) {
        return db.collection("appointment_reminders").document(reminderId).get();
    }
}

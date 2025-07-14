package com.example.project_prm.Repository;

import com.example.project_prm.Model.AppointmentModel;
import com.example.project_prm.Model.DoctorModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentRepository {
    private FirebaseFirestore db;
    private static final String COLLECTION_APPOINTMENTS = "appointments";

    public AppointmentRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void saveAppointment(AppointmentModel appointment, OnAppointmentSavedListener listener) {
        if (appointment.getId() == null || appointment.getId().isEmpty()) {
            appointment.setId(UUID.randomUUID().toString());
        }
        
        db.collection(COLLECTION_APPOINTMENTS)
                .document(appointment.getId())
                .set(appointment)
                .addOnSuccessListener(aVoid -> listener.onAppointmentSaved(appointment))
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void getAppointmentsByPatientId(String patientId, OnAppointmentsLoadedListener listener) {
        db.collection(COLLECTION_APPOINTMENTS)
                .whereEqualTo("patientId", patientId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentModel> appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        AppointmentModel appointment = document.toObject(AppointmentModel.class);
                        if (appointment != null) {
                            appointment.setId(document.getId());
                            appointments.add(appointment);
                        }
                    }
                    listener.onAppointmentsLoaded(appointments);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void getAppointmentsByDoctorId(String doctorId, OnAppointmentsLoadedListener listener) {
        db.collection(COLLECTION_APPOINTMENTS)
                .whereEqualTo("doctorId", doctorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentModel> appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        AppointmentModel appointment = document.toObject(AppointmentModel.class);
                        if (appointment != null) {
                            appointment.setId(document.getId());
                            appointments.add(appointment);
                        }
                    }
                    listener.onAppointmentsLoaded(appointments);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void updateAppointmentStatus(String appointmentId, String status, OnAppointmentUpdatedListener listener) {
        db.collection(COLLECTION_APPOINTMENTS)
                .document(appointmentId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> listener.onAppointmentUpdated())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void deleteAppointment(String appointmentId, OnAppointmentDeletedListener listener) {
        db.collection(COLLECTION_APPOINTMENTS)
                .document(appointmentId)
                .delete()
                .addOnSuccessListener(aVoid -> listener.onAppointmentDeleted())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public interface OnAppointmentSavedListener {
        void onAppointmentSaved(AppointmentModel appointment);
        void onError(String error);
    }

    public interface OnAppointmentsLoadedListener {
        void onAppointmentsLoaded(List<AppointmentModel> appointments);
        void onError(String error);
    }

    public interface OnAppointmentUpdatedListener {
        void onAppointmentUpdated();
        void onError(String error);
    }

    public interface OnAppointmentDeletedListener {
        void onAppointmentDeleted();
        void onError(String error);
    }

    // Static method for backward compatibility
    public static AppointmentRepository getInstance() {
        return new AppointmentRepository();
    }
} 
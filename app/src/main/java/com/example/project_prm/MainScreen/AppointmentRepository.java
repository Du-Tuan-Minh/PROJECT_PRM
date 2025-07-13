package com.example.project_prm.MainScreen;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {
    private static AppointmentRepository instance;
    private final List<AppointmentModel> appointments = new ArrayList<>();

    private AppointmentRepository() {}

    public static AppointmentRepository getInstance() {
        if (instance == null) instance = new AppointmentRepository();
        return instance;
    }

    public void addAppointment(AppointmentModel appointment) {
        appointments.add(appointment);
    }

    public List<AppointmentModel> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public void updateStatus(String id, String status) {
        for (AppointmentModel a : appointments) {
            if (a.getId().equals(id)) {
                a.setStatus(status);
                break;
            }
        }
    }

    public void saveReview(String appointmentId, int rating, String reviewText) {
        for (AppointmentModel a : appointments) {
            if (a.getId().equals(appointmentId)) {
                a.setReviewRating(rating);
                a.setReviewText(reviewText);
                a.setHasReview(true);
                break;
            }
        }
    }
} 
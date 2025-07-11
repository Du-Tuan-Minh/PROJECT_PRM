package com.example.project_prm.DataManager.Repository;

import android.content.Context;

import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.UserSessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentRepository {

    private AppointmentDAO appointmentDAO;
    private UserSessionManager sessionManager;
    private ExecutorService executor;

    public AppointmentRepository(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        appointmentDAO = new AppointmentDAO(dbHelper.getWritableDatabase());
        sessionManager = new UserSessionManager(context);
        executor = Executors.newFixedThreadPool(4);
    }

    // Interfaces for callbacks
    public interface OnAppointmentListCallback {
        void onSuccess(List<Appointment> appointments);
        void onError(String error);
    }

    public interface OnAppointmentCallback {
        void onSuccess(Appointment appointment);
        void onError(String error);
    }

    public interface OnActionCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    // Get upcoming appointments for current user
    public void getUpcomingAppointments(OnAppointmentListCallback callback) {
        executor.execute(() -> {
            try {
                int userId = sessionManager.getCurrentUserId();
                List<Appointment> appointments = appointmentDAO.getUpcomingAppointments(userId);
                callback.onSuccess(appointments);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Get completed appointments for current user
    public void getCompletedAppointments(OnAppointmentListCallback callback) {
        executor.execute(() -> {
            try {
                int userId = sessionManager.getCurrentUserId();
                List<Appointment> appointments = appointmentDAO.getCompletedAppointments(userId);
                callback.onSuccess(appointments);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Get cancelled appointments for current user
    public void getCancelledAppointments(OnAppointmentListCallback callback) {
        executor.execute(() -> {
            try {
                int userId = sessionManager.getCurrentUserId();
                List<Appointment> appointments = appointmentDAO.getCancelledAppointments(userId);
                callback.onSuccess(appointments);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Get appointment by ID
    public void getAppointmentById(int appointmentId, OnAppointmentCallback callback) {
        executor.execute(() -> {
            try {
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment != null) {
                    callback.onSuccess(appointment);
                } else {
                    callback.onError("Appointment not found");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Add new appointment
    public void addAppointment(Appointment appointment, OnActionCallback callback) {
        executor.execute(() -> {
            try {
                appointment.setUserId(sessionManager.getCurrentUserId());
                appointment.setStatus(AppointmentStatus.PENDING.getValue());

                long result = appointmentDAO.addAppointmentWithPatientInfo(appointment);
                if (result > 0) {
                    callback.onSuccess("Appointment booked successfully");
                } else {
                    callback.onError("Failed to book appointment");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Cancel appointment
    public void cancelAppointment(int appointmentId, String reason, OnActionCallback callback) {
        executor.execute(() -> {
            try {
                // Check if appointment exists and belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    callback.onError("Appointment not found");
                    return;
                }

                if (appointment.getUserId() != sessionManager.getCurrentUserId()) {
                    callback.onError("Unauthorized to cancel this appointment");
                    return;
                }

                // Check if appointment can be cancelled
                if (appointment.getStatus().equals(AppointmentStatus.CANCELLED.getValue())) {
                    callback.onError("Appointment is already cancelled");
                    return;
                }

                if (appointment.getStatus().equals(AppointmentStatus.COMPLETED.getValue())) {
                    callback.onError("Cannot cancel completed appointment");
                    return;
                }

                boolean success = appointmentDAO.cancelAppointment(appointmentId, reason);
                if (success) {
                    callback.onSuccess("Appointment cancelled successfully. 50% of the funds will be returned to your account.");
                } else {
                    callback.onError("Failed to cancel appointment");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Reschedule appointment
    public void rescheduleAppointment(int appointmentId, String newDate, String newTime, OnActionCallback callback) {
        executor.execute(() -> {
            try {
                // Check if appointment exists and belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    callback.onError("Appointment not found");
                    return;
                }

                if (appointment.getUserId() != sessionManager.getCurrentUserId()) {
                    callback.onError("Unauthorized to reschedule this appointment");
                    return;
                }

                // Check if appointment can be rescheduled
                if (!appointment.getStatus().equals(AppointmentStatus.UPCOMING.getValue()) &&
                        !appointment.getStatus().equals(AppointmentStatus.PENDING.getValue())) {
                    callback.onError("This appointment cannot be rescheduled");
                    return;
                }

                boolean success = appointmentDAO.rescheduleAppointment(appointmentId, newDate, newTime);
                if (success) {
                    callback.onSuccess("Appointment rescheduled successfully. You will receive a notification and the doctor will contact you.");
                } else {
                    callback.onError("Failed to reschedule appointment");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Add feedback/review for appointment
    public void addAppointmentFeedback(int appointmentId, int rating, String feedback, OnActionCallback callback) {
        executor.execute(() -> {
            try {
                // Check if appointment exists and belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    callback.onError("Appointment not found");
                    return;
                }

                if (appointment.getUserId() != sessionManager.getCurrentUserId()) {
                    callback.onError("Unauthorized to review this appointment");
                    return;
                }

                // Check if appointment is completed
                if (!appointment.getStatus().equals(AppointmentStatus.COMPLETED.getValue())) {
                    callback.onError("Can only review completed appointments");
                    return;
                }

                // Validate rating
                if (rating < 1 || rating > 5) {
                    callback.onError("Rating must be between 1 and 5");
                    return;
                }

                boolean success = appointmentDAO.addFeedback(appointmentId, rating, feedback);
                if (success) {
                    callback.onSuccess("Review submitted successfully, thank you very much!");
                } else {
                    callback.onError("Failed to submit review");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Get appointment template for booking again
    public void getBookAgainTemplate(int originalAppointmentId, OnAppointmentCallback callback) {
        executor.execute(() -> {
            try {
                Appointment originalAppointment = appointmentDAO.getAppointmentById(originalAppointmentId);
                if (originalAppointment == null) {
                    callback.onError("Original appointment not found");
                    return;
                }

                if (originalAppointment.getUserId() != sessionManager.getCurrentUserId()) {
                    callback.onError("Unauthorized access");
                    return;
                }

                // Create new appointment with same details but reset status and dates
                Appointment template = new Appointment();
                template.setUserId(originalAppointment.getUserId());
                template.setDoctor(originalAppointment.getDoctor());
                template.setClinic(originalAppointment.getClinic());
                template.setPatientName(originalAppointment.getPatientName());
                template.setPatientPhone(originalAppointment.getPatientPhone());
                template.setPatientAge(originalAppointment.getPatientAge());
                template.setPatientGender(originalAppointment.getPatientGender());
                template.setSymptoms(originalAppointment.getSymptoms());
                template.setMedicalHistory(originalAppointment.getMedicalHistory());
                template.setAppointmentType(originalAppointment.getAppointmentType());
                template.setAppointmentFee(originalAppointment.getAppointmentFee());
                template.setOriginalAppointmentId(String.valueOf(originalAppointmentId));

                callback.onSuccess(template);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Get all appointments for current user
    public void getAllAppointments(OnAppointmentListCallback callback) {
        executor.execute(() -> {
            try {
                int userId = sessionManager.getCurrentUserId();
                List<Appointment> appointments = appointmentDAO.getAppointmentsByUserId(userId);
                callback.onSuccess(appointments);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Update appointment status
    public void updateAppointmentStatus(int appointmentId, AppointmentStatus newStatus, OnActionCallback callback) {
        executor.execute(() -> {
            try {
                boolean success = appointmentDAO.updateAppointmentStatus(appointmentId, newStatus.getValue());
                if (success) {
                    callback.onSuccess("Appointment status updated successfully");
                } else {
                    callback.onError("Failed to update appointment status");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Delete appointment (for admin or testing purposes)
    public void deleteAppointment(int appointmentId, OnActionCallback callback) {
        executor.execute(() -> {
            try {
                appointmentDAO.deleteAppointment(appointmentId);
                callback.onSuccess("Appointment deleted successfully");
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
}
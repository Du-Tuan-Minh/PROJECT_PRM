// NEW FILE: app/src/main/java/com/example/project_prm/DataManager/HistoryManager/AppointmentHistoryManager.java
package com.example.project_prm.DataManager.HistoryManager;

import android.content.Context;
import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Manager class for Appointment History functionality (Chức năng 9)
 * Handles all appointment history logic for Tung's features
 */
public class AppointmentHistoryManager {
    private AppointmentDAO appointmentDAO;
    private DatabaseHelper dbHelper;

    public AppointmentHistoryManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        appointmentDAO = new AppointmentDAO(dbHelper.getWritableDatabase());
    }

    // ========== HISTORY RETRIEVAL METHODS ==========

    /**
     * Get all appointments by status for user
     */
    public void getAppointmentsByStatus(int userId, String status, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                List<Appointment> appointments = appointmentDAO.getAppointmentsByStatus(userId, status);
                List<AppointmentHistoryItem> historyItems = convertToHistoryItems(appointments);
                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get pending appointments (Chờ xác nhận)
     */
    public void getPendingAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                List<Appointment> appointments = appointmentDAO.getPendingAppointments(userId);
                List<AppointmentHistoryItem> historyItems = convertToHistoryItems(appointments);
                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get upcoming appointments (Sắp tới)
     */
    public void getUpcomingAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                List<Appointment> appointments = appointmentDAO.getUpcomingAppointments(userId);

                // Also include confirmed appointments that are in the future
                List<Appointment> confirmedAppointments = appointmentDAO.getAppointmentsByStatus(userId, AppointmentStatus.CONFIRMED.getValue());

                // Merge and filter future appointments
                List<Appointment> allUpcoming = new ArrayList<>();
                allUpcoming.addAll(appointments);

                for (Appointment apt : confirmedAppointments) {
                    if (isFutureAppointment(apt) && !containsAppointment(allUpcoming, apt.getId())) {
                        allUpcoming.add(apt);
                    }
                }

                // Sort by date and time
                allUpcoming.sort((a, b) -> {
                    int dateCompare = a.getDate().compareTo(b.getDate());
                    if (dateCompare != 0) return dateCompare;
                    return a.getTime().compareTo(b.getTime());
                });

                List<AppointmentHistoryItem> historyItems = convertToHistoryItems(allUpcoming);
                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get completed appointments (Hoàn thành)
     */
    public void getCompletedAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                List<Appointment> appointments = appointmentDAO.getCompletedAppointments(userId);

                // Sort by date descending (most recent first)
                appointments.sort((a, b) -> {
                    int dateCompare = b.getDate().compareTo(a.getDate());
                    if (dateCompare != 0) return dateCompare;
                    return b.getTime().compareTo(a.getTime());
                });

                List<AppointmentHistoryItem> historyItems = convertToHistoryItems(appointments);
                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get cancelled appointments (Đã hủy)
     */
    public void getCancelledAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                List<Appointment> appointments = appointmentDAO.getCancelledAppointments(userId);

                // Sort by date descending
                appointments.sort((a, b) -> {
                    int dateCompare = b.getDate().compareTo(a.getDate());
                    if (dateCompare != 0) return dateCompare;
                    return b.getTime().compareTo(a.getTime());
                });

                List<AppointmentHistoryItem> historyItems = convertToHistoryItems(appointments);
                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get all appointments for user with pagination
     */
    public void getAllAppointments(int userId, int offset, int limit, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                List<Appointment> allAppointments = appointmentDAO.getAppointmentsByUserId(userId);

                // Apply pagination
                int start = Math.min(offset, allAppointments.size());
                int end = Math.min(offset + limit, allAppointments.size());

                List<Appointment> pagedAppointments = allAppointments.subList(start, end);
                List<AppointmentHistoryItem> historyItems = convertToHistoryItems(pagedAppointments);

                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== APPOINTMENT ACTIONS ==========

    /**
     * Cancel appointment (for Upcoming appointments)
     */
    public void cancelAppointment(int appointmentId, String reason, OnActionListener listener) {
        new Thread(() -> {
            try {
                // Validate appointment can be cancelled
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    listener.onError("Appointment not found");
                    return;
                }

                if (!appointment.canBeCancelled()) {
                    listener.onError("This appointment cannot be cancelled");
                    return;
                }

                // Check if appointment is not too close (less than 2 hours)
                if (isAppointmentTooClose(appointment)) {
                    listener.onError("Cannot cancel appointment less than 2 hours before scheduled time");
                    return;
                }

                boolean success = appointmentDAO.cancelAppointment(appointmentId, reason);
                if (success) {
                    listener.onSuccess("Appointment cancelled successfully");
                } else {
                    listener.onError("Failed to cancel appointment");
                }
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Reschedule appointment (for Upcoming appointments)
     */
    public void rescheduleAppointment(int appointmentId, String newDate, String newTime, OnActionListener listener) {
        new Thread(() -> {
            try {
                // Validate appointment can be rescheduled
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    listener.onError("Appointment not found");
                    return;
                }

                if (!appointment.canBeRescheduled()) {
                    listener.onError("This appointment cannot be rescheduled");
                    return;
                }

                // Validate new date and time
                if (!isValidFutureDateTime(newDate, newTime)) {
                    listener.onError("Invalid date or time for rescheduling");
                    return;
                }

                boolean success = appointmentDAO.rescheduleAppointment(appointmentId, newDate, newTime);
                if (success) {
                    listener.onSuccess("Appointment rescheduled successfully");
                } else {
                    listener.onError("Failed to reschedule appointment");
                }
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Add feedback and rating (for Completed appointments)
     */
    public void addFeedback(int appointmentId, int rating, String feedback, OnActionListener listener) {
        new Thread(() -> {
            try {
                // Validate appointment can receive feedback
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    listener.onError("Appointment not found");
                    return;
                }

                if (!appointment.canReceiveFeedback()) {
                    listener.onError("This appointment cannot receive feedback");
                    return;
                }

                // Validate rating
                if (rating < 1 || rating > 5) {
                    listener.onError("Rating must be between 1 and 5");
                    return;
                }

                boolean success = appointmentDAO.addFeedback(appointmentId, rating, feedback);
                if (success) {
                    listener.onSuccess("Feedback added successfully");
                } else {
                    listener.onError("Failed to add feedback");
                }
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Book again (create new appointment based on completed/cancelled appointment)
     */
    public void bookAgain(int originalAppointmentId, OnBookAgainListener listener) {
        new Thread(() -> {
            try {
                Appointment originalAppointment = appointmentDAO.getAppointmentById(originalAppointmentId);
                if (originalAppointment == null) {
                    listener.onError("Original appointment not found");
                    return;
                }

                // Create new appointment with same details
                AppointmentTemplate template = new AppointmentTemplate();
                template.clinicId = originalAppointment.getClinic();
                template.doctorName = originalAppointment.getDoctor();
                template.patientName = originalAppointment.getPatientName();
                template.patientPhone = originalAppointment.getPatientPhone();
                template.patientAge = originalAppointment.getPatientAge();
                template.patientGender = originalAppointment.getPatientGender();
                template.appointmentType = originalAppointment.getAppointmentType();
                template.symptoms = originalAppointment.getSymptoms();
                template.medicalHistory = originalAppointment.getMedicalHistory();

                listener.onSuccess(template);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== STATISTICS METHODS ==========

    /**
     * Get appointment statistics for user
     */
    public void getAppointmentStatistics(int userId, OnStatisticsListener listener) {
        new Thread(() -> {
            try {
                AppointmentStatistics stats = new AppointmentStatistics();

                stats.totalAppointments = appointmentDAO.getTotalAppointmentsByUser(userId);
                stats.pendingCount = appointmentDAO.getAppointmentCountByStatus(userId, AppointmentStatus.PENDING.getValue());
                stats.upcomingCount = appointmentDAO.getAppointmentCountByStatus(userId, AppointmentStatus.UPCOMING.getValue()) +
                        appointmentDAO.getAppointmentCountByStatus(userId, AppointmentStatus.CONFIRMED.getValue());
                stats.completedCount = appointmentDAO.getAppointmentCountByStatus(userId, AppointmentStatus.COMPLETED.getValue());
                stats.cancelledCount = appointmentDAO.getAppointmentCountByStatus(userId, AppointmentStatus.CANCELLED.getValue());

                // Calculate completion rate
                if (stats.totalAppointments > 0) {
                    stats.completionRate = (double) stats.completedCount / stats.totalAppointments * 100;
                }

                listener.onSuccess(stats);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== HELPER METHODS ==========

    private List<AppointmentHistoryItem> convertToHistoryItems(List<Appointment> appointments) {
        List<AppointmentHistoryItem> items = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentHistoryItem item = new AppointmentHistoryItem();
            item.appointment = appointment;
            item.status = AppointmentStatus.fromValue(appointment.getStatus());
            item.canCancel = appointment.canBeCancelled() && !isAppointmentTooClose(appointment);
            item.canReschedule = appointment.canBeRescheduled();
            item.canRate = appointment.canReceiveFeedback();
            item.canBookAgain = appointment.isCompleted() || appointment.isCancelled();
            item.timeUntilAppointment = calculateTimeUntilAppointment(appointment);

            items.add(item);
        }

        return items;
    }

    private boolean isFutureAppointment(Appointment appointment) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String appointmentDateTime = appointment.getDate() + " " + appointment.getTime();
            Date appointmentDate = sdf.parse(appointmentDateTime);
            return appointmentDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean containsAppointment(List<Appointment> appointments, int appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == appointmentId) {
                return true;
            }
        }
        return false;
    }

    private boolean isAppointmentTooClose(Appointment appointment) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String appointmentDateTime = appointment.getDate() + " " + appointment.getTime();
            Date appointmentDate = sdf.parse(appointmentDateTime);

            long diffInMillis = appointmentDate.getTime() - System.currentTimeMillis();
            long diffInHours = diffInMillis / (1000 * 60 * 60);

            return diffInHours < 2; // Less than 2 hours
        } catch (Exception e) {
            return true; // Treat invalid dates as too close
        }
    }

    private boolean isValidFutureDateTime(String date, String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String dateTime = date + " " + time;
            Date appointmentDate = sdf.parse(dateTime);
            return appointmentDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String calculateTimeUntilAppointment(Appointment appointment) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String appointmentDateTime = appointment.getDate() + " " + appointment.getTime();
            Date appointmentDate = sdf.parse(appointmentDateTime);

            long diffInMillis = appointmentDate.getTime() - System.currentTimeMillis();

            if (diffInMillis < 0) {
                return "Đã qua";
            }

            long days = diffInMillis / (1000 * 60 * 60 * 24);
            long hours = (diffInMillis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

            if (days > 0) {
                return days + " ngày " + hours + " giờ";
            } else if (hours > 0) {
                return hours + " giờ";
            } else {
                long minutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60);
                return minutes + " phút";
            }
        } catch (Exception e) {
            return "Không xác định";
        }
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // ========== DATA CLASSES ==========

    public static class AppointmentHistoryItem {
        public Appointment appointment;
        public AppointmentStatus status;
        public boolean canCancel;
        public boolean canReschedule;
        public boolean canRate;
        public boolean canBookAgain;
        public String timeUntilAppointment;
    }

    public static class AppointmentTemplate {
        public String clinicId;
        public String doctorName;
        public String patientName;
        public String patientPhone;
        public String patientAge;
        public String patientGender;
        public String appointmentType;
        public String symptoms;
        public String medicalHistory;
    }

    public static class AppointmentStatistics {
        public int totalAppointments;
        public int pendingCount;
        public int upcomingCount;
        public int completedCount;
        public int cancelledCount;
        public double completionRate;
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnHistoryListener {
        void onSuccess(List<AppointmentHistoryItem> items);
        void onError(String error);
    }

    public interface OnActionListener {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface OnBookAgainListener {
        void onSuccess(AppointmentTemplate template);
        void onError(String error);
    }

    public interface OnStatisticsListener {
        void onSuccess(AppointmentStatistics statistics);
        void onError(String error);
    }
}
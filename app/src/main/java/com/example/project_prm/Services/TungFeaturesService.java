package com.example.project_prm.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.DataManager.SampleDataCreator;
import com.example.project_prm.DataManager.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * TungFeaturesService - Service for Tung's appointment management features
 * Chức năng 9: Quản lý lịch sử đặt lịch khám
 * Provides appointment CRUD operations with user authorization
 */
public class TungFeaturesService {

    private static TungFeaturesService instance;
    private Context context;
    private DatabaseHelper dbHelper;
    private AppointmentDAO appointmentDAO;
    private AppointmentHistoryManager historyManager;
    private UserSessionManager sessionManager;
    private SharedPreferences sharedPreferences;

    private TungFeaturesService(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = new DatabaseHelper(this.context);
        this.appointmentDAO = new AppointmentDAO(dbHelper.getWritableDatabase());
        this.historyManager = new AppointmentHistoryManager(this.context);
        this.sessionManager = new UserSessionManager(this.context);
        this.sharedPreferences = context.getSharedPreferences("TungFeaturesPrefs", Context.MODE_PRIVATE);

        // Initialize sample data if first time
        initializeSampleDataIfNeeded();
    }

    public static synchronized TungFeaturesService getInstance(Context context) {
        if (instance == null) {
            instance = new TungFeaturesService(context);
        }
        return instance;
    }

    private void initializeSampleDataIfNeeded() {
        boolean isFirstTime = sharedPreferences.getBoolean("is_first_time", true);
        if (isFirstTime) {
            // Ensure demo session exists
            if (!sessionManager.isLoggedIn()) {
                sessionManager.createDemoSession();
            }

            // Create sample data for current user
            SampleDataCreator sampleDataCreator = new SampleDataCreator(context);
            sampleDataCreator.createSampleAppointments(getCurrentUserId());
            sampleDataCreator.close();

            // Mark as initialized
            sharedPreferences.edit().putBoolean("is_first_time", false).apply();
        }
    }

    private int getCurrentUserId() {
        return sessionManager.getCurrentUserId();
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnAppointmentDetailListener {
        void onSuccess(Appointment appointment);
        void onError(String error);
    }

    public interface OnStatsListener {
        void onSuccess(String stats);
        void onError(String error);
    }

    // ========== APPOINTMENT DETAIL METHODS ==========

    /**
     * Get appointment details by ID with user authorization
     */
    public void getAppointmentDetails(int appointmentId, OnAppointmentDetailListener listener) {
        new Thread(() -> {
            try {
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment != null) {
                    // Verify appointment belongs to current user
                    if (appointment.getUserId() == getCurrentUserId()) {
                        listener.onSuccess(appointment);
                    } else {
                        listener.onError("Unauthorized access to appointment");
                    }
                } else {
                    listener.onError("Appointment not found");
                }
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== APPOINTMENT HISTORY METHODS ==========

    /**
     * Get upcoming appointments for current user
     */
    public void getUpcomingAppointments(AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getUpcomingAppointments(getCurrentUserId(), listener);
    }

    public void getUpcomingAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        // Only allow current user to access their own appointments
        if (userId != getCurrentUserId()) {
            listener.onError("Unauthorized access");
            return;
        }
        historyManager.getUpcomingAppointments(userId, listener);
    }

    /**
     * Get completed appointments for current user
     */
    public void getCompletedAppointments(AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getCompletedAppointments(getCurrentUserId(), listener);
    }

    public void getCompletedAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        if (userId != getCurrentUserId()) {
            listener.onError("Unauthorized access");
            return;
        }
        historyManager.getCompletedAppointments(userId, listener);
    }

    /**
     * Get cancelled appointments for current user
     */
    public void getCancelledAppointments(AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getCancelledAppointments(getCurrentUserId(), listener);
    }

    public void getCancelledAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        if (userId != getCurrentUserId()) {
            listener.onError("Unauthorized access");
            return;
        }
        historyManager.getCancelledAppointments(userId, listener);
    }

    // ========== APPOINTMENT ACTIONS ==========

    /**
     * Cancel appointment with authorization check
     */
    public void cancelAppointment(int appointmentId, String reason, AppointmentHistoryManager.OnActionListener listener) {
        new Thread(() -> {
            try {
                // Verify appointment belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    listener.onError("Appointment not found");
                    return;
                }

                if (appointment.getUserId() != getCurrentUserId()) {
                    listener.onError("Unauthorized to cancel this appointment");
                    return;
                }

                // Use history manager to cancel
                historyManager.cancelAppointment(appointmentId, reason, listener);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Reschedule appointment with authorization check
     */
    public void rescheduleAppointment(int appointmentId, String newDate, String newTime, AppointmentHistoryManager.OnActionListener listener) {
        new Thread(() -> {
            try {
                // Verify appointment belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    listener.onError("Appointment not found");
                    return;
                }

                if (appointment.getUserId() != getCurrentUserId()) {
                    listener.onError("Unauthorized to reschedule this appointment");
                    return;
                }

                // Use history manager to reschedule
                historyManager.rescheduleAppointment(appointmentId, newDate, newTime, listener);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Add feedback for appointment with authorization check
     */
    public void addAppointmentFeedback(int appointmentId, int rating, String feedback, AppointmentHistoryManager.OnActionListener listener) {
        new Thread(() -> {
            try {
                // Verify appointment belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
                if (appointment == null) {
                    listener.onError("Appointment not found");
                    return;
                }

                if (appointment.getUserId() != getCurrentUserId()) {
                    listener.onError("Unauthorized to review this appointment");
                    return;
                }

                // Use history manager to add feedback
                historyManager.addFeedback(appointmentId, rating, feedback, listener);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get template for booking again with authorization check
     */
    public void getBookAgainTemplate(int originalAppointmentId, AppointmentHistoryManager.OnBookAgainListener listener) {
        new Thread(() -> {
            try {
                // Verify appointment belongs to current user
                Appointment appointment = appointmentDAO.getAppointmentById(originalAppointmentId);
                if (appointment == null) {
                    listener.onError("Original appointment not found");
                    return;
                }

                if (appointment.getUserId() != getCurrentUserId()) {
                    listener.onError("Unauthorized access to appointment");
                    return;
                }

                // Use history manager to get template
                historyManager.bookAgain(originalAppointmentId, listener);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== APPOINTMENT MANAGEMENT ==========

    /**
     * Add new appointment for current user
     */
    public void addAppointment(Appointment appointment, AppointmentHistoryManager.OnActionListener listener) {
        new Thread(() -> {
            try {
                appointment.setUserId(getCurrentUserId());
                long result = appointmentDAO.addAppointmentWithPatientInfo(appointment);
                if (result > 0) {
                    listener.onSuccess("Appointment booked successfully");
                } else {
                    listener.onError("Failed to book appointment");
                }
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    /**
     * Get all appointments for current user
     */
    public void getAllAppointments(AppointmentHistoryManager.OnHistoryListener listener) {
        new Thread(() -> {
            try {
                // Get all appointments for current user from database
                List<Appointment> allAppointments = appointmentDAO.getAppointmentsByUserId(getCurrentUserId());

                // Convert to AppointmentHistoryItem list
                List<AppointmentHistoryManager.AppointmentHistoryItem> historyItems = new ArrayList<>();

                for (Appointment appointment : allAppointments) {
                    AppointmentHistoryManager.AppointmentHistoryItem item = new AppointmentHistoryManager.AppointmentHistoryItem();

                    // Map appointment data to history item
                    item.appointmentId = appointment.getId();
                    item.doctorName = appointment.getDoctor();
                    item.specialty = getSpecialtyFromAppointmentType(appointment.getAppointmentType());
                    item.clinicName = appointment.getClinic();
                    item.appointmentDate = appointment.getDate();
                    item.appointmentTime = appointment.getTime();
                    item.fee = appointment.getAppointmentFee();
                    item.packageType = appointment.getAppointmentType();

                    // Set status as string
                    String status = appointment.getStatus();
                    if (status != null) {
                        item.status = status.toLowerCase();
                    } else {
                        item.status = "pending";
                    }

                    historyItems.add(item);
                }

                listener.onSuccess(historyItems);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== UTILITY METHODS ==========

    /**
     * Get specialty from appointment type since Appointment entity doesn't have specialty field
     */
    private String getSpecialtyFromAppointmentType(String appointmentType) {
        if (appointmentType == null) {
            return "Tổng quát";
        }

        switch (appointmentType.toLowerCase()) {
            case "consultation":
                return "Tư vấn";
            case "checkup":
                return "Khám tổng quát";
            case "followup":
                return "Tái khám";
            case "messaging":
                return "Tư vấn trực tuyến";
            case "video call":
                return "Tư vấn video";
            case "voice call":
                return "Tư vấn điện thoại";
            default:
                return "Tổng quát";
        }
    }

    // ========== TESTING AND UTILITY METHODS ==========

    /**
     * Generate random appointments for testing
     */
    public void generateRandomAppointments(int count) {
        new Thread(() -> {
            try {
                SampleDataCreator sampleDataCreator = new SampleDataCreator(context);
                sampleDataCreator.createRandomAppointments(getCurrentUserId(), count);
                sampleDataCreator.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Force refresh sample data (for testing)
     */
    public void refreshSampleData() {
        new Thread(() -> {
            try {
                SampleDataCreator sampleDataCreator = new SampleDataCreator(context);
                sampleDataCreator.createSampleAppointments(getCurrentUserId());
                sampleDataCreator.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Clear all data (for testing)
     */
    public void clearAllData() {
        new Thread(() -> {
            try {
                // Reset first time flag to recreate sample data
                sharedPreferences.edit().putBoolean("is_first_time", true).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Get database statistics
     */
    public void getDatabaseStats(OnStatsListener listener) {
        new Thread(() -> {
            try {
                int userId = getCurrentUserId();
                int upcomingCount = appointmentDAO.getUpcomingAppointments(userId).size();
                int completedCount = appointmentDAO.getCompletedAppointments(userId).size();
                int cancelledCount = appointmentDAO.getCancelledAppointments(userId).size();
                int totalCount = upcomingCount + completedCount + cancelledCount;

                String stats = String.format(
                        "Total: %d | Upcoming: %d | Completed: %d | Cancelled: %d",
                        totalCount, upcomingCount, completedCount, cancelledCount
                );

                listener.onSuccess(stats);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== GETTERS ==========

    /**
     * Get current user session manager
     */
    public UserSessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Close service and free resources
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (historyManager != null) {
            historyManager.close();
        }
    }
}
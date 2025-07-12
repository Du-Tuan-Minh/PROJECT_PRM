// File: app/src/main/java/com/example/project_prm/DataManager/HistoryManager/AppointmentHistoryManager.java
package com.example.project_prm.DataManager.HistoryManager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for Appointment History functionality (Chức năng 9)
 * Handles appointment history, cancel, reschedule, and book again features
 */
public class AppointmentHistoryManager {
    private Context context;
    private Handler mainHandler;

    public AppointmentHistoryManager(Context context) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnHistoryListener {
        void onSuccess(List<AppointmentHistoryItem> appointments);
        void onError(String error);
    }

    public interface OnActionListener {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface OnBookAgainListener {
        void onSuccess(BookAgainTemplate template);
        void onError(String error);
    }

    public interface OnStatisticsListener {
        void onSuccess(AppointmentStatistics statistics);
        void onError(String error);
    }

    // ========== DATA CLASSES ==========

    public static class AppointmentHistoryItem {
        public int appointmentId;
        public String doctorName;
        public String specialty;
        public String clinicName;
        public String appointmentDate;
        public String appointmentTime;
        public double fee;
        public String status;
        public String packageType;
        public String reason; // For cancelled appointments
        public int rating; // For completed appointments
        public String feedback; // For completed appointments

        public AppointmentHistoryItem() {}

        public AppointmentHistoryItem(int appointmentId, String doctorName, String specialty,
                                      String clinicName, String appointmentDate, String appointmentTime,
                                      double fee, String status, String packageType) {
            this.appointmentId = appointmentId;
            this.doctorName = doctorName;
            this.specialty = specialty;
            this.clinicName = clinicName;
            this.appointmentDate = appointmentDate;
            this.appointmentTime = appointmentTime;
            this.fee = fee;
            this.status = status;
            this.packageType = packageType;
        }
    }

    public static class BookAgainTemplate {
        public String appointmentId;
        public String doctorId;
        public String doctorName;
        public String specialty;
        public String clinicName;
        public String originalDate;
        public String originalTime;
        public double fee;
        public String packageType;

        public BookAgainTemplate(String appointmentId, String doctorId, String doctorName,
                                 String specialty, String clinicName, String originalDate,
                                 String originalTime, double fee, String packageType) {
            this.appointmentId = appointmentId;
            this.doctorId = doctorId;
            this.doctorName = doctorName;
            this.specialty = specialty;
            this.clinicName = clinicName;
            this.originalDate = originalDate;
            this.originalTime = originalTime;
            this.fee = fee;
            this.packageType = packageType;
        }
    }

    public static class RescheduleTemplate {
        public String appointmentId;
        public String currentDate;
        public String currentTime;
        public List<String> availableDates;
        public List<String> availableTimeSlots;
        public double rescheduleFee;
        public String doctorId;
        public String clinicId;

        public RescheduleTemplate(String appointmentId, String currentDate, String currentTime,
                                  List<String> availableDates, List<String> availableTimeSlots,
                                  double rescheduleFee, String doctorId, String clinicId) {
            this.appointmentId = appointmentId;
            this.currentDate = currentDate;
            this.currentTime = currentTime;
            this.availableDates = availableDates != null ? availableDates : new ArrayList<>();
            this.availableTimeSlots = availableTimeSlots != null ? availableTimeSlots : new ArrayList<>();
            this.rescheduleFee = rescheduleFee;
            this.doctorId = doctorId;
            this.clinicId = clinicId;
        }
    }

    public static class AppointmentStatistics {
        public int totalAppointments;
        public int upcomingCount;
        public int completedCount;
        public int cancelledCount;
        public int pendingCount;
        public double totalSpent;
        public String mostVisitedClinic;
        public String favoriteSpecialty;

        public AppointmentStatistics() {}

        public AppointmentStatistics(int totalAppointments, int upcomingCount, int completedCount,
                                     int cancelledCount, int pendingCount, double totalSpent,
                                     String mostVisitedClinic, String favoriteSpecialty) {
            this.totalAppointments = totalAppointments;
            this.upcomingCount = upcomingCount;
            this.completedCount = completedCount;
            this.cancelledCount = cancelledCount;
            this.pendingCount = pendingCount;
            this.totalSpent = totalSpent;
            this.mostVisitedClinic = mostVisitedClinic;
            this.favoriteSpecialty = favoriteSpecialty;
        }
    }

    // ========== APPOINTMENT HISTORY METHODS ==========

    /**
     * Get upcoming appointments for user
     */
    public void getUpcomingAppointments(int userId, OnHistoryListener listener) {
        // Mock implementation - replace with real database query
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simulate network delay

                List<AppointmentHistoryItem> upcomingAppointments = new ArrayList<>();

                // Mock upcoming appointments
                upcomingAppointments.add(new AppointmentHistoryItem(
                        1, "Dr. Nguyễn Văn A", "Nội khoa", "Phòng khám ABC",
                        "2025-07-15", "09:00", 200000, "upcoming", "Gói khám cơ bản"
                ));

                upcomingAppointments.add(new AppointmentHistoryItem(
                        2, "Dr. Trần Thị B", "Tim mạch", "Bệnh viện XYZ",
                        "2025-07-18", "14:00", 350000, "upcoming", "Gói khám chuyên sâu"
                ));

                mainHandler.post(() -> listener.onSuccess(upcomingAppointments));

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        }).start();
    }

    /**
     * Get completed appointments for user
     */
    public void getCompletedAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(500);

                List<AppointmentHistoryItem> completedAppointments = new ArrayList<>();

                // Mock completed appointments
                AppointmentHistoryItem completed1 = new AppointmentHistoryItem(
                        3, "Dr. Lê Văn C", "Da liễu", "Phòng khám DEF",
                        "2025-07-05", "10:30", 180000, "completed", "Gói khám da"
                );
                completed1.rating = 5;
                completed1.feedback = "Bác sĩ rất tận tình và chuyên nghiệp";
                completedAppointments.add(completed1);

                AppointmentHistoryItem completed2 = new AppointmentHistoryItem(
                        4, "Dr. Phạm Thị D", "Mắt", "Bệnh viện GHI",
                        "2025-06-28", "16:00", 250000, "completed", "Khám mắt tổng quát"
                );
                completed2.rating = 4;
                completed2.feedback = "Khám rất kỹ, hài lòng với dịch vụ";
                completedAppointments.add(completed2);

                mainHandler.post(() -> listener.onSuccess(completedAppointments));

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        }).start();
    }

    /**
     * Get cancelled appointments for user
     */
    public void getCancelledAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(500);

                List<AppointmentHistoryItem> cancelledAppointments = new ArrayList<>();

                // Mock cancelled appointments
                AppointmentHistoryItem cancelled1 = new AppointmentHistoryItem(
                        5, "Dr. Hoàng Văn E", "Răng hàm mặt", "Nha khoa JKL",
                        "2025-07-01", "08:00", 300000, "cancelled", "Implant răng"
                );
                cancelled1.reason = "Có việc đột xuất, không thể tham gia";
                cancelledAppointments.add(cancelled1);

                AppointmentHistoryItem cancelled2 = new AppointmentHistoryItem(
                        6, "Dr. Vũ Thị F", "Phụ khoa", "Phòng khám MNO",
                        "2025-06-20", "15:30", 220000, "cancelled", "Khám phụ khoa"
                );
                cancelled2.reason = "Bác sĩ bận, đề nghị đổi lịch";
                cancelledAppointments.add(cancelled2);

                mainHandler.post(() -> listener.onSuccess(cancelledAppointments));

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        }).start();
    }

    /**
     * Get pending appointments for user
     */
    public void getPendingAppointments(int userId, OnHistoryListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(500);

                List<AppointmentHistoryItem> pendingAppointments = new ArrayList<>();

                // Mock pending appointments
                pendingAppointments.add(new AppointmentHistoryItem(
                        7, "Dr. Đỗ Văn G", "Thần kinh", "Bệnh viện PQR",
                        "2025-07-20", "11:00", 400000, "pending", "Khám thần kinh"
                ));

                mainHandler.post(() -> listener.onSuccess(pendingAppointments));

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        }).start();
    }

    // ========== APPOINTMENT ACTIONS ==========

    /**
     * Cancel an appointment (for Upcoming appointments)
     */
    public void cancelAppointment(int appointmentId, String reason, OnActionListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate processing time

                // Mock cancellation logic
                // In real implementation, update database status to 'cancelled'

                mainHandler.post(() ->
                        listener.onSuccess("Đặt lịch đã được hủy thành công. 50% số tiền sẽ được hoàn lại.")
                );

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError("Lỗi khi hủy đặt lịch: " + e.getMessage()));
            }
        }).start();
    }

    /**
     * Reschedule an appointment (for Upcoming appointments)
     */
    public void rescheduleAppointment(int appointmentId, String newDate, String newTime, OnActionListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                // Mock reschedule logic
                // In real implementation, update appointment date/time in database

                mainHandler.post(() ->
                        listener.onSuccess("Lịch hẹn đã được đổi thành công. Phí đổi lịch: 50,000đ")
                );

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError("Lỗi khi đổi lịch: " + e.getMessage()));
            }
        }).start();
    }

    /**
     * Add feedback for completed appointment
     */
    public void addFeedback(int appointmentId, int rating, String feedback, OnActionListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(800);

                // Mock feedback logic
                // In real implementation, save feedback to database

                mainHandler.post(() ->
                        listener.onSuccess("Cảm ơn bạn đã đánh giá! Phản hồi của bạn rất quan trọng.")
                );

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError("Lỗi khi gửi đánh giá: " + e.getMessage()));
            }
        }).start();
    }

    /**
     * Get template for booking again (for Completed/Cancelled appointments)
     */
    public void bookAgain(int originalAppointmentId, OnBookAgainListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(800);

                // Mock book again template
                BookAgainTemplate template = new BookAgainTemplate(
                        String.valueOf(originalAppointmentId),
                        "doc123",
                        "Dr. Nguyễn Văn A",
                        "Tim mạch",
                        "Phòng khám ABC",
                        "2025-07-10",
                        "10:00",
                        200000.0,
                        "Gói cơ bản"
                );

                mainHandler.post(() -> listener.onSuccess(template));

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError("Lỗi khi tạo template đặt lại: " + e.getMessage()));
            }
        }).start();
    }

    /**
     * Get appointment statistics for user
     */
    public void getAppointmentStatistics(int userId, OnStatisticsListener listener) {
        new Thread(() -> {
            try {
                Thread.sleep(600);

                // Mock statistics
                AppointmentStatistics stats = new AppointmentStatistics(
                        15, // total appointments
                        3,  // upcoming
                        8,  // completed
                        3,  // cancelled
                        1,  // pending
                        2500000.0, // total spent
                        "Phòng khám ABC",
                        "Tim mạch"
                );

                mainHandler.post(() -> listener.onSuccess(stats));

            } catch (Exception e) {
                mainHandler.post(() -> listener.onError("Lỗi khi lấy thống kê: " + e.getMessage()));
            }
        }).start();
    }

    // ========== UTILITY METHODS ==========

    /**
     * Get appointment history by status
     */
    public List<Appointment> getAppointmentsByStatus(String status) {
        // Mock data - replace with real implementation
        List<Appointment> appointments = new ArrayList<>();

        // Add mock appointments based on status
        if ("upcoming".equals(status)) {
            // Add upcoming appointments
        } else if ("completed".equals(status)) {
            // Add completed appointments
        } else if ("cancelled".equals(status)) {
            // Add cancelled appointments
        }

        return appointments;
    }

    /**
     * Close manager and free resources
     */
    public void close() {
        // Clean up resources if needed
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }
}
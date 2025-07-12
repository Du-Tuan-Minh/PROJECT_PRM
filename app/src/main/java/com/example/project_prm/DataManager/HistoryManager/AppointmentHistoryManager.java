// File: app/src/main/java/com/example/project_prm/DataManager/AppointmentManager/AppointmentHistoryManager.java
package com.example.project_prm.DataManager.AppointmentManager;

import android.content.Context;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.Doctor;

import java.util.ArrayList;
import java.util.List;

public class AppointmentHistoryManager {

    private Context context;

    public AppointmentHistoryManager(Context context) {
        this.context = context;
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnCancelListener {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface OnRescheduleListener {
        void onSuccess(RescheduleTemplate template);
        void onError(String error);
    }

    public interface OnBookAgainListener {
        void onSuccess(BookAgainTemplate template);
        void onError(String error);
    }

    // ========== TEMPLATE CLASSES ==========

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

    // ========== METHODS ==========

    // CHỨC NĂNG 9: Cancel appointment (Upcoming appointments)
    public void cancelAppointment(String appointmentId, String reason, OnCancelListener listener) {
        // Mock implementation
        new android.os.Handler().postDelayed(() -> {
            if (listener != null) {
                listener.onSuccess("Đã hủy lịch hẹn thành công");
            }
        }, 1000);
    }

    // CHỨC NĂNG 9: Get reschedule template (Upcoming appointments)
    public void getRescheduleTemplate(String appointmentId, OnRescheduleListener listener) {
        // Mock implementation
        new android.os.Handler().postDelayed(() -> {
            if (listener != null) {
                List<String> availableDates = new ArrayList<>();
                availableDates.add("2025-07-15");
                availableDates.add("2025-07-16");
                availableDates.add("2025-07-17");

                List<String> availableTimeSlots = new ArrayList<>();
                availableTimeSlots.add("09:00");
                availableTimeSlots.add("10:00");
                availableTimeSlots.add("14:00");
                availableTimeSlots.add("15:00");

                RescheduleTemplate template = new RescheduleTemplate(
                        appointmentId,
                        "2025-07-13",
                        "10:00",
                        availableDates,
                        availableTimeSlots,
                        50000.0, // reschedule fee
                        "doc123",
                        "clinic456"
                );

                listener.onSuccess(template);
            }
        }, 1000);
    }

    // CHỨC NĂNG 9: Get book again template (Completed/Cancelled appointments)
    public void getBookAgainTemplate(String appointmentId, OnBookAgainListener listener) {
        // Mock implementation
        new android.os.Handler().postDelayed(() -> {
            if (listener != null) {
                BookAgainTemplate template = new BookAgainTemplate(
                        appointmentId,
                        "doc123",
                        "Dr. Nguyễn Văn A",
                        "Tim mạch",
                        "Phòng khám ABC",
                        "2025-07-10",
                        "10:00",
                        200000.0,
                        "Gói cơ bản"
                );

                listener.onSuccess(template);
            }
        }, 1000);
    }

    // Get appointment history by status
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
}
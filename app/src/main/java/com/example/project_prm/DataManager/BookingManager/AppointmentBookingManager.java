// FIXED FILE: app/src/main/java/com/example/project_prm/DataManager/BookingManager/AppointmentBookingManager.java
package com.example.project_prm.DataManager.BookingManager;

import android.content.Context;
import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.DAO.ClinicDAO;
import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Manager class for Appointment Booking functionality (Chức năng 8)
 * Handles all appointment booking logic for Tung's features
 */
public class AppointmentBookingManager {
    private final AppointmentDAO appointmentDAO;
    private final ClinicDAO clinicDAO;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public AppointmentBookingManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.appointmentDAO = new AppointmentDAO(dbHelper.getWritableDatabase());
        this.clinicDAO = new ClinicDAO();
    }

    // ========== BOOKING PROCESS METHODS ==========

    /**
     * Step 1: Get available time slots for a clinic on specific date
     */
    public void getAvailableTimeSlots(String clinicId, String date, OnTimeSlotsListener listener) {
        clinicDAO.getAvailableTimeSlots(clinicId, date, new ClinicDAO.OnTimeSlotsListener() {
            @Override
            public void onSuccess(List<String> timeSlots) {
                listener.onSuccess(timeSlots);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    /**
     * Step 2: Validate appointment data before booking
     */
    public ValidationResult validateAppointmentData(AppointmentBookingRequest request) {
        ValidationResult result = new ValidationResult();

        // Validate basic appointment info
        if (request.clinicId == null || request.clinicId.trim().isEmpty()) {
            result.addError("Clinic ID is required");
        }

        if (request.doctorName == null || request.doctorName.trim().isEmpty()) {
            result.addError("Doctor name is required");
        }

        if (request.date == null || request.date.trim().isEmpty()) {
            result.addError("Appointment date is required");
        } else if (!isValidDate(request.date)) {
            result.addError("Invalid appointment date format");
        } else if (isPastDate(request.date)) {
            result.addError("Cannot book appointment for past date");
        }

        if (request.time == null || request.time.trim().isEmpty()) {
            result.addError("Appointment time is required");
        } else if (!isValidTime(request.time)) {
            result.addError("Invalid appointment time format");
        }

        // Validate patient information
        if (request.patientName == null || request.patientName.trim().isEmpty()) {
            result.addError("Patient name is required");
        }

        if (request.patientPhone == null || request.patientPhone.trim().isEmpty()) {
            result.addError("Patient phone is required");
        } else if (!isValidPhone(request.patientPhone)) {
            result.addError("Invalid phone number format");
        }

        if (request.patientAge == null || request.patientAge.trim().isEmpty()) {
            result.addError("Patient age is required");
        } else if (!isValidAge(request.patientAge)) {
            result.addError("Invalid age (must be between 1-120)");
        }

        if (request.symptoms == null || request.symptoms.trim().isEmpty()) {
            result.addError("Symptoms description is required");
        }

        return result;
    }

    /**
     * Step 3: Book appointment with full patient information
     */
    public void bookAppointment(AppointmentBookingRequest request, OnBookingListener listener) {
        // Validate data first
        ValidationResult validation = validateAppointmentData(request);
        if (!validation.isValid()) {
            listener.onError("Validation failed: " + validation.getErrorsAsString());
            return;
        }

        // Check if time slot is still available
        checkTimeSlotAvailability(request.clinicId, request.date, request.time, new OnAvailabilityListener() {
            @Override
            public void onAvailable() {
                // Create appointment object
                Appointment appointment = createAppointmentFromRequest(request);

                // Save to database
                new Thread(() -> {
                    try {
                        long appointmentId = appointmentDAO.addAppointmentWithPatientInfo(appointment);
                        if (appointmentId > 0) {
                            appointment.setId((int) appointmentId);
                            listener.onSuccess(appointment);
                        } else {
                            listener.onError("Failed to save appointment to database");
                        }
                    } catch (Exception e) {
                        listener.onError("Database error: " + e.getMessage());
                    }
                }).start();
            }

            @Override
            public void onNotAvailable() {
                listener.onError("Selected time slot is no longer available");
            }

            @Override
            public void onError(String error) {
                listener.onError("Availability check failed: " + error);
            }
        });
    }

    /**
     * Calculate appointment fee based on clinic and appointment type
     */
    public void calculateAppointmentFee(String clinicId, String appointmentType, OnFeeCalculationListener listener) {
        // Get clinic services and pricing
        clinicDAO.getClinicById(clinicId, new ClinicDAO.OnClinicDetailListener() {
            @Override
            public void onSuccess(Map<String, Object> clinic) {
                double fee = getBaseFee(appointmentType);

                // Adjust fee based on clinic rating (premium clinics charge more)
                Object ratingObj = clinic.get("rating");
                double rating = (ratingObj instanceof Number) ? ((Number) ratingObj).doubleValue() : 0.0;
                if (rating >= 4.5) {
                    fee *= 1.3; // 30% premium for high-rated clinics
                } else if (rating >= 4.0) {
                    fee *= 1.15; // 15% premium for good-rated clinics
                }

                listener.onSuccess(fee);
            }

            @Override
            public void onFailure(Exception e) {
                // Return default fee if clinic not found
                double defaultFee = getBaseFee(appointmentType);
                listener.onSuccess(defaultFee);
            }
        });
    }

    /**
     * Get available dates for booking (next 30 days, excluding Sundays)
     */
    public List<String> getAvailableDates() {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Start from tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        for (int i = 0; i < 30; i++) {
            // Skip Sundays (most clinics closed)
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                dates.add(sdf.format(calendar.getTime()));
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }

    /**
     * Get appointment types with descriptions
     */
    public List<AppointmentType> getAppointmentTypes() {
        List<AppointmentType> types = new ArrayList<>();

        types.add(new AppointmentType("consultation", "Tư vấn khám bệnh", "Khám và tư vấn sức khỏe tổng quát", 200000));
        types.add(new AppointmentType("checkup", "Khám sức khỏe", "Kiểm tra sức khỏe định kỳ", 300000));
        types.add(new AppointmentType("followup", "Tái khám", "Khám lại sau điều trị", 150000));
        types.add(new AppointmentType("specialist", "Khám chuyên khoa", "Khám bệnh chuyên khoa", 400000));
        types.add(new AppointmentType("emergency", "Cấp cứu", "Khám cấp cứu", 500000));

        return types;
    }

    // ========== HELPER METHODS ==========

    private void checkTimeSlotAvailability(String clinicId, String date, String time, OnAvailabilityListener listener) {
        new Thread(() -> {
            try {
                // Check in local database first
                List<Appointment> existingAppointments = appointmentDAO.getAppointmentsByDateRange(0, date, date);

                for (Appointment appointment : existingAppointments) {
                    if (appointment.getClinic().equals(clinicId) &&
                            appointment.getTime().equals(time) &&
                            !appointment.isCancelled()) {
                        listener.onNotAvailable();
                        return;
                    }
                }

                // If not found locally, assume available
                listener.onAvailable();

            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    private Appointment createAppointmentFromRequest(AppointmentBookingRequest request) {
        Appointment appointment = new Appointment();

        // Basic info
        appointment.setUserId(request.userId);
        appointment.setClinic(request.clinicId);
        appointment.setDoctor(request.doctorName);
        appointment.setDate(request.date);
        appointment.setTime(request.time);
        appointment.setStatus(AppointmentStatus.PENDING.getValue());

        // Patient info
        appointment.setPatientName(request.patientName);
        appointment.setPatientPhone(request.patientPhone);
        appointment.setPatientAge(request.patientAge);
        appointment.setPatientGender(request.patientGender);
        appointment.setSymptoms(request.symptoms);
        appointment.setMedicalHistory(request.medicalHistory);

        // Appointment details
        appointment.setAppointmentType(request.appointmentType);
        appointment.setAppointmentFee(request.appointmentFee);
        appointment.setNotes(request.notes);

        // Timestamps
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        appointment.setCreatedAt(currentTime);
        appointment.setUpdatedAt(currentTime);

        return appointment;
    }

    private double getBaseFee(String appointmentType) {
        switch (appointmentType) {
            case "consultation": return 200000; // 200k VND
            case "checkup": return 300000;      // 300k VND
            case "followup": return 150000;     // 150k VND
            case "specialist": return 400000;   // 400k VND
            case "emergency": return 500000;    // 500k VND
            default: return 200000;
        }
    }

    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPastDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date appointmentDate = sdf.parse(date);
            Date today = new Date();

            // Reset time to start of day for accurate comparison
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            return appointmentDate.before(cal.getTime());
        } catch (Exception e) {
            return true; // Treat invalid dates as past
        }
    }

    private boolean isValidTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidPhone(String phone) {
        // Vietnamese phone number validation
        return phone.matches("^(0|\\+84)[0-9]{9,10}$");
    }

    private boolean isValidAge(String age) {
        try {
            int ageInt = Integer.parseInt(age);
            return ageInt >= 1 && ageInt <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // ========== DATA CLASSES ==========

    public static class AppointmentBookingRequest {
        public int userId;
        public String clinicId;
        public String clinicName;
        public String doctorName;
        public String date;
        public String time;

        // Patient Information
        public String patientName;
        public String patientPhone;
        public String patientAge;
        public String patientGender;
        public String symptoms;
        public String medicalHistory;

        // Appointment Details
        public String appointmentType;
        public double appointmentFee;
        public String notes;

        public AppointmentBookingRequest() {
            appointmentType = "consultation";
            appointmentFee = 0.0;
            medicalHistory = "";
            notes = "";
        }
    }

    public static class AppointmentType {
        public String code;
        public String name;
        public String description;
        public double baseFee;

        public AppointmentType(String code, String name, String description, double baseFee) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.baseFee = baseFee;
        }
    }

    public static class ValidationResult {
        private final List<String> errors;

        public ValidationResult() {
            errors = new ArrayList<>();
        }

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorsAsString() {
            return String.join(", ", errors);
        }
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnTimeSlotsListener {
        void onSuccess(List<String> timeSlots);
        void onError(String error);
    }

    public interface OnBookingListener {
        void onSuccess(Appointment appointment);
        void onError(String error);
    }

    public interface OnAvailabilityListener {
        void onAvailable();
        void onNotAvailable();
        void onError(String error);
    }

    public interface OnFeeCalculationListener {
        void onSuccess(double fee);
        void onError(String error);
    }
}
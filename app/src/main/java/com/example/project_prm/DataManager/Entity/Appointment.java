// UPDATED FILE: app/src/main/java/com/example/project_prm/DataManager/Entity/Appointment.java
package com.example.project_prm.DataManager.Entity;

public class Appointment {
    // ========== EXISTING FIELDS ==========
    private int id;
    private int userId;
    private String clinic;
    private String doctor;
    private String date;
    private String time;

    // ========== NEW FIELDS FOR ENHANCED FEATURES ==========

    // Patient Information (Chức năng 8: Điền thông tin người bệnh)
    private String patientName;
    private String patientPhone;
    private String patientAge;
    private String patientGender;
    private String symptoms;
    private String medicalHistory;

    // Appointment Details
    private String appointmentType; // "consultation", "checkup", "followup"
    private double appointmentFee;
    private String notes;
    private String status; // "pending", "confirmed", "upcoming", "completed", "cancelled"

    // For History & Feedback (Chức năng 9: Lịch sử)
    private String cancellationReason;
    private int rating; // 1-5 stars for completed appointments
    private String feedback;
    private String originalAppointmentId; // for rescheduled appointments

    // Timestamps
    private String createdAt;
    private String updatedAt;

    // ========== CONSTRUCTORS ==========
    public Appointment() {
        this.status = AppointmentStatus.PENDING.getValue();
        this.rating = 0;
        this.appointmentFee = 0.0;
    }

    public Appointment(int userId, String clinic, String doctor, String date, String time) {
        this();
        this.userId = userId;
        this.clinic = clinic;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    // ========== EXISTING GETTERS & SETTERS ==========
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getClinic() { return clinic; }
    public void setClinic(String clinic) { this.clinic = clinic; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    // ========== NEW GETTERS & SETTERS ==========

    // Patient Information
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public String getPatientAge() { return patientAge; }
    public void setPatientAge(String patientAge) { this.patientAge = patientAge; }

    public String getPatientGender() { return patientGender; }
    public void setPatientGender(String patientGender) { this.patientGender = patientGender; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    // Appointment Details
    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public double getAppointmentFee() { return appointmentFee; }
    public void setAppointmentFee(double appointmentFee) { this.appointmentFee = appointmentFee; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // History & Feedback
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public int getRating() { return rating; }
    public void setRating(int rating) {
        if (rating >= 0 && rating <= 5) {
            this.rating = rating;
        }
    }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public String getOriginalAppointmentId() { return originalAppointmentId; }
    public void setOriginalAppointmentId(String originalAppointmentId) { this.originalAppointmentId = originalAppointmentId; }

    // Timestamps
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // ========== HELPER METHODS ==========

    public boolean isPending() {
        return AppointmentStatus.PENDING.getValue().equals(this.status);
    }

    public boolean isUpcoming() {
        return AppointmentStatus.UPCOMING.getValue().equals(this.status);
    }

    public boolean isCompleted() {
        return AppointmentStatus.COMPLETED.getValue().equals(this.status);
    }

    public boolean isCancelled() {
        return AppointmentStatus.CANCELLED.getValue().equals(this.status);
    }

    public boolean canBeCancelled() {
        return isPending() || isUpcoming();
    }

    public boolean canBeRescheduled() {
        return isPending() || isUpcoming();
    }

    public boolean canReceiveFeedback() {
        return isCompleted() && rating == 0;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", clinic='" + clinic + '\'' +
                ", doctor='" + doctor + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
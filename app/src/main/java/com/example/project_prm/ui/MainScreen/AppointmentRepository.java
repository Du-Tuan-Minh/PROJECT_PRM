package com.example.project_prm.ui.MainScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentRepository {
    private static AppointmentRepository instance;
    private List<AppointmentModel> appointments;

    private AppointmentRepository() {
        appointments = new ArrayList<>();
        initializeSampleData();
    }

    public static synchronized AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Tất cả appointments đều thuộc về user hiện tại
        String currentUser = "Nguyễn Văn An"; // Tên user hiện tại
        String currentPhone = "0987654321"; // SĐT user hiện tại

        // Upcoming appointments
        AppointmentModel upcoming1 = new AppointmentModel(
            "apt_001", "BS. Nguyễn Văn A", "Tim mạch", 
            "15/07/2025", "09:00", "Gọi video", 500000, "upcoming"
        );
        upcoming1.setPatientName(currentUser);
        upcoming1.setPatientPhone(currentPhone);
        upcoming1.setProblemDescription("Đau ngực, khó thở");
        upcoming1.setDuration("45 phút");
        appointments.add(upcoming1);

        AppointmentModel upcoming2 = new AppointmentModel(
            "apt_002", "BS. Trần Thị C", "Da liễu", 
            "16/07/2025", "14:30", "Khám trực tiếp", 800000, "upcoming"
        );
        upcoming2.setPatientName(currentUser);
        upcoming2.setPatientPhone(currentPhone);
        upcoming2.setProblemDescription("Nổi mẩn đỏ ở tay");
        upcoming2.setDuration("60 phút");
        appointments.add(upcoming2);

        // Completed appointments (some reviewed, some not)
        AppointmentModel completed1 = new AppointmentModel(
            "apt_003", "BS. Phạm Minh E", "Nội tổng quát", 
            "10/07/2025", "08:30", "Gọi thoại", 300000, "completed"
        );
        completed1.setPatientName(currentUser);
        completed1.setPatientPhone(currentPhone);
        completed1.setProblemDescription("Đau đầu, mệt mỏi");
        completed1.setDuration("30 phút");
        // This one is already reviewed
        completed1.markAsReviewed(5, "Bác sĩ rất tận tâm và chuyên nghiệp!");
        appointments.add(completed1);

        AppointmentModel completed2 = new AppointmentModel(
            "apt_004", "BS. Jenny Watson", "Miễn dịch học", 
            "08/07/2025", "10:00", "Nhắn tin", 200000, "completed"
        );
        completed2.setPatientName(currentUser);
        completed2.setPatientPhone(currentPhone);
        completed2.setProblemDescription("Dị ứng thức ăn");
        completed2.setDuration("15 phút");
        // This one is NOT reviewed yet - user can review
        appointments.add(completed2);

        AppointmentModel completed3 = new AppointmentModel(
            "apt_005", "BS. Drake Boeson", "Thần kinh", 
            "05/07/2025", "15:00", "Gọi video", 500000, "completed"
        );
        completed3.setPatientName(currentUser);
        completed3.setPatientPhone(currentPhone);
        completed3.setProblemDescription("Đau đầu migraine");
        completed3.setDuration("45 phút");
        // This one is NOT reviewed yet - user can review
        appointments.add(completed3);

        // Cancelled appointments
        AppointmentModel cancelled1 = new AppointmentModel(
            "apt_006", "BS. Lê Quang I", "Ngoại khoa", 
            "03/07/2025", "11:00", "Khám trực tiếp", 800000, "cancelled"
        );
        cancelled1.setPatientName(currentUser);
        cancelled1.setPatientPhone(currentPhone);
        cancelled1.setProblemDescription("Đau bụng");
        cancelled1.setDuration("60 phút");
        appointments.add(cancelled1);
    }

    // CRUD Operations
    public List<AppointmentModel> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public List<AppointmentModel> getAppointmentsByStatus(String status) {
        List<AppointmentModel> filteredAppointments = new ArrayList<>();
        for (AppointmentModel appointment : appointments) {
            if (appointment.getStatus().equalsIgnoreCase(status)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    public AppointmentModel getAppointmentById(String id) {
        for (AppointmentModel appointment : appointments) {
            if (appointment.getId().equals(id)) {
                return appointment;
            }
        }
        return null;
    }

    public void addAppointment(AppointmentModel appointment) {
        if (appointment.getId() == null) {
            appointment.setId(generateAppointmentId());
        }
        appointments.add(appointment);
    }

    public void updateAppointment(AppointmentModel updatedAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId().equals(updatedAppointment.getId())) {
                appointments.set(i, updatedAppointment);
                return;
            }
        }
    }

    public void deleteAppointment(String appointmentId) {
        appointments.removeIf(appointment -> appointment.getId().equals(appointmentId));
    }

    // Status Management
    public void cancelAppointment(String appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        if (appointment != null && appointment.canBeCancelled()) {
            appointment.setStatus("cancelled");
            updateAppointment(appointment);
        }
    }

    public void completeAppointment(String appointmentId) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        if (appointment != null && appointment.isUpcoming()) {
            appointment.setStatus("completed");
            updateAppointment(appointment);
        }
    }

    public void rescheduleAppointment(String appointmentId, String newDate, String newTime) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        if (appointment != null && appointment.canBeRescheduled()) {
            appointment.setDate(newDate);
            appointment.setTime(newTime);
            updateAppointment(appointment);
        }
    }

    // Review Management
    public void saveReview(String appointmentId, int rating, String reviewText) {
        saveReview(appointmentId, rating, reviewText, false);
    }

    public void saveReview(String appointmentId, int rating, String reviewText, boolean recommend) {
        AppointmentModel appointment = getAppointmentById(appointmentId);
        if (appointment != null && appointment.canBeReviewed()) {
            appointment.markAsReviewed(rating, reviewText);
            updateAppointment(appointment);
        }
    }

    // Query Methods - These will be replaced by user-specific methods below

    public List<AppointmentModel> getReviewableAppointments() {
        List<AppointmentModel> reviewable = new ArrayList<>();
        for (AppointmentModel appointment : getCompletedAppointments()) {
            if (appointment.canBeReviewed()) {
                reviewable.add(appointment);
            }
        }
        return reviewable;
    }

    public List<AppointmentModel> getReviewedAppointments() {
        List<AppointmentModel> reviewed = new ArrayList<>();
        for (AppointmentModel appointment : getCompletedAppointments()) {
            if (appointment.isReviewed()) {
                reviewed.add(appointment);
            }
        }
        return reviewed;
    }

    // Statistics
    public int getTotalAppointments() {
        return appointments.size();
    }

    public int getAppointmentCountByStatus(String status) {
        return getAppointmentsByStatus(status).size();
    }

    public double getAverageRating() {
        List<AppointmentModel> reviewed = getReviewedAppointments();
        if (reviewed.isEmpty()) return 0.0;

        int totalRating = 0;
        for (AppointmentModel appointment : reviewed) {
            totalRating += appointment.getRating();
        }
        return (double) totalRating / reviewed.size();
    }

    // Utility Methods
    private String generateAppointmentId() {
        return "apt_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void clearAllAppointments() {
        appointments.clear();
    }

    // User-specific data methods
    private String currentUserId = "user_001"; // Mock user ID

    public List<AppointmentModel> getUserAppointments() {
        // In real implementation, filter by user ID
        return new ArrayList<>(appointments);
    }

    public List<AppointmentModel> getUserAppointmentsByStatus(String status) {
        List<AppointmentModel> userAppointments = getUserAppointments();
        List<AppointmentModel> filteredAppointments = new ArrayList<>();
        for (AppointmentModel appointment : userAppointments) {
            if (appointment.getStatus().equalsIgnoreCase(status)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    // Update existing methods to use user-specific data
    public List<AppointmentModel> getUpcomingAppointments() {
        return getUserAppointmentsByStatus("upcoming");
    }

    public List<AppointmentModel> getCompletedAppointments() {
        return getUserAppointmentsByStatus("completed");
    }

    public List<AppointmentModel> getCancelledAppointments() {
        return getUserAppointmentsByStatus("cancelled");
    }

    // Add method to set user context
    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public AppointmentModel createNewAppointment(String doctorName, String specialty, 
                                                String date, String time, String packageType, 
                                                int amount, String patientName, String phone, 
                                                String problemDescription) {
        AppointmentModel appointment = new AppointmentModel();
        appointment.setId(generateAppointmentId());
        appointment.setDoctorName(doctorName);
        appointment.setSpecialty(specialty);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setPackageType(packageType);
        appointment.setAmount(amount);
        appointment.setStatus("upcoming");
        appointment.setPatientName(patientName);
        appointment.setPatientPhone(phone);
        appointment.setProblemDescription(problemDescription);
        
        addAppointment(appointment);
        return appointment;
    }
} 
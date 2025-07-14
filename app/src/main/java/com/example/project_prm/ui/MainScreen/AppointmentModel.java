package com.example.project_prm.ui.MainScreen;

public class AppointmentModel {
    private String id;
    private String doctorName;
    private String specialty;
    private String date;
    private String time;
    private String packageType;
    private int amount;
    private String duration;
    private String status; // "upcoming", "completed", "cancelled"
    private String problemDescription;
    private String patientName;
    private String patientPhone;
    private String emergencyContact;
    private String emergencyPhone;
    private long createdAt;
    private long updatedAt;
    
    // Review tracking
    private boolean isReviewed;
    private int rating;
    private String reviewText;
    private long reviewedAt;

    public AppointmentModel() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.isReviewed = false;
        this.rating = 0;
    }

    public AppointmentModel(String id, String doctorName, String specialty, String date, 
                           String time, String packageType, int amount, String status) {
        this();
        this.id = id;
        this.doctorName = doctorName;
        this.specialty = specialty;
        this.date = date;
        this.time = time;
        this.packageType = packageType;
        this.amount = amount;
        this.status = status;
    }

    // Basic getters and setters
    public String getId() { return id; }
    public void setId(String id) { 
        this.id = id; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { 
        this.doctorName = doctorName; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { 
        this.specialty = specialty; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getDate() { return date; }
    public void setDate(String date) { 
        this.date = date; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getTime() { return time; }
    public void setTime(String time) { 
        this.time = time; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { 
        this.packageType = packageType; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { 
        this.amount = amount; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { 
        this.duration = duration; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { 
        this.problemDescription = problemDescription; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { 
        this.patientName = patientName; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { 
        this.patientPhone = patientPhone; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { 
        this.emergencyContact = emergencyContact; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { 
        this.emergencyPhone = emergencyPhone; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    // Review related getters and setters
    public boolean isReviewed() { return isReviewed; }
    public void setReviewed(boolean reviewed) { 
        this.isReviewed = reviewed; 
        this.updatedAt = System.currentTimeMillis();
        if (reviewed && reviewedAt == 0) {
            this.reviewedAt = System.currentTimeMillis();
        }
    }

    public int getRating() { return rating; }
    public void setRating(int rating) { 
        this.rating = rating; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { 
        this.reviewText = reviewText; 
        this.updatedAt = System.currentTimeMillis(); 
    }

    public long getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(long reviewedAt) { this.reviewedAt = reviewedAt; }

    // Business logic methods
    public boolean canBeReviewed() {
        return "completed".equalsIgnoreCase(status) && !isReviewed;
    }

    public boolean canBeCancelled() {
        return "upcoming".equalsIgnoreCase(status);
    }

    public boolean canBeRescheduled() {
        return "upcoming".equalsIgnoreCase(status);
    }

    public void markAsReviewed(int rating, String reviewText) {
        this.isReviewed = true;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewedAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public String getFormattedAmount() {
        return String.format("%,d đ", amount);
    }

    public String getDateTimeFormatted() {
        return date + " • " + time;
    }

    // Status check methods
    public boolean isUpcoming() {
        return "upcoming".equalsIgnoreCase(status);
    }

    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }

    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "AppointmentModel{" +
                "id='" + id + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", isReviewed=" + isReviewed +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AppointmentModel that = (AppointmentModel) obj;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 
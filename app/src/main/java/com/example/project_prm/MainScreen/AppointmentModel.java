package com.example.project_prm.MainScreen;

import java.util.Date;
import com.example.project_prm.R;

public class AppointmentModel {
    private String id;
    private String doctorName;
    private String doctorSpecialty;
    private String doctorLocation;
    private String packageType;
    private double packagePrice;
    private String date;
    private String time;
    private String status; // "Upcoming", "Completed", "Cancelled"
    private PatientInfo patient;
    private boolean hasReview;
    private String reviewText;
    private int reviewRating;

    public static class PatientInfo {
        private String fullName;
        private String gender;
        private String age;
        private String problem;

        public PatientInfo() {}
        public PatientInfo(String fullName, String gender, String age, String problem) {
            this.fullName = fullName;
            this.gender = gender;
            this.age = age;
            this.problem = problem;
        }
        public String getFullName() { return fullName; }
        public String getGender() { return gender; }
        public String getAge() { return age; }
        public String getProblem() { return problem; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public void setGender(String gender) { this.gender = gender; }
        public void setAge(String age) { this.age = age; }
        public void setProblem(String problem) { this.problem = problem; }
    }

    public AppointmentModel() {}

    public AppointmentModel(String id, String doctorName, String doctorSpecialty, String doctorLocation,
                            String packageType, double packagePrice, String date, String time, String status,
                            PatientInfo patient, boolean hasReview) {
        this.id = id;
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
        this.doctorLocation = doctorLocation;
        this.packageType = packageType;
        this.packagePrice = packagePrice;
        this.date = date;
        this.time = time;
        this.status = status;
        this.patient = patient;
        this.hasReview = hasReview;
    }

    public String getId() { return id; }
    public String getDoctorName() { return doctorName; }
    public String getDoctorSpecialty() { return doctorSpecialty; }
    public String getDoctorLocation() { return doctorLocation; }
    public String getPackageType() { return packageType; }
    public double getPackagePrice() { return packagePrice; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public PatientInfo getPatient() { return patient; }
    public boolean hasReview() { return hasReview; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public int getReviewRating() { return reviewRating; }
    public void setReviewRating(int reviewRating) { this.reviewRating = reviewRating; }

    public void setId(String id) { this.id = id; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setDoctorSpecialty(String doctorSpecialty) { this.doctorSpecialty = doctorSpecialty; }
    public void setDoctorLocation(String doctorLocation) { this.doctorLocation = doctorLocation; }
    public void setPackageType(String packageType) { this.packageType = packageType; }
    public void setPackagePrice(double packagePrice) { this.packagePrice = packagePrice; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setStatus(String status) { this.status = status; }
    public void setPatient(PatientInfo patient) { this.patient = patient; }
    public void setHasReview(boolean hasReview) { this.hasReview = hasReview; }

    // Helper methods for status
    public String getStatusText() {
        switch (status) {
            case "upcoming":
                return "Sắp tới";
            case "completed":
                return "Hoàn thành";
            case "cancelled":
                return "Đã hủy";
            default:
                return status;
        }
    }

    public int getStatusColor() {
        switch (status) {
            case "upcoming":
                return R.color.primary_blue;
            case "completed":
                return R.color.primary_blue;
            case "cancelled":
                return R.color.text_gray;
            default:
                return R.color.text_gray;
        }
    }

    // Constructor for backward compatibility
    public AppointmentModel(String id, String doctorName, String doctorSpecialty, 
                          String doctorLocation, String packageType, double packagePrice,
                          String date, String time, String status) {
        this.id = id;
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
        this.doctorLocation = doctorLocation;
        this.packageType = packageType;
        this.packagePrice = packagePrice;
        this.date = date;
        this.time = time;
        this.status = status;
        this.patient = new PatientInfo();
        this.hasReview = false;
    }
} 
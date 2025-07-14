package com.example.project_prm.Model;

public class ReviewModel {
    private String id;
    private String doctorId;
    private String patientName;
    private String content;
    private float rating;
    private String date;

    public ReviewModel() {}

    public ReviewModel(String id, String doctorId, String patientName, String content, float rating, String date) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
} 
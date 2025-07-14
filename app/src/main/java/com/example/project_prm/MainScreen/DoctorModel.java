package com.example.project_prm.MainScreen;

public class DoctorModel {
    public String id;
    public String name;
    public String specialty;
    public String location;
    public String hospital;
    public String avatarUrl;
    public double rating;
    public int reviewCount;
    public int patientCount;
    public int experienceYears;
    public String about;
    public String workingTime;
    public boolean isAvailable;
    public String description;
    
    public DoctorModel() {}
    
    public DoctorModel(String id, String name, String specialty, String location) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }
    
    public double getRating() {
        return rating;
    }
    
    public String getFormattedRating() {
        return String.format("%.1f", rating);
    }
    
    public String getFormattedPatientCount() {
        if (patientCount >= 1000) {
            return (patientCount / 1000) + ".000+";
        }
        return String.valueOf(patientCount);
    }
    
    public String getFormattedExperience() {
        return experienceYears + "+";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getHospital() { return hospital; }
    public String getExperience() { return experienceYears + " năm"; }
    public int getReviewCount() { return reviewCount; }
} 
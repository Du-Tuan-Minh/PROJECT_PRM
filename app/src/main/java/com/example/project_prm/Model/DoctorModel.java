package com.example.project_prm.Model;

public class DoctorModel {
    private String id;
    private String name;
    private String specialty;
    private String hospital;
    private float rating;
    private int experience;
    private String image;
    private String description;
    private String location;
    private int reviewCount;
    private int experienceYears;

    public DoctorModel() {}

    public DoctorModel(String id, String name, String specialty, String hospital, float rating, int experience, String image, String description, String location, int reviewCount, int experienceYears) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.hospital = hospital;
        this.rating = rating;
        this.experience = experience;
        this.image = image;
        this.description = description;
        this.location = location;
        this.reviewCount = reviewCount;
        this.experienceYears = experienceYears;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
} 
package com.example.project_prm.DataManager.Entity;

public class Doctor {
    private String name;
    private String specialty;
    private String experience;
    private String clinic;
    private String specialtyCode;
    private float rating;
    private int reviewCount;
    private String description;
    private String avatarResource;

    public Doctor() {}

    public Doctor(String name, String specialty, String experience, String clinic,
                  String specialtyCode, float rating, int reviewCount, String description, String avatarResource) {
        this.name = name;
        this.specialty = specialty;
        this.experience = experience;
        this.clinic = clinic;
        this.specialtyCode = specialtyCode;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.description = description;
        this.avatarResource = avatarResource;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getSpecialtyCode() {
        return specialtyCode;
    }

    public void setSpecialtyCode(String specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(String avatarResource) {
        this.avatarResource = avatarResource;
    }

    // Helper methods
    public String getFormattedRating() {
        return String.format("%.1f", rating);
    }

    public String getFormattedReviewCount() {
        if (reviewCount >= 1000) {
            return String.format("%.1fk", reviewCount / 1000.0);
        }
        return String.valueOf(reviewCount);
    }

    public String getRatingWithReviews() {
        return String.format("%.1f (%d reviews)", rating, reviewCount);
    }

    public boolean isTopRated() {
        return rating >= 4.7f;
    }

    public boolean isExperienced() {
        return experience.contains("8+") || experience.contains("9+") ||
                experience.contains("10+") || experience.contains("11+") ||
                experience.contains("12+");
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", clinic='" + clinic + '\'' +
                ", rating=" + rating +
                '}';
    }
}
package com.example.project_prm.MainScreen;

import com.google.android.gms.maps.model.LatLng;

public class ClinicModel {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String description;
    private double rating;
    private int reviewCount;
    private String imageUrl;
    private LatLng location;
    private String specialties;
    private String workingHours;
    private boolean isOpen;
    private double distance; // Distance from user location in km

    public ClinicModel() {}

    public ClinicModel(String id, String name, String address, String phone, 
                      String email, String website, String description, 
                      double rating, int reviewCount, String imageUrl, 
                      LatLng location, String specialties, String workingHours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.location = location;
        this.specialties = specialties;
        this.workingHours = workingHours;
        this.isOpen = true; // Default to open
        this.distance = 0.0;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getWebsite() { return website; }
    public String getDescription() { return description; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getImageUrl() { return imageUrl; }
    public LatLng getLocation() { return location; }
    public String getSpecialties() { return specialties; }
    public String getWorkingHours() { return workingHours; }
    public boolean isOpen() { return isOpen; }
    public double getDistance() { return distance; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setWebsite(String website) { this.website = website; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setLocation(LatLng location) { this.location = location; }
    public void setSpecialties(String specialties) { this.specialties = specialties; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
    public void setOpen(boolean open) { isOpen = open; }
    public void setDistance(double distance) { this.distance = distance; }

    // Helper methods
    public String getFormattedDistance() {
        if (distance < 1) {
            return String.format("%.0f m", distance * 1000);
        } else {
            return String.format("%.1f km", distance);
        }
    }

    public String getFormattedRating() {
        return String.format("%.1f", rating);
    }

    public String getFormattedReviewCount() {
        return reviewCount + " đánh giá";
    }
} 
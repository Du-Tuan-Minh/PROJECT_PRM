package com.example.project_prm.ui.MainScreen;

import com.google.android.gms.maps.model.LatLng;

public class ClinicModel {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private double rating;
    private int reviewCount;
    private LatLng location;
    private String imageUrl;
    private boolean isOpen;
    private String workingHours;
    private String specialties;

    public ClinicModel(String id, String name, String address, String phone, 
                      String description, double rating, int reviewCount, 
                      LatLng location, String imageUrl, boolean isOpen, 
                      String workingHours, String specialties) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.location = location;
        this.imageUrl = imageUrl;
        this.isOpen = isOpen;
        this.workingHours = workingHours;
        this.specialties = specialties;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public LatLng getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public boolean isOpen() { return isOpen; }
    public String getWorkingHours() { return workingHours; }
    public String getSpecialties() { return specialties; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public void setLocation(LatLng location) { this.location = location; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setOpen(boolean open) { isOpen = open; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
    public void setSpecialties(String specialties) { this.specialties = specialties; }
} 
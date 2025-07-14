package com.example.project_prm.ui.MainScreen;

import java.util.List;

public class EnhancedClinicModel {
    public String id;
    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public String phone;
    public String email;
    public String website;
    public String specialty;
    public String workingHours;
    public double rating;
    public int reviewCount;
    public String imageUrl;
    public List<String> services;
    public List<String> facilities;
    public String priceRange;
    public boolean isOpen;
    public double distance; // in km
    
    public EnhancedClinicModel() {}
    
    public EnhancedClinicModel(String id, String name, String address, String specialty) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.specialty = specialty;
    }
    
    public String getFormattedRating() {
        return String.format("%.1f", rating);
    }
    
    public String getFormattedDistance() {
        if (distance < 1.0) {
            return String.format("%.0f m", distance * 1000);
        }
        return String.format("%.1f km", distance);
    }
    
    public String getStatusText() {
        return isOpen ? "Đang mở" : "Đã đóng";
    }
    
    public int getStatusColor() {
        return isOpen ? android.R.color.holo_green_dark : android.R.color.holo_red_dark;
    }
} 
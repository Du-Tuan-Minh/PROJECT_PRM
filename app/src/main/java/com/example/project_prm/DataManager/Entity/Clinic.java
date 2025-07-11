package com.example.project_prm.DataManager.Entity;

import java.util.Arrays;

public class Clinic {
    private String name;
    private String address;
    private String hours;
    private String phone;
    private String email;
    private float rating;
    private int reviewCount;
    private String description;
    private String[] services;
    private String logoResource;

    public Clinic() {}

    public Clinic(String name, String address, String hours, String phone, String email,
                  float rating, int reviewCount, String description, String[] services, String logoResource) {
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.description = description;
        this.services = services;
        this.logoResource = logoResource;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public String getLogoResource() {
        return logoResource;
    }

    public void setLogoResource(String logoResource) {
        this.logoResource = logoResource;
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

    public String getServicesAsString() {
        if (services == null || services.length == 0) {
            return "General Services";
        }
        return String.join(", ", services);
    }

    public boolean hasService(String service) {
        if (services == null) return false;

        for (String clinicService : services) {
            if (clinicService.toLowerCase().contains(service.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTopRated() {
        return rating >= 4.5f;
    }

    public boolean isHighlyReviewed() {
        return reviewCount >= 200;
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", services=" + Arrays.toString(services) +
                '}';
    }
}
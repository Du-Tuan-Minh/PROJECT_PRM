package com.example.project_prm.DataManager.Entity;

import java.util.Arrays;

public class Clinic {
    private String id;
    private String name;
    private String address;
    private String hours; // Keep original field name
    private String phone;
    private String email;
    private float rating;
    private int reviewCount; // Keep original field name
    private String description;
    private String[] services; // Keep original field name
    private String logoResource;
    private double latitude;
    private double longitude;
    private boolean isOpen;

    public Clinic() {}

    // ✅ MAIN CONSTRUCTOR (handles both old and new usage)
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
        this.latitude = 0;
        this.longitude = 0;

        // Generate ID from name
        this.id = generateIdFromName(name);
    }

    // ✅ CONSTRUCTOR with location (for new usage)
    public Clinic(String name, String address, String hours, String phone, String email,
                  float rating, int reviewCount, String description, String[] services,
                  String logoResource, double latitude, double longitude) {
        this(name, address, hours, phone, email, rating, reviewCount, description, services, logoResource);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String generateIdFromName(String name) {
        if (name == null) return "clinic_unknown";
        return "clinic_" + name.toLowerCase()
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    // ✅ GETTERS AND SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (this.id == null) {
            this.id = generateIdFromName(name);
        }
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

    // ✅ ALIAS METHODS for ClinicRepository compatibility
    public String getWorkingHours() {
        return hours;
    }

    public void setWorkingHours(String workingHours) {
        this.hours = workingHours;
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

    // ✅ ALIAS for compatibility
    public int getTotalReviews() {
        return reviewCount;
    }

    public void setTotalReviews(int totalReviews) {
        this.reviewCount = totalReviews;
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

    // ✅ ALIAS for compatibility
    public String[] getSpecialties() {
        return services;
    }

    public void setSpecialties(String[] specialties) {
        this.services = specialties;
    }

    public String getLogoResource() {
        return logoResource;
    }

    public void setLogoResource(String logoResource) {
        this.logoResource = logoResource;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    // ✅ HELPER METHODS
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

    // ✅ ALIAS for compatibility
    public String getSpecialtiesAsString() {
        return getServicesAsString();
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

    // ✅ ALIAS for compatibility
    public boolean hasSpecialty(String specialty) {
        return hasService(specialty);
    }

    public boolean hasLocation() {
        return latitude != 0 && longitude != 0;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", services=" + Arrays.toString(services) +
                '}';
    }
}
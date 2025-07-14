package com.example.project_prm.Model;

public class ServicePackage {
    private String id;
    private String name;
    private int price;
    private String duration;
    private String description;
    private String iconResource;
    private boolean isAvailable;

    public ServicePackage() {
        this.isAvailable = true;
    }

    public ServicePackage(String id, String name, int price, String duration, String description) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.description = description;
        setIconResource();
    }

    // Auto set icon based on service type
    private void setIconResource() {
        switch (id) {
            case "message":
                this.iconResource = "ic_message";
                break;
            case "voice":
                this.iconResource = "ic_phone";
                break;
            case "video":
                this.iconResource = "ic_video";
                break;
            case "in_person":
                this.iconResource = "ic_hospital";
                break;
            default:
                this.iconResource = "ic_service";
                break;
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setIconResource();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconResource() {
        return iconResource;
    }

    public void setIconResource(String iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Utility methods
    public String getFormattedPrice() {
        return String.format("%,d đ", price);
    }

    public String getDisplayInfo() {
        return name + " - " + getFormattedPrice() + " (" + duration + ")";
    }

    @Override
    public String toString() {
        return "ServicePackage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", duration='" + duration + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServicePackage that = (ServicePackage) obj;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 
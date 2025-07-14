package com.example.project_prm.MainScreen;

public class PackageModel {
    private String id;
    private String name;
    private String description;
    private int price;
    private String duration;
    private String features;

    public PackageModel(String id, String name, String description, int price, String duration, String features) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.features = features;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public String getFormattedPrice() {
        return String.format("%,dđ", price);
    }
} 
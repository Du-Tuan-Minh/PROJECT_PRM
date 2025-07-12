package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class Clinic {
    public long id;
    public String name, address, phone, email, website, specialties, working_hours, image_url;
    public double latitude, longitude, rating;
    public int total_reviews;

    public Clinic(long id, String name, String address, double latitude, double longitude, String phone,
                  String email, String website, String specialties, String working_hours,
                  double rating, int total_reviews, String image_url) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.specialties = specialties;
        this.working_hours = working_hours;
        this.rating = rating;
        this.total_reviews = total_reviews;
        this.image_url = image_url;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("address", address);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("phone", phone);
        map.put("email", email);
        map.put("website", website);
        map.put("specialties", specialties);
        map.put("working_hours", working_hours);
        map.put("rating", rating);
        map.put("total_reviews", total_reviews);
        map.put("image_url", image_url);
        return map;
    }
}

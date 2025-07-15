package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class Clinic {
    public long id;
    public String name, address, phone, email, website
            , specialties, working_hours, image_url
            , user_id;
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

    public Clinic(long id, String name, String address, double latitude, double longitude,
                  String phone, String email, String website, String specialties,
                  String working_hours, double rating, int total_reviews,
                  String image_url, String user_id) {
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
        this.user_id = user_id;
    }

    public Clinic(String name, String phone, String email,
                  String address, String specialties, String userId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialties = specialties;
        this.user_id = userId;
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
        map.put("user_id", user_id);
        return map;
    }
    public static Clinic fromMap(Map<String, Object> map) {
        long id = (long) map.get("id");
        String name = (String) map.get("name");
        String address = (String) map.get("address");
        double latitude = (double) map.get("latitude");
        double longitude = (double) map.get("longitude");
        String phone = (String) map.get("phone");
        String email = (String) map.get("email");
        String website = (String) map.get("website");
        String specialties = (String) map.get("specialties");
        double rating = (double) map.get("rating");
        String image_url = (String) map.get("image_url");
        String user_id = (String) map.get("user_id");
        return new Clinic(id, name, address, latitude, longitude, phone, email, website,
                specialties, "", rating, 1, image_url, user_id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(int total_reviews) {
        this.total_reviews = total_reviews;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

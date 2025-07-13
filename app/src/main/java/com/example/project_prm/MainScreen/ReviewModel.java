package com.example.project_prm.MainScreen;

public class ReviewModel {
    public String reviewerName;
    public int rating;
    public String reviewText;
    public int likes;
    public String timeAgo;
    
    public ReviewModel() {}
    
    public ReviewModel(String reviewerName, int rating, String reviewText, int likes, String timeAgo) {
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.likes = likes;
        this.timeAgo = timeAgo;
    }
} 
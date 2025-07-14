package com.example.project_prm.ui.Article;


public class Article {
    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private String category;
    private String date;
    private boolean isBookmarked;
    private boolean isTrending;

    public Article() {}

    public Article(int id, String title, String content, String imageUrl,
                   String category, String date, boolean isBookmarked, boolean isTrending) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.date = date;
        this.isBookmarked = isBookmarked;
        this.isTrending = isTrending;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }

    public boolean isTrending() { return isTrending; }
    public void setTrending(boolean trending) { isTrending = trending; }
}

package com.example.project_prm.MainScreen;

public class Notification {
    private String content;
    private long timestamp;

    public Notification() {}
    public Notification(String content, long timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
} 
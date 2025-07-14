package com.example.project_prm.ui.MainScreen;

public class Notification {
    private String title;
    private String description;
    private String iconType;
    private long timestamp;
    private boolean isNew;

    public Notification() {}
    public Notification(String title, String description, String iconType, long timestamp, boolean isNew) {
        this.title = title;
        this.description = description;
        this.iconType = iconType;
        this.timestamp = timestamp;
        this.isNew = isNew;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIconType() { return iconType; }
    public void setIconType(String iconType) { this.iconType = iconType; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public boolean isNew() { return isNew; }
    public void setNew(boolean aNew) { isNew = aNew; }
} 
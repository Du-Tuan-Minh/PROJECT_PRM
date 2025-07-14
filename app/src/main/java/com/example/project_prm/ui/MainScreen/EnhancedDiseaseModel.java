package com.example.project_prm.ui.MainScreen;

public class EnhancedDiseaseModel {
    public String id;
    public String name;
    public String englishName;
    public String category;
    public String description;
    public String symptoms;
    public String causes;
    public String treatment;
    public String prevention;
    public String whenToSeeDoctor;
    public String severity; // mild, moderate, severe
    public boolean isBookmarked;
    
    public EnhancedDiseaseModel() {}
    
    public EnhancedDiseaseModel(String id, String name, String category, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
    }
    
    public String getSeverityColor() {
        switch (severity) {
            case "mild":
                return "#4CAF50"; // Green
            case "moderate":
                return "#FF9800"; // Orange
            case "severe":
                return "#F44336"; // Red
            default:
                return "#9E9E9E"; // Gray
        }
    }
    
    public String getSeverityText() {
        switch (severity) {
            case "mild":
                return "Nhẹ";
            case "moderate":
                return "Trung bình";
            case "severe":
                return "Nghiêm trọng";
            default:
                return "Không xác định";
        }
    }
} 
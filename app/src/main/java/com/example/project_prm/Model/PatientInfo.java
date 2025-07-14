package com.example.project_prm.Model;

public class PatientInfo {
    private String fullName;
    private String gender;
    private int age;
    private String phone;
    private String problemDescription;
    private ServicePackage servicePackage;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private long createdAt;
    private long updatedAt;

    public PatientInfo() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public PatientInfo(String fullName, String gender, int age, String phone, String problemDescription) {
        this();
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.problemDescription = problemDescription;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.updatedAt = System.currentTimeMillis();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
        this.updatedAt = System.currentTimeMillis();
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
        this.updatedAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Validation methods
    public boolean isValid() {
        return fullName != null && !fullName.trim().isEmpty() &&
               gender != null && !gender.trim().isEmpty() &&
               age > 0 && age <= 120 &&
               phone != null && isValidPhoneNumber(phone) &&
               problemDescription != null && !problemDescription.trim().isEmpty() &&
               servicePackage != null;
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^(\\+84|0)[3|5|7|8|9]\\d{8}$");
    }

    @Override
    public String toString() {
        return "PatientInfo{" +
                "fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", servicePackage=" + (servicePackage != null ? servicePackage.getName() : "null") +
                '}';
    }
} 
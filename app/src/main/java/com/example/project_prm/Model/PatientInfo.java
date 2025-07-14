package com.example.project_prm.Model;

public class PatientInfo {
    private String name;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String address;
    private String problem;

    public PatientInfo() {}

    public PatientInfo(String name, String gender, String dateOfBirth, String phone, String address, String problem) {
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
        this.problem = problem;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProblem() { return problem; }
    public void setProblem(String problem) { this.problem = problem; }
} 
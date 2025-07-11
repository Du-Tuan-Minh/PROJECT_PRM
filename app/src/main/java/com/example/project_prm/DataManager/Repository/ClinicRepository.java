package com.example.project_prm.DataManager.Repository;

import com.example.project_prm.DataManager.Entity.Clinic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClinicRepository {

    private static ClinicRepository instance;
    private Map<String, Clinic> clinicMap;

    private ClinicRepository() {
        initializeClinics();
    }

    public static synchronized ClinicRepository getInstance() {
        if (instance == null) {
            instance = new ClinicRepository();
        }
        return instance;
    }

    private void initializeClinics() {
        clinicMap = new HashMap<>();

        // Initialize sample clinics with real data
        addClinic(new Clinic(
                "Health Care Clinic",
                "123 Medical Center Blvd, Downtown",
                "Mon-Fri: 8:00 AM - 6:00 PM\nSat: 9:00 AM - 4:00 PM\nSun: Closed",
                "+1 (555) 123-4567",
                "info@healthcareclinic.com",
                4.7f,
                320,
                "Full-service medical clinic providing comprehensive healthcare services including general medicine, preventive care, and specialist consultations.",
                new String[]{"General Medicine", "Dermatology", "Cardiology"},
                "healthcare_clinic_logo"
        ));

        addClinic(new Clinic(
                "Central Medical Center",
                "456 Health Ave, Medical District",
                "24/7 Emergency Services\nOutpatient: Mon-Sat 7:00 AM - 8:00 PM",
                "+1 (555) 234-5678",
                "contact@centralmedical.com",
                4.8f,
                450,
                "State-of-the-art medical center with advanced diagnostic equipment and specialized departments for various medical conditions.",
                new String[]{"Emergency Medicine", "Neurology", "Orthopedics", "Radiology"},
                "central_medical_logo"
        ));

        addClinic(new Clinic(
                "Family Health Center",
                "789 Wellness St, Suburban Area",
                "Mon-Fri: 8:30 AM - 5:30 PM\nSat: 9:00 AM - 2:00 PM",
                "+1 (555) 345-6789",
                "appointments@familyhealth.com",
                4.6f,
                280,
                "Family-oriented healthcare facility specializing in pediatrics, family medicine, and women's health services.",
                new String[]{"Family Medicine", "Pediatrics", "Women's Health", "Vaccination"},
                "family_health_logo"
        ));

        addClinic(new Clinic(
                "Wellness Medical Center",
                "321 Care Road, Healthcare Plaza",
                "Mon-Thu: 9:00 AM - 7:00 PM\nFri: 9:00 AM - 5:00 PM\nWeekends: By appointment",
                "+1 (555) 456-7890",
                "wellness@medicalcenter.com",
                4.5f,
                195,
                "Integrated wellness center focusing on preventive medicine, chronic disease management, and holistic health approaches.",
                new String[]{"General Practice", "Nutrition", "Mental Health", "Physical Therapy"},
                "wellness_medical_logo"
        ));

        addClinic(new Clinic(
                "Advanced Health Clinic",
                "654 Innovation Blvd, Tech District",
                "Mon-Fri: 7:00 AM - 8:00 PM\nSat-Sun: 8:00 AM - 6:00 PM",
                "+1 (555) 567-8901",
                "info@advancedhealth.com",
                4.9f,
                520,
                "Cutting-edge medical facility with the latest technology for advanced diagnostics, treatments, and minimally invasive procedures.",
                new String[]{"Advanced Diagnostics", "Dermatology", "Cosmetic Surgery", "Laser Therapy"},
                "advanced_health_logo"
        ));

        addClinic(new Clinic(
                "Community Health Center",
                "987 Community Ave, Residential Area",
                "Mon-Fri: 8:00 AM - 6:00 PM\nSat: 9:00 AM - 3:00 PM",
                "+1 (555) 678-9012",
                "community@healthcenter.org",
                4.4f,
                210,
                "Community-focused healthcare center providing affordable medical services and health education programs for all ages.",
                new String[]{"Community Health", "Pediatrics", "Senior Care", "Health Education"},
                "community_health_logo"
        ));

        addClinic(new Clinic(
                "Metro Health Clinic",
                "159 Metropolitan St, City Center",
                "Mon-Fri: 7:30 AM - 7:00 PM\nWeekends: 9:00 AM - 5:00 PM",
                "+1 (555) 789-0123",
                "metro@healthclinic.com",
                4.3f,
                165,
                "Urban healthcare facility serving the metropolitan area with quick access to medical services and specialist referrals.",
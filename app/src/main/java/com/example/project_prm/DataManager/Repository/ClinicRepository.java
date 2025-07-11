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

        // ✅ Initialize sample clinics using EXISTING constructor signature
        // Constructor: (name, address, hours, phone, email, rating, reviewCount, description, services, logoResource)

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
                new String[]{"General Medicine", "Urgent Care", "Occupational Health", "Travel Medicine"},
                "metro_health_logo"
        ));

        addClinic(new Clinic(
                "City Medical Center",
                "741 Downtown Medical Plaza",
                "Mon-Fri: 8:00 AM - 8:00 PM\nSat: 9:00 AM - 5:00 PM\nSun: 10:00 AM - 4:00 PM",
                "+1 (555) 890-1234",
                "info@citymedical.com",
                4.8f,
                390,
                "Premier medical center in the heart of the city offering comprehensive healthcare services with state-of-the-art facilities.",
                new String[]{"Cardiology", "Neurology", "Gastroenterology", "Endocrinology"},
                "city_medical_logo"
        ));

        // ✅ Add location data after creation
        setClinicLocations();
    }

    private void setClinicLocations() {
        // Set location for clinics that have location data
        Clinic healthCare = getClinicByName("Health Care Clinic");
        if (healthCare != null) {
            healthCare.setLatitude(21.0285);
            healthCare.setLongitude(105.8542);
        }

        Clinic centralMedical = getClinicByName("Central Medical Center");
        if (centralMedical != null) {
            centralMedical.setLatitude(21.0245);
            centralMedical.setLongitude(105.8412);
        }

        Clinic familyHealth = getClinicByName("Family Health Center");
        if (familyHealth != null) {
            familyHealth.setLatitude(21.0368);
            familyHealth.setLongitude(105.8472);
        }

        Clinic wellness = getClinicByName("Wellness Medical Center");
        if (wellness != null) {
            wellness.setLatitude(21.0311);
            wellness.setLongitude(105.8516);
        }

        Clinic advanced = getClinicByName("Advanced Health Clinic");
        if (advanced != null) {
            advanced.setLatitude(21.0333);
            advanced.setLongitude(105.8500);
        }

        Clinic community = getClinicByName("Community Health Center");
        if (community != null) {
            community.setLatitude(21.0199);
            community.setLongitude(105.8456);
        }

        Clinic metro = getClinicByName("Metro Health Clinic");
        if (metro != null) {
            metro.setLatitude(21.0277);
            metro.setLongitude(105.8355);
        }

        Clinic city = getClinicByName("City Medical Center");
        if (city != null) {
            city.setLatitude(21.0300);
            city.setLongitude(105.8600);
        }
    }

    private void addClinic(Clinic clinic) {
        clinicMap.put(clinic.getId(), clinic);
    }

    // Repository methods
    public Clinic getClinicById(String id) {
        return clinicMap.get(id);
    }

    public Clinic getClinicByName(String name) {
        for (Clinic clinic : clinicMap.values()) {
            if (clinic.getName().equalsIgnoreCase(name)) {
                return clinic;
            }
        }
        return null;
    }

    public List<Clinic> getAllClinics() {
        return new ArrayList<>(clinicMap.values());
    }

    public List<Clinic> searchClinicsByName(String query) {
        List<Clinic> results = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            return getAllClinics();
        }

        String lowerQuery = query.toLowerCase();
        for (Clinic clinic : clinicMap.values()) {
            if (clinic.getName().toLowerCase().contains(lowerQuery) ||
                    clinic.getAddress().toLowerCase().contains(lowerQuery)) {
                results.add(clinic);
            }
        }
        return results;
    }

    public List<Clinic> getClinicsBySpecialty(String specialty) {
        List<Clinic> results = new ArrayList<>();
        if (specialty == null || specialty.isEmpty()) {
            return getAllClinics();
        }

        for (Clinic clinic : clinicMap.values()) {
            if (clinic.hasSpecialty(specialty)) {
                results.add(clinic);
            }
        }
        return results;
    }

    public List<Clinic> searchClinicsWithFilters(String name, String specialty, String sortBy) {
        List<Clinic> results = new ArrayList<>();

        // Start with all clinics
        for (Clinic clinic : clinicMap.values()) {
            boolean matches = true;

            // Filter by name
            if (name != null && !name.isEmpty()) {
                if (!clinic.getName().toLowerCase().contains(name.toLowerCase()) &&
                        !clinic.getAddress().toLowerCase().contains(name.toLowerCase())) {
                    matches = false;
                }
            }

            // Filter by specialty
            if (specialty != null && !specialty.isEmpty() && matches) {
                if (!clinic.hasSpecialty(specialty)) {
                    matches = false;
                }
            }

            if (matches) {
                results.add(clinic);
            }
        }

        // Sort results
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "rating":
                    results.sort((a, b) -> Float.compare(b.getRating(), a.getRating()));
                    break;
                case "name":
                    results.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                    break;
                case "reviews":
                    results.sort((a, b) -> Integer.compare(b.getTotalReviews(), a.getTotalReviews()));
                    break;
                default:
                    // Default sort by rating
                    results.sort((a, b) -> Float.compare(b.getRating(), a.getRating()));
                    break;
            }
        }

        return results;
    }

    public List<String> getAllSpecialties() {
        List<String> specialties = new ArrayList<>();
        for (Clinic clinic : clinicMap.values()) {
            if (clinic.getSpecialties() != null) {
                for (String specialty : clinic.getSpecialties()) {
                    if (!specialties.contains(specialty)) {
                        specialties.add(specialty);
                    }
                }
            }
        }
        return specialties;
    }

    public int getTotalClinicsCount() {
        return clinicMap.size();
    }

    public List<Clinic> getTopRatedClinics(int limit) {
        List<Clinic> allClinics = getAllClinics();
        allClinics.sort((a, b) -> Float.compare(b.getRating(), a.getRating()));

        if (limit > 0 && limit < allClinics.size()) {
            return allClinics.subList(0, limit);
        }
        return allClinics;
    }

    // Calculate distance (simple implementation)
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km

        return distance;
    }

    public List<Clinic> getClinicsNearLocation(double latitude, double longitude, double radiusKm) {
        List<Clinic> nearbyClinics = new ArrayList<>();

        for (Clinic clinic : clinicMap.values()) {
            if (clinic.hasLocation()) {
                double distance = calculateDistance(latitude, longitude,
                        clinic.getLatitude(), clinic.getLongitude());
                if (distance <= radiusKm) {
                    nearbyClinics.add(clinic);
                }
            }
        }

        // Sort by distance
        nearbyClinics.sort((a, b) -> {
            double distA = calculateDistance(latitude, longitude, a.getLatitude(), a.getLongitude());
            double distB = calculateDistance(latitude, longitude, b.getLatitude(), b.getLongitude());
            return Double.compare(distA, distB);
        });

        return nearbyClinics;
    }
}
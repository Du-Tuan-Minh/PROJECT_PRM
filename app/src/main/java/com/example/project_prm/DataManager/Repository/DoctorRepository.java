package com.example.project_prm.DataManager.Repository;

import com.example.project_prm.DataManager.Entity.Doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorRepository {

    private static DoctorRepository instance;
    private Map<String, Doctor> doctorMap;

    private DoctorRepository() {
        initializeDoctors();
    }

    public static synchronized DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }
        return instance;
    }

    private void initializeDoctors() {
        doctorMap = new HashMap<>();

        // Initialize sample doctors with real data
        addDoctor(new Doctor(
                "Dr. Drake Boeson",
                "Dermatologist",
                "5+ years of experience",
                "Health Care Clinic",
                "dermatology",
                4.8f,
                150,
                "Specializes in skin conditions, acne treatment, and cosmetic dermatology. Board-certified dermatologist with extensive experience in treating various skin disorders.",
                "avatar_drake_boeson"
        ));

        addDoctor(new Doctor(
                "Dr. Aidan Allende",
                "Neurologist",
                "8+ years of experience",
                "Central Medical Center",
                "neurology",
                4.9f,
                200,
                "Expert in neurological disorders, brain injuries, and nervous system diseases. Specialized in migraine treatment and epilepsy management.",
                "avatar_aidan_allende"
        ));

        addDoctor(new Doctor(
                "Dr. Jenny Watson",
                "Cardiologist",
                "10+ years of experience",
                "Family Health Center",
                "cardiology",
                4.7f,
                180,
                "Cardiovascular specialist focusing on heart disease prevention, hypertension management, and cardiac rehabilitation programs.",
                "avatar_jenny_watson"
        ));

        addDoctor(new Doctor(
                "Dr. Iker Hall",
                "General Practice",
                "6+ years of experience",
                "Wellness Medical Center",
                "general",
                4.6f,
                120,
                "Family medicine physician providing comprehensive healthcare for patients of all ages. Expertise in preventive care and chronic disease management.",
                "avatar_iker_hall"
        ));

        addDoctor(new Doctor(
                "Dr. Jada Srinky",
                "Dermatologist",
                "7+ years of experience",
                "Advanced Health Clinic",
                "dermatology",
                4.8f,
                160,
                "Advanced dermatological treatments including laser therapy, skin cancer screening, and anti-aging procedures.",
                "avatar_jada_srinky"
        ));

        addDoctor(new Doctor(
                "Dr. Maria Foose",
                "Pediatrician",
                "12+ years of experience",
                "Community Health Center",
                "pediatrics",
                4.9f,
                220,
                "Pediatric specialist with expertise in child development, vaccinations, and adolescent medicine. Gentle approach with children.",
                "avatar_maria_foose"
        ));

        addDoctor(new Doctor(
                "Dr. Raul Zirkind",
                "Gastroenterologist",
                "9+ years of experience",
                "Metro Health Clinic",
                "gastroenterology",
                4.5f,
                140,
                "Digestive system specialist treating conditions like IBS, GERD, and liver diseases. Expert in endoscopic procedures.",
                "avatar_raul_zirkind"
        ));

        addDoctor(new Doctor(
                "Dr. Keegan Dash",
                "Orthopedist",
                "11+ years of experience",
                "City Medical Center",
                "orthopedics",
                4.7f,
                190,
                "Orthopedic surgeon specializing in joint replacement, sports injuries, and spine surgery. Minimally invasive techniques.",
                "avatar_keegan_dash"
        ));
    }

    private void addDoctor(Doctor doctor) {
        doctorMap.put(doctor.getName(), doctor);
    }

    public Doctor getDoctorByName(String name) {
        return doctorMap.get(name);
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctorMap.values());
    }

    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        List<Doctor> result = new ArrayList<>();
        for (Doctor doctor : doctorMap.values()) {
            if (doctor.getSpecialtyCode().equalsIgnoreCase(specialty)) {
                result.add(doctor);
            }
        }
        return result;
    }

    public List<Doctor> searchDoctors(String query) {
        List<Doctor> result = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Doctor doctor : doctorMap.values()) {
            if (doctor.getName().toLowerCase().contains(lowerQuery) ||
                    doctor.getSpecialty().toLowerCase().contains(lowerQuery) ||
                    doctor.getClinic().toLowerCase().contains(lowerQuery) ||
                    doctor.getDescription().toLowerCase().contains(lowerQuery)) {
                result.add(doctor);
            }
        }
        return result;
    }

    // Get appointment types for a doctor
    public List<String> getAppointmentTypesForDoctor(String doctorName) {
        Doctor doctor = getDoctorByName(doctorName);
        if (doctor == null) {
            return getDefaultAppointmentTypes();
        }

        List<String> types = new ArrayList<>();

        // Add available types based on doctor's specialty
        switch (doctor.getSpecialtyCode().toLowerCase()) {
            case "dermatology":
                types.add("Messaging");
                types.add("Video Call");
                break;
            case "neurology":
                types.add("Video Call");
                types.add("Voice Call");
                break;
            case "cardiology":
                types.add("Voice Call");
                types.add("Video Call");
                break;
            case "general":
                types.add("Messaging");
                types.add("Voice Call");
                break;
            default:
                return getDefaultAppointmentTypes();
        }

        return types;
    }

    private List<String> getDefaultAppointmentTypes() {
        List<String> types = new ArrayList<>();
        types.add("Messaging");
        types.add("Video Call");
        types.add("Voice Call");
        return types;
    }

    // Get fee for appointment type
    public double getAppointmentFee(String doctorName, String appointmentType) {
        Doctor doctor = getDoctorByName(doctorName);
        if (doctor == null) {
            return getDefaultFee(appointmentType);
        }

        // Base fee calculation
        double baseFee = getDefaultFee(appointmentType);

        // Adjust based on doctor's experience and rating
        if (doctor.getRating() >= 4.8f) {
            baseFee *= 1.2; // 20% premium for top-rated doctors
        }

        if (doctor.getExperience().contains("10+") || doctor.getExperience().contains("11+") || doctor.getExperience().contains("12+")) {
            baseFee *= 1.1; // 10% premium for very experienced doctors
        }

        return Math.round(baseFee);
    }

    private double getDefaultFee(String appointmentType) {
        switch (appointmentType.toLowerCase()) {
            case "messaging":
                return 25.0;
            case "voice call":
                return 35.0;
            case "video call":
                return 45.0;
            default:
                return 30.0;
        }
    }
}
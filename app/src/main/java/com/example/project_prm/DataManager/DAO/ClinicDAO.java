// NEW FILE: app/src/main/java/com/example/project_prm/DataManager/DAO/ClinicDAO.java
package com.example.project_prm.DataManager.DAO;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for Clinic operations using Firebase Firestore
 * Used for Tung's search functionality (Chức năng 6)
 */
public class ClinicDAO {
    private final FirebaseFirestore db;
    private static final String COLLECTION_CLINICS = "clinics";

    public ClinicDAO() {
        this.db = FirebaseFirestore.getInstance();
    }

    // ========== SEARCH METHODS FOR TUNG'S FEATURES ==========

    /**
     * Search clinics by name (Chức năng 6: Search phòng khám)
     */
    public void searchClinicsByName(String name, OnClinicSearchListener listener) {
        db.collection(COLLECTION_CLINICS)
                .whereGreaterThanOrEqualTo("name", name)
                .whereLessThanOrEqualTo("name", name + '\uf8ff')
                .orderBy("name")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> clinics = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> clinic = document.getData();
                        clinic.put("id", document.getId());
                        clinics.add(clinic);
                    }
                    listener.onSuccess(clinics);
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Search clinics by specialty
     */
    public void searchClinicsBySpecialty(String specialty, OnClinicSearchListener listener) {
        db.collection(COLLECTION_CLINICS)
                .whereArrayContains("specialties", specialty)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> clinics = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> clinic = document.getData();
                        clinic.put("id", document.getId());
                        clinics.add(clinic);
                    }
                    listener.onSuccess(clinics);
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Search clinics by location (within radius)
     */
    public void searchClinicsByLocation(double userLat, double userLng, double radiusKm, OnClinicSearchListener listener) {
        // Firebase doesn't support native geo queries, so we'll get all clinics and filter
        db.collection(COLLECTION_CLINICS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> clinics = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> clinic = document.getData();

                        // Calculate distance
                        if (clinic.containsKey("latitude") && clinic.containsKey("longitude")) {
                            double clinicLat = ((Number) clinic.get("latitude")).doubleValue();
                            double clinicLng = ((Number) clinic.get("longitude")).doubleValue();

                            double distance = calculateDistance(userLat, userLng, clinicLat, clinicLng);
                            if (distance <= radiusKm) {
                                clinic.put("id", document.getId());
                                clinic.put("distance", distance);
                                clinics.add(clinic);
                            }
                        }
                    }
                    // Sort by distance
                    clinics.sort((c1, c2) -> {
                        double d1 = (double) c1.get("distance");
                        double d2 = (double) c2.get("distance");
                        return Double.compare(d1, d2);
                    });
                    listener.onSuccess(clinics);
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Get all clinics with pagination
     */
    public void getAllClinics(int limit, OnClinicSearchListener listener) {
        Query query = db.collection(COLLECTION_CLINICS)
                .orderBy("name")
                .limit(limit);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> clinics = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> clinic = document.getData();
                        clinic.put("id", document.getId());
                        clinics.add(clinic);
                    }
                    listener.onSuccess(clinics);
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Get clinic details by ID (for detail screen)
     */
    public void getClinicById(String clinicId, OnClinicDetailListener listener) {
        db.collection(COLLECTION_CLINICS)
                .document(clinicId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> clinic = documentSnapshot.getData();
                        clinic.put("id", documentSnapshot.getId());
                        listener.onSuccess(clinic);
                    } else {
                        listener.onFailure(new Exception("Clinic not found"));
                    }
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Get available time slots for a clinic (Chức năng 8: Đặt lịch khám)
     */
    public void getAvailableTimeSlots(String clinicId, String date, OnTimeSlotsListener listener) {
        // This would typically check against booked appointments
        // For now, return default time slots
        List<String> defaultSlots = getDefaultTimeSlots();

        // TODO: Query appointments collection to filter out booked slots
        db.collection("appointments")
                .whereEqualTo("clinic_id", clinicId)
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> bookedSlots = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String time = doc.getString("time");
                        if (time != null) {
                            bookedSlots.add(time);
                        }
                    }

                    // Remove booked slots from available slots
                    List<String> availableSlots = new ArrayList<>();
                    for (String slot : defaultSlots) {
                        if (!bookedSlots.contains(slot)) {
                            availableSlots.add(slot);
                        }
                    }

                    listener.onSuccess(availableSlots);
                })
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Search clinics with multiple filters
     */
    public void searchClinicsWithFilters(String name, String specialty, double userLat, double userLng,
                                         double radiusKm, OnClinicSearchListener listener) {
        // Start with base query
        Query query = db.collection(COLLECTION_CLINICS);

        // Apply filters
        if (name != null && !name.trim().isEmpty()) {
            query = query.whereGreaterThanOrEqualTo("name", name)
                    .whereLessThanOrEqualTo("name", name + '\uf8ff');
        }

        if (specialty != null && !specialty.trim().isEmpty()) {
            query = query.whereArrayContains("specialties", specialty);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> clinics = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> clinic = document.getData();

                        // Apply location filter if specified
                        if (radiusKm > 0 && clinic.containsKey("latitude") && clinic.containsKey("longitude")) {
                            double clinicLat = ((Number) clinic.get("latitude")).doubleValue();
                            double clinicLng = ((Number) clinic.get("longitude")).doubleValue();

                            double distance = calculateDistance(userLat, userLng, clinicLat, clinicLng);
                            if (distance <= radiusKm) {
                                clinic.put("id", document.getId());
                                clinic.put("distance", distance);
                                clinics.add(clinic);
                            }
                        } else {
                            clinic.put("id", document.getId());
                            clinics.add(clinic);
                        }
                    }

                    // Sort by distance if location filter applied
                    if (radiusKm > 0) {
                        clinics.sort((c1, c2) -> {
                            double d1 = c1.containsKey("distance") ? (double) c1.get("distance") : Double.MAX_VALUE;
                            double d2 = c2.containsKey("distance") ? (double) c2.get("distance") : Double.MAX_VALUE;
                            return Double.compare(d1, d2);
                        });
                    }

                    listener.onSuccess(clinics);
                })
                .addOnFailureListener(listener::onFailure);
    }

    // ========== HELPER METHODS ==========

    /**
     * Calculate distance between two points using Haversine formula
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in km
    }

    /**
     * Get default time slots for clinics
     */
    private List<String> getDefaultTimeSlots() {
        List<String> slots = new ArrayList<>();

        // Morning slots
        slots.add("08:00");
        slots.add("08:30");
        slots.add("09:00");
        slots.add("09:30");
        slots.add("10:00");
        slots.add("10:30");
        slots.add("11:00");
        slots.add("11:30");

        // Afternoon slots
        slots.add("13:30");
        slots.add("14:00");
        slots.add("14:30");
        slots.add("15:00");
        slots.add("15:30");
        slots.add("16:00");
        slots.add("16:30");
        slots.add("17:00");

        return slots;
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnClinicSearchListener {
        void onSuccess(List<Map<String, Object>> clinics);
        void onFailure(Exception e);
    }

    public interface OnClinicDetailListener {
        void onSuccess(Map<String, Object> clinic);
        void onFailure(Exception e);
    }

    public interface OnTimeSlotsListener {
        void onSuccess(List<String> timeSlots);
        void onFailure(Exception e);
    }
}
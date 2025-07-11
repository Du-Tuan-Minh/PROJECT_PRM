// NEW FILE: app/src/main/java/com/example/project_prm/DataManager/SearchManager/ClinicSearchManager.java
package com.example.project_prm.DataManager.SearchManager;

import com.example.project_prm.DataManager.DAO.ClinicDAO;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class for Clinic Search functionality (Chức năng 6)
 * Handles all clinic search logic for Tung's features
 */
public class ClinicSearchManager {
    private ClinicDAO clinicDAO;

    public ClinicSearchManager() {
        clinicDAO = new ClinicDAO();
    }

    // ========== SEARCH METHODS ==========

    /**
     * Search clinics by name
     */
    public void searchByName(String name, OnClinicSearchListener listener) {
        if (name == null || name.trim().isEmpty()) {
            listener.onSuccess(new ArrayList<>());
            return;
        }

        clinicDAO.searchClinicsByName(name.trim(), new ClinicDAO.OnClinicSearchListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> clinics) {
                List<ClinicSearchResult> results = convertToSearchResults(clinics);
                listener.onSuccess(results);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    /**
     * Search clinics by specialty
     */
    public void searchBySpecialty(String specialty, OnClinicSearchListener listener) {
        if (specialty == null || specialty.trim().isEmpty()) {
            listener.onSuccess(new ArrayList<>());
            return;
        }

        clinicDAO.searchClinicsBySpecialty(specialty.trim(), new ClinicDAO.OnClinicSearchListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> clinics) {
                List<ClinicSearchResult> results = convertToSearchResults(clinics);
                listener.onSuccess(results);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    /**
     * Search clinics by location (near user)
     */
    public void searchByLocation(double userLat, double userLng, double radiusKm, OnClinicSearchListener listener) {
        clinicDAO.searchClinicsByLocation(userLat, userLng, radiusKm, new ClinicDAO.OnClinicSearchListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> clinics) {
                List<ClinicSearchResult> results = convertToSearchResults(clinics);
                listener.onSuccess(results);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    /**
     * Advanced search with multiple filters
     */
    public void searchWithFilters(SearchFilter filter, OnClinicSearchListener listener) {
        clinicDAO.searchClinicsWithFilters(
                filter.name,
                filter.specialty,
                filter.userLat,
                filter.userLng,
                filter.radiusKm,
                new ClinicDAO.OnClinicSearchListener() {
                    @Override
                    public void onSuccess(List<Map<String, Object>> clinics) {
                        List<ClinicSearchResult> results = convertToSearchResults(clinics);

                        // Apply additional filters
                        if (filter.minRating > 0) {
                            results = filterByRating(results, filter.minRating);
                        }

                        if (filter.sortBy != null) {
                            results = sortResults(results, filter.sortBy);
                        }

                        listener.onSuccess(results);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        listener.onError(e.getMessage());
                    }
                });
    }

    /**
     * Get all clinics with pagination
     */
    public void getAllClinics(int limit, OnClinicSearchListener listener) {
        clinicDAO.getAllClinics(limit, new ClinicDAO.OnClinicSearchListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> clinics) {
                List<ClinicSearchResult> results = convertToSearchResults(clinics);
                listener.onSuccess(results);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    /**
     * Get clinic details by ID (for detail screen)
     */
    public void getClinicDetails(String clinicId, OnClinicDetailListener listener) {
        clinicDAO.getClinicById(clinicId, new ClinicDAO.OnClinicDetailListener() {
            @Override
            public void onSuccess(Map<String, Object> clinic) {
                ClinicDetail detail = convertToClinicDetail(clinic);
                listener.onSuccess(detail);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onError(e.getMessage());
            }
        });
    }

    /**
     * Get available specialties for filter dropdown
     */
    public void getAvailableSpecialties(OnSpecialtiesListener listener) {
        // Get all clinics and extract unique specialties
        FirebaseFirestore.getInstance()
                .collection("clinics")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> specialties = new ArrayList<>();

                    querySnapshot.forEach(document -> {
                        Object specialtiesObj = document.get("specialties");
                        if (specialtiesObj instanceof String) {
                            String[] specs = ((String) specialtiesObj).split(",");
                            for (String spec : specs) {
                                String trimmed = spec.trim();
                                if (!specialties.contains(trimmed)) {
                                    specialties.add(trimmed);
                                }
                            }
                        }
                    });

                    specialties.sort(String::compareTo);
                    listener.onSuccess(specialties);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // ========== HELPER METHODS ==========

    private List<ClinicSearchResult> convertToSearchResults(List<Map<String, Object>> clinics) {
        List<ClinicSearchResult> results = new ArrayList<>();

        for (Map<String, Object> clinic : clinics) {
            ClinicSearchResult result = new ClinicSearchResult();
            result.id = (String) clinic.get("id");
            result.name = (String) clinic.get("name");
            result.address = (String) clinic.get("address");
            result.specialties = (String) clinic.get("specialties");
            result.phone = (String) clinic.get("phone");
            result.rating = getDoubleValue(clinic, "rating");
            result.totalReviews = getIntValue(clinic, "total_reviews");
            result.distance = getDoubleValue(clinic, "distance");
            result.imageUrl = (String) clinic.get("image_url");

            // Parse working hours
            Object workingHoursObj = clinic.get("working_hours");
            if (workingHoursObj instanceof Map) {
                result.workingHours = (Map<String, String>) workingHoursObj;
            }

            results.add(result);
        }

        return results;
    }

    private ClinicDetail convertToClinicDetail(Map<String, Object> clinic) {
        ClinicDetail detail = new ClinicDetail();
        detail.id = (String) clinic.get("id");
        detail.name = (String) clinic.get("name");
        detail.address = (String) clinic.get("address");
        detail.latitude = getDoubleValue(clinic, "latitude");
        detail.longitude = getDoubleValue(clinic, "longitude");
        detail.phone = (String) clinic.get("phone");
        detail.email = (String) clinic.get("email");
        detail.website = (String) clinic.get("website");
        detail.specialties = (String) clinic.get("specialties");
        detail.rating = getDoubleValue(clinic, "rating");
        detail.totalReviews = getIntValue(clinic, "total_reviews");
        detail.imageUrl = (String) clinic.get("image_url");

        // Parse working hours
        Object workingHoursObj = clinic.get("working_hours");
        if (workingHoursObj instanceof Map) {
            detail.workingHours = (Map<String, String>) workingHoursObj;
        }

        return detail;
    }

    private List<ClinicSearchResult> filterByRating(List<ClinicSearchResult> results, double minRating) {
        List<ClinicSearchResult> filtered = new ArrayList<>();
        for (ClinicSearchResult result : results) {
            if (result.rating >= minRating) {
                filtered.add(result);
            }
        }
        return filtered;
    }

    private List<ClinicSearchResult> sortResults(List<ClinicSearchResult> results, String sortBy) {
        switch (sortBy) {
            case "rating":
                results.sort((a, b) -> Double.compare(b.rating, a.rating));
                break;
            case "distance":
                results.sort((a, b) -> Double.compare(a.distance, b.distance));
                break;
            case "name":
                results.sort((a, b) -> a.name.compareTo(b.name));
                break;
            case "reviews":
                results.sort((a, b) -> Integer.compare(b.totalReviews, a.totalReviews));
                break;
        }
        return results;
    }

    private double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    private int getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    // ========== DATA CLASSES ==========

    public static class ClinicSearchResult {
        public String id;
        public String name;
        public String address;
        public String specialties;
        public String phone;
        public double rating;
        public int totalReviews;
        public double distance; // in km
        public String imageUrl;
        public Map<String, String> workingHours;

        public boolean isOpen(String currentDay, String currentTime) {
            if (workingHours == null) return false;

            String hours = workingHours.get(currentDay.toLowerCase());
            if (hours == null || hours.equals("Nghỉ")) return false;

            // Parse working hours (e.g., "8:00-17:00" or "8:00-11:30, 13:30-17:00")
            String[] timePeriods = hours.split(",");
            for (String period : timePeriods) {
                String[] timeRange = period.trim().split("-");
                if (timeRange.length == 2) {
                    String openTime = timeRange[0].trim();
                    String closeTime = timeRange[1].trim();

                    if (isTimeBetween(currentTime, openTime, closeTime)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean isTimeBetween(String current, String start, String end) {
            try {
                int currentMinutes = timeToMinutes(current);
                int startMinutes = timeToMinutes(start);
                int endMinutes = timeToMinutes(end);

                return currentMinutes >= startMinutes && currentMinutes <= endMinutes;
            } catch (Exception e) {
                return false;
            }
        }

        private int timeToMinutes(String time) {
            String[] parts = time.split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        }
    }

    public static class ClinicDetail extends ClinicSearchResult {
        public double latitude;
        public double longitude;
        public String email;
        public String website;
    }

    public static class SearchFilter {
        public String name;
        public String specialty;
        public double userLat;
        public double userLng;
        public double radiusKm;
        public double minRating;
        public String sortBy; // "rating", "distance", "name", "reviews"

        public SearchFilter() {
            radiusKm = 10.0; // Default 10km radius
            minRating = 0.0;
            sortBy = "rating";
        }
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnClinicSearchListener {
        void onSuccess(List<ClinicSearchResult> results);
        void onError(String error);
    }

    public interface OnClinicDetailListener {
        void onSuccess(ClinicDetail clinic);
        void onError(String error);
    }

    public interface OnSpecialtiesListener {
        void onSuccess(List<String> specialties);
        void onError(String error);
    }
}
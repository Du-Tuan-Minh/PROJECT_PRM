package com.example.project_prm.MainScreen;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClinicRepository {
    private static ClinicRepository instance;
    private final List<ClinicModel> clinics = new ArrayList<>();
    private LatLng userLocation;

    private ClinicRepository() {
        initializeDummyData();
    }

    public static ClinicRepository getInstance() {
        if (instance == null) {
            instance = new ClinicRepository();
        }
        return instance;
    }

    private void initializeDummyData() {
        // Hà Nội coordinates
        clinics.add(new ClinicModel(
            "1", "Bệnh viện Bạch Mai", 
            "78 Giải Phóng, Đống Đa, Hà Nội", 
            "024 3869 3731", "contact@bachmai.gov.vn", 
            "https://bachmai.gov.vn", 
            "Bệnh viện đa khoa trung ương hàng đầu Việt Nam",
            4.5, 1250, "bachmai.jpg",
            new LatLng(21.0278, 105.8342), 
            "Tim mạch, Thần kinh, Ung bướu", 
            "24/7"
        ));

        clinics.add(new ClinicModel(
            "2", "Bệnh viện Việt Đức", 
            "40 Tràng Thi, Hoàn Kiếm, Hà Nội", 
            "024 3825 3531", "contact@vietduc.org", 
            "https://vietduc.org", 
            "Bệnh viện chuyên khoa ngoại hàng đầu",
            4.3, 890, "vietduc.jpg",
            new LatLng(21.0245, 105.8412), 
            "Ngoại khoa, Chấn thương", 
            "7:00 - 17:00"
        ));

        clinics.add(new ClinicModel(
            "3", "Bệnh viện Nhi Trung ương", 
            "18/879 La Thành, Đống Đa, Hà Nội", 
            "024 6273 8532", "contact@nhitrunguong.org.vn", 
            "https://nhitrunguong.org.vn", 
            "Bệnh viện chuyên khoa nhi hàng đầu",
            4.7, 2100, "nhitrunguong.jpg",
            new LatLng(21.0198, 105.8234), 
            "Nhi khoa, Sơ sinh", 
            "24/7"
        ));

        clinics.add(new ClinicModel(
            "4", "Bệnh viện Mắt Trung ương", 
            "85 Bà Triệu, Hai Bà Trưng, Hà Nội", 
            "024 3943 4288", "contact@mattrunguong.org.vn", 
            "https://mattrunguong.org.vn", 
            "Bệnh viện chuyên khoa mắt hàng đầu",
            4.4, 1560, "mattrunguong.jpg",
            new LatLng(21.0167, 105.8500), 
            "Nhãn khoa, Phẫu thuật mắt", 
            "7:30 - 17:30"
        ));

        clinics.add(new ClinicModel(
            "5", "Bệnh viện Da liễu Trung ương", 
            "15A Phương Mai, Đống Đa, Hà Nội", 
            "024 3576 4441", "contact@dalieu.org.vn", 
            "https://dalieu.org.vn", 
            "Bệnh viện chuyên khoa da liễu",
            4.2, 980, "dalieu.jpg",
            new LatLng(21.0222, 105.8300), 
            "Da liễu, Thẩm mỹ", 
            "7:00 - 17:00"
        ));

        // Set default user location (Hà Nội center)
        userLocation = new LatLng(21.0285, 105.8542);
        calculateDistances();
    }

    public List<ClinicModel> getAllClinics() {
        return new ArrayList<>(clinics);
    }

    public List<ClinicModel> searchClinics(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllClinics();
        }
        
        String lowerQuery = query.toLowerCase().trim();
        return clinics.stream()
            .filter(clinic -> 
                clinic.getName().toLowerCase().contains(lowerQuery) ||
                clinic.getAddress().toLowerCase().contains(lowerQuery) ||
                clinic.getSpecialties().toLowerCase().contains(lowerQuery)
            )
            .collect(Collectors.toList());
    }

    public List<ClinicModel> getClinicsByDistance(double maxDistance) {
        return clinics.stream()
            .filter(clinic -> clinic.getDistance() <= maxDistance)
            .sorted((c1, c2) -> Double.compare(c1.getDistance(), c2.getDistance()))
            .collect(Collectors.toList());
    }

    public List<ClinicModel> getClinicsByRating(double minRating) {
        return clinics.stream()
            .filter(clinic -> clinic.getRating() >= minRating)
            .sorted((c1, c2) -> Double.compare(c2.getRating(), c1.getRating()))
            .collect(Collectors.toList());
    }

    public void setUserLocation(LatLng location) {
        this.userLocation = location;
        calculateDistances();
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    private void calculateDistances() {
        if (userLocation == null) return;
        
        for (ClinicModel clinic : clinics) {
            double distance = calculateDistance(userLocation, clinic.getLocation());
            clinic.setDistance(distance);
        }
    }

    private double calculateDistance(LatLng point1, LatLng point2) {
        double lat1 = Math.toRadians(point1.latitude);
        double lat2 = Math.toRadians(point2.latitude);
        double deltaLat = Math.toRadians(point2.latitude - point1.latitude);
        double deltaLon = Math.toRadians(point2.longitude - point1.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Earth's radius in kilometers
        double earthRadius = 6371;
        return earthRadius * c;
    }

    public ClinicModel getClinicById(String id) {
        return clinics.stream()
            .filter(clinic -> clinic.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
} 
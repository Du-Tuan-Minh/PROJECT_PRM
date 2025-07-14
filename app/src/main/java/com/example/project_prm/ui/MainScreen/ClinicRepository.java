package com.example.project_prm.ui.MainScreen;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class ClinicRepository {
    private static ClinicRepository instance;
    private List<ClinicModel> clinics;

    private ClinicRepository() {
        clinics = new ArrayList<>();
        initializeSampleData();
    }

    public static synchronized ClinicRepository getInstance() {
        if (instance == null) {
            instance = new ClinicRepository();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Hà Nội clinics
        clinics.add(new ClinicModel(
            "clinic_001", 
            "Phòng khám Đa khoa Hà Nội", 
            "123 Đường Lê Văn Lương, Thanh Xuân, Hà Nội",
            "024 3856 7890",
            "Phòng khám đa khoa chất lượng cao với đội ngũ bác sĩ giàu kinh nghiệm",
            4.7, 156, new LatLng(21.0285, 105.8542),
            "clinic1.jpg", true, "7:00 - 20:00", "Tim mạch, Nội tổng quát, Da liễu"
        ));

        clinics.add(new ClinicModel(
            "clinic_002",
            "Phòng khám Chuyên khoa Tim mạch",
            "456 Đường Nguyễn Trãi, Đống Đa, Hà Nội",
            "024 3856 7891",
            "Chuyên khám và điều trị các bệnh về tim mạch",
            4.8, 234, new LatLng(21.0275, 105.8442),
            "clinic2.jpg", true, "8:00 - 18:00", "Tim mạch"
        ));

        clinics.add(new ClinicModel(
            "clinic_003",
            "Phòng khám Nhi khoa Sunshine",
            "789 Đường Cầu Giấy, Cầu Giấy, Hà Nội",
            "024 3856 7892",
            "Phòng khám chuyên khoa nhi với không gian thân thiện",
            4.6, 189, new LatLng(21.0295, 105.8642),
            "clinic3.jpg", true, "7:30 - 19:30", "Nhi khoa"
        ));

        clinics.add(new ClinicModel(
            "clinic_004",
            "Phòng khám Da liễu Thẩm mỹ",
            "321 Đường Trần Duy Hưng, Cầu Giấy, Hà Nội",
            "024 3856 7893",
            "Chuyên điều trị các bệnh về da và thẩm mỹ",
            4.5, 145, new LatLng(21.0265, 105.8742),
            "clinic4.jpg", true, "8:00 - 20:00", "Da liễu, Thẩm mỹ"
        ));

        clinics.add(new ClinicModel(
            "clinic_005",
            "Phòng khám Răng hàm mặt",
            "654 Đường Láng Hạ, Đống Đa, Hà Nội",
            "024 3856 7894",
            "Chuyên điều trị các bệnh răng miệng",
            4.7, 267, new LatLng(21.0255, 105.8842),
            "clinic5.jpg", true, "8:00 - 18:00", "Răng hàm mặt"
        ));

        clinics.add(new ClinicModel(
            "clinic_006",
            "Phòng khám Mắt",
            "987 Đường Kim Mã, Ba Đình, Hà Nội",
            "024 3856 7895",
            "Chuyên khám và điều trị các bệnh về mắt",
            4.8, 198, new LatLng(21.0245, 105.8942),
            "clinic6.jpg", true, "7:00 - 19:00", "Mắt"
        ));

        clinics.add(new ClinicModel(
            "clinic_007",
            "Phòng khám Tai mũi họng",
            "147 Đường Nguyễn Chí Thanh, Đống Đa, Hà Nội",
            "024 3856 7896",
            "Chuyên điều trị các bệnh tai mũi họng",
            4.6, 178, new LatLng(21.0235, 105.9042),
            "clinic7.jpg", true, "8:00 - 17:30", "Tai mũi họng"
        ));

        clinics.add(new ClinicModel(
            "clinic_008",
            "Phòng khám Sản phụ khoa",
            "258 Đường Lê Duẩn, Hai Bà Trưng, Hà Nội",
            "024 3856 7897",
            "Chuyên khám thai và điều trị các bệnh phụ khoa",
            4.7, 223, new LatLng(21.0225, 105.9142),
            "clinic8.jpg", true, "7:30 - 18:30", "Sản phụ khoa"
        ));
    }

    public List<ClinicModel> getAllClinics() {
        return new ArrayList<>(clinics);
    }

    public ClinicModel getClinicById(String id) {
        for (ClinicModel clinic : clinics) {
            if (clinic.getId().equals(id)) {
                return clinic;
            }
        }
        return null;
    }

    public List<ClinicModel> getClinicsBySpecialty(String specialty) {
        List<ClinicModel> filtered = new ArrayList<>();
        for (ClinicModel clinic : clinics) {
            if (clinic.getSpecialties().toLowerCase().contains(specialty.toLowerCase())) {
                filtered.add(clinic);
            }
        }
        return filtered;
    }

    public List<ClinicModel> getTopRatedClinics() {
        List<ClinicModel> sorted = new ArrayList<>(clinics);
        sorted.sort((c1, c2) -> Double.compare(c2.getRating(), c1.getRating()));
        return sorted;
    }

    public List<ClinicModel> searchClinics(String query) {
        List<ClinicModel> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (ClinicModel clinic : clinics) {
            if (clinic.getName().toLowerCase().contains(lowerQuery) ||
                clinic.getAddress().toLowerCase().contains(lowerQuery) ||
                clinic.getSpecialties().toLowerCase().contains(lowerQuery)) {
                results.add(clinic);
            }
        }
        return results;
    }

    public List<ClinicModel> getClinicsByDistance(double maxDistance) {
        // For now, return all clinics since we don't have distance calculation
        return new ArrayList<>(clinics);
    }

    public List<ClinicModel> getClinicsByRating(double minRating) {
        List<ClinicModel> filtered = new ArrayList<>();
        for (ClinicModel clinic : clinics) {
            if (clinic.getRating() >= minRating) {
                filtered.add(clinic);
            }
        }
        return filtered;
    }

    public void setUserLocation(LatLng location) {
        // For now, just store the location for future use
        // In a real app, this would calculate distances
    }
} 
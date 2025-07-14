package com.example.project_prm.MainScreen;

import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {
    private static DoctorRepository instance;
    private List<DoctorModel> doctors;

    private DoctorRepository() {
        doctors = new ArrayList<>();
        initializeSampleData();
    }

    public static synchronized DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Tim mạch
        DoctorModel doc1 = new DoctorModel("doc_001", "BS. Nguyễn Văn An", "Tim mạch", "Bệnh viện Tim Hà Nội");
        doc1.rating = 4.8;
        doc1.reviewCount = 245;
        doc1.patientCount = 1200;
        doc1.experienceYears = 15;
        doc1.isAvailable = true;
        doc1.description = "Chuyên gia tim mạch với 15 năm kinh nghiệm, chuyên điều trị các bệnh tim mạch, tăng huyết áp, suy tim.";
        doctors.add(doc1);

        DoctorModel doc2 = new DoctorModel("doc_002", "BS. Trần Thị Bình", "Da liễu", "Bệnh viện Da liễu Trung ương");
        doc2.rating = 4.6;
        doc2.reviewCount = 189;
        doc2.patientCount = 950;
        doc2.experienceYears = 12;
        doc2.isAvailable = true;
        doc2.description = "Bác sĩ da liễu chuyên điều trị các bệnh về da, dị ứng, mụn trứng cá và các bệnh da liễu khác.";
        doctors.add(doc2);

        DoctorModel doc3 = new DoctorModel("doc_003", "BS. Phạm Minh Cường", "Nội tổng quát", "Bệnh viện Bạch Mai");
        doc3.rating = 4.7;
        doc3.reviewCount = 312;
        doc3.patientCount = 1800;
        doc3.experienceYears = 18;
        doc3.isAvailable = true;
        doc3.description = "Bác sĩ nội tổng quát với kinh nghiệm điều trị các bệnh nội khoa, khám sức khỏe tổng quát.";
        doctors.add(doc3);

        DoctorModel doc4 = new DoctorModel("doc_004", "BS. Lê Thị Hương", "Miễn dịch học", "Bệnh viện Đại học Y Hà Nội");
        doc4.rating = 4.9;
        doc4.reviewCount = 156;
        doc4.patientCount = 800;
        doc4.experienceYears = 20;
        doc4.isAvailable = true;
        doc4.description = "Chuyên gia miễn dịch học, chuyên điều trị các bệnh tự miễn, dị ứng và rối loạn miễn dịch.";
        doctors.add(doc4);

        DoctorModel doc5 = new DoctorModel("doc_005", "BS. Hoàng Văn Đức", "Thần kinh", "Viện Thần kinh Quốc gia");
        doc5.rating = 4.8;
        doc5.reviewCount = 278;
        doc5.patientCount = 1100;
        doc5.experienceYears = 16;
        doc5.isAvailable = true;
        doc5.description = "Bác sĩ thần kinh chuyên điều trị đau đầu, động kinh, đột quỵ và các bệnh thần kinh khác.";
        doctors.add(doc5);

        // Thêm nhiều bác sĩ hơn
        DoctorModel doc6 = new DoctorModel("doc_006", "BS. Vũ Thị Lan", "Nhi khoa", "Bệnh viện Nhi Trung ương");
        doc6.rating = 4.7;
        doc6.reviewCount = 198;
        doc6.patientCount = 1400;
        doc6.experienceYears = 14;
        doc6.isAvailable = true;
        doc6.description = "Bác sĩ nhi khoa chuyên khám và điều trị các bệnh ở trẻ em từ 0-18 tuổi.";
        doctors.add(doc6);

        DoctorModel doc7 = new DoctorModel("doc_007", "BS. Nguyễn Đức Minh", "Ngoại khoa", "Bệnh viện Việt Đức");
        doc7.rating = 4.6;
        doc7.reviewCount = 234;
        doc7.patientCount = 1600;
        doc7.experienceYears = 17;
        doc7.isAvailable = true;
        doc7.description = "Bác sĩ ngoại khoa chuyên phẫu thuật các bệnh về tiêu hóa, gan mật.";
        doctors.add(doc7);

        DoctorModel doc8 = new DoctorModel("doc_008", "BS. Trần Văn Hùng", "Tâm thần", "Bệnh viện Tâm thần Trung ương");
        doc8.rating = 4.5;
        doc8.reviewCount = 145;
        doc8.patientCount = 700;
        doc8.experienceYears = 13;
        doc8.isAvailable = true;
        doc8.description = "Bác sĩ tâm thần chuyên điều trị trầm cảm, lo âu, rối loạn tâm thần.";
        doctors.add(doc8);

        DoctorModel doc9 = new DoctorModel("doc_009", "BS. Lê Thị Mai", "Sản phụ khoa", "Bệnh viện Phụ sản Trung ương");
        doc9.rating = 4.8;
        doc9.reviewCount = 267;
        doc9.patientCount = 1300;
        doc9.experienceYears = 15;
        doc9.isAvailable = true;
        doc9.description = "Bác sĩ sản phụ khoa chuyên khám thai, sinh nở và các bệnh phụ khoa.";
        doctors.add(doc9);

        DoctorModel doc10 = new DoctorModel("doc_010", "BS. Phạm Văn Tuấn", "Mắt", "Bệnh viện Mắt Trung ương");
        doc10.rating = 4.7;
        doc10.reviewCount = 189;
        doc10.patientCount = 1100;
        doc10.experienceYears = 16;
        doc10.isAvailable = true;
        doc10.description = "Bác sĩ nhãn khoa chuyên điều trị các bệnh về mắt, phẫu thuật mắt.";
        doctors.add(doc10);

        DoctorModel doc11 = new DoctorModel("doc_011", "BS. Hoàng Thị Thảo", "Tai mũi họng", "Bệnh viện Tai mũi họng Trung ương");
        doc11.rating = 4.6;
        doc11.reviewCount = 178;
        doc11.patientCount = 950;
        doc11.experienceYears = 12;
        doc11.isAvailable = true;
        doc11.description = "Bác sĩ tai mũi họng chuyên điều trị các bệnh về tai, mũi, họng.";
        doctors.add(doc11);

        DoctorModel doc12 = new DoctorModel("doc_012", "BS. Nguyễn Văn Sơn", "Răng hàm mặt", "Bệnh viện Răng hàm mặt Trung ương");
        doc12.rating = 4.8;
        doc12.reviewCount = 223;
        doc12.patientCount = 1200;
        doc12.experienceYears = 14;
        doc12.isAvailable = true;
        doc12.description = "Bác sĩ răng hàm mặt chuyên nhổ răng, trồng răng, chỉnh nha.";
        doctors.add(doc12);
    }

    public List<DoctorModel> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    public DoctorModel getDoctorById(String id) {
        for (DoctorModel doctor : doctors) {
            if (doctor.id.equals(id)) {
                return doctor;
            }
        }
        return null;
    }

    public List<String> getAllSpecialties() {
        List<String> specialties = new ArrayList<>();
        for (DoctorModel doctor : doctors) {
            if (!specialties.contains(doctor.specialty)) {
                specialties.add(doctor.specialty);
            }
        }
        return specialties;
    }

    public List<DoctorModel> getDoctorsBySpecialty(String specialty) {
        List<DoctorModel> filtered = new ArrayList<>();
        for (DoctorModel doctor : doctors) {
            if (doctor.specialty.equalsIgnoreCase(specialty)) {
                filtered.add(doctor);
            }
        }
        return filtered;
    }

    public List<DoctorModel> getTopRatedClinics() {
        List<DoctorModel> sorted = new ArrayList<>(doctors);
        sorted.sort((c1, c2) -> Double.compare(c2.getRating(), c1.getRating()));
        return sorted;
    }

    public List<DoctorModel> searchDoctors(String query) {
        List<DoctorModel> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (DoctorModel doctor : doctors) {
            if (doctor.name.toLowerCase().contains(lowerQuery) ||
                doctor.specialty.toLowerCase().contains(lowerQuery) ||
                doctor.location.toLowerCase().contains(lowerQuery)) {
                results.add(doctor);
            }
        }
        return results;
    }
} 
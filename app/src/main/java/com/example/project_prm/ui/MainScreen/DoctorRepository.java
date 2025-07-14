package com.example.project_prm.ui.MainScreen;

import java.util.ArrayList;
import java.util.List;
import com.example.project_prm.Model.DoctorModel;
import com.example.project_prm.Model.ReviewModel;

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
        doctors.clear();
        doctors.add(new DoctorModel("doc_001", "BS. Nguyễn Văn An", "Tim mạch", "Bệnh viện Tim Hà Nội", 4.8f, 15, "", "Chuyên gia tim mạch với 15 năm kinh nghiệm, chuyên điều trị các bệnh tim mạch, tăng huyết áp, suy tim.", "Hà Nội", 120, 15));
        doctors.add(new DoctorModel("doc_002", "BS. Trần Thị Bình", "Da liễu", "Bệnh viện Da liễu Trung ương", 4.6f, 12, "", "Bác sĩ da liễu chuyên điều trị các bệnh về da, dị ứng, mụn trứng cá và các bệnh da liễu khác.", "Hà Nội", 98, 12));
        doctors.add(new DoctorModel("doc_003", "BS. Phạm Minh Cường", "Nội tổng quát", "Bệnh viện Bạch Mai", 4.7f, 18, "", "Bác sĩ nội tổng quát với kinh nghiệm điều trị các bệnh nội khoa, khám sức khỏe tổng quát.", "Hà Nội", 110, 18));
        doctors.add(new DoctorModel("doc_004", "BS. Lê Thị Hương", "Miễn dịch học", "Bệnh viện Đại học Y Hà Nội", 4.9f, 20, "", "Chuyên gia miễn dịch học, chuyên điều trị các bệnh tự miễn, dị ứng và rối loạn miễn dịch.", "Hà Nội", 135, 20));
        doctors.add(new DoctorModel("doc_005", "BS. Hoàng Văn Đức", "Thần kinh", "Viện Thần kinh Quốc gia", 4.8f, 16, "", "Bác sĩ thần kinh chuyên điều trị đau đầu, động kinh, đột quỵ và các bệnh thần kinh khác.", "Hà Nội", 105, 16));
        doctors.add(new DoctorModel("doc_006", "BS. Vũ Thị Lan", "Nhi khoa", "Bệnh viện Nhi Trung ương", 4.7f, 14, "", "Bác sĩ nhi khoa chuyên khám và điều trị các bệnh ở trẻ em từ 0-18 tuổi.", "Hà Nội", 90, 14));
        doctors.add(new DoctorModel("doc_007", "BS. Nguyễn Đức Minh", "Ngoại khoa", "Bệnh viện Việt Đức", 4.6f, 17, "", "Bác sĩ ngoại khoa chuyên phẫu thuật các bệnh về tiêu hóa, gan mật.", "Hà Nội", 112, 17));
        doctors.add(new DoctorModel("doc_008", "BS. Trần Văn Hùng", "Tâm thần", "Bệnh viện Tâm thần Trung ương", 4.5f, 13, "", "Bác sĩ tâm thần chuyên điều trị trầm cảm, lo âu, rối loạn tâm thần.", "Hà Nội", 87, 13));
        doctors.add(new DoctorModel("doc_009", "BS. Lê Thị Mai", "Sản phụ khoa", "Bệnh viện Phụ sản Trung ương", 4.8f, 15, "", "Bác sĩ sản phụ khoa chuyên khám thai, sinh nở và các bệnh phụ khoa.", "Hà Nội", 101, 15));
        doctors.add(new DoctorModel("doc_010", "BS. Phạm Văn Tuấn", "Mắt", "Bệnh viện Mắt Trung ương", 4.7f, 16, "", "Bác sĩ nhãn khoa chuyên điều trị các bệnh về mắt, phẫu thuật mắt.", "Hà Nội", 95, 16));
        doctors.add(new DoctorModel("doc_011", "BS. Hoàng Thị Thảo", "Tai mũi họng", "Bệnh viện Tai mũi họng Trung ương", 4.6f, 12, "", "Bác sĩ tai mũi họng chuyên điều trị các bệnh về tai, mũi, họng.", "Hà Nội", 80, 12));
        doctors.add(new DoctorModel("doc_012", "BS. Nguyễn Văn Sơn", "Răng hàm mặt", "Bệnh viện Răng hàm mặt Trung ương", 4.8f, 14, "", "Bác sĩ răng hàm mặt chuyên nhổ răng, trồng răng, chỉnh nha.", "Hà Nội", 99, 14));
    }

    public List<DoctorModel> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    public DoctorModel getDoctorById(String id) {
        for (DoctorModel doctor : doctors) {
            if (doctor.getId().equals(id)) {
                return doctor;
            }
        }
        return null;
    }

    public List<String> getAllSpecialties() {
        List<String> specialties = new ArrayList<>();
        for (DoctorModel doctor : doctors) {
            if (!specialties.contains(doctor.getSpecialty())) {
                specialties.add(doctor.getSpecialty());
            }
        }
        return specialties;
    }

    public List<DoctorModel> getDoctorsBySpecialty(String specialty) {
        List<DoctorModel> filtered = new ArrayList<>();
        for (DoctorModel doctor : doctors) {
            if (doctor.getSpecialty().equalsIgnoreCase(specialty)) {
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
            if (doctor.getName().toLowerCase().contains(lowerQuery) ||
                doctor.getSpecialty().toLowerCase().contains(lowerQuery) ||
                doctor.getHospital().toLowerCase().contains(lowerQuery)) {
                results.add(doctor);
            }
        }
        return results;
    }

    public List<ReviewModel> getDoctorReviews(String doctorId) {
        List<ReviewModel> reviews = new ArrayList<>();
        // Dữ liệu mẫu, có thể thay bằng truy vấn database thực tế
        reviews.add(new ReviewModel("1", doctorId, "Nguyễn Thị B", "Bác sĩ rất tận tâm và chuyên nghiệp. Tôi cảm thấy rất hài lòng với dịch vụ.", 4.5f, "15/12/2024"));
        reviews.add(new ReviewModel("2", doctorId, "Trần Văn C", "Khám bệnh rất kỹ càng, giải thích rõ ràng về tình trạng bệnh.", 5.0f, "10/12/2024"));
        reviews.add(new ReviewModel("3", doctorId, "Lê Thị D", "Bác sĩ nhiệt tình, thời gian chờ hợp lý.", 4.0f, "05/12/2024"));
        return reviews;
    }
} 
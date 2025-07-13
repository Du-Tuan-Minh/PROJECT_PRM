package com.example.project_prm.DataManager.Repository;

import com.example.project_prm.DataManager.Entity.Clinic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClinicRepository {
    private Map<String, Clinic> clinicMap;

    public ClinicRepository() {
        clinicMap = new HashMap<>();
        initializeClinics();
    }

    private void initializeClinics() {
        // ========== HÀ NỘI ==========

        // Bệnh viện Bạch Mai
        addClinic(new Clinic("Bệnh viện Bạch Mai",
                "78 Giải Phóng, Đồng Tâm, Hai Bà Trưng, Hà Nội",
                "06:00-20:00", "024 3869 3731", "info@bachmai.gov.vn",
                4.5f, 1250, "Bệnh viện đa khoa hàng đầu Việt Nam với đội ngũ bác sĩ giỏi",
                new String[]{"Tim mạch", "Thần kinh", "Ung bướu", "Nhi khoa", "Cấp cứu", "Khám tổng quát"},
                "bachmai_logo", 21.0067, 105.8431));

        // Bệnh viện Việt Đức
        addClinic(new Clinic("Bệnh viện Việt Đức",
                "40 Tràng Thi, Hoàn Kiếm, Hà Nội",
                "07:00-17:00", "024 3825 3531", "info@vietduc.vn",
                4.3f, 980, "Bệnh viện phẫu thuật chuyên khoa hàng đầu",
                new String[]{"Phẫu thuật", "Cấp cứu", "Tim mạch", "Chấn thương chỉnh hình"},
                "vietduc_logo", 21.0285, 105.8542));

        // Bệnh viện 108
        addClinic(new Clinic("Bệnh viện Trung ương Quân đội 108",
                "1 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội",
                "06:00-18:00", "024 3972 6781", "info@bv108.vn",
                4.4f, 850, "Bệnh viện quân đội với trang thiết bị hiện đại",
                new String[]{"Tim mạch", "Thần kinh", "Ung bướu", "Khám tổng quát"},
                "bv108_logo", 21.0245, 105.8412));

        // Bệnh viện K
        addClinic(new Clinic("Bệnh viện K Trung ương",
                "43 Quán Sứ, Hoàn Kiếm, Hà Nội",
                "07:00-16:30", "024 3826 5188", "info@benhvienk.vn",
                4.6f, 920, "Bệnh viện chuyên khoa ung bướu hàng đầu",
                new String[]{"Ung bướu", "Xạ trị", "Hóa trị", "Phẫu thuật ung thư"},
                "benhvienk_logo", 21.0275, 105.8520));

        // ========== HẢI PHÒNG (GẦN USER) ==========

        // Bệnh viện Việt Tiệp
        addClinic(new Clinic("Bệnh viện Việt Tiệp",
                "1 Nhật Tân, Ngô Quyền, Hải Phòng",
                "06:00-18:00", "0225 3842 281", "info@viettieponline.vn",
                4.2f, 800, "Bệnh viện tỉnh hàng đầu Hải Phòng với đội ngũ y bác sĩ giỏi",
                new String[]{"Khám tổng quát", "Sản phụ khoa", "Nhi khoa", "Da liễu", "Tim mạch"},
                "viettiep_logo", 20.8563, 106.6828));

        // Phòng khám Đa khoa Hải Phòng
        addClinic(new Clinic("Phòng khám Đa khoa Hải Phòng",
                "123 Lê Lợi, Ngô Quyền, Hải Phòng",
                "07:00-19:00", "0225 3741 852", "info@pkhaiphong.vn",
                4.0f, 450, "Phòng khám hiện đại tại trung tâm thành phố",
                new String[]{"Khám tổng quát", "Tim mạch", "Da liễu", "Tai mũi họng", "Nội khoa"},
                "pkhaiphong_logo", 20.8648, 106.6837));

        // Bệnh viện An Sinh Hải Phòng
        addClinic(new Clinic("Bệnh viện An Sinh Hải Phòng",
                "64 Võ Nguyên Giáp, Lê Chân, Hải Phòng",
                "24/7", "0225 3955 595", "info@ansinh.vn",
                4.1f, 650, "Bệnh viện tư nhân với dịch vụ cao cấp",
                new String[]{"Khám tổng quát", "Sản phụ khoa", "Nhi khoa", "Cấp cứu 24/7"},
                "ansinh_logo", 20.8442, 106.6906));

        // ========== TP. HỒ CHÍ MINH ==========

        // Bệnh viện Chợ Rẫy
        addClinic(new Clinic("Bệnh viện Chợ Rẫy",
                "201B Nguyễn Chí Thanh, Quận 5, TP.HCM",
                "06:00-22:00", "028 3855 4269", "info@choray.vn",
                4.4f, 1500, "Bệnh viện đa khoa lớn nhất miền Nam với đầy đủ chuyên khoa",
                new String[]{"Cấp cứu", "Tim mạch", "Thần kinh", "Ung bướu", "Khám tổng quát"},
                "choray_logo", 10.7554, 106.6660));

        // Bệnh viện Nhân dân 115
        addClinic(new Clinic("Bệnh viện Nhân dân 115",
                "527 Sư Vạn Hạnh, Quận 10, TP.HCM",
                "24/7", "028 3855 8744", "info@bv115.vn",
                4.3f, 1200, "Bệnh viện cấp cứu và đa khoa uy tín",
                new String[]{"Cấp cứu 24/7", "Ngoại khoa", "Nội khoa", "Sản phụ khoa"},
                "bv115_logo", 10.7831, 106.6712));

        // Bệnh viện Đại học Y Dược
        addClinic(new Clinic("Bệnh viện Đại học Y Dược TP.HCM",
                "215 Hồng Bàng, Quận 5, TP.HCM",
                "07:00-17:00", "028 3855 2269", "info@umc.edu.vn",
                4.5f, 950, "Bệnh viện đào tạo với công nghệ y tế tiên tiến",
                new String[]{"Tim mạch", "Thần kinh", "Khám tổng quát", "Chẩn đoán hình ảnh"},
                "umc_logo", 10.7569, 106.6653));

        // ========== ĐÀ NẴNG ==========

        // Bệnh viện Đà Nẵng
        addClinic(new Clinic("Bệnh viện Đà Nẵng",
                "124 Hải Phòng, Thanh Khê, Đà Nẵng",
                "06:00-18:00", "0236 3650 676", "info@bvdanang.vn",
                4.2f, 700, "Bệnh viện đa khoa tuyến cuối miền Trung",
                new String[]{"Khám tổng quát", "Tim mạch", "Thần kinh", "Sản phụ khoa"},
                "bvdanang_logo", 16.0544, 108.2022));

        // Bệnh viện C Đà Nẵng
        addClinic(new Clinic("Bệnh viện C Đà Nẵng",
                "35 Hải Phòng, Thanh Khê, Đà Nẵng",
                "06:00-17:00", "0236 3822 480", "info@bvc-danang.vn",
                4.1f, 480, "Bệnh viện quân đội với chuyên khoa đa dạng",
                new String[]{"Khám tổng quát", "Ngoại khoa", "Nội khoa", "Cấp cứu"},
                "bvc_logo", 16.0570, 108.2018));

        // ========== CẦN THƠ ==========

        // Bệnh viện Đa khoa Trung ương Cần Thơ
        addClinic(new Clinic("Bệnh viện Đa khoa Trung ương Cần Thơ",
                "4 Châu Văn Liêm, Ninh Kiều, Cần Thơ",
                "06:00-18:00", "0292 3891 777", "info@ctump.edu.vn",
                4.3f, 800, "Bệnh viện tuyến cuối khu vực ĐBSCL",
                new String[]{"Khám tổng quát", "Tim mạch", "Tiêu hóa", "Sản phụ khoa"},
                "bvct_logo", 10.0452, 105.7469));

        // ========== PHÒNG KHÁM TƯ NHÂN ==========

        // Phòng khám Quốc tế Vinmec Hà Nội
        addClinic(new Clinic("Vinmec International Hospital Hanoi",
                "458 Minh Khai, Hai Bà Trưng, Hà Nội",
                "24/7", "024 3974 3556", "info@vinmec.com",
                4.8f, 2500, "Bệnh viện quốc tế với tiêu chuẩn JCI",
                new String[]{"Khám tổng quát", "Tim mạch", "Ung bướu", "Sản phụ khoa", "Nhi khoa", "Checkup cao cấp"},
                "vinmec_logo", 21.0067, 105.8542));

        // FV Hospital TP.HCM
        addClinic(new Clinic("FV Hospital",
                "6 Nguyễn Lương Bằng, Quận 7, TP.HCM",
                "24/7", "028 5411 3333", "info@fvhospital.com",
                4.7f, 1800, "Bệnh viện Pháp-Việt với dịch vụ quốc tế",
                new String[]{"Khám tổng quát", "Tim mạch", "Thần kinh", "Ung bướu", "Sản phụ khoa"},
                "fv_logo", 10.7307, 106.7218));

        // Columbia Asia Gia Định
        addClinic(new Clinic("Columbia Asia Saigon",
                "8 Alexandre de Rhodes, Quận 1, TP.HCM",
                "24/7", "028 3823 8888", "info@columbiaasia.com",
                4.6f, 1200, "Bệnh viện quốc tế Malaysia tại Việt Nam",
                new String[]{"Khám tổng quát", "Tim mạch", "Nhi khoa", "Sản phụ khoa", "Cấp cứu"},
                "columbia_logo", 10.7764, 106.7009));

        // ========== PHÒNG KHÁM ĐA KHOA ĐỊA PHƯƠNG ==========

        // Hải Phòng - Thêm các phòng khám gần user
        addClinic(new Clinic("Phòng khám Đa khoa Medlatec Hải Phòng",
                "số 2 Nguyễn Văn Linh, Máy Chai, Ngô Quyền, Hải Phòng",
                "07:00-20:00", "0225 6270 270", "haiphong@medlatec.vn",
                4.2f, 350, "Hệ thống phòng khám chuyên nghiệp",
                new String[]{"Khám tổng quát", "Xét nghiệm", "Chẩn đoán hình ảnh", "Tiêm chủng"},
                "medlatec_logo", 20.8667, 106.6833));

        addClinic(new Clinic("Phòng khám Tim mạch Hải Phòng",
                "15 Điện Biên Phủ, Hồng Bàng, Hải Phòng",
                "08:00-17:00", "0225 3841 555", "info@timhp.vn",
                4.3f, 280, "Chuyên khoa tim mạch uy tín tại Hải Phòng",
                new String[]{"Tim mạch", "Điện tim", "Siêu âm tim", "Khám tổng quát"},
                "timhp_logo", 20.8619, 106.6804));

        addClinic(new Clinic("Phòng khám Nhi Hải Phòng",
                "88 Tô Hiệu, Lê Chân, Hải Phòng",
                "07:30-18:30", "0225 3831 234", "info@nhihp.vn",
                4.4f, 420, "Chuyên khoa nhi với bác sĩ giàu kinh nghiệm",
                new String[]{"Nhi khoa", "Tiêm chủng", "Khám sức khỏe trẻ em", "Dinh dưỡng"},
                "nhihp_logo", 20.8503, 106.6889));

        // Quảng Ninh (gần Hải Phòng)
        addClinic(new Clinic("Bệnh viện Bãi Cháy",
                "Đường Bãi Cháy, Hạ Long, Quảng Ninh",
                "06:00-18:00", "0203 3845 115", "info@bvbaichay.vn",
                4.0f, 320, "Bệnh viện tỉnh Quảng Ninh",
                new String[]{"Khám tổng quát", "Cấp cứu", "Nội khoa", "Ngoại khoa"},
                "bvbc_logo", 20.9570, 107.0429));

        // Thái Bình (gần Hải Phòng)
        addClinic(new Clinic("Bệnh viện Đa khoa Thái Bình",
                "Đường Lý Bôn, Thái Bình",
                "06:00-17:00", "0227 3852 146", "info@bvtb.vn",
                4.1f, 380, "Bệnh viện tỉnh Thái Bình",
                new String[]{"Khám tổng quát", "Sản phụ khoa", "Nhi khoa", "Tim mạch"},
                "bvtb_logo", 20.4500, 106.3333));

        // ========== BỆNH VIỆN CHUYÊN KHOA ==========

        // Viện Mắt Trung ương
        addClinic(new Clinic("Viện Mắt Trung ương",
                "85 Bà Triệu, Hai Bà Trưng, Hà Nội",
                "07:00-16:30", "024 3974 8950", "info@vnio.vn",
                4.6f, 800, "Viện chuyên khoa mắt hàng đầu Việt Nam",
                new String[]{"Nhãn khoa", "Phẫu thuật mắt", "Khúc xạ", "Võng mạc"},
                "vienmat_logo", 21.0188, 105.8513));

        // Viện Tim Hà Nội
        addClinic(new Clinic("Viện Tim Hà Nội",
                "7 Phạm Ngọc Thạch, Đống Đa, Hà Nội",
                "06:00-18:00", "024 3576 1170", "info@vnhi.vn",
                4.7f, 950, "Viện tim mạch uy tín với công nghệ tiên tiến",
                new String[]{"Tim mạch", "Phẫu thuật tim", "Thông tim", "Siêu âm tim"},
                "vientim_logo", 21.0227, 105.8126));

        // Bệnh viện Phụ sản Trung ương
        addClinic(new Clinic("Bệnh viện Phụ sản Trung ương",
                "43 Tràng Thi, Hoàn Kiếm, Hà Nội",
                "06:00-18:00", "024 3825 3357", "info@obsgynhospital.vn",
                4.5f, 1100, "Bệnh viện chuyên sản phụ khoa hàng đầu",
                new String[]{"Sản phụ khoa", "Sản khoa", "Phụ khoa", "Kế hoạch hóa gia đình"},
                "bvps_logo", 21.0275, 105.8520));

        // ========== PHÒNG KHÁM NỘI THÀNH HÀ NỘI ==========

        addClinic(new Clinic("Phòng khám Đa khoa Thiện Hòa",
                "52 Nguyễn Khánh Toàn, Cầu Giấy, Hà Nội",
                "07:00-19:00", "024 3766 6666", "info@thienhoa.vn",
                4.2f, 450, "Phòng khám đa khoa với bác sĩ giỏi",
                new String[]{"Khám tổng quát", "Nội khoa", "Da liễu", "Tim mạch"},
                "thienhoa_logo", 21.0338, 105.8019));

        addClinic(new Clinic("Phòng khám Đa khoa Quân dân 102",
                "18 Võ Chí Công, Tây Hồ, Hà Nội",
                "07:30-17:30", "024 3718 1020", "info@pk102.vn",
                4.1f, 320, "Phòng khám quân dân y với chất lượng tốt",
                new String[]{"Khám tổng quát", "Ngoại khoa", "Nội khoa", "Xét nghiệm"},
                "pk102_logo", 21.0583, 105.8197));

        // ========== PHÒNG KHÁM TP.HCM ==========

        addClinic(new Clinic("Phòng khám Đa khoa Saigon Healthcare",
                "61-63 Trường Sơn, Quận Tân Bình, TP.HCM",
                "07:00-20:00", "028 3844 6666", "info@saigonhealthcare.vn",
                4.3f, 680, "Phòng khám hiện đại với đầy đủ chuyên khoa",
                new String[]{"Khám tổng quát", "Tim mạch", "Tiêu hóa", "Da liễu"},
                "sghc_logo", 10.8012, 106.6519));

        addClinic(new Clinic("Phòng khám Đa khoa Hồng Phát",
                "391 Nam Kỳ Khởi Nghĩa, Quận 3, TP.HCM",
                "07:00-18:00", "028 3932 1818", "info@hongphat.vn",
                4.0f, 350, "Phòng khám gia đình với giá cả hợp lý",
                new String[]{"Khám tổng quát", "Nhi khoa", "Sản phụ khoa", "Tiêm chủng"},
                "hongphat_logo", 10.7860, 106.6917));

        // Set tất cả là đang mở (giờ hành chính)
        for (Clinic clinic : clinicMap.values()) {
            clinic.setOpen(true);
        }
    }

    private void addClinic(Clinic clinic) {
        // Generate ID if not set
        if (clinic.getId() == null || clinic.getId().isEmpty()) {
            clinic.setId(generateIdFromName(clinic.getName()));
        }
        clinicMap.put(clinic.getId(), clinic);
    }

    private String generateIdFromName(String name) {
        if (name == null) return "clinic_unknown";
        return "clinic_" + name.toLowerCase()
                .replaceAll("[^a-zA-Z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    // ========== REPOSITORY METHODS ==========

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
            if (specialty != null && !specialty.isEmpty()) {
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
                    results.sort((c1, c2) -> Float.compare(c2.getRating(), c1.getRating()));
                    break;
                case "name":
                    results.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
                    break;
                case "reviews":
                    results.sort((c1, c2) -> Integer.compare(c2.getReviewCount(), c1.getReviewCount()));
                    break;
                // Distance sorting should be handled in the service layer with user location
            }
        }

        return results;
    }

    public List<Clinic> getNearbyClinic(double userLat, double userLng, double radiusKm) {
        List<Clinic> results = new ArrayList<>();

        for (Clinic clinic : clinicMap.values()) {
            double distance = calculateDistance(userLat, userLng,
                    clinic.getLatitude(), clinic.getLongitude());
            if (distance <= radiusKm) {
                results.add(clinic);
            }
        }

        // Sort by distance
        results.sort((c1, c2) -> {
            double d1 = calculateDistance(userLat, userLng, c1.getLatitude(), c1.getLongitude());
            double d2 = calculateDistance(userLat, userLng, c2.getLatitude(), c2.getLongitude());
            return Double.compare(d1, d2);
        });

        return results;
    }

    /**
     * Calculate distance between two points using Haversine formula
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // distance in km

        return distance;
    }

    /**
     * Get unique specialties from all clinics
     */
    public List<String> getAllSpecialties() {
        List<String> specialties = new ArrayList<>();

        for (Clinic clinic : clinicMap.values()) {
            String[] clinicSpecialties = clinic.getSpecialties();
            if (clinicSpecialties != null) {
                for (String specialty : clinicSpecialties) {
                    if (!specialties.contains(specialty)) {
                        specialties.add(specialty);
                    }
                }
            }
        }

        return specialties;
    }

    /**
     * Get clinics count
     */
    public int getClinicCount() {
        return clinicMap.size();
    }
}
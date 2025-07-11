// NEW FILE: app/src/main/java/com/example/project_prm/ui/SearchScreen/ClinicDetailActivity.java
package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.SearchManager.ClinicSearchManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Map;

public class ClinicDetailActivity extends AppCompatActivity {

    private ImageView ivClinicImage, ivBack;
    private TextView tvClinicName, tvSpecialty, tvAddress, tvPhone, tvEmail;
    private TextView tvRating, tvReviewCount, tvOpenStatus;
    private TextView tvWorkingHours, tvDescription;
    private RatingBar ratingBar;
    private MaterialButton btnBookAppointment, btnCallClinic, btnGetDirections;
    private RecyclerView rvServices;
    private View progressBar;

    private ClinicSearchManager.ClinicDetail clinic;
    private HealthcareService service;
    private String clinicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_detail);

        // Get clinic ID from intent
        clinicId = getIntent().getStringExtra("clinic_id");
        String clinicName = getIntent().getStringExtra("clinic_name");

        if (clinicId == null) {
            Toast.makeText(this, "Invalid clinic ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadClinicDetails();
    }

    private void initViews() {
        // Header
        ivBack = findViewById(R.id.iv_back);
        ivClinicImage = findViewById(R.id.iv_clinic_image);

        // Basic info
        tvClinicName = findViewById(R.id.tv_clinic_name);
        tvSpecialty = findViewById(R.id.tv_specialty);
        tvAddress = findViewById(R.id.tv_address);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);

        // Rating
        ratingBar = findViewById(R.id.rating_bar);
        tvRating = findViewById(R.id.tv_rating);
        tvReviewCount = findViewById(R.id.tv_review_count);
        tvOpenStatus = findViewById(R.id.tv_open_status);

        // Details
        tvWorkingHours = findViewById(R.id.tv_working_hours);
        tvDescription = findViewById(R.id.tv_description);

        // Action buttons
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        btnCallClinic = findViewById(R.id.btn_call_clinic);
        btnGetDirections = findViewById(R.id.btn_get_directions);

        // Services list
        rvServices = findViewById(R.id.rv_services);

        // Progress
        progressBar = findViewById(R.id.progress_bar);

        // Initialize service
        service = HealthcareService.getInstance(this);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnBookAppointment.setOnClickListener(v -> {
            if (clinic != null) {
                Intent intent = new Intent(this, AppointmentBookingActivity.class);
                intent.putExtra("clinic_id", clinic.id);
                intent.putExtra("clinic_name", clinic.name);
                startActivity(intent);
            }
        });

        btnCallClinic.setOnClickListener(v -> {
            if (clinic != null && clinic.phone != null) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + clinic.phone));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
        });

        btnGetDirections.setOnClickListener(v -> {
            if (clinic != null && clinic.latitude != 0 && clinic.longitude != 0) {
                String uri = String.format("geo:%f,%f?q=%f,%f(%s)",
                        clinic.latitude, clinic.longitude,
                        clinic.latitude, clinic.longitude,
                        clinic.name);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Fallback to browser
                    String url = String.format("https://maps.google.com/?q=%f,%f",
                            clinic.latitude, clinic.longitude);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadClinicDetails() {
        showLoading(true);

        service.getClinicDetails(clinicId, new ClinicSearchManager.OnClinicDetailListener() {
            @Override
            public void onSuccess(ClinicSearchManager.ClinicDetail clinicDetail) {
                runOnUiThread(() -> {
                    clinic = clinicDetail;
                    displayClinicDetails(clinicDetail);
                    showLoading(false);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Failed to load clinic details: " + error);
                });
            }
        });
    }

    private void displayClinicDetails(ClinicSearchManager.ClinicDetail clinic) {
        // Basic information
        tvClinicName.setText(clinic.name);
        tvSpecialty.setText(clinic.specialties != null ? clinic.specialties : "Khám tổng quát");
        tvAddress.setText(clinic.address != null ? clinic.address : "Địa chỉ không có sẵn");
        tvPhone.setText(clinic.phone != null ? clinic.phone : "Chưa có số điện thoại");
        tvEmail.setText(clinic.email != null ? clinic.email : "Chưa có email");

        // Rating
        ratingBar.setRating((float) clinic.rating);
        tvRating.setText(String.format("%.1f", clinic.rating));
        tvReviewCount.setText(String.format("(%d đánh giá)", clinic.totalReviews));

        // Open status
        boolean isOpen = isClinicCurrentlyOpen(clinic);
        tvOpenStatus.setText(isOpen ? "Đang mở cửa" : "Đã đóng cửa");
        tvOpenStatus.setTextColor(getColor(isOpen ? R.color.success_green : R.color.error_red));

        // Working hours
        displayWorkingHours(clinic.workingHours);

        // Description (placeholder)
        tvDescription.setText("Phòng khám chuyên nghiệp với đội ngũ bác sĩ giàu kinh nghiệm, " +
                "trang thiết bị hiện đại và dịch vụ chăm sóc sức khỏe tận tâm.");

        // Clinic image (use default)
        ivClinicImage.setImageResource(R.drawable.default_clinic_image);
    }

    private void displayWorkingHours(Map<String, String> workingHours) {
        if (workingHours == null || workingHours.isEmpty()) {
            tvWorkingHours.setText("Giờ làm việc: Chưa cập nhật");
            return;
        }

        StringBuilder hoursText = new StringBuilder("Giờ làm việc:\n");

        String[] dayNames = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        String[] dayKeys = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};

        for (int i = 0; i < dayKeys.length; i++) {
            String hours = workingHours.get(dayKeys[i]);
            if (hours != null) {
                hoursText.append(dayNames[i]).append(": ").append(hours).append("\n");
            }
        }

        tvWorkingHours.setText(hoursText.toString().trim());
    }

    private boolean isClinicCurrentlyOpen(ClinicSearchManager.ClinicDetail clinic) {
        // Simple implementation - check if clinic has working hours
        if (clinic.workingHours == null || clinic.workingHours.isEmpty()) {
            return false;
        }

        // For demo purposes, assume clinic is open during business hours (8-17)
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        return hour >= 8 && hour < 17;
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
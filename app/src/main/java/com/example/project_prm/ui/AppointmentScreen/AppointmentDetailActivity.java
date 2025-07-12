// File: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/AppointmentDetailActivity.java
package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.Doctor;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.DataManager.Repository.DoctorRepository;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity hiển thị chi tiết cuộc hẹn
 * Chức năng 10: Appointment Detail
 * Hiển thị thông tin đầy đủ về cuộc hẹn, bác sĩ, bệnh nhân
 */
public class AppointmentDetailActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivBack, ivDoctorAvatar, ivMore, ivPackageIcon;
    private TextView tvDoctorName, tvSpecialty, tvScheduledAppointment, tvDateTime;
    private TextView tvPatientName, tvPatientPhone, tvPatientAge, tvPatientGender;
    private TextView tvSymptoms, tvPackageName, tvPackagePrice;
    private MaterialButton btnPrimaryAction;

    // Data
    private Appointment appointment;
    private Doctor doctor;
    private HealthcareService service;
    private DoctorRepository doctorRepository;
    private int appointmentId;
    private String appointmentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // Get appointment ID from intent
        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        appointmentStatus = getIntent().getStringExtra("appointment_status");

        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        service = HealthcareService.getInstance(this);
        doctorRepository = DoctorRepository.getInstance();
        initViews();
        loadAppointmentDetails();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivMore = findViewById(R.id.iv_more);
        ivDoctorAvatar = findViewById(R.id.iv_doctor_avatar);
        ivPackageIcon = findViewById(R.id.iv_package_icon);

        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvSpecialty = findViewById(R.id.tv_specialty);
        tvScheduledAppointment = findViewById(R.id.tv_scheduled_appointment);
        tvDateTime = findViewById(R.id.tv_date_time);

        tvPatientName = findViewById(R.id.tv_patient_name);
        tvPatientPhone = findViewById(R.id.tv_patient_phone);
        tvPatientAge = findViewById(R.id.tv_patient_age);
        tvPatientGender = findViewById(R.id.tv_patient_gender);
        tvSymptoms = findViewById(R.id.tv_symptoms);

        tvPackageName = findViewById(R.id.tv_package_name);
        tvPackagePrice = findViewById(R.id.tv_package_price);

        btnPrimaryAction = findViewById(R.id.btn_primary_action);

        // Set click listeners
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> onBackPressed());
        }

        if (ivMore != null) {
            ivMore.setOnClickListener(v -> showMoreOptions());
        }

        if (btnPrimaryAction != null) {
            btnPrimaryAction.setOnClickListener(v -> handlePrimaryAction());
        }
    }

    private void loadAppointmentDetails() {
        // Get appointment from database
        service.getAppointmentDetails(appointmentId, new HealthcareService.OnAppointmentDetailListener() {
            @Override
            public void onSuccess(Appointment appointment) {
                runOnUiThread(() -> {
                    AppointmentDetailActivity.this.appointment = appointment;

                    // Get doctor details from repository
                    doctor = doctorRepository.getDoctorByName(appointment.getDoctor());

                    displayAppointmentDetails(appointment);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AppointmentDetailActivity.this, "Error loading appointment: " + error, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void displayAppointmentDetails(Appointment appointment) {
        // Doctor Information
        if (tvDoctorName != null) {
            tvDoctorName.setText(appointment.getDoctor() != null ? appointment.getDoctor() : "Bác sĩ");
        }

        if (doctor != null) {
            // Use real doctor data
            if (tvSpecialty != null) {
                tvSpecialty.setText(doctor.getSpecialty());
            }
            setDoctorAvatar(doctor);

            // Add experience info if available
            TextView tvExperienceView = findViewById(R.id.tv_experience);
            if (tvExperienceView != null && doctor.getExperience() != null) {
                tvExperienceView.setText(doctor.getExperience());
            }
        } else {
            // Fallback to appointment type based specialty
            if (tvSpecialty != null) {
                tvSpecialty.setText(getSpecialtyFromType(appointment.getAppointmentType()));
            }
            // Use default avatar
            if (ivDoctorAvatar != null) {
                ivDoctorAvatar.setImageResource(R.drawable.ic_launcher_foreground); // Fallback
            }
        }

        // Scheduled Appointment
        if (tvScheduledAppointment != null) {
            tvScheduledAppointment.setText("Lịch hẹn đã đặt");
        }

        if (tvDateTime != null) {
            String dateTime = formatDateTime(appointment.getDate(), appointment.getTime());
            tvDateTime.setText(dateTime);
        }

        // Patient Information
        if (tvPatientName != null) {
            tvPatientName.setText(appointment.getPatientName() != null ? appointment.getPatientName() : "N/A");
        }

        if (tvPatientPhone != null) {
            tvPatientPhone.setText(appointment.getPatientPhone() != null ? appointment.getPatientPhone() : "N/A");
        }

        if (tvPatientAge != null) {
            String ageText = appointment.getPatientAge() != null ? appointment.getPatientAge() + " tuổi" : "N/A";
            tvPatientAge.setText(ageText);
        }

        if (tvPatientGender != null) {
            tvPatientGender.setText(appointment.getPatientGender() != null ? appointment.getPatientGender() : "N/A");
        }

        if (tvSymptoms != null) {
            tvSymptoms.setText(appointment.getSymptoms() != null ? appointment.getSymptoms() : "Không có triệu chứng mô tả");
        }

        // Package Information
        displayPackageInformation(appointment);

        // Configure primary action button based on appointment status
        configurePrimaryActionButton(appointment);
    }

    private void setDoctorAvatar(Doctor doctor) {
        if (ivDoctorAvatar == null) return;

        String avatarResource = doctor.getAvatarResource();

        if (avatarResource != null) {
            // Try to get resource ID by name
            int resourceId = getResourceIdSafely(avatarResource);
            ivDoctorAvatar.setImageResource(resourceId);
        } else {
            // Use specialty-based avatar
            setAvatarBySpecialty(doctor.getSpecialtyCode());
        }
    }

    private void setAvatarBySpecialty(String specialtyCode) {
        if (ivDoctorAvatar == null) return;

        int avatarResource = R.drawable.ic_launcher_foreground; // Default fallback

        if (specialtyCode != null) {
            switch (specialtyCode.toLowerCase()) {
                case "dermatology":
                case "neurology":
                case "cardiology":
                case "pediatrics":
                case "orthopedics":
                case "gastroenterology":
                    // For now use default, can be customized later
                    avatarResource = R.drawable.ic_launcher_foreground;
                    break;
                default:
                    avatarResource = R.drawable.ic_launcher_foreground;
                    break;
            }
        }

        ivDoctorAvatar.setImageResource(avatarResource);
    }

    private int getResourceIdSafely(String resourceName) {
        try {
            int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            return resourceId != 0 ? resourceId : R.drawable.ic_launcher_foreground;
        } catch (Exception e) {
            return R.drawable.ic_launcher_foreground;
        }
    }

    private void displayPackageInformation(Appointment appointment) {
        String appointmentType = appointment.getAppointmentType() != null ?
                appointment.getAppointmentType() : "Khám tổng quát";
        double fee = appointment.getAppointmentFee() > 0 ? appointment.getAppointmentFee() : 100000.0;

        if (tvPackageName != null) {
            tvPackageName.setText(appointmentType);
        }

        if (tvPackagePrice != null) {
            tvPackagePrice.setText(String.format(Locale.getDefault(), "%.0f VND", fee));
        }

        // Set package icon based on type
        if (ivPackageIcon != null) {
            // Use default icon since specific icons might not exist
            ivPackageIcon.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private void configurePrimaryActionButton(Appointment appointment) {
        if (btnPrimaryAction == null) return;

        String status = appointment.getStatus() != null ? appointment.getStatus().toLowerCase() : "pending";

        switch (status) {
            case "upcoming":
                btnPrimaryAction.setText("Nhắn tin (Bắt đầu lúc " + appointment.getTime() + ")");
                // Use safe color - no custom color resources
                break;
            case "completed":
                btnPrimaryAction.setText("Để lại đánh giá");
                // Use safe color - no custom color resources
                break;
            case "cancelled":
                btnPrimaryAction.setText("Đặt lại lịch");
                // Use safe color - no custom color resources
                break;
            default:
                btnPrimaryAction.setText("Xem chi tiết");
                // Use safe color - no custom color resources
                break;
        }
    }

    private void handlePrimaryAction() {
        if (appointment == null) return;

        String status = appointment.getStatus() != null ? appointment.getStatus().toLowerCase() : "pending";

        switch (status) {
            case "upcoming":
                Toast.makeText(this, "Nhắn tin sẽ khả dụng vào thời gian hẹn", Toast.LENGTH_SHORT).show();
                break;
            case "completed":
                // Navigate to review screen
                navigateToReviewScreen();
                break;
            case "cancelled":
                // Navigate to booking screen to book again
                navigateToBookAgainScreen();
                break;
            default:
                Toast.makeText(this, "Xem chi tiết cuộc hẹn", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void navigateToReviewScreen() {
        try {
            Intent reviewIntent = new Intent();
            reviewIntent.setClassName(this, "com.example.project_prm.ui.ReviewScreen.ReviewActivity");
            reviewIntent.putExtra("appointment_id", appointmentId);
            reviewIntent.putExtra("doctor_name", appointment.getDoctor());
            startActivity(reviewIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Chức năng đánh giá đang được phát triển", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToBookAgainScreen() {
        try {
            Intent bookIntent = new Intent();
            bookIntent.setClassName(this, "com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity");
            bookIntent.putExtra("appointment_id", appointmentId);
            bookIntent.putExtra("from_detail", true);
            startActivity(bookIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Chức năng đặt lại đang được phát triển", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMoreOptions() {
        // Simple implementation without menu resource dependency
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Tùy chọn");

        String[] options = {"Đổi lịch", "Hủy lịch", "Chia sẻ"};

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    rescheduleAppointment();
                    break;
                case 1:
                    cancelAppointment();
                    break;
                case 2:
                    shareAppointmentDetails();
                    break;
            }
        });

        builder.show();
    }

    private void rescheduleAppointment() {
        try {
            Intent intent = new Intent();
            intent.setClassName(this, "com.example.project_prm.ui.RescheduleScreen.RescheduleActivity");
            intent.putExtra("appointment_id", appointmentId);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Chức năng đổi lịch đang được phát triển", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAppointment() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Hủy cuộc hẹn")
                .setMessage("Bạn có chắc muốn hủy cuộc hẹn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Handle cancellation
                    service.cancelAppointment(appointmentId, "Người dùng yêu cầu hủy",
                            new AppointmentHistoryManager.OnActionListener() {
                                @Override
                                public void onSuccess(String message) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(AppointmentDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(AppointmentDetailActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void shareAppointmentDetails() {
        if (appointment == null) return;

        String shareText = String.format(
                "Chi tiết cuộc hẹn:\n" +
                        "Bác sĩ: %s\n" +
                        "Ngày: %s\n" +
                        "Giờ: %s\n" +
                        "Phòng khám: %s",
                appointment.getDoctor(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getClinic()
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ cuộc hẹn"));
    }

    private String formatDateTime(String date, String time) {
        if (date == null || time == null) {
            return "N/A";
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
            Date dateObj = inputFormat.parse(date);
            String formattedDate = outputFormat.format(dateObj);
            return formattedDate + " | " + time;
        } catch (ParseException e) {
            return date + " | " + time;
        }
    }

    private String getSpecialtyFromType(String appointmentType) {
        if (appointmentType == null) return "Tổng quát";

        String type = appointmentType.toLowerCase();
        if (type.contains("dermatology") || type.contains("da liễu")) {
            return "Bác sĩ Da liễu";
        } else if (type.contains("cardiology") || type.contains("tim mạch")) {
            return "Bác sĩ Tim mạch";
        } else if (type.contains("neurology") || type.contains("thần kinh")) {
            return "Bác sĩ Thần kinh";
        } else if (type.contains("pediatric") || type.contains("nhi")) {
            return "Bác sĩ Nhi khoa";
        } else if (type.contains("orthopedic") || type.contains("xương khớp")) {
            return "Bác sĩ Xương khớp";
        } else {
            return "Bác sĩ Tổng quát";
        }
    }
}
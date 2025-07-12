// File: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/AppointmentDetailActivity.java
package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.Entity.Doctor;
import com.example.project_prm.DataManager.Repository.DoctorRepository;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private TungFeaturesService service;
    private DoctorRepository doctorRepository;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // Get appointment ID from intent
        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        service = TungFeaturesService.getInstance(this);
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
        ivBack.setOnClickListener(v -> onBackPressed());
        ivMore.setOnClickListener(v -> showMoreOptions());
        btnPrimaryAction.setOnClickListener(v -> handlePrimaryAction());
    }

    private void loadAppointmentDetails() {
        // Get appointment from database
        service.getAppointmentDetails(appointmentId, new TungFeaturesService.OnAppointmentDetailListener() {
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
        tvDoctorName.setText(appointment.getDoctor());

        if (doctor != null) {
            // Use real doctor data
            tvSpecialty.setText(doctor.getSpecialty());
            setDoctorAvatar(doctor);

            // FIXED: Add experience info if available - check if view exists first
            TextView tvExperienceView = findViewById(R.id.tv_experience);
            if (tvExperienceView != null && doctor.getExperience() != null) {
                tvExperienceView.setText(doctor.getExperience());
            }
        } else {
            // Fallback to appointment type based specialty
            tvSpecialty.setText(getSpecialtyFromType(appointment.getAppointmentType()));
            // FIXED: Use default avatar instead of problematic resources
            ivDoctorAvatar.setImageResource(R.drawable.ic_doctor_default);
        }

        // Scheduled Appointment
        tvScheduledAppointment.setText("Scheduled Appointment");
        String dateTime = formatDateTime(appointment.getDate(), appointment.getTime());
        tvDateTime.setText(dateTime);

        // Patient Information
        tvPatientName.setText(appointment.getPatientName() != null ? appointment.getPatientName() : "N/A");
        tvPatientPhone.setText(appointment.getPatientPhone() != null ? appointment.getPatientPhone() : "N/A");
        tvPatientAge.setText(appointment.getPatientAge() != null ? appointment.getPatientAge() + " years old" : "N/A");
        tvPatientGender.setText(appointment.getPatientGender() != null ? appointment.getPatientGender() : "N/A");
        tvSymptoms.setText(appointment.getSymptoms() != null ? appointment.getSymptoms() : "No symptoms described");

        // Package Information with real data
        displayPackageInformation(appointment);

        // Configure primary action button based on appointment type and status
        configurePrimaryActionButton(appointment);
    }

    private void setDoctorAvatar(Doctor doctor) {
        String avatarResource = doctor.getAvatarResource();

        if (avatarResource != null) {
            // Try to get resource ID by name
            int resourceId = getResources().getIdentifier(
                    avatarResource, "drawable", getPackageName());

            if (resourceId != 0) {
                ivDoctorAvatar.setImageResource(resourceId);
            } else {
                // Use specialty-based avatar
                setAvatarBySpecialty(doctor.getSpecialtyCode());
            }
        } else {
            setAvatarBySpecialty(doctor.getSpecialtyCode());
        }
    }

    private void setAvatarBySpecialty(String specialtyCode) {
        int avatarResource;

        // FIXED: Use safe specialty checking and fallback to default
        if (specialtyCode == null) {
            avatarResource = R.drawable.ic_doctor_default;
        } else {
            switch (specialtyCode.toLowerCase()) {
                case "dermatology":
                    // FIXED: Check if resource exists, fallback to default
                    avatarResource = getResourceIdSafely("avatar_dermatologist");
                    break;
                case "neurology":
                    avatarResource = getResourceIdSafely("avatar_neurologist");
                    break;
                case "cardiology":
                    avatarResource = getResourceIdSafely("avatar_cardiologist");
                    break;
                case "pediatrics":
                    avatarResource = getResourceIdSafely("avatar_pediatrician");
                    break;
                case "orthopedics":
                    avatarResource = getResourceIdSafely("avatar_orthopedist");
                    break;
                case "gastroenterology":
                    avatarResource = getResourceIdSafely("avatar_gastroenterologist");
                    break;
                default:
                    avatarResource = R.drawable.ic_doctor_default;
                    break;
            }
        }

        ivDoctorAvatar.setImageResource(avatarResource);
    }

    // FIXED: Helper method to safely get resource ID
    private int getResourceIdSafely(String resourceName) {
        try {
            int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
            return resourceId != 0 ? resourceId : R.drawable.ic_doctor_default;
        } catch (Exception e) {
            return R.drawable.ic_doctor_default;
        }
    }

    private void displayPackageInformation(Appointment appointment) {
        String appointmentType = appointment.getAppointmentType() != null ?
                appointment.getAppointmentType() : "General Consultation";
        double fee = appointment.getFee() > 0 ? appointment.getFee() : 100000.0;

        tvPackageName.setText(appointmentType);
        tvPackagePrice.setText(String.format(Locale.getDefault(), "%.0f VND", fee));

        // Set package icon based on type
        if (appointmentType.toLowerCase().contains("emergency")) {
            ivPackageIcon.setImageResource(R.drawable.ic_emergency);
        } else if (appointmentType.toLowerCase().contains("consultation")) {
            ivPackageIcon.setImageResource(R.drawable.ic_consultation);
        } else {
            ivPackageIcon.setImageResource(R.drawable.ic_medical_package);
        }
    }

    private void configurePrimaryActionButton(Appointment appointment) {
        AppointmentStatus status = appointment.getStatus();

        switch (status) {
            case UPCOMING:
                btnPrimaryAction.setText("Message (Start at 10:00 PM)");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.primary_blue));
                break;
            case COMPLETED:
                btnPrimaryAction.setText("Leave a Review");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.success_green));
                break;
            case CANCELLED:
                btnPrimaryAction.setText("Book Again");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.primary_blue));
                break;
            default:
                btnPrimaryAction.setText("View Details");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.text_gray));
                break;
        }
    }

    private void handlePrimaryAction() {
        if (appointment == null) return;

        AppointmentStatus status = appointment.getStatus();

        switch (status) {
            case UPCOMING:
                // Handle messaging functionality
                Toast.makeText(this, "Messaging will be available at appointment time", Toast.LENGTH_SHORT).show();
                break;
            case COMPLETED:
                // Navigate to review screen
                Intent reviewIntent = new Intent(this, ReviewActivity.class);
                reviewIntent.putExtra("appointment_id", appointmentId);
                reviewIntent.putExtra("doctor_name", appointment.getDoctor());
                startActivity(reviewIntent);
                break;
            case CANCELLED:
                // Navigate to booking screen to book again
                Intent bookIntent = new Intent(this, RescheduleActivity.class);
                bookIntent.putExtra("appointment_id", appointmentId);
                startActivity(bookIntent);
                break;
        }
    }

    private void showMoreOptions() {
        // Show popup menu with options like cancel, reschedule, etc.
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, ivMore);
        popup.getMenuInflater().inflate(R.menu.appointment_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_reschedule) {
                rescheduleAppointment();
                return true;
            } else if (itemId == R.id.action_cancel) {
                cancelAppointment();
                return true;
            } else if (itemId == R.id.action_share) {
                shareAppointmentDetails();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void rescheduleAppointment() {
        Intent intent = new Intent(this, RescheduleActivity.class);
        intent.putExtra("appointment_id", appointmentId);
        startActivity(intent);
    }

    private void cancelAppointment() {
        // Show confirmation dialog then cancel
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel this appointment?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle cancellation
                    service.cancelAppointment(appointmentId, "User requested cancellation",
                            new TungFeaturesService.OnCancelListener() {
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
                                        Toast.makeText(AppointmentDetailActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void shareAppointmentDetails() {
        if (appointment == null) return;

        String shareText = String.format(
                "Appointment Details:\n" +
                        "Doctor: %s\n" +
                        "Date: %s\n" +
                        "Time: %s\n" +
                        "Clinic: %s",
                appointment.getDoctor(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getClinic()
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share Appointment"));
    }

    private String formatDateTime(String date, String time) {
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
        if (appointmentType == null) return "General Practice";

        if (appointmentType.toLowerCase().contains("dermatology")) {
            return "Dermatologist";
        } else if (appointmentType.toLowerCase().contains("cardiology")) {
            return "Cardiologist";
        } else if (appointmentType.toLowerCase().contains("neurology")) {
            return "Neurologist";
        } else if (appointmentType.toLowerCase().contains("pediatric")) {
            return "Pediatrician";
        } else if (appointmentType.toLowerCase().contains("orthopedic")) {
            return "Orthopedist";
        } else {
            return "General Practice";
        }
    }
}
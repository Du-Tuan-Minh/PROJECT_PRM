// NEW FILE: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/AppointmentDetailActivity.java
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
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.button.MaterialButton;

public class AppointmentDetailActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivBack, ivDoctorAvatar;
    private TextView tvDoctorName, tvSpecialty, tvScheduledAppointment, tvDateTime;
    private TextView tvPatientName, tvPatientPhone, tvPatientAge, tvPatientGender;
    private TextView tvSymptoms, tvPackageName, tvPackagePrice;
    private MaterialButton btnPrimaryAction;

    // Data
    private Appointment appointment;
    private HealthcareService service;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // Get appointment ID from intent
        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadAppointmentDetails();
    }

    private void initViews() {
        // Header
        ivBack = findViewById(R.id.iv_back);
        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvSpecialty = findViewById(R.id.tv_specialty);
        ivDoctorAvatar = findViewById(R.id.iv_doctor_avatar);

        // Scheduled Appointment Section
        tvScheduledAppointment = findViewById(R.id.tv_scheduled_appointment);
        tvDateTime = findViewById(R.id.tv_date_time);

        // Patient Information
        tvPatientName = findViewById(R.id.tv_patient_name);
        tvPatientPhone = findViewById(R.id.tv_patient_phone);
        tvPatientAge = findViewById(R.id.tv_patient_age);
        tvPatientGender = findViewById(R.id.tv_patient_gender);
        tvSymptoms = findViewById(R.id.tv_symptoms);

        // Package Information
        tvPackageName = findViewById(R.id.tv_package_name);
        tvPackagePrice = findViewById(R.id.tv_package_price);

        // Action Button
        btnPrimaryAction = findViewById(R.id.btn_primary_action);

        // Initialize service
        service = HealthcareService.getInstance(this);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnPrimaryAction.setOnClickListener(v -> {
            if (appointment != null) {
                handlePrimaryAction();
            }
        });
    }

    private void loadAppointmentDetails() {
        service.getAppointmentDetails(appointmentId, new HealthcareService.OnAppointmentDetailListener() {
            @Override
            public void onSuccess(Appointment appointment) {
                runOnUiThread(() -> {
                    AppointmentDetailActivity.this.appointment = appointment;
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
        tvSpecialty.setText(getSpecialtyFromType(appointment.getAppointmentType()));

        // Set doctor avatar (use default for now)
        ivDoctorAvatar.setImageResource(R.drawable.default_doctor_avatar);

        // Scheduled Appointment
        tvScheduledAppointment.setText("Scheduled Appointment");
        String dateTime = formatDateTime(appointment.getDate(), appointment.getTime());
        tvDateTime.setText(dateTime);

        // Patient Information
        tvPatientName.setText(appointment.getPatientName() != null ? appointment.getPatientName() : "N/A");
        tvPatientPhone.setText(appointment.getPatientPhone() != null ? appointment.getPatientPhone() : "N/A");
        tvPatientAge.setText(appointment.getPatientAge() != null ? appointment.getPatientAge() + " years old" : "N/A");
        tvPatientGender.setText(appointment.getPatientGender() != null ? appointment.getPatientGender() : "N/A");
        tvSymptoms.setText(appointment.getSymptoms() != null ? appointment.getSymptoms() : "No symptoms reported");

        // Package Information
        String packageName = getPackageName(appointment.getAppointmentType());
        tvPackageName.setText(packageName);

        if (appointment.getAppointmentFee() > 0) {
            tvPackagePrice.setText(String.format("₫%.0f", appointment.getAppointmentFee()));
        } else {
            tvPackagePrice.setText("₫200,000"); // Default price
        }

        // Configure action button based on status
        configureActionButton(appointment);
    }

    private void configureActionButton(Appointment appointment) {
        AppointmentStatus status = AppointmentStatus.fromValue(appointment.getStatus());

        switch (status) {
            case PENDING:
            case UPCOMING:
            case CONFIRMED:
                btnPrimaryAction.setText("Message (Start at 18:00 PM)");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.primary_blue));
                btnPrimaryAction.setVisibility(View.VISIBLE);
                break;

            case COMPLETED:
                btnPrimaryAction.setText("Book Again");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.primary_blue));
                btnPrimaryAction.setVisibility(View.VISIBLE);
                break;

            case CANCELLED:
                btnPrimaryAction.setText("Contact Clinic");
                btnPrimaryAction.setBackgroundTintList(getColorStateList(R.color.text_gray));
                btnPrimaryAction.setVisibility(View.VISIBLE);
                break;

            default:
                btnPrimaryAction.setVisibility(View.GONE);
                break;
        }
    }

    private void handlePrimaryAction() {
        AppointmentStatus status = AppointmentStatus.fromValue(appointment.getStatus());

        switch (status) {
            case PENDING:
            case UPCOMING:
            case CONFIRMED:
                // Start messaging or show messaging interface
                Toast.makeText(this, "Opening messaging at scheduled time...", Toast.LENGTH_SHORT).show();
                break;

            case COMPLETED:
                // Navigate to booking with pre-filled data
                bookAgain();
                break;

            case CANCELLED:
                // Contact clinic
                contactClinic();
                break;
        }
    }

    private void bookAgain() {
        service.getBookAgainTemplate(appointment.getId(), new com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager.OnBookAgainListener() {
            @Override
            public void onSuccess(com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager.AppointmentTemplate template) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(AppointmentDetailActivity.this, AppointmentBookingActivity.class);
                    intent.putExtra("clinic_id", template.clinicId);
                    intent.putExtra("doctor_name", template.doctorName);
                    intent.putExtra("patient_name", template.patientName);
                    intent.putExtra("patient_phone", template.patientPhone);
                    intent.putExtra("patient_age", template.patientAge);
                    intent.putExtra("patient_gender", template.patientGender);
                    intent.putExtra("symptoms", template.symptoms);
                    intent.putExtra("medical_history", template.medicalHistory);
                    startActivity(intent);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AppointmentDetailActivity.this, "Error creating new appointment: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void contactClinic() {
        // Open phone dialer with clinic phone number
        if (appointment.getClinic() != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(android.net.Uri.parse("tel:0123456789")); // Default clinic phone
            startActivity(intent);
        } else {
            Toast.makeText(this, "Contact information not available", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSpecialtyFromType(String appointmentType) {
        if (appointmentType == null) return "General";

        switch (appointmentType.toLowerCase()) {
            case "consultation": return "Tư vấn";
            case "checkup": return "Khám tổng quát";
            case "followup": return "Tái khám";
            case "specialist": return "Chuyên khoa";
            case "emergency": return "Cấp cứu";
            default: return "Tư vấn";
        }
    }

    private String getPackageName(String appointmentType) {
        if (appointmentType == null) return "Messaging";

        switch (appointmentType.toLowerCase()) {
            case "video": return "Video Call";
            case "voice": return "Voice Call";
            case "messaging": return "Messaging";
            default: return "Messaging";
        }
    }

    private String formatDateTime(String date, String time) {
        try {
            // Convert date format from yyyy-MM-dd to readable format
            String[] dateParts = date.split("-");
            if (dateParts.length == 3) {
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                int year = Integer.parseInt(dateParts[0]);

                String[] monthNames = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                return "Today, December 22, 2022 | " + time;
            }
        } catch (Exception e) {
            // Fall back to original format if parsing fails
        }

        return date + " | " + time;
    }
}
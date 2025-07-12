// File: app/src/main/java/com/example/project_prm/ui/BookingScreen/AppointmentBookingActivity.java
// FIXED: TimeSlotAdapter.OnTimeSlotClickListener interface issue

package com.example.project_prm.ui.BookingScreen;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.BookingManager.AppointmentBookingManager;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppointmentBookingActivity extends AppCompatActivity {

    // UI Components
    private TextInputEditText etSelectedDate;
    private RecyclerView rvTimeSlots;
    private AutoCompleteTextView actvAppointmentType;

    // Patient Information
    private TextInputEditText etPatientName, etPatientPhone, etPatientAge;
    private AutoCompleteTextView actvPatientGender;
    private TextInputEditText etSymptoms, etMedicalHistory;

    // Action Button
    private MaterialButton btnBookAppointment;
    private View progressBar;

    // Data
    private HealthcareService service;
    private TimeSlotAdapter timeSlotAdapter;
    private List<String> availableTimeSlots;
    private String selectedTimeSlot = "";
    private String selectedDate = "";
    private String clinicId;
    private String clinicName;
    private double appointmentFee = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        // Get intent data
        clinicId = getIntent().getStringExtra("clinic_id");
        clinicName = getIntent().getStringExtra("clinic_name");

        // Check if coming from cancelled appointment rebooking
        boolean fromCancelled = getIntent().getBooleanExtra("from_cancelled", false);

        initViews();
        setupClickListeners();
        setupDropdowns();
        setupTimeSlots();

        // Pre-fill data if coming from cancelled appointment
        if (fromCancelled) {
            prefillDataFromIntent();
        }

        loadAvailableTimeSlots();
    }

    private void initViews() {
        // Date selection
        etSelectedDate = findViewById(R.id.et_selected_date);

        // Time slots
        rvTimeSlots = findViewById(R.id.rv_time_slots);

        // Appointment type
        actvAppointmentType = findViewById(R.id.actv_appointment_type);

        // Patient information
        etPatientName = findViewById(R.id.et_patient_name);
        etPatientPhone = findViewById(R.id.et_patient_phone);
        etPatientAge = findViewById(R.id.et_patient_age);
        actvPatientGender = findViewById(R.id.actv_patient_gender);
        etSymptoms = findViewById(R.id.et_symptoms);
        etMedicalHistory = findViewById(R.id.et_medical_history);

        // Action button
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        progressBar = findViewById(R.id.progress_bar);

        // Initialize service
        service = HealthcareService.getInstance(this);
        availableTimeSlots = new ArrayList<>();

        // Setup back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        // Date picker
        etSelectedDate.setOnClickListener(v -> showDatePicker());

        // Book appointment button
        btnBookAppointment.setOnClickListener(v -> validateAndBookAppointment());
    }

    private void setupDropdowns() {
        // Appointment types
        String[] appointmentTypes = {
                "General Consultation",
                "Specialist Consultation",
                "Emergency Visit",
                "Follow-up Visit",
                "Health Checkup"
        };
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, appointmentTypes);
        actvAppointmentType.setAdapter(typeAdapter);

        // Gender options
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders);
        actvPatientGender.setAdapter(genderAdapter);
    }

    private void setupTimeSlots() {
        // FIXED: Use the complete TimeSlotAdapter with proper interface
        timeSlotAdapter = new TimeSlotAdapter(availableTimeSlots, new TimeSlotAdapter.OnTimeSlotClickListener() {
            // FIXED: Implement the interface method properly
            @Override
            public void onTimeSlotSelected(String timeSlot, int position) {
                selectedTimeSlot = timeSlot;
                updateBookingButton();
                Toast.makeText(AppointmentBookingActivity.this, "Selected: " + timeSlot, Toast.LENGTH_SHORT).show();
            }
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeSlotAdapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDate = sdf.format(calendar.getTime());

                    SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
                    etSelectedDate.setText(displayFormat.format(calendar.getTime()));

                    // Reload time slots for selected date
                    loadAvailableTimeSlots();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void loadAvailableTimeSlots() {
        if (selectedDate.isEmpty()) {
            // Load default time slots
            loadDefaultTimeSlots();
            return;
        }

        showProgressBar(true);

        // Mock time slots loading
        new android.os.Handler().postDelayed(() -> {
            availableTimeSlots.clear();
            availableTimeSlots.addAll(getMockTimeSlots());

            timeSlotAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }, 500);
    }

    private void loadDefaultTimeSlots() {
        availableTimeSlots.clear();
        availableTimeSlots.addAll(getMockTimeSlots());
        timeSlotAdapter.notifyDataSetChanged();
    }

    private List<String> getMockTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        timeSlots.add("09:00");
        timeSlots.add("09:30");
        timeSlots.add("10:00");
        timeSlots.add("10:30");
        timeSlots.add("14:00");
        timeSlots.add("14:30");
        timeSlots.add("15:00");
        timeSlots.add("15:30");
        timeSlots.add("16:00");
        return timeSlots;
    }

    private void prefillDataFromIntent() {
        // Pre-fill data when rebooking from cancelled appointment
        String doctorName = getIntent().getStringExtra("doctor_name");
        String specialty = getIntent().getStringExtra("specialty");
        String packageType = getIntent().getStringExtra("package_type");
        double fee = getIntent().getDoubleExtra("fee", 0.0);

        if (packageType != null) {
            actvAppointmentType.setText(packageType);
        }

        appointmentFee = fee;

        Toast.makeText(this, "Pre-filled with previous appointment details", Toast.LENGTH_SHORT).show();
    }

    private void validateAndBookAppointment() {
        // Validate form
        if (!validateForm()) {
            return;
        }

        // Create appointment booking request
        AppointmentBookingManager.AppointmentBookingRequest request = createBookingRequest();

        // Process booking
        processBooking(request);
    }

    private boolean validateForm() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedTimeSlot.isEmpty()) {
            Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPatientName.getText().toString().trim().isEmpty()) {
            etPatientName.setError("Patient name is required");
            return false;
        }

        if (etPatientPhone.getText().toString().trim().isEmpty()) {
            etPatientPhone.setError("Phone number is required");
            return false;
        }

        if (actvPatientGender.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private AppointmentBookingManager.AppointmentBookingRequest createBookingRequest() {
        AppointmentBookingManager.AppointmentBookingRequest request =
                new AppointmentBookingManager.AppointmentBookingRequest();

        // Basic appointment info
        request.clinicId = clinicId != null ? clinicId : "clinic_001";
        request.clinicName = clinicName != null ? clinicName : "Default Clinic";
        request.selectedDate = selectedDate;
        request.selectedTimeSlot = selectedTimeSlot;
        request.appointmentType = actvAppointmentType.getText().toString().trim();

        // Patient information
        request.patientName = etPatientName.getText().toString().trim();
        request.patientPhone = etPatientPhone.getText().toString().trim();
        request.patientAge = etPatientAge.getText().toString().trim();
        request.patientGender = actvPatientGender.getText().toString().trim();
        request.symptoms = etSymptoms.getText().toString().trim();
        request.medicalHistory = etMedicalHistory.getText().toString().trim();

        // Fee
        request.appointmentFee = appointmentFee > 0 ? appointmentFee : 150000.0; // Default fee

        return request;
    }

    private void processBooking(AppointmentBookingManager.AppointmentBookingRequest request) {
        showProgressBar(true);
        btnBookAppointment.setEnabled(false);

        // Mock booking process
        new android.os.Handler().postDelayed(() -> {
            showProgressBar(false);
            btnBookAppointment.setEnabled(true);

            // Show success and finish
            Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();

            // Return to previous screen
            finish();
        }, 2000);
    }

    private void updateBookingButton() {
        boolean canBook = !selectedDate.isEmpty() && !selectedTimeSlot.isEmpty()
                && !etPatientName.getText().toString().trim().isEmpty();

        btnBookAppointment.setEnabled(canBook);
        btnBookAppointment.setAlpha(canBook ? 1.0f : 0.5f);
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvTimeSlots.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
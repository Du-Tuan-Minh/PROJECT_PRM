package com.example.project_prm.ui.BookingScreen;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private TextInputEditText etPatientName, etPatientPhone, etPatientAge;
    private AutoCompleteTextView actvPatientGender;
    private TextInputEditText etSymptoms, etMedicalHistory;
    private MaterialButton btnBookAppointment;
    private View progressBar;

    // Data
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

        clinicId = getIntent().getStringExtra("clinic_id");
        clinicName = getIntent().getStringExtra("clinic_name");
        boolean fromCancelled = getIntent().getBooleanExtra("from_cancelled", false);

        initViews();
        setupClickListeners();
        setupDropdowns();
        setupTimeSlots();

        if (fromCancelled) {
            prefillDataFromIntent();
        }

        loadAvailableTimeSlots();
    }

    private void initViews() {
        etSelectedDate = findViewById(R.id.et_selected_date);
        rvTimeSlots = findViewById(R.id.rv_time_slots);
        actvAppointmentType = findViewById(R.id.actv_appointment_type);
        etPatientName = findViewById(R.id.et_patient_name);
        etPatientPhone = findViewById(R.id.et_patient_phone);
        etPatientAge = findViewById(R.id.et_patient_age);
        actvPatientGender = findViewById(R.id.actv_patient_gender);
        etSymptoms = findViewById(R.id.et_symptoms);
        etMedicalHistory = findViewById(R.id.et_medical_history);
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        progressBar = findViewById(R.id.progress_bar);
        availableTimeSlots = new ArrayList<>();
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        etSelectedDate.setOnClickListener(v -> showDatePicker());
        btnBookAppointment.setOnClickListener(v -> validateAndBookAppointment());
    }

    private void setupDropdowns() {
        String[] appointmentTypes = { "General Consultation", "Specialist Consultation", "Emergency Visit", "Follow-up Visit", "Health Checkup" };
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, appointmentTypes);
        actvAppointmentType.setAdapter(typeAdapter);

        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        actvPatientGender.setAdapter(genderAdapter);
    }

    private void setupTimeSlots() {
        timeSlotAdapter = new TimeSlotAdapter(availableTimeSlots, (timeSlot, position) -> {
            selectedTimeSlot = timeSlot;
            updateBookingButton();
            Toast.makeText(AppointmentBookingActivity.this, "Selected: " + timeSlot, Toast.LENGTH_SHORT).show();
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
                    loadAvailableTimeSlots();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void loadAvailableTimeSlots() {
        if (selectedDate.isEmpty()) {
            loadDefaultTimeSlots();
            return;
        }
        showProgressBar(true);
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
        timeSlots.add("09:00"); timeSlots.add("09:30"); timeSlots.add("10:00");
        timeSlots.add("10:30"); timeSlots.add("14:00"); timeSlots.add("14:30");
        timeSlots.add("15:00"); timeSlots.add("15:30"); timeSlots.add("16:00");
        return timeSlots;
    }

    private void prefillDataFromIntent() {
        String packageType = getIntent().getStringExtra("package_type");
        if (packageType != null) {
            actvAppointmentType.setText(packageType);
        }
        appointmentFee = getIntent().getDoubleExtra("fee", 0.0);
        Toast.makeText(this, "Pre-filled with previous appointment details", Toast.LENGTH_SHORT).show();
    }

    private void validateAndBookAppointment() {
        if (!validateForm()) return;
        Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();
        finish();
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

    private void updateBookingButton() {
        boolean canBook = !selectedDate.isEmpty() && !selectedTimeSlot.isEmpty() && !etPatientName.getText().toString().trim().isEmpty();
        btnBookAppointment.setEnabled(canBook);
        btnBookAppointment.setAlpha(canBook ? 1.0f : 0.5f);
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvTimeSlots.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
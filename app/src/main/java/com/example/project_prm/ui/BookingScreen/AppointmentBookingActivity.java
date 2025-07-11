// NEW FILE: app/src/main/java/com/example/project_prm/ui/BookingScreen/AppointmentBookingActivity.java
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

        // Get clinic info from intent
        clinicId = getIntent().getStringExtra("clinic_id");
        clinicName = getIntent().getStringExtra("clinic_name");

        if (clinicId == null) {
            Toast.makeText(this, "Invalid clinic information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        setupRecyclerView();
        setupDropdowns();
        prefillDataIfAvailable();
    }

    private void initViews() {
        // Date selection
        etSelectedDate = findViewById(R.id.et_selected_date);
        rvTimeSlots = findViewById(R.id.rv_time_slots);
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
        etSelectedDate.setFocusable(false);

        // Book appointment
        btnBookAppointment.setOnClickListener(v -> bookAppointment());
    }

    private void setupRecyclerView() {
        timeSlotAdapter = new TimeSlotAdapter(availableTimeSlots, new TimeSlotAdapter.OnTimeSlotClickListener() {
            @Override
            public void onTimeSlotSelected(String timeSlot) {
                selectedTimeSlot = timeSlot;
                updateBookingButton();
            }
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeSlotAdapter);
    }

    private void setupDropdowns() {
        // Appointment types
        service.getAppointmentTypes();
        String[] appointmentTypes = {"Tư vấn", "Khám tổng quát", "Tái khám", "Khám chuyên khoa", "Cấp cứu"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, appointmentTypes);
        actvAppointmentType.setAdapter(typeAdapter);
        actvAppointmentType.setText(appointmentTypes[0]); // Default selection

        // Patient gender
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        actvPatientGender.setAdapter(genderAdapter);

        // Calculate fee when appointment type changes
        actvAppointmentType.setOnItemClickListener((parent, view, position, id) -> {
            calculateAppointmentFee();
        });
    }

    private void prefillDataIfAvailable() {
        // Pre-fill data if coming from "Book Again" or other sources
        String prefillName = getIntent().getStringExtra("patient_name");
        String prefillPhone = getIntent().getStringExtra("patient_phone");
        String prefillAge = getIntent().getStringExtra("patient_age");
        String prefillGender = getIntent().getStringExtra("patient_gender");
        String prefillSymptoms = getIntent().getStringExtra("symptoms");
        String prefillMedicalHistory = getIntent().getStringExtra("medical_history");

        if (prefillName != null) etPatientName.setText(prefillName);
        if (prefillPhone != null) etPatientPhone.setText(prefillPhone);
        if (prefillAge != null) etPatientAge.setText(prefillAge);
        if (prefillGender != null) actvPatientGender.setText(prefillGender);
        if (prefillSymptoms != null) etSymptoms.setText(prefillSymptoms);
        if (prefillMedicalHistory != null) etMedicalHistory.setText(prefillMedicalHistory);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDate = sdf.format(calendar.getTime());

                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    etSelectedDate.setText(displayFormat.format(calendar.getTime()));

                    loadTimeSlots();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Set maximum date to 30 days from now
        calendar.add(Calendar.DAY_OF_MONTH, 29);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void loadTimeSlots() {
        if (selectedDate.isEmpty()) return;

        showLoading(true);

        service.getAvailableTimeSlots(clinicId, selectedDate, new AppointmentBookingManager.OnTimeSlotsListener() {
            @Override
            public void onSuccess(List<String> timeSlots) {
                runOnUiThread(() -> {
                    showLoading(false);
                    availableTimeSlots.clear();
                    availableTimeSlots.addAll(timeSlots);
                    timeSlotAdapter.notifyDataSetChanged();

                    // Reset selected time slot
                    selectedTimeSlot = "";
                    updateBookingButton();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Failed to load time slots: " + error);
                });
            }
        });
    }

    private void calculateAppointmentFee() {
        String appointmentType = actvAppointmentType.getText().toString();
        if (appointmentType.isEmpty()) return;

        service.calculateAppointmentFee(clinicId, appointmentType, new AppointmentBookingManager.OnFeeCalculationListener() {
            @Override
            public void onSuccess(double fee) {
                runOnUiThread(() -> {
                    appointmentFee = fee;
                    updateBookingButton();
                });
            }

            @Override
            public void onError(String error) {
                // Use default fee
                appointmentFee = 200000; // 200k VND default
                updateBookingButton();
            }
        });
    }

    private void updateBookingButton() {
        boolean canBook = !selectedDate.isEmpty() && !selectedTimeSlot.isEmpty() &&
                !etPatientName.getText().toString().trim().isEmpty();

        btnBookAppointment.setEnabled(canBook);

        if (appointmentFee > 0) {
            btnBookAppointment.setText(String.format("Đặt lịch - ₫%.0f", appointmentFee));
        } else {
            btnBookAppointment.setText("Đặt lịch khám");
        }
    }

    private void bookAppointment() {
        // Create booking request
        AppointmentBookingManager.AppointmentBookingRequest request = new AppointmentBookingManager.AppointmentBookingRequest();

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        request.userId = prefs.getInt("userId", 1); // Default to 1 for testing

        // Clinic and appointment info
        request.clinicId = clinicId;
        request.clinicName = clinicName;
        request.doctorName = "Dr. " + clinicName; // Default doctor name
        request.date = selectedDate;
        request.time = selectedTimeSlot;

        // Patient information
        request.patientName = etPatientName.getText().toString().trim();
        request.patientPhone = etPatientPhone.getText().toString().trim();
        request.patientAge = etPatientAge.getText().toString().trim();
        request.patientGender = actvPatientGender.getText().toString().trim();
        request.symptoms = etSymptoms.getText().toString().trim();
        request.medicalHistory = etMedicalHistory.getText().toString().trim();

        // Appointment details
        request.appointmentType = actvAppointmentType.getText().toString().trim();
        request.appointmentFee = appointmentFee;

        showLoading(true);

        service.bookAppointment(request, new AppointmentBookingManager.OnBookingListener() {
            @Override
            public void onSuccess(Appointment appointment) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(AppointmentBookingActivity.this,
                            "Đặt lịch thành công! ID: " + appointment.getId(),
                            Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Booking failed: " + error);
                });
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnBookAppointment.setEnabled(!show);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
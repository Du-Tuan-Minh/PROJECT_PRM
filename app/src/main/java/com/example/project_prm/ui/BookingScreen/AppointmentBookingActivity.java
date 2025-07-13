package com.example.project_prm.ui.BookingScreen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.BookingManager.AppointmentBookingManager;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.example.project_prm.ui.AppointmentScreen.MyAppointmentActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppointmentBookingActivity extends AppCompatActivity {
    private static final String TAG = "AppointmentBooking";

    // UI Components
    private TextInputEditText etSelectedDate;
    private RecyclerView rvTimeSlots;
    private AutoCompleteTextView actvAppointmentType;
    private TextInputEditText etPatientName, etPatientPhone, etPatientAge;
    private AutoCompleteTextView actvPatientGender;
    private TextInputEditText etSymptoms, etMedicalHistory;
    private MaterialButton btnBookAppointment;
    private View progressBar;
    private TextView tvAppointmentFee, tvDoctorInfo, tvClinicInfo;

    // Data
    private TimeSlotAdapter timeSlotAdapter;
    private List<String> availableTimeSlots;
    private String selectedTimeSlot = "";
    private String selectedDate = "";
    private String clinicId;
    private String clinicName;
    private String doctorName;
    private double appointmentFee = 150000.0; // Default fee
    private String selectedAppointmentType = "";

    // Services
    private HealthcareService service;
    private AppointmentBookingManager bookingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        // Get intent data
        getIntentData();

        // Initialize services
        service = HealthcareService.getInstance(this);
        bookingManager = new AppointmentBookingManager(this);
        availableTimeSlots = new ArrayList<>();

        initViews();
        setupClickListeners();
        setupDropdowns();
        setupTimeSlots();
        displayBookingInfo();

        // Prefill data if coming from other activities
        prefillDataIfNeeded();

        // Load initial time slots
        loadAvailableTimeSlots();
    }

    private void getIntentData() {
        clinicId = getIntent().getStringExtra("clinic_id");
        clinicName = getIntent().getStringExtra("clinic_name");
        doctorName = getIntent().getStringExtra("doctor_name");

        // Handle different entry points
        boolean fromCancelled = getIntent().getBooleanExtra("from_cancelled", false);
        boolean fromCompleted = getIntent().getBooleanExtra("from_completed", false);

        Log.d(TAG, "Intent data - Clinic: " + clinicName + ", Doctor: " + doctorName);
    }

    private void initViews() {
        // Back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // Main form fields
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

        // Info displays
        tvAppointmentFee = findViewById(R.id.tv_appointment_fee);
        tvDoctorInfo = findViewById(R.id.tv_doctor_info);
        tvClinicInfo = findViewById(R.id.tv_clinic_info);

        // Set white background and black text for all inputs
        setWhiteBackgroundBlackText();
    }

    private void setWhiteBackgroundBlackText() {
        // Set white background for all input fields
        etSelectedDate.setBackgroundColor(getColor(android.R.color.white));
        etPatientName.setBackgroundColor(getColor(android.R.color.white));
        etPatientPhone.setBackgroundColor(getColor(android.R.color.white));
        etPatientAge.setBackgroundColor(getColor(android.R.color.white));
        etSymptoms.setBackgroundColor(getColor(android.R.color.white));
        etMedicalHistory.setBackgroundColor(getColor(android.R.color.white));
        actvAppointmentType.setBackgroundColor(getColor(android.R.color.white));
        actvPatientGender.setBackgroundColor(getColor(android.R.color.white));

        // Set black text color
        etSelectedDate.setTextColor(getColor(android.R.color.black));
        etPatientName.setTextColor(getColor(android.R.color.black));
        etPatientPhone.setTextColor(getColor(android.R.color.black));
        etPatientAge.setTextColor(getColor(android.R.color.black));
        etSymptoms.setTextColor(getColor(android.R.color.black));
        etMedicalHistory.setTextColor(getColor(android.R.color.black));
        actvAppointmentType.setTextColor(getColor(android.R.color.black));
        actvPatientGender.setTextColor(getColor(android.R.color.black));
    }

    private void setupClickListeners() {
        etSelectedDate.setOnClickListener(v -> showDatePicker());
        btnBookAppointment.setOnClickListener(v -> validateAndBookAppointment());
    }

    private void setupDropdowns() {
        // Vietnamese appointment types with fees
        String[] appointmentTypes = {
                "Khám tổng quát",
                "Khám chuyên khoa",
                "Khám cấp cứu",
                "Tái khám",
                "Kiểm tra sức khỏe định kỳ",
                "Khám da liễu",
                "Khám tim mạch",
                "Khám thần kinh",
                "Khám nhi khoa",
                "Khám sản phụ khoa",
                "Khám tai mũi họng",
                "Khám mắt",
                "Khám răng hàm mặt",
                "Khám cột sống",
                "Khám tiêu hóa",
                "Khám nội tiết"
        };

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, appointmentTypes);
        actvAppointmentType.setAdapter(typeAdapter);

        // Listen for appointment type selection
        actvAppointmentType.setOnItemClickListener((parent, view, position, id) -> {
            selectedAppointmentType = appointmentTypes[position];
            updateAppointmentFee(selectedAppointmentType);
            updateDoctorSpecialty(selectedAppointmentType);
            Log.d(TAG, "Selected appointment type: " + selectedAppointmentType);
        });

        // Gender dropdown in Vietnamese
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders);
        actvPatientGender.setAdapter(genderAdapter);
    }

    private void updateAppointmentFee(String appointmentType) {
        double fee = 150000; // Default fee

        switch (appointmentType) {
            case "Khám tổng quát":
                fee = 150000; // 150k VND
                break;
            case "Khám chuyên khoa":
                fee = 250000; // 250k VND
                break;
            case "Khám cấp cứu":
                fee = 500000; // 500k VND
                break;
            case "Tái khám":
                fee = 100000; // 100k VND
                break;
            case "Kiểm tra sức khỏe định kỳ":
                fee = 300000; // 300k VND
                break;
            case "Khám da liễu":
                fee = 220000; // 220k VND
                break;
            case "Khám tim mạch":
                fee = 350000; // 350k VND
                break;
            case "Khám thần kinh":
                fee = 400000; // 400k VND
                break;
            case "Khám nhi khoa":
                fee = 180000; // 180k VND
                break;
            case "Khám sản phụ khoa":
                fee = 280000; // 280k VND
                break;
            case "Khám tai mũi họng":
                fee = 200000; // 200k VND
                break;
            case "Khám mắt":
                fee = 250000; // 250k VND
                break;
            case "Khám răng hàm mặt":
                fee = 300000; // 300k VND
                break;
            case "Khám cột sống":
                fee = 320000; // 320k VND
                break;
            case "Khám tiêu hóa":
                fee = 260000; // 260k VND
                break;
            case "Khám nội tiết":
                fee = 290000; // 290k VND
                break;
        }

        appointmentFee = fee;
        updateFeeDisplay();

        Log.d(TAG, "Updated fee for " + appointmentType + ": " + fee + " VND");
    }

    private void updateFeeDisplay() {
        if (tvAppointmentFee != null) {
            tvAppointmentFee.setText(String.format("💰 Phí khám: %,.0f VNĐ", appointmentFee));
            tvAppointmentFee.setVisibility(View.VISIBLE);
            tvAppointmentFee.setTextColor(getColor(android.R.color.holo_blue_dark));

            // Animate fee display
            tvAppointmentFee.setAlpha(0f);
            tvAppointmentFee.animate().alpha(1f).setDuration(300).start();
        }
    }

    private void updateDoctorSpecialty(String appointmentType) {
        if (tvDoctorInfo != null) {
            String specialty = getSpecialtyFromAppointmentType(appointmentType);
            String doctorText = "👨‍⚕️ Bác sĩ: " + (doctorName != null ? doctorName : "Sẽ được phân công")
                    + "\n🏥 Chuyên khoa: " + specialty;
            tvDoctorInfo.setText(doctorText);
            tvDoctorInfo.setVisibility(View.VISIBLE);
        }
    }

    private String getSpecialtyFromAppointmentType(String type) {
        switch (type) {
            case "Khám da liễu": return "Da liễu";
            case "Khám tim mạch": return "Tim mạch";
            case "Khám thần kinh": return "Thần kinh";
            case "Khám nhi khoa": return "Nhi khoa";
            case "Khám sản phụ khoa": return "Sản phụ khoa";
            case "Khám tai mũi họng": return "Tai Mũi Họng";
            case "Khám mắt": return "Nhãn khoa";
            case "Khám răng hàm mặt": return "Răng Hàm Mặt";
            case "Khám cột sống": return "Cơ xương khớp";
            case "Khám tiêu hóa": return "Tiêu hóa";
            case "Khám nội tiết": return "Nội tiết";
            case "Khám chuyên khoa": return "Chuyên khoa";
            default: return "Tổng quát";
        }
    }

    private void setupTimeSlots() {
        timeSlotAdapter = new TimeSlotAdapter(availableTimeSlots, (timeSlot, position) -> {
            selectedTimeSlot = timeSlot;
            updateBookingButton();
            Toast.makeText(AppointmentBookingActivity.this,
                    "✅ Đã chọn: " + timeSlot, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Selected time slot: " + timeSlot);
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeSlotAdapter);
    }

    private void displayBookingInfo() {
        // Display clinic info
        if (tvClinicInfo != null && clinicName != null) {
            tvClinicInfo.setText("🏥 Phòng khám: " + clinicName);
            tvClinicInfo.setVisibility(View.VISIBLE);
        }

        // Set default fee display
        updateFeeDisplay();
    }

    private void prefillDataIfNeeded() {
        // Check if coming from other activities with prefilled data
        String prefillName = getIntent().getStringExtra("patient_name");
        String prefillPhone = getIntent().getStringExtra("patient_phone");
        String prefillAge = getIntent().getStringExtra("patient_age");
        String prefillGender = getIntent().getStringExtra("patient_gender");
        String prefillType = getIntent().getStringExtra("appointment_type");

        if (prefillName != null) etPatientName.setText(prefillName);
        if (prefillPhone != null) etPatientPhone.setText(prefillPhone);
        if (prefillAge != null) etPatientAge.setText(prefillAge);
        if (prefillGender != null) actvPatientGender.setText(prefillGender, false);
        if (prefillType != null) {
            actvAppointmentType.setText(prefillType, false);
            selectedAppointmentType = prefillType;
            updateAppointmentFee(prefillType);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDate = sdf.format(calendar.getTime());

                    SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
                    etSelectedDate.setText(displayFormat.format(calendar.getTime()));

                    // Load time slots for selected date
                    loadAvailableTimeSlots();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to tomorrow
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Set maximum date to 30 days from now
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void loadAvailableTimeSlots() {
        if (selectedDate.isEmpty()) {
            // Set default date to tomorrow
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(tomorrow.getTime());

            SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            etSelectedDate.setText(displayFormat.format(tomorrow.getTime()));
        }

        // Show loading
        findViewById(R.id.time_slots_loading).setVisibility(View.VISIBLE);
        rvTimeSlots.setVisibility(View.GONE);

        // Get time slots from service
        String clinicIdForSlots = clinicId != null ? clinicId : "default_clinic";

        bookingManager.getAvailableTimeSlots(clinicIdForSlots, selectedDate,
                new AppointmentBookingManager.OnTimeSlotsListener() {
                    @Override
                    public void onSuccess(List<String> timeSlots) {
                        runOnUiThread(() -> {
                            availableTimeSlots.clear();
                            if (timeSlots != null && !timeSlots.isEmpty()) {
                                availableTimeSlots.addAll(timeSlots);
                            } else {
                                // Default time slots if none available
                                availableTimeSlots.addAll(getDefaultTimeSlots());
                            }

                            timeSlotAdapter.notifyDataSetChanged();

                            findViewById(R.id.time_slots_loading).setVisibility(View.GONE);
                            rvTimeSlots.setVisibility(View.VISIBLE);

                            if (availableTimeSlots.isEmpty()) {
                                findViewById(R.id.tv_no_slots).setVisibility(View.VISIBLE);
                            } else {
                                findViewById(R.id.tv_no_slots).setVisibility(View.GONE);
                            }

                            Log.d(TAG, "Loaded " + availableTimeSlots.size() + " time slots");
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            // Use default time slots on error
                            availableTimeSlots.clear();
                            availableTimeSlots.addAll(getDefaultTimeSlots());
                            timeSlotAdapter.notifyDataSetChanged();

                            findViewById(R.id.time_slots_loading).setVisibility(View.GONE);
                            rvTimeSlots.setVisibility(View.VISIBLE);

                            Log.e(TAG, "Error loading time slots: " + error);
                            Toast.makeText(AppointmentBookingActivity.this,
                                    "Sử dụng khung giờ mặc định", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private List<String> getDefaultTimeSlots() {
        List<String> defaultSlots = new ArrayList<>();
        defaultSlots.add("08:00");
        defaultSlots.add("08:30");
        defaultSlots.add("09:00");
        defaultSlots.add("09:30");
        defaultSlots.add("10:00");
        defaultSlots.add("10:30");
        defaultSlots.add("14:00");
        defaultSlots.add("14:30");
        defaultSlots.add("15:00");
        defaultSlots.add("15:30");
        defaultSlots.add("16:00");
        defaultSlots.add("16:30");
        return defaultSlots;
    }

    private void updateBookingButton() {
        boolean canBook = !selectedTimeSlot.isEmpty() &&
                !selectedDate.isEmpty() &&
                !selectedAppointmentType.isEmpty();

        btnBookAppointment.setEnabled(canBook);
        btnBookAppointment.setText(canBook ? "Đặt lịch khám" : "Vui lòng điền đầy đủ thông tin");
    }

    private void validateAndBookAppointment() {
        // Validate required fields
        String patientName = etPatientName.getText().toString().trim();
        String patientPhone = etPatientPhone.getText().toString().trim();
        String patientAge = etPatientAge.getText().toString().trim();
        String patientGender = actvPatientGender.getText().toString().trim();
        String symptoms = etSymptoms.getText().toString().trim();

        // Validation
        if (patientName.isEmpty()) {
            etPatientName.setError("Vui lòng nhập tên bệnh nhân");
            etPatientName.requestFocus();
            return;
        }

        if (patientPhone.isEmpty()) {
            etPatientPhone.setError("Vui lòng nhập số điện thoại");
            etPatientPhone.requestFocus();
            return;
        }

        if (!isValidPhone(patientPhone)) {
            etPatientPhone.setError("Số điện thoại không hợp lệ");
            etPatientPhone.requestFocus();
            return;
        }

        if (patientAge.isEmpty()) {
            etPatientAge.setError("Vui lòng nhập tuổi");
            etPatientAge.requestFocus();
            return;
        }

        if (patientGender.isEmpty()) {
            actvPatientGender.setError("Vui lòng chọn giới tính");
            actvPatientGender.requestFocus();
            return;
        }

        if (selectedAppointmentType.isEmpty()) {
            actvAppointmentType.setError("Vui lòng chọn loại khám");
            actvAppointmentType.requestFocus();
            return;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày khám", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTimeSlot.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn giờ khám", Toast.LENGTH_SHORT).show();
            return;
        }

        if (symptoms.isEmpty()) {
            etSymptoms.setError("Vui lòng mô tả triệu chứng");
            etSymptoms.requestFocus();
            return;
        }

        // Create booking request
        AppointmentBookingManager.AppointmentBookingRequest request =
                new AppointmentBookingManager.AppointmentBookingRequest();

        request.clinicId = clinicId != null ? clinicId : "default_clinic";
        request.clinicName = clinicName != null ? clinicName : "Phòng khám mặc định";
        request.doctorName = doctorName != null ? doctorName : "Bác sĩ sẽ được phân công";
        request.appointmentDate = selectedDate;
        request.appointmentTime = selectedTimeSlot;
        request.patientName = patientName;
        request.patientPhone = patientPhone;
        request.patientAge = patientAge;
        request.patientGender = patientGender;
        request.symptoms = symptoms;
        request.medicalHistory = etMedicalHistory.getText().toString().trim();
        request.appointmentType = selectedAppointmentType;
        request.appointmentFee = appointmentFee;

        // Book appointment
        bookAppointment(request);
    }

    private boolean isValidPhone(String phone) {
        // Vietnamese phone number validation
        return phone.matches("^(\\+84|0)(3|5|7|8|9)\\d{8}$");
    }

    private void bookAppointment(AppointmentBookingManager.AppointmentBookingRequest request) {
        // Show loading
        showLoading(true);
        btnBookAppointment.setEnabled(false);
        btnBookAppointment.setText("Đang đặt lịch...");

        bookingManager.bookAppointment(request, new AppointmentBookingManager.OnBookingListener() {
            @Override
            public void onSuccess(Appointment appointment) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showSuccessDialog(appointment);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    btnBookAppointment.setEnabled(true);
                    btnBookAppointment.setText("Đặt lịch khám");

                    Toast.makeText(AppointmentBookingActivity.this,
                            "Lỗi đặt lịch: " + error, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Booking error: " + error);
                });
            }
        });
    }

    private void showSuccessDialog(Appointment appointment) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(this);

        String message = String.format(
                "✅ Đặt lịch thành công!\n\n" +
                        "📅 Ngày: %s\n" +
                        "🕐 Giờ: %s\n" +
                        "🏥 Phòng khám: %s\n" +
                        "👨‍⚕️ Bác sĩ: %s\n" +
                        "💰 Phí khám: %,.0f VNĐ\n\n" +
                        "Vui lòng đến đúng giờ hẹn!",
                selectedDate, selectedTimeSlot,
                clinicName != null ? clinicName : "Phòng khám",
                doctorName != null ? doctorName : "Sẽ được thông báo",
                appointmentFee
        );

        builder.setTitle("Đặt lịch thành công")
                .setMessage(message)
                .setPositiveButton("Xem lịch hẹn", (dialog, which) -> {
                    Intent intent = new Intent(this, MyAppointmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Đặt lịch khác", (dialog, which) -> {
                    clearForm();
                })
                .setCancelable(false)
                .show();
    }

    private void clearForm() {
        etPatientName.setText("");
        etPatientPhone.setText("");
        etPatientAge.setText("");
        actvPatientGender.setText("", false);
        etSymptoms.setText("");
        etMedicalHistory.setText("");
        actvAppointmentType.setText("", false);
        etSelectedDate.setText("");
        selectedTimeSlot = "";
        selectedDate = "";
        selectedAppointmentType = "";

        timeSlotAdapter.clearSelection();
        updateBookingButton();
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookingManager != null) {
            bookingManager.close();
        }
    }
}
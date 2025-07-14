package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.project_prm.R;
import com.example.project_prm.Repository.AppointmentRepository;
import com.example.project_prm.Model.AppointmentModel;
import java.util.Date;

public class BookAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "BookAppointmentActivity";
    private ImageView ivBack;
    private TextView tvTitle;
    private int currentStep = 1;
    private AppointmentRepository appointmentRepository;
    public BookingData bookingData = new BookingData();

    public static class BookingData {
        public String doctorId;
        public String doctorName;
        public String doctorSpecialty;
        public String doctorHospital;
        public String doctorImage;
        public String packageType;
        public String duration;
        public String amount;
        public String date;
        public String time;
        public String patientName;
        public String patientGender;
        public String patientAge;
        public String patientPhone;
        public String patientProblem;
        @Override
        public String toString() {
            return "BookingData{" +
                    "doctorName='" + doctorName + '\'' +
                    ", specialty='" + doctorSpecialty + '\'' +
                    ", packageType='" + packageType + '\'' +
                    ", date='" + date + '\'' +
                    ", time='" + time + '\'' +
                    ", patientName='" + patientName + '\'' +
                    ", amount='" + amount + '\'' +
                    '}';
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        appointmentRepository = AppointmentRepository.getInstance();
        getIntentData();
        initViews();
        setupListeners();
        showStep(1);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            bookingData.doctorId = intent.getStringExtra("doctor_id");
            bookingData.doctorName = intent.getStringExtra("doctor_name");
            bookingData.doctorSpecialty = intent.getStringExtra("doctor_specialty");
            bookingData.doctorHospital = intent.getStringExtra("doctor_hospital");
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> handleBackPress());
    }

    private void handleBackPress() {
        if (currentStep > 1) {
            showStep(currentStep - 1);
        } else {
            finish();
        }
    }

    public void showStep(int step) {
        currentStep = step;
        updateTitle();
        Fragment fragment;
        switch (step) {
            case 1:
                fragment = new DoctorSelectFragment();
                break;
            case 2:
                fragment = new DoctorInfoFragment();
                break;
            case 3:
                fragment = new DateTimeSelectionFragment();
                break;
            case 4:
                fragment = new PatientDetailsFragment();
                break;
            case 5:
                fragment = new AppointmentSummaryFragment();
                break;
            default:
                return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private void updateTitle() {
        String title;
        switch (currentStep) {
            case 1: title = "Chọn bác sĩ"; break;
            case 2: title = "Thông tin bác sĩ"; break;
            case 3: title = "Chọn ngày và giờ"; break;
            case 4: title = "Thông tin bệnh nhân"; break;
            case 5: title = "Xác nhận thông tin"; break;
            default: title = "Đặt lịch khám"; break;
        }
        tvTitle.setText(title);
    }

    public void onDoctorSelected(String doctorId, String doctorName, String specialty, String hospital, String image) {
        bookingData.doctorId = doctorId;
        bookingData.doctorName = doctorName;
        bookingData.doctorSpecialty = specialty;
        bookingData.doctorHospital = hospital;
        bookingData.doctorImage = image;
        showStep(2);
    }
    public void onDoctorInfoConfirmed() {
        showStep(3);
    }
    public void onDateTimeSelected(String date, String time) {
        bookingData.date = date;
        bookingData.time = time;
        showStep(4);
    }
    public void onPatientDetailsEntered(String fullName, String gender, String age, String phone, String problem, String packageType, String duration, String amount) {
        bookingData.patientName = fullName;
        bookingData.patientGender = gender;
        bookingData.patientAge = age;
        bookingData.patientPhone = phone;
        bookingData.patientProblem = problem;
        bookingData.packageType = packageType;
        bookingData.duration = duration;
        bookingData.amount = amount;
        showStep(5);
    }
    public void onAppointmentConfirmed() {
        Log.d(TAG, "onAppointmentConfirmed called");
        try {
            if (bookingData.doctorName == null || bookingData.doctorName.isEmpty() ||
                bookingData.doctorSpecialty == null || bookingData.doctorSpecialty.isEmpty() ||
                bookingData.date == null || bookingData.date.isEmpty() ||
                bookingData.time == null || bookingData.time.isEmpty() ||
                bookingData.packageType == null || bookingData.packageType.isEmpty() ||
                bookingData.patientName == null || bookingData.patientName.isEmpty() ||
                bookingData.patientProblem == null || bookingData.patientProblem.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin trước khi xác nhận!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Thiếu dữ liệu bắt buộc: " + bookingData);
                return;
            }
            Log.d(TAG, "Booking data: " + bookingData);
            String amountStr = bookingData.amount != null ? bookingData.amount.replaceAll("[^0-9]", "") : "0";
            int amount = amountStr.isEmpty() ? 0 : Integer.parseInt(amountStr);
            AppointmentModel appointment = new AppointmentModel(
                generateAppointmentId(),
                bookingData.doctorName,
                bookingData.doctorSpecialty,
                bookingData.date,
                bookingData.time,
                "upcoming",
                bookingData.packageType,
                amount,
                bookingData.patientName,
                bookingData.patientProblem,
                bookingData.doctorHospital != null ? bookingData.doctorHospital : "Bệnh viện ABC",
                bookingData.doctorImage != null ? bookingData.doctorImage : "ic_doctor_placeholder"
            );
            boolean success = appointmentRepository.addAppointment(appointment);
            if (success) {
                Log.d(TAG, "Appointment saved successfully");
                BookingSuccessDialog dialog = new BookingSuccessDialog();
                dialog.show(getSupportFragmentManager(), "BookingSuccessDialog");
            } else {
                Log.e(TAG, "Failed to save appointment");
                Toast.makeText(this, "Đặt lịch thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onAppointmentConfirmed: " + e.getMessage(), e);
            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
    private String generateAppointmentId() {
        return "APT" + System.currentTimeMillis();
    }
    public void onBookingSuccess() {
        Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleBackPress();
    }
}
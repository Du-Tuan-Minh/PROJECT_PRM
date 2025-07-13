package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.project_prm.R;
import com.example.project_prm.MainScreen.AppointmentModel;
import com.example.project_prm.MainScreen.AppointmentRepository;

public class BookAppointmentActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private int currentStep = 1;
    public BookingData bookingData;

    public static class BookingData {
        public String selectedDate;
        public String selectedTime;
        public String fullName;
        public String gender;
        public String age;
        public String problem;
        public String doctorName = "BS. Jenny Watson";
        public String doctorSpecialty = "Chuyên khoa Miễn dịch";
        public String doctorLocation = "Bệnh viện Christ, London, UK";
        public String packageType = "Nhắn tin";
        public String duration = "30 phút";
        public int amount = 500000; // VND
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        bookingData = new BookingData();
        initViews();
        setupListeners();
        showStep(1);
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
                fragment = new DateTimeSelectionFragment();
                break;
            case 3:
                fragment = new PatientDetailsFragment();
                break;
            case 4:
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
            case 2: title = "Chọn ngày & giờ"; break;
            case 3: title = "Thông tin bệnh nhân"; break;
            case 4: title = "Xem lại thông tin"; break;
            default: title = "Đặt lịch khám"; break;
        }
        tvTitle.setText(title);
    }

    public void onDoctorSelected(String doctor, String specialty, String location) {
        bookingData.doctorName = doctor;
        bookingData.doctorSpecialty = specialty;
        bookingData.doctorLocation = location;
        showStep(2);
    }

    public void onDateTimeSelected(String date, String time) {
        bookingData.selectedDate = date;
        bookingData.selectedTime = time;
        showStep(3);
    }

    public void onPatientDetailsSubmitted(String fullName, String gender, String age, String problem) {
        bookingData.fullName = fullName;
        bookingData.gender = gender;
        bookingData.age = age;
        bookingData.problem = problem;
        showStep(4);
    }

    public void onSummaryConfirmed() {
        // Bỏ bước nhập mã PIN, chuyển thẳng sang dialog thành công
        showSuccessModal();
    }

    public void onPinComplete(String pin) {
        // Mô phỏng quá trình đặt lịch
        // Trong app thực tế: xác thực PIN, tạo cuộc hẹn trong Firebase
        showSuccessModal();
    }

    private void showSuccessModal() {
        // Tạo lịch hẹn mới và lưu vào repository
        AppointmentModel.PatientInfo patient = new AppointmentModel.PatientInfo(
            bookingData.fullName, bookingData.gender, bookingData.age, bookingData.problem
        );
        String id = String.valueOf(System.currentTimeMillis());
        AppointmentModel appointment = new AppointmentModel(
            id,
            bookingData.doctorName,
            bookingData.doctorSpecialty,
            bookingData.doctorLocation,
            bookingData.packageType,
            bookingData.amount,
            bookingData.selectedDate,
            bookingData.selectedTime,
            "upcoming",
            patient,
            false
        );
        AppointmentRepository.getInstance().addAppointment(appointment);

        BookingSuccessDialog dialog = new BookingSuccessDialog();
        dialog.setOnActionListener(new BookingSuccessDialog.OnActionListener() {
            @Override
            public void onViewAppointment() {
                Intent intent = new Intent(BookAppointmentActivity.this, AppointmentHistoryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onClose() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "success_dialog");
    }

    public BookingData getBookingData() {
        return bookingData;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handleBackPress();
    }
}
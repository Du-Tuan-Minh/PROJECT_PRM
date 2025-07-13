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

public class RescheduleAppointmentActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private int currentStep = 1;
    private String selectedReason;
    private String selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_appointment);

        initViews();
        setupListeners();
        showStep(1);
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Đổi lịch hẹn");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                showStep(currentStep - 1);
            } else {
                finish();
            }
        });
    }

    private void showStep(int step) {
        currentStep = step;

        Fragment fragment;
        switch (step) {
            case 1:
                fragment = createReasonSelectionFragment();
                break;
            case 2:
                fragment = new DateTimeSelectionFragment();
                break;
            default:
                return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private Fragment createReasonSelectionFragment() {
        String[] rescheduleReasons = {
                "Tôi có lịch trình xung đột",
                "Tôi không có mặt vào thời gian đã hẹn",
                "Tôi có hoạt động không thể bỏ qua",
                "Tôi không muốn nói",
                "Khác"
        };

        ReasonSelectionFragment fragment = ReasonSelectionFragment.newInstance(
                "Lý do đổi lịch hẹn",
                rescheduleReasons,
                "Tiếp theo"
        );

        fragment.setOnReasonSelectedListener((reason, otherReason) -> {
            selectedReason = reason.equals("Khác") ? otherReason : reason;
            showStep(2);
        });

        return fragment;
    }

    public void onDateTimeSelected(String date, String time) {
        selectedDate = date;
        selectedTime = time;

        // Xử lý đổi lịch hẹn
        processReschedule();
    }

    private void processReschedule() {
        // Gửi request đổi lịch hẹn lên server
        // Hiển thị loading

        // Mô phỏng delay
        new android.os.Handler().postDelayed(() -> {
            showSuccessModal();
        }, 1000);
    }

    private void showSuccessModal() {
        RescheduleSuccessDialog dialog = new RescheduleSuccessDialog();
        dialog.setOnActionListener(new RescheduleSuccessDialog.OnActionListener() {
            @Override
            public void onViewAppointment() {
                Intent intent = new Intent(RescheduleAppointmentActivity.this, AppointmentDetailsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onClose() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "reschedule_success");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (currentStep > 1) {
            showStep(currentStep - 1);
        } else {
            finish();
        }
    }
}
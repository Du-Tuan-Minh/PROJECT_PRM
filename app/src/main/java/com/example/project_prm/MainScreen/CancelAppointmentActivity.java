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

public class CancelAppointmentActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private boolean showConfirmation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);

        initViews();
        setupListeners();

        if (showConfirmation) {
            showConfirmationDialog();
        } else {
            showReasonSelection();
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Hủy lịch hẹn");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
    }

    private void showConfirmationDialog() {
        CancelConfirmationDialog dialog = new CancelConfirmationDialog();
        dialog.setOnActionListener(new CancelConfirmationDialog.OnActionListener() {
            @Override
            public void onConfirm() {
                showConfirmation = false;
                showReasonSelection();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "cancel_confirmation");
    }

    private void showReasonSelection() {
        String[] cancelReasons = {
                "Tôi muốn đổi sang bác sĩ khác",
                "Tôi muốn đổi gói khám",
                "Tôi không muốn tư vấn nữa",
                "Tôi đã khỏi bệnh",
                "Tôi đã tìm được thuốc phù hợp",
                "Tôi không muốn nói",
                "Khác"
        };

        ReasonSelectionFragment fragment = ReasonSelectionFragment.newInstance(
                "Lý do hủy lịch hẹn",
                cancelReasons,
                "Gửi"
        );

        fragment.setOnReasonSelectedListener((reason, otherReason) -> {
            // Xử lý hủy lịch hẹn
            processCancellation(reason, otherReason);
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private void processCancellation(String reason, String otherReason) {
        // Gửi request hủy lịch hẹn lên server
        // Hiển thị loading

        // Mô phỏng delay
        new android.os.Handler().postDelayed(() -> {
            showSuccessModal();
        }, 1000);
    }

    private void showSuccessModal() {
        CancelSuccessDialog dialog = new CancelSuccessDialog();
        dialog.setOnActionListener(new CancelSuccessDialog.OnActionListener() {
            @Override
            public void onClose() {
                // Quay về màn hình lịch sử
                Intent intent = new Intent(CancelAppointmentActivity.this, AppointmentHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "cancel_success");
    }
}
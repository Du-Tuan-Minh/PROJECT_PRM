package com.example.project_prm.ui.User_Profile;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.project_prm.R;

public class SecurityActivity extends AppCompatActivity {
    private Switch rememberMeSwitch, biometricSwitch, faceIdSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_security);

        initViews();                  // Ánh xạ các view
        setupBackButton();           // Gán sự kiện nút back
        loadPreferences();           // Tải trạng thái Remember Me
        setupSwitchListeners();      // Lắng nghe các switch thay đổi
    }

    // ✅ Ánh xạ các view từ layout
    private void initViews() {
        rememberMeSwitch = findViewById(R.id.remember_me_switch);
        biometricSwitch = findViewById(R.id.biometric_id_switch);
        faceIdSwitch = findViewById(R.id.face_id_switch);
    }

    // ✅ Gán sự kiện cho nút quay lại
    private void setupBackButton() {
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    // ✅ Tải trạng thái Remember Me đã lưu trước đó
    private void loadPreferences() {
        boolean rememberMe = getSharedPreferences("SecurityPrefs", MODE_PRIVATE)
                .getBoolean("remember_me", false);
        rememberMeSwitch.setChecked(rememberMe);
    }

    // ✅ Gán các sự kiện khi switch thay đổi trạng thái
    private void setupSwitchListeners() {
        rememberMeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("SecurityPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("remember_me", isChecked)
                    .apply();
        });

        biometricSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) showBiometricPrompt();
        });

        faceIdSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) showBiometricPrompt();
        });
    }

    // ✅ Hiển thị hộp thoại xác thực vân tay / khuôn mặt
    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực sinh trắc")
                .setSubtitle("Sử dụng vân tay hoặc khuôn mặt để đăng nhập")
                .setNegativeButtonText("Huỷ")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                ContextCompat.getMainExecutor(this),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        Toast.makeText(SecurityActivity.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(SecurityActivity.this, "Xác thực thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
}

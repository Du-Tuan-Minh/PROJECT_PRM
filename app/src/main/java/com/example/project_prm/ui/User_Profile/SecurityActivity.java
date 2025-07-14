package com.example.project_prm.ui.User_Profile;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.project_prm.R;

public class SecurityActivity extends AppCompatActivity {
    private Switch rememberMeSwitch, biometricSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_security);

        initViews();             // Ánh xạ view
        setupBackButton();       // Gán sự kiện nút back
        loadPreferences();       // Tải trạng thái Remember Me
        setupSwitchListeners();  // Lắng nghe Switch thay đổi
    }

    // ✅ Ánh xạ các view từ layout
    private void initViews() {
        rememberMeSwitch = findViewById(R.id.remember_me_switch);
        biometricSwitch = findViewById(R.id.biometric_id_switch);
    }

    // ✅ Quay lại màn trước
    private void setupBackButton() {
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    // ✅ Tải trạng thái Remember Me đã lưu
    private void loadPreferences() {
        boolean rememberMe = getSharedPreferences("SecurityPrefs", MODE_PRIVATE)
                .getBoolean("remember_me", false);
        rememberMeSwitch.setChecked(rememberMe);
    }

    // ✅ Lắng nghe sự kiện bật tắt switch
    private void setupSwitchListeners() {
        rememberMeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("SecurityPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("remember_me", isChecked)
                    .apply();
        });

        biometricSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showBiometricPrompt();
            }
        });
    }

    // ✅ Hiển thị xác thực sinh trắc học (vân tay/khuôn mặt)
    private void showBiometricPrompt() {
        if (!isBiometricAvailable()) {
            Toast.makeText(this, "Thiết bị không hỗ trợ sinh trắc học", Toast.LENGTH_SHORT).show();
            biometricSwitch.setChecked(false);
            return;
        }

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực sinh trắc học")
                .setSubtitle("Xác thực để tiếp tục")
                .setNegativeButtonText("Huỷ")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
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

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        Toast.makeText(SecurityActivity.this, "Lỗi: " + errString, Toast.LENGTH_SHORT).show();
                        biometricSwitch.setChecked(false);
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }

    // ✅ Kiểm tra xem thiết bị có hỗ trợ sinh trắc học không
    private boolean isBiometricAvailable() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG);
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS;
    }
}

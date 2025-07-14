package com.example.project_prm.ui.User_Profile;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.project_prm.R;

public class SecurityActivity extends AppCompatActivity {
    private Switch rememberMeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_security);

        rememberMeSwitch = findViewById(R.id.remember_me_switch);
        loadRememberMePreference();

        rememberMeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("SecurityPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("remember_me", isChecked)
                    .apply();
        });

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    // ✅ Tải trạng thái lưu remember me khi mở app
    private void loadRememberMePreference() {
        boolean rememberMe = getSharedPreferences("SecurityPrefs", MODE_PRIVATE)
                .getBoolean("remember_me", false);
        rememberMeSwitch.setChecked(rememberMe);
    }



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


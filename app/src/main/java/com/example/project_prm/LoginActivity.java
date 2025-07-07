package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, goToSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
>>>>>>> 9eaa2470d2d0e00d60c7953d58d22122bf72a13a
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        if (prefs.getInt("userId", -1) != -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToSignupButton = findViewById(R.id.goToSignupButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.equals("minh") && password.equals("123")) {
                // Lưu userId (giả lập là 1)
                SharedPreferences.Editor editor = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit();
                editor.putInt("userId", 1);
                editor.apply();

                // Chuyển sang MainActivity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút chuyển sang màn đăng ký nếu bạn có
        goToSignupButton.setOnClickListener(v -> {
            // Ví dụ chuyển sang SignupActivity
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }
}

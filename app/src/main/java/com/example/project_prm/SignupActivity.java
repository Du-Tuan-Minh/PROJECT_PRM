package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.DAO.UserDAO;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, nameEditText, profileEditText;
    private Button signupButton, goToLoginButton;
    private DatabaseHelper dbHelper;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        profileEditText = findViewById(R.id.profileEditText);
        signupButton = findViewById(R.id.signupButton);
        goToLoginButton = findViewById(R.id.goToLoginButton);

        dbHelper = new DatabaseHelper(this);
        userDAO = new UserDAO(dbHelper.getWritableDatabase());

        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String profile = profileEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || profile.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!username.matches("[a-zA-Z0-9]+")) {
                Toast.makeText(this, "Tên đăng nhập chỉ được chứa chữ cái và số", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = userDAO.registerUser(name, profile, username, password);
            if (result != -1) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Đăng ký thất bại, username có thể đã tồn tại", Toast.LENGTH_SHORT).show();
            }
        });

        goToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
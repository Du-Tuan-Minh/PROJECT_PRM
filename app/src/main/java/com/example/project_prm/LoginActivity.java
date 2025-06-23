package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.DataManager.Entity.User;
import com.example.project_prm.MainScreen.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, goToSignupButton;
    private DatabaseHelper dbHelper;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        dbHelper = new DatabaseHelper(this);
        userDAO = new UserDAO(dbHelper.getWritableDatabase());

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = userDAO.loginUser(username, password);
            if (user != null) {
                Toast.makeText(this, "Đăng nhập thành công: " + user.getName(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("userId", user.getId());
                editor.putString("username", user.getUsername());
                editor.apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        goToSignupButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) { // Kiểm tra dbHelper không null trước khi đóng
            dbHelper.close();
        }
    }
}
package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.ui.AppointmentScreen.MyAppointmentActivity;
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;
//import com.example.project_prm.ui.ChatBotScreen.ChatbotActivity;
//import com.example.project_prm.ui.DiseaseScreen.DiseaseLibraryActivity;
import com.example.project_prm.ui.SearchScreen.ClinicSearchActivity;
//import com.example.project_prm.ui.auth.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Hiển thị tên người dùng
        String username = prefs.getString("username", "Người dùng");
        TextView welcomeText = findViewById(R.id.tv_header);
        welcomeText.setText("🩺 Ứng dụng Sức khỏe - Chào " + username);

        // Initialize buttons
        MaterialButton btnDiseaseLibrary = findViewById(R.id.btn_disease_library);
        MaterialButton btnFindClinic = findViewById(R.id.btn_find_clinic);
        MaterialButton btnBookAppointment = findViewById(R.id.btn_book_appointment);
        MaterialButton btnViewAppointments = findViewById(R.id.btn_view_appointments);
        MaterialButton btnChatbot = findViewById(R.id.btn_chatbot);
        MaterialButton btnLogout = findViewById(R.id.btn_logout);

        // Set click listeners for navigation
        btnDiseaseLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });

        btnFindClinic.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClinicSearchActivity.class);
            startActivity(intent);
        });

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppointmentBookingActivity.class);
            startActivity(intent);
        });

        btnViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyAppointmentActivity.class);
            startActivity(intent);
        });

        btnChatbot.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(MainActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
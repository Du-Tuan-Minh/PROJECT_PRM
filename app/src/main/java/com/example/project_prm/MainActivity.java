// File: app/src/main/java/com/example/project_prm/MainActivity.java
package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.ui.AppointmentScreen.MyAppointmentActivity;
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;
import com.example.project_prm.ui.SearchScreen.ClinicSearchActivity;
import com.example.project_prm.ui.SearchScreen.DiseaseSearchActivity;
// import com.example.project_prm.ui.auth.LoginActivity; // Comment out for now
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comment out login check for now
        /*
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        */

        setContentView(R.layout.activity_main);

        // Setup welcome text
        TextView welcomeText = findViewById(R.id.tv_header);
        welcomeText.setText("🩺 Ứng dụng Sức khỏe");

        // Initialize buttons for main features
        MaterialButton btnDiseaseSearch = findViewById(R.id.btn_disease_library);
        MaterialButton btnFindClinic = findViewById(R.id.btn_find_clinic);
        MaterialButton btnBookAppointment = findViewById(R.id.btn_book_appointment);
        MaterialButton btnViewAppointments = findViewById(R.id.btn_view_appointments);

        // Comment out features not yet implemented
        // MaterialButton btnChatbot = findViewById(R.id.btn_chatbot);
        // MaterialButton btnLogout = findViewById(R.id.btn_logout);

        // CHỨC NĂNG 6: Search theo bệnh
        btnDiseaseSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiseaseSearchActivity.class);
            startActivity(intent);
        });

        // CHỨC NĂNG 6: Search phòng khám
        btnFindClinic.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClinicSearchActivity.class);
            startActivity(intent);
        });

        // CHỨC NĂNG 8: Đặt lịch khám
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppointmentBookingActivity.class);
            startActivity(intent);
        });

        // CHỨC NĂNG 9: Xem lịch sử đặt lịch khám
        btnViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyAppointmentActivity.class);
            startActivity(intent);
        });

        // Comment out chatbot and logout for now
        /*
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
        */
    }
}
package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.ui.AppointmentScreen.MyAppointmentActivity;
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;
import com.example.project_prm.ui.SearchScreen.ClinicSearchActivity;
import com.example.project_prm.ui.SearchScreen.DiseaseSearchActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);

            // Setup welcome text
            TextView welcomeText = findViewById(R.id.tv_header);
            if (welcomeText != null) {
                welcomeText.setText("🩺 Ứng dụng Sức khỏe");
            }

            // Initialize buttons for main features with null checks
            MaterialButton btnDiseaseSearch = findViewById(R.id.btn_disease_library);
            MaterialButton btnFindClinic = findViewById(R.id.btn_find_clinic);
            MaterialButton btnBookAppointment = findViewById(R.id.btn_book_appointment);
            MaterialButton btnViewAppointments = findViewById(R.id.btn_view_appointments);

            // CHỨC NĂNG 6: Search theo bệnh
            if (btnDiseaseSearch != null) {
                btnDiseaseSearch.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "Disease search button clicked");
                        Intent intent = new Intent(MainActivity.this, DiseaseSearchActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting DiseaseSearchActivity", e);
                        Toast.makeText(this, "Không thể mở tìm kiếm bệnh: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            // CHỨC NĂNG 6: Search phòng khám
            if (btnFindClinic != null) {
                btnFindClinic.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "Find clinic button clicked");
                        Intent intent = new Intent(MainActivity.this, ClinicSearchActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting ClinicSearchActivity", e);
                        Toast.makeText(this, "Không thể mở tìm phòng khám: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            // CHỨC NĂNG 8: Đặt lịch khám
            if (btnBookAppointment != null) {
                btnBookAppointment.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "Book appointment button clicked");
                        Intent intent = new Intent(MainActivity.this, AppointmentBookingActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting AppointmentBookingActivity", e);
                        Toast.makeText(this, "Không thể mở đặt lịch: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            // CHỨC NĂNG 9: Xem lịch sử đặt lịch khám
            if (btnViewAppointments != null) {
                btnViewAppointments.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "View appointments button clicked");
                        Intent intent = new Intent(MainActivity.this, MyAppointmentActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting MyAppointmentActivity", e);
                        Toast.makeText(this, "Không thể mở lịch hẹn: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            Log.d(TAG, "MainActivity setup completed successfully");

        } catch (Exception e) {
            Log.e(TAG, "Critical error in MainActivity onCreate", e);
            Toast.makeText(this, "Lỗi khởi tạo ứng dụng: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
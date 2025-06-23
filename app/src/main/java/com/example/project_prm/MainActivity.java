package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        MaterialButton btnDiseaseLibrary = findViewById(R.id.btn_disease_library);
        MaterialButton btnFindClinic = findViewById(R.id.btn_find_clinic);
        MaterialButton btnBookAppointment = findViewById(R.id.btn_book_appointment);
        MaterialButton btnChatbot = findViewById(R.id.btn_chatbot);

        // Set click listeners for navigation
        btnDiseaseLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });

        btnFindClinic.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FindClinicActivity.class);
            startActivity(intent);
        });

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookAppointmentActivity.class);
            startActivity(intent);
        });

        btnChatbot.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
            startActivity(intent);
        });
    }
}
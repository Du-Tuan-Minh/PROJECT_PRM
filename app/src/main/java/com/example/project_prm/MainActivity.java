package com.example.project_prm;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.Article.ArticlesFragment;
import com.example.project_prm.DataManager.Entity.FirestoreSeeder;
import com.example.project_prm.MainScreen.BookAppointmentActivity;
import com.example.project_prm.MainScreen.ChatbotActivity;
import com.example.project_prm.MainScreen.DiseaseLibraryActivity;
import com.example.project_prm.MainScreen.FindClinicActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FirebaseApp.initializeApp(this);
//        FirestoreSeeder.seedAll();
//        Toast.makeText(this, "Seeding hoàn tất!", Toast.LENGTH_SHORT).show();
        // Kiểm tra trạng thái đăng nhập
//        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
//        int userId = prefs.getInt("userId", -1);
//        if (userId == -1) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return;
//        }

        setContentView(R.layout.article_main);
        // Gọi ArticlesFragment ngay khi mở app
        loadFragment(new ArticlesFragment());
        // Hiển thị tên người dùng
//        String username = prefs.getString("username", "Người dùng");
//        TextView welcomeText = findViewById(R.id.tv_header);
//        welcomeText.setText("🩺 Ứng dụng Sức khỏe - Chào " + username);
//
//        // Initialize buttons
//        MaterialButton btnDiseaseLibrary = findViewById(R.id.btn_disease_library);
//        MaterialButton btnFindClinic = findViewById(R.id.btn_find_clinic);
//        MaterialButton btnBookAppointment = findViewById(R.id.btn_book_appointment);
//        MaterialButton btnChatbot = findViewById(R.id.btn_chatbot);
//        MaterialButton btnLogout = findViewById(R.id.btn_logout);
//
//        // Set click listeners for navigation
//        btnDiseaseLibrary.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
//            startActivity(intent);
//        });
//
//        btnFindClinic.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, FindClinicActivity.class);
//            startActivity(intent);
//        });
//
//        btnBookAppointment.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, BookAppointmentActivity.class);
//            startActivity(intent);
//        });
//
//        btnChatbot.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
//            startActivity(intent);
//        });
//
//        btnLogout.setOnClickListener(v -> {
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.clear();
//            editor.apply();
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        });
    }


    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // fragment_container là ID trong layout XML
                .addToBackStack(null)
                .commit();
    }

}
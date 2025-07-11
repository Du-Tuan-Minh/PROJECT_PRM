package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.GridLayout;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Arrays;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.project_prm.MainScreen.ChatbotActivity;

import com.example.project_prm.MainScreen.DiseaseLibraryActivity;
import com.example.project_prm.MainScreen.FindClinicActivity;
import com.example.project_prm.MainScreen.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;



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



//        setContentView(R.layout.article_main);
//        // Gọi ArticlesFragment ngay khi mở app
//        loadFragment(new ArticlesFragment());

        setContentView(R.layout.activity_main);

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
        
        // Load HomeFragment as the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();
        }
        
        // UI logic đã được chuyển sang HomeFragment
        // ImageView ivNotification = findViewById(R.id.ivNotification);
        // ivNotification.setOnClickListener(v -> {
        //     Intent intent = new Intent(MainActivity.this, com.example.project_prm.MainScreen.NotificationActivity.class);
        //     startActivity(intent);
        // });
        // EditText etSearch = findViewById(R.id.etSearch);
        // GridLayout gridSpeciality = findViewById(R.id.gridSpeciality);
        // etSearch.addTextChangedListener(new TextWatcher() {
        //     @Override
        //     public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        //     @Override
        //     public void onTextChanged(CharSequence s, int start, int before, int count) {}
        //     @Override
        //     public void afterTextChanged(Editable s) {
        //         String keyword = s.toString().trim().toLowerCase();
        //         for (int i = 0; i < gridSpeciality.getChildCount(); i++) {
        //             View child = gridSpeciality.getChildAt(i);
        //             if (child instanceof LinearLayout) {
        //                 TextView tv = null;
        //                 for (int j = 0; j < ((LinearLayout) child).getChildCount(); j++) {
        //                     View sub = ((LinearLayout) child).getChildAt(j);
        //                     if (sub instanceof TextView) {
        //                         tv = (TextView) sub;
        //                         break;
        //                     }
        //                 }
        //                 if (tv != null) {
        //                     String name = tv.getText().toString().toLowerCase();
        //                     if (keyword.isEmpty() || name.contains(keyword)) {
        //                         child.setVisibility(View.VISIBLE);
        //                     } else {
        //                         child.setVisibility(View.GONE);
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // });
        // ViewPager2 bannerViewPager = findViewById(R.id.bannerViewPager);
        // BannerAdapter bannerAdapter = new BannerAdapter(Arrays.asList(
        //     R.layout.banner_kiem_tra_y_te,
        //     R.layout.banner_artical,
        //     R.layout.banner_chat_ai
        // ));
        // bannerViewPager.setAdapter(bannerAdapter);
        // DotsIndicator bannerIndicator = findViewById(R.id.bannerIndicator);
        // bannerIndicator.setViewPager2(bannerViewPager);
        // TextView tvSeeAllSpeciality = findViewById(R.id.tvSeeAllSpeciality);
        // tvSeeAllSpeciality.setOnClickListener(v -> {
        //     startActivity(new android.content.Intent(MainActivity.this, AllUtilitiesActivity.class));
        // });
    }


    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)  // Thay thế fragment hiện tại
                .addToBackStack(null)                         // Thêm vào back stack để quay lại được
                .commit();                                    // Thực hiện transaction
    }


}
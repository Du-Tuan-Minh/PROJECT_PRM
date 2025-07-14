package com.example.project_prm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;


import androidx.appcompat.app.AppCompatActivity;


import com.example.project_prm.ui.Article.ArticlesActivity;

import com.example.project_prm.MainScreen.HomeFragment;
import com.example.project_prm.ui.User_Profile.LanguageHelper;
import com.example.project_prm.ui.User_Profile.ProfileActivity;
import com.example.project_prm.ui.auth.SignInActivity;
import com.example.project_prm.utils.CurrentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(R.layout.activity_main);



        // ✅ Kiểm tra nếu chưa login thì chuyển về LoginActivity
        if (!CurrentUser.isLoggedIn(this)) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Load HomeFragment trong fragmentContainer
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new HomeFragment())
                        .commit();
                return true;

            } else if (id == R.id.nav_appointment) {
                Intent intent = new Intent(MainActivity.this, com.example.project_prm.MainScreen.BookAppointmentActivity.class);
                startActivity(intent);
                return false; // Không chọn tab này vì là màn riêng

            } else if (id == R.id.nav_history) {
                Intent intent = new Intent(MainActivity.this, com.example.project_prm.MainScreen.AppointmentHistoryActivity.class);
                startActivity(intent);
                return false; // Không chọn tab này vì là màn riêng

            } else if (id == R.id.nav_articles) {
                // 👉 Mở màn ArticlesActivity
                Intent intent = new Intent(MainActivity.this, ArticlesActivity.class);
                startActivity(intent);
                return false; // Không chọn tab này vì là màn riêng

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                return false; // Vì là màn riêng, không chọn tab
            }

            return true;
        });


        // ✅ Chỉ chạy khi Activity mới khởi tạo (không phải quay lại)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();

            // ✅ Đảm bảo tab "Home" được chọn trên thanh điều hướng
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
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

}
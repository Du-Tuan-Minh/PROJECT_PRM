package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_prm.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Activity chính cho quản lý lịch hẹn
 * Chức năng 9: Xem lịch sử đặt lịch khám
 * Có 3 tabs: Upcoming, Completed, Cancelled
 */
public class MyAppointmentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AppointmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        initViews();
        setupViewPager();
        setupBottomNavigation();
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // Setup toolbar
        findViewById(R.id.btn_search).setOnClickListener(v -> {
            // Open disease search activity
            try {
                Intent intent = new Intent();
                intent.setClassName(this, "com.example.project_prm.ui.SearchScreen.DiseaseSearchActivity");
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Chức năng tìm kiếm đang được phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_notifications).setOnClickListener(v -> {
            // Show notifications
            Toast.makeText(this, "Thông báo đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupViewPager() {
        pagerAdapter = new AppointmentPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Sắp tới");
                            break;
                        case 1:
                            tab.setText("Hoàn thành");
                            break;
                        case 2:
                            tab.setText("Đã hủy");
                            break;
                    }
                }).attach();
    }

    private void setupBottomNavigation() {
        findViewById(R.id.nav_appointments).setOnClickListener(v -> {
            // Already on appointments screen
        });

        findViewById(R.id.nav_history).setOnClickListener(v -> {
            // Navigate to history
            Toast.makeText(this, "Lịch sử đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.nav_articles).setOnClickListener(v -> {
            // Navigate to articles
            Toast.makeText(this, "Bài viết đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            // Navigate to profile
            Toast.makeText(this, "Hồ sơ đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        if (pagerAdapter != null) {
            // Notify adapter to refresh data
            pagerAdapter.notifyDataSetChanged();
        }
    }
}
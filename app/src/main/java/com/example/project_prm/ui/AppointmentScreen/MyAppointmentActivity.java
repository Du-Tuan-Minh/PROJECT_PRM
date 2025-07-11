// NEW FILE: app/src/main/java/com/example/project_prm/UI/AppointmentScreen/MyAppointmentActivity.java
package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_prm.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
            // Open search
            Intent intent = new Intent(this, DiseaseSearchActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_notifications).setOnClickListener(v -> {
            // Show notifications
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
                            tab.setText("Upcoming");
                            break;
                        case 1:
                            tab.setText("Completed");
                            break;
                        case 2:
                            tab.setText("Cancelled");
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
        });

        findViewById(R.id.nav_articles).setOnClickListener(v -> {
            // Navigate to articles
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            // Navigate to profile
        });
    }
}
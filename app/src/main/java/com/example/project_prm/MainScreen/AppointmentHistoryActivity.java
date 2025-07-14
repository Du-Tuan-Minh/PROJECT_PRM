package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.project_prm.R;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private ImageView ivBack, ivSearch;
    private TextView tvTitle;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AppointmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        initViews();
        setupListeners();
        setupTabs();

        // Nhận tab_index từ intent để chuyển tab
        int tabIndex = getIntent().getIntExtra("tab_index", -1);
        if (tabIndex >= 0 && tabLayout != null) {
            tabLayout.post(() -> {
                TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
                if (tab != null) tab.select();
            });
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        ivSearch = findViewById(R.id.ivSearch);
        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tvTitle.setText("Lịch hẹn của tôi");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
        ivSearch.setOnClickListener(v -> {
            // Mở màn hình tìm kiếm lịch hẹn
        });
    }

    private void setupTabs() {
        pagerAdapter = new AppointmentPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Sắp tới");
                    break;
                case 1:
                    tab.setText("Đã hoàn thành");
                    break;
                case 2:
                    tab.setText("Đã hủy");
                    break;
            }
        }).attach();
    }

    public void onAppointmentClick(String appointmentId, String status) {
        Intent intent = new Intent(this, AppointmentDetailsActivity.class);
        intent.putExtra("appointment_id", appointmentId);
        intent.putExtra("status", status);
        startActivity(intent);
    }

    public void onCancelAppointment(String appointmentId) {
        Intent intent = new Intent(this, CancelAppointmentActivity.class);
        intent.putExtra("appointment_id", appointmentId);
        startActivity(intent);
    }

    public void onRescheduleAppointment(String appointmentId) {
        Intent intent = new Intent(this, RescheduleAppointmentActivity.class);
        intent.putExtra("appointment_id", appointmentId);
        startActivity(intent);
    }

    public void onBookAgain(String doctorId) {
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        intent.putExtra("doctor_id", doctorId);
        startActivity(intent);
    }

    public void onLeaveReview(String appointmentId) {
        Intent intent = new Intent(this, WriteReviewActivity.class);
        intent.putExtra("appointment_id", appointmentId);
        startActivity(intent);
    }
}
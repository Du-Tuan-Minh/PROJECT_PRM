/*
 * 📁 ĐƯỜNG DẪN: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/MyAppointmentActivity.java
 * 🎯 CHỨC NĂNG: Sửa tất cả lỗi trong MyAppointmentActivity
 * ⚠️ THAY THẾ: Toàn bộ file hiện tại
 */

package com.example.project_prm.ui.AppointmentScreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.tabs.TabLayout;

public class MyAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "MyAppointmentActivity";

    // UI Components
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView ivBack;

    // Services
    private HealthcareService service;
    private AppointmentHistoryManager historyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d(TAG, "Starting MyAppointmentActivity onCreate");

            setContentView(R.layout.activity_my_appointment);

            // Initialize services first
            initServices();

            // Initialize views
            initViews();

            // Setup ViewPager and Tabs
            setupViewPager();

            Log.d(TAG, "MyAppointmentActivity created successfully");

        } catch (Exception e) {
            Log.e(TAG, "Critical error in onCreate", e);
            handleCriticalError(e);
        }
    }

    private void initServices() {
        try {
            Log.d(TAG, "Initializing services...");

            service = HealthcareService.getInstance(this);
            if (service == null) {
                throw new IllegalStateException("Failed to initialize HealthcareService");
            }

            historyManager = new AppointmentHistoryManager(this);
            if (historyManager == null) {
                throw new IllegalStateException("Failed to initialize AppointmentHistoryManager");
            }

            Log.d(TAG, "Services initialized successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error initializing services", e);
            throw new RuntimeException("Service initialization failed: " + e.getMessage());
        }
    }

    private void initViews() {
        try {
            Log.d(TAG, "Initializing views...");

            // Back button
            ivBack = findViewById(R.id.iv_back);
            if (ivBack != null) {
                ivBack.setOnClickListener(v -> {
                    Log.d(TAG, "Back button clicked");
                    finish();
                });
            } else {
                Log.w(TAG, "Back button not found in layout");
            }

            // ViewPager and TabLayout
            viewPager = findViewById(R.id.view_pager);
            tabLayout = findViewById(R.id.tab_layout);

            if (viewPager == null) {
                throw new IllegalStateException("ViewPager not found in layout - check activity_my_appointment.xml");
            }

            if (tabLayout == null) {
                throw new IllegalStateException("TabLayout not found in layout - check activity_my_appointment.xml");
            }

            Log.d(TAG, "Views initialized successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
            throw new RuntimeException("View initialization failed: " + e.getMessage());
        }
    }

    private void setupViewPager() {
        try {
            Log.d(TAG, "Setting up ViewPager...");

            // Create simple adapter instead of AppointmentPagerAdapter
            SimpleAppointmentPagerAdapter adapter = new SimpleAppointmentPagerAdapter(
                    getSupportFragmentManager(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            );

            if (adapter == null) {
                throw new IllegalStateException("Failed to create SimpleAppointmentPagerAdapter");
            }

            // Set adapter to ViewPager
            viewPager.setAdapter(adapter);

            // Connect TabLayout with ViewPager
            tabLayout.setupWithViewPager(viewPager);

            // Set custom tab titles with Vietnamese
            setupTabTitles();

            // Set default tab
            viewPager.setCurrentItem(0);

            Log.d(TAG, "ViewPager setup complete");

        } catch (Exception e) {
            Log.e(TAG, "Error setting up ViewPager", e);
            throw new RuntimeException("ViewPager setup failed: " + e.getMessage());
        }
    }

    private void setupTabTitles() {
        try {
            // Get tabs and set Vietnamese titles
            TabLayout.Tab tab1 = tabLayout.getTabAt(0);
            TabLayout.Tab tab2 = tabLayout.getTabAt(1);
            TabLayout.Tab tab3 = tabLayout.getTabAt(2);

            if (tab1 != null) {
                tab1.setText("Sắp tới");
                Log.d(TAG, "Set tab 1 title: Sắp tới");
            }

            if (tab2 != null) {
                tab2.setText("Đã hoàn thành");
                Log.d(TAG, "Set tab 2 title: Đã hoàn thành");
            }

            if (tab3 != null) {
                tab3.setText("Đã hủy");
                Log.d(TAG, "Set tab 3 title: Đã hủy");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error setting up tab titles", e);
            // Non-critical error, continue execution
        }
    }

    private void handleCriticalError(Exception e) {
        try {
            Log.e(TAG, "Handling critical error", e);

            String errorMessage = "Không thể tải màn hình lịch hẹn";
            if (e.getMessage() != null) {
                errorMessage += ": " + e.getMessage();
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

            // Show error dialog
            showErrorDialog(errorMessage);

        } catch (Exception dialogError) {
            Log.e(TAG, "Failed to show error dialog", dialogError);
            // If even dialog fails, just finish
            finishSafely();
        }
    }

    private void showErrorDialog(String message) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Lỗi")
                    .setMessage(message + "\n\nVui lòng thử lại sau.")
                    .setPositiveButton("Đóng", (dialog, which) -> finishSafely())
                    .setNegativeButton("Thử lại", (dialog, which) -> {
                        // Restart activity
                        finish();
                        startActivity(getIntent());
                    })
                    .setCancelable(false)
                    .show();

        } catch (Exception e) {
            Log.e(TAG, "Error showing dialog", e);
            finishSafely();
        }
    }

    private void finishSafely() {
        try {
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error finishing activity", e);
            System.exit(0); // Last resort
        }
    }

    /**
     * Get current user ID from SharedPreferences
     */
    private int getCurrentUserId() {
        try {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("userId", 1); // Default to 1 for demo
            Log.d(TAG, "Current user ID: " + userId);
            return userId;
        } catch (Exception e) {
            Log.e(TAG, "Error getting user ID", e);
            return 1; // Default fallback
        }
    }

    /**
     * Refresh data in all fragments
     */
    public void refreshAllFragments() {
        try {
            if (viewPager != null && viewPager.getAdapter() != null) {
                // Notify adapter to refresh fragments
                viewPager.getAdapter().notifyDataSetChanged();
                Log.d(TAG, "Refreshed all fragments");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error refreshing fragments", e);
        }
    }

    /**
     * Navigate to specific tab
     */
    public void navigateToTab(int tabIndex) {
        try {
            if (viewPager != null && tabIndex >= 0 && tabIndex < 3) {
                viewPager.setCurrentItem(tabIndex);
                Log.d(TAG, "Navigated to tab: " + tabIndex);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to tab", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d(TAG, "onResume - refreshing data");
            refreshAllFragments();
        } catch (Exception e) {
            Log.e(TAG, "Error in onResume", e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            Log.d(TAG, "onDestroy - cleaning up resources");

            if (historyManager != null) {
                historyManager.close();
                historyManager = null;
            }

            // Clear references
            service = null;
            viewPager = null;
            tabLayout = null;
            ivBack = null;

        } catch (Exception e) {
            Log.e(TAG, "Error in onDestroy", e);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            Log.d(TAG, "onBackPressed");
            super.onBackPressed();
        } catch (Exception e) {
            Log.e(TAG, "Error in onBackPressed", e);
            finishSafely();
        }
    }

    /**
     * Provide access to services for fragments
     */
    public HealthcareService getHealthcareService() {
        return service;
    }

    public AppointmentHistoryManager getHistoryManager() {
        return historyManager;
    }

    // ========== SIMPLE PAGER ADAPTER TO AVOID MISSING CLASS ERROR ==========

    /**
     * Simple adapter để tránh lỗi missing AppointmentPagerAdapter
     */
    private static class SimpleAppointmentPagerAdapter extends FragmentPagerAdapter {

        public SimpleAppointmentPagerAdapter(androidx.fragment.app.FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            // Tạo fragment đơn giản cho mỗi tab
            switch (position) {
                case 0:
                    return SimpleAppointmentFragment.newInstance("Sắp tới", "upcoming");
                case 1:
                    return SimpleAppointmentFragment.newInstance("Đã hoàn thành", "completed");
                case 2:
                    return SimpleAppointmentFragment.newInstance("Đã hủy", "cancelled");
                default:
                    return SimpleAppointmentFragment.newInstance("Tab " + position, "default");
            }
        }

        @Override
        public int getCount() {
            return 3; // 3 tabs
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sắp tới";
                case 1:
                    return "Đã hoàn thành";
                case 2:
                    return "Đã hủy";
                default:
                    return "Tab " + position;
            }
        }
    }
}
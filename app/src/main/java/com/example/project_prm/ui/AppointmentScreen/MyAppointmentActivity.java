package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentActivity extends AppCompatActivity implements AppointmentAdapter.OnAppointmentClickListener {
    private static final String TAG = "MyAppointmentActivity";

    // UI Components
    private ViewPager2 viewPager;
    private TextView tabUpcoming, tabCompleted, tabCancelled;
    private ImageView ivSearch, ivNotification;

    // Data
    private AppointmentPagerAdapter pagerAdapter;
    private HealthcareService service;
    private AppointmentHistoryManager historyManager;

    // Current selected tab
    private int currentTab = 0; // 0: Upcoming, 1: Completed, 2: Cancelled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        initServices();
        initViews();
        setupViewPager();
        setupTabNavigation();
        setupBottomNavigation();
    }

    private void initServices() {
        try {
            service = HealthcareService.getInstance(this);
            historyManager = new AppointmentHistoryManager(this);
            Log.d(TAG, "Services initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing services", e);
            Toast.makeText(this, "Error initializing services", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        // Header icons
        ivSearch = findViewById(R.id.iv_search);
        ivNotification = findViewById(R.id.iv_notification);

        // Tab navigation
        tabUpcoming = findViewById(R.id.tab_upcoming);
        tabCompleted = findViewById(R.id.tab_completed);
        tabCancelled = findViewById(R.id.tab_cancelled);

        // ViewPager
        viewPager = findViewById(R.id.view_pager);

        // Setup click listeners for header icons
        ivSearch.setOnClickListener(v -> {
            Toast.makeText(this, "Search functionality", Toast.LENGTH_SHORT).show();
        });

        ivNotification.setOnClickListener(v -> {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupViewPager() {
        pagerAdapter = new AppointmentPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Setup page change callback
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTabSelection(position);
            }
        });
    }

    private void setupTabNavigation() {
        tabUpcoming.setOnClickListener(v -> selectTab(0));
        tabCompleted.setOnClickListener(v -> selectTab(1));
        tabCancelled.setOnClickListener(v -> selectTab(2));

        // Set initial tab selection
        updateTabSelection(0);
    }

    private void selectTab(int tabIndex) {
        viewPager.setCurrentItem(tabIndex, true);
    }

    private void updateTabSelection(int selectedTab) {
        currentTab = selectedTab;

        // Reset all tabs
        resetTabStyles();

        // Highlight selected tab
        switch (selectedTab) {
            case 0:
                tabUpcoming.setTextColor(getColor(android.R.color.holo_blue_dark));
                tabUpcoming.setBackgroundResource(R.drawable.tab_selected_background);
                break;
            case 1:
                tabCompleted.setTextColor(getColor(android.R.color.holo_blue_dark));
                tabCompleted.setBackgroundResource(R.drawable.tab_selected_background);
                break;
            case 2:
                tabCancelled.setTextColor(getColor(android.R.color.holo_blue_dark));
                tabCancelled.setBackgroundResource(R.drawable.tab_selected_background);
                break;
        }
    }

    private void resetTabStyles() {
        int unselectedColor = getColor(android.R.color.darker_gray);

        tabUpcoming.setTextColor(unselectedColor);
        tabUpcoming.setBackgroundResource(R.drawable.tab_unselected_background);

        tabCompleted.setTextColor(unselectedColor);
        tabCompleted.setBackgroundResource(R.drawable.tab_unselected_background);

        tabCancelled.setTextColor(unselectedColor);
        tabCancelled.setBackgroundResource(R.drawable.tab_unselected_background);
    }

    private void setupBottomNavigation() {
        findViewById(R.id.nav_home).setOnClickListener(v -> {
            finish(); // Go back to home/main activity
        });

        findViewById(R.id.nav_appointments).setOnClickListener(v -> {
            // Already on appointments page
        });

        findViewById(R.id.nav_history).setOnClickListener(v -> {
            Toast.makeText(this, "Navigate to History", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.nav_articles).setOnClickListener(v -> {
            Toast.makeText(this, "Navigate to Articles", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            Toast.makeText(this, "Navigate to Profile", Toast.LENGTH_SHORT).show();
        });
    }

    // AppointmentAdapter.OnAppointmentClickListener implementation
    @Override
    public void onAppointmentClick(Appointment appointment) {
        // Navigate to appointment detail
        Intent intent = new Intent(this, AppointmentDetailActivity.class);
        intent.putExtra("appointment_id", appointment.getId());
        intent.putExtra("doctor_name", appointment.getDoctor());
        intent.putExtra("appointment_type", appointment.getAppointmentType());
        intent.putExtra("date", appointment.getDate());
        intent.putExtra("time", appointment.getTime());
        intent.putExtra("status", appointment.getStatus());
        startActivity(intent);
    }

    @Override
    public void onCancelClick(Appointment appointment) {
        // Show cancel appointment dialog or navigate to cancel screen
        Toast.makeText(this, "Cancel appointment: " + appointment.getDoctor(), Toast.LENGTH_SHORT).show();

        // You can implement actual cancel functionality here
        // For example: navigate to CancelAppointmentActivity
        // Intent intent = new Intent(this, CancelAppointmentActivity.class);
        // intent.putExtra("appointment_id", appointment.getId());
        // startActivity(intent);
    }

    @Override
    public void onRescheduleClick(Appointment appointment) {
        // Navigate to reschedule screen
        Toast.makeText(this, "Reschedule appointment: " + appointment.getDoctor(), Toast.LENGTH_SHORT).show();

        // You can implement actual reschedule functionality here
        // For example: navigate to RescheduleActivity
        // Intent intent = new Intent(this, RescheduleActivity.class);
        // intent.putExtra("appointment_id", appointment.getId());
        // startActivity(intent);
    }

    @Override
    public void onReviewClick(Appointment appointment) {
        // Navigate to review screen
        Toast.makeText(this, "Leave review for: " + appointment.getDoctor(), Toast.LENGTH_SHORT).show();

        // You can implement actual review functionality here
        // For example: navigate to ReviewActivity
        // Intent intent = new Intent(this, ReviewActivity.class);
        // intent.putExtra("appointment_id", appointment.getId());
        // intent.putExtra("doctor_name", appointment.getDoctor());
        // startActivity(intent);
    }

    @Override
    public void onBookAgainClick(Appointment appointment) {
        // Navigate to booking screen with pre-filled data
        Toast.makeText(this, "Book again with: " + appointment.getDoctor(), Toast.LENGTH_SHORT).show();

        // You can implement actual book again functionality here
        // For example: navigate to AppointmentBookingActivity
        // Intent intent = new Intent(this, AppointmentBookingActivity.class);
        // intent.putExtra("doctor_name", appointment.getDoctor());
        // intent.putExtra("appointment_type", appointment.getAppointmentType());
        // startActivity(intent);
    }

    // ViewPager2 Adapter
    private class AppointmentPagerAdapter extends FragmentStateAdapter {

        public AppointmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new UpcomingAppointmentsFragment();
                case 1:
                    return new CompletedAppointmentsFragment();
                case 2:
                    return new CancelledAppointmentsFragment();
                default:
                    return new UpcomingAppointmentsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Upcoming, Completed, Cancelled
        }
    }

    // Base Fragment for appointment lists
    public static abstract class BaseAppointmentFragment extends Fragment {
        protected RecyclerView recyclerView;
        protected View emptyStateView;
        protected AppointmentAdapter adapter;
        protected List<Appointment> appointments;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            appointments = new ArrayList<>();
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_appointment_list, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setupRecyclerView(view);
            loadAppointments();
        }

        protected void setupRecyclerView(View rootView) {
            recyclerView = rootView.findViewById(R.id.rv_appointments);
            emptyStateView = rootView.findViewById(R.id.layout_empty_state);

            // Get the listener from parent activity
            AppointmentAdapter.OnAppointmentClickListener listener = null;
            if (getActivity() instanceof AppointmentAdapter.OnAppointmentClickListener) {
                listener = (AppointmentAdapter.OnAppointmentClickListener) getActivity();
            }

            adapter = new AppointmentAdapter((List<Object>) (List<?>) appointments, listener);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        protected void updateUI() {
            if (appointments.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyStateView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyStateView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }

        protected abstract void loadAppointments();
    }

    // Upcoming Appointments Fragment
    public static class UpcomingAppointmentsFragment extends BaseAppointmentFragment {

        @Override
        protected void loadAppointments() {
            // Load upcoming appointments
            appointments.clear();

            // Sample data - replace with actual data loading
            appointments.add(createSampleAppointment("Dr. Drake Boeson", "Messaging", "Today | 16:00 PM", "upcoming"));
            appointments.add(createSampleAppointment("Dr. Jenny Watson", "Voice Call", "Today | 14:00 PM", "upcoming"));
            appointments.add(createSampleAppointment("Dr. Maria Foose", "Video Call", "Today | 10:00 AM", "upcoming"));

            updateUI();
        }
    }

    // Completed Appointments Fragment
    public static class CompletedAppointmentsFragment extends BaseAppointmentFragment {

        @Override
        protected void loadAppointments() {
            // Load completed appointments
            appointments.clear();

            // Sample data
            appointments.add(createSampleAppointment("Dr. Aidan Allende", "Video Call", "Dec 14, 2022 | 15:00 PM", "completed"));
            appointments.add(createSampleAppointment("Dr. Iker Holl", "Messaging", "Nov 22, 2022 | 09:00 AM", "completed"));
            appointments.add(createSampleAppointment("Dr. Jada Srnsky", "Voice Call", "Nov 06, 2022 | 18:00 PM", "completed"));

            updateUI();
        }
    }

    // Cancelled Appointments Fragment
    public static class CancelledAppointmentsFragment extends BaseAppointmentFragment {

        @Override
        protected void loadAppointments() {
            // Load cancelled appointments
            appointments.clear();

            // Sample data
            appointments.add(createSampleAppointment("Dr. Raul Zirkind", "Voice Call", "Dec 12, 2022 | 16:00 PM", "cancelled"));
            appointments.add(createSampleAppointment("Dr. Keegan Dach", "Messaging", "Nov 20, 2022 | 10:00 AM", "cancelled"));
            appointments.add(createSampleAppointment("Dr. Drake Boeson", "Video Call", "Nov 08, 2022 | 13:00 PM", "cancelled"));
            appointments.add(createSampleAppointment("Dr. Quinn Slatter", "Voice Call", "Oct 16, 2022 | 09:00 AM", "cancelled"));

            updateUI();
        }
    }

    // Helper method to create sample appointments
    private static Appointment createSampleAppointment(String doctorName, String type, String dateTime, String status) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorName);
        appointment.setAppointmentType(type);
        appointment.setDate(dateTime.split(" \\| ")[0]);
        appointment.setTime(dateTime.contains(" | ") ? dateTime.split(" \\| ")[1] : "");
        appointment.setStatus(status);
        appointment.setId((int)(System.currentTimeMillis() % Integer.MAX_VALUE)); // Generate a simple ID
        return appointment;
    }

    public HealthcareService getHealthcareService() {
        return service;
    }

    public AppointmentHistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (historyManager != null) {
            historyManager.close();
        }
    }
}
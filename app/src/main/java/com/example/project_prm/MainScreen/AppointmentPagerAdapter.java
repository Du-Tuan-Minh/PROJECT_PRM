package com.example.project_prm.MainScreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AppointmentPagerAdapter extends FragmentStateAdapter {

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
        return 3; // Upcoming, Completed, and Cancelled tabs
    }
} 
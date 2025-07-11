package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;

import java.util.ArrayList;
import java.util.List;

public class UpcomingAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private View emptyStateView;
    private View progressBar;

    private TungFeaturesService service;
    private List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadUpcomingAppointments();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyStateView = view.findViewById(R.id.empty_state_view);
        progressBar = view.findViewById(R.id.progress_bar);

        service = TungFeaturesService.getInstance(requireContext());
        appointmentList = new ArrayList<>();

        // Setup empty state for upcoming appointments
        if (view.findViewById(R.id.tv_empty_title) != null) {
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_title)).setText("You don't have an appointment yet");
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_description)).setText("You don't have a doctor's appointment scheduled at the moment");
        }
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Navigate to Cancel Appointment Activity
                Intent intent = new Intent(getContext(), CancelAppointmentActivity.class);
                intent.putExtra("appointment_id", item.appointment.getId());
                startActivity(intent);
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Navigate to Reschedule Activity
                Intent intent = new Intent(getContext(), RescheduleActivity.class);
                intent.putExtra("appointment_id", item.appointment.getId());
                startActivity(intent);
            }

            @Override
            public void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in upcoming
            }

            @Override
            public void onLeaveReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in upcoming
            }

            @Override
            public void onContactClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in upcoming
            }

            @Override
            public void onItemClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Navigate to Appointment Detail
                Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
                intent.putExtra("appointment_id", item.appointment.getId());
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadUpcomingAppointments() {
        showLoading(true);

        // Use service to get real data from database
        service.getUpcomingAppointments(new AppointmentHistoryManager.OnHistoryListener() {
            @Override
            public void onSuccess(List<AppointmentHistoryManager.AppointmentHistoryItem> items) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        appointmentList.clear();
                        appointmentList.addAll(items);
                        adapter.notifyDataSetChanged();

                        showEmptyState(items.isEmpty());

                        // Show success message if data loaded
                        if (!items.isEmpty()) {
                            Toast.makeText(getContext(), "Loaded " + items.size() + " upcoming appointments", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Error loading appointments: " + error);
                        showEmptyState(true);
                    });
                }
            }
        });
    }

    private void showLoading(boolean show) {
        if (progressBar != null && recyclerView != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    private void showEmptyState(boolean show) {
        if (emptyStateView != null && recyclerView != null) {
            emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        loadUpcomingAppointments();
    }
}
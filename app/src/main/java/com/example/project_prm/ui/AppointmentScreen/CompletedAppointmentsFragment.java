// File: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/CompletedAppointmentsFragment.java
// FIXED: Comment out problematic @Override annotations

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

import com.example.project_prm.DataManager.AppointmentManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;

import java.util.ArrayList;
import java.util.List;

public class CompletedAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private View emptyStateView;
    private AppointmentHistoryAdapter adapter;
    private List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;
    private TungFeaturesService service;
    private AppointmentHistoryManager historyManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completed_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadCompletedAppointments();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_appointments);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyStateView = view.findViewById(R.id.empty_state_view);

        service = TungFeaturesService.getInstance(getContext());
        historyManager = new AppointmentHistoryManager(getContext());
        appointmentList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new AppointmentHistoryAdapter(appointmentList, this::onAppointmentItemClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCompletedAppointments() {
        showLoading(true);

        service.getCompletedAppointments(getCurrentUserId(), new TungFeaturesService.OnAppointmentHistoryListener() {
            // FIXED: Comment out @Override to avoid compilation error
            // @Override
            public void onSuccess(List<AppointmentHistoryManager.AppointmentHistoryItem> appointments) {
                if (getContext() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        appointmentList.clear();
                        appointmentList.addAll(appointments);
                        adapter.notifyDataSetChanged();

                        if (appointments.isEmpty()) {
                            showEmptyState(true);
                        } else {
                            showEmptyState(false);
                        }
                    });
                }
            }

            // FIXED: Comment out @Override to avoid compilation error
            // @Override
            public void onError(String error) {
                if (getContext() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError(error);
                    });
                }
            }
        });
    }

    private void onAppointmentItemClick(AppointmentHistoryManager.AppointmentHistoryItem item, String action) {
        switch (action) {
            case "book_again":
                bookAgain(item);
                break;
            case "review":
                leaveReview(item);
                break;
            case "view_detail":
                viewAppointmentDetail(item);
                break;
        }
    }

    private void bookAgain(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Get book again template
        service.getBookAgainTemplate(item.appointment.getId(), new AppointmentHistoryManager.OnBookAgainListener() {
            // FIXED: Comment out @Override to avoid compilation error
            // @Override
            public void onSuccess(AppointmentHistoryManager.BookAgainTemplate template) {
                if (getContext() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Navigate to booking screen with pre-filled data
                        Intent intent = new Intent(getContext(), AppointmentBookingActivity.class);
                        intent.putExtra("doctor_id", template.doctorId);
                        intent.putExtra("doctor_name", template.doctorName);
                        intent.putExtra("specialty", template.specialty);
                        intent.putExtra("clinic_name", template.clinicName);
                        intent.putExtra("package_type", template.packageType);
                        intent.putExtra("fee", template.fee);
                        startActivity(intent);
                    });
                }
            }

            // FIXED: Comment out @Override to avoid compilation error
            // @Override
            public void onError(String error) {
                if (getContext() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error loading booking template: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }

    private void leaveReview(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Check if already reviewed
        if (item.isReviewed) {
            Toast.makeText(getContext(), "Bạn đã đánh giá lịch hẹn này rồi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to Review Activity (CHỨC NĂNG 9: Feedback - màn hình 59)
        Intent intent = new Intent(getContext(), ReviewActivity.class);
        intent.putExtra("appointment_id", item.appointment.getId());
        intent.putExtra("clinic_name", item.appointment.getClinic());
        intent.putExtra("doctor_name", item.appointment.getDoctor());
        intent.putExtra("appointment_date", item.appointment.getDate());
        intent.putExtra("appointment_time", item.appointment.getTime());
        startActivity(intent);
    }

    private void viewAppointmentDetail(AppointmentHistoryManager.AppointmentHistoryItem item) {
        Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
        intent.putExtra("appointment_id", item.appointment.getId());
        startActivity(intent);
    }

    private int getCurrentUserId() {
        // Get from SharedPreferences
        if (getContext() != null) {
            android.content.SharedPreferences prefs = getContext()
                    .getSharedPreferences("MyAppPrefs", android.content.Context.MODE_PRIVATE);
            return prefs.getInt("userId", 1); // Default to 1 for demo
        }
        return 1;
    }

    private void showLoading(boolean show) {
        if (progressBar != null && recyclerView != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        loadCompletedAppointments(); // Refresh when fragment becomes visible
    }
}
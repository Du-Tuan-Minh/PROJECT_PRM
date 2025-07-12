// File: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/CancelledAppointmentsFragment.java
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

public class CancelledAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private View emptyStateView;
    private AppointmentHistoryAdapter adapter;
    private List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;
    private TungFeaturesService service;
    private AppointmentHistoryManager historyManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cancelled_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadCancelledAppointments();
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

    private void loadCancelledAppointments() {
        showLoading(true);

        service.getCancelledAppointments(getCurrentUserId(), new TungFeaturesService.OnAppointmentHistoryListener() {
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
            case "view_detail":
                viewAppointmentDetail(item);
                break;
            case "cancel_reason":
                showCancelReason(item);
                break;
        }
    }

    private void bookAgain(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // CHỨC NĂNG 9: Đặt lịch lại từ cancelled appointments (màn hình 55, 56, 57)

        // Get book again template
        service.getBookAgainTemplate(item.appointment.getId(), new AppointmentHistoryManager.OnBookAgainListener() {
            // FIXED: Comment out @Override to avoid compilation error
            // @Override
            public void onSuccess(AppointmentHistoryManager.BookAgainTemplate template) {
                if (getContext() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Navigate to booking screen with pre-filled data from cancelled appointment
                        Intent intent = new Intent(getContext(), AppointmentBookingActivity.class);
                        intent.putExtra("doctor_id", template.doctorId);
                        intent.putExtra("doctor_name", template.doctorName);
                        intent.putExtra("specialty", template.specialty);
                        intent.putExtra("clinic_name", template.clinicName);
                        intent.putExtra("package_type", template.packageType);
                        intent.putExtra("fee", template.fee);
                        intent.putExtra("from_cancelled", true); // Flag to indicate rebooking from cancelled
                        startActivity(intent);

                        Toast.makeText(getContext(), "Redirecting to booking with previous appointment details...", Toast.LENGTH_SHORT).show();
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

    private void viewAppointmentDetail(AppointmentHistoryManager.AppointmentHistoryItem item) {
        Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
        intent.putExtra("appointment_id", item.appointment.getId());
        startActivity(intent);
    }

    private void showCancelReason(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Show cancellation reason in a dialog
        String reason = item.cancellationReason != null ? item.cancellationReason : "No reason provided";

        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Cancellation Reason")
                .setMessage(reason)
                .setPositiveButton("OK", null)
                .show();
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
        loadCancelledAppointments(); // Refresh when fragment becomes visible
    }
}
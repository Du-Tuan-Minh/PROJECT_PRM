// NEW FILE: app/src/main/java/com/example/project_prm/UI/AppointmentScreen/UpcomingAppointmentsFragment.java
package com.example.project_prm.ui.AppointmentScreen;

import android.app.AlertDialog;
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
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                showCancelDialog(item);
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                showRescheduleDialog(item);
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
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadUpcomingAppointments() {
        showLoading(true);

        // Get user ID from SharedPreferences or pass as parameter
        int userId = 1; // TODO: Get actual user ID

        service.getUpcomingAppointments(userId, new AppointmentHistoryManager.OnHistoryListener() {
            @Override
            public void onSuccess(List<AppointmentHistoryManager.AppointmentHistoryItem> items) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        updateAppointmentList(items);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Không thể tải lịch hẹn: " + error);
                        showEmptyState(true);
                    });
                }
            }
        });
    }

    private void updateAppointmentList(List<AppointmentHistoryManager.AppointmentHistoryItem> items) {
        appointmentList.clear();
        appointmentList.addAll(items);
        adapter.notifyDataSetChanged();

        showEmptyState(items.isEmpty());
    }

    private void showCancelDialog(AppointmentHistoryManager.AppointmentHistoryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hủy lịch hẹn")
                .setMessage("Bạn có chắc chắn muốn hủy lịch hẹn với " + item.appointment.getDoctor() + "?")
                .setPositiveButton("Hủy lịch", (dialog, which) -> {
                    cancelAppointment(item, "Người dùng hủy");
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void showRescheduleDialog(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // TODO: Implement reschedule dialog with date/time picker
        Toast.makeText(getContext(), "Chức năng đổi lịch đang được phát triển", Toast.LENGTH_SHORT).show();
    }

    private void cancelAppointment(AppointmentHistoryManager.AppointmentHistoryItem item, String reason) {
        service.cancelAppointment(item.appointment.getId(), reason, new AppointmentHistoryManager.OnActionListener() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadUpcomingAppointments(); // Refresh list
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showError("Không thể hủy lịch: " + error);
                    });
                }
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUpcomingAppointments(); // Refresh when fragment becomes visible
    }
}
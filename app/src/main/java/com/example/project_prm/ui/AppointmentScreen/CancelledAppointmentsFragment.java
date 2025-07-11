// NEW FILE: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/CancelledAppointmentsFragment.java
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
import com.example.project_prm.Services.HealthcareService;

import java.util.ArrayList;
import java.util.List;

public class CancelledAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private View emptyStateView;
    private View progressBar;

    private HealthcareService service;
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
        loadCancelledAppointments();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyStateView = view.findViewById(R.id.empty_state_view);
        progressBar = view.findViewById(R.id.progress_bar);

        service = HealthcareService.getInstance(requireContext());
        appointmentList = new ArrayList<>();

        // Update empty state for cancelled appointments
        if (view.findViewById(R.id.tv_empty_title) != null) {
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_title)).setText("No cancelled appointments");
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_description)).setText("Your cancelled appointments will appear here");
        }
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in cancelled
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in cancelled
            }

            @Override
            public void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                bookAgain(item);
            }

            @Override
            public void onLeaveReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in cancelled
            }

            @Override
            public void onContactClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                contactClinic(item);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCancelledAppointments() {
        showLoading(true);

        int userId = 1; // TODO: Get actual user ID from SharedPreferences

        service.getCancelledAppointments(userId, new AppointmentHistoryManager.OnHistoryListener() {
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

    private void bookAgain(AppointmentHistoryManager.AppointmentHistoryItem item) {
        service.getBookAgainTemplate(item.appointment.getId(), new AppointmentHistoryManager.OnBookAgainListener() {
            @Override
            public void onSuccess(AppointmentHistoryManager.AppointmentTemplate template) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Navigate to booking screen with pre-filled data
                        Intent intent = new Intent(getContext(), AppointmentBookingActivity.class);
                        intent.putExtra("clinic_id", template.clinicId);
                        intent.putExtra("doctor_name", template.doctorName);
                        intent.putExtra("patient_name", template.patientName);
                        intent.putExtra("patient_phone", template.patientPhone);
                        intent.putExtra("patient_age", template.patientAge);
                        intent.putExtra("patient_gender", template.patientGender);
                        intent.putExtra("symptoms", template.symptoms);
                        intent.putExtra("medical_history", template.medicalHistory);
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showError("Không thể tạo lịch mới: " + error);
                    });
                }
            }
        });
    }

    private void contactClinic(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Show contact options dialog or navigate to contact screen
        String clinicPhone = item.appointment.getPatientPhone(); // This should be clinic phone

        if (clinicPhone != null && !clinicPhone.isEmpty()) {
            // Open phone dialer
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(android.net.Uri.parse("tel:" + clinicPhone));
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Thông tin liên hệ không có sẵn", Toast.LENGTH_SHORT).show();
        }
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
        loadCancelledAppointments();
    }
}
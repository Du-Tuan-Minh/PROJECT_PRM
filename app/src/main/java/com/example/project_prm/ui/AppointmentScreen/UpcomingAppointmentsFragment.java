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
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;

import java.util.ArrayList;
import java.util.List;

public class UpcomingAppointmentsFragment extends Fragment {

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
        loadUpcomingAppointments();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyStateView = view.findViewById(R.id.empty_state_view);
        progressBar = view.findViewById(R.id.progress_bar);

        service = HealthcareService.getInstance(getContext());
        appointmentList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, AppointmentAdapter.TYPE_UPCOMING);

        adapter.setOnActionClickListener(new AppointmentAdapter.OnActionClickListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                cancelAppointment(item);
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                rescheduleAppointment(item);
            }

            @Override
            public void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not applicable for upcoming
            }

            @Override
            public void onReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not applicable for upcoming
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

        int userId = 1; // TODO: Get actual user ID from SharedPreferences

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
                        showError("Không thể tải lịch hẹn sắp tới: " + error);
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

    private void cancelAppointment(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Show confirmation dialog first
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Hủy lịch hẹn")
                .setMessage("Bạn có chắc chắn muốn hủy lịch hẹn này?")
                .setPositiveButton("Hủy lịch", (dialog, which) -> {
                    performCancelAppointment(item);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void performCancelAppointment(AppointmentHistoryManager.AppointmentHistoryItem item) {
        service.cancelAppointment(item.appointment.getId(), "Hủy bởi người dùng",
                new AppointmentHistoryManager.OnCancelListener() {
                    @Override
                    public void onSuccess() {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Đã hủy lịch hẹn thành công", Toast.LENGTH_SHORT).show();
                                loadUpcomingAppointments(); // Refresh list
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                showError("Không thể hủy lịch hẹn: " + error);
                            });
                        }
                    }
                });
    }

    private void rescheduleAppointment(AppointmentHistoryManager.AppointmentHistoryItem item) {
        service.getRescheduleTemplate(item.appointment.getId(), new AppointmentHistoryManager.OnRescheduleListener() {
            @Override
            public void onSuccess(AppointmentHistoryManager.RescheduleTemplate template) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Navigate to booking screen with pre-filled data for rescheduling
                        Intent intent = new Intent(getContext(), AppointmentBookingActivity.class);
                        intent.putExtra("is_reschedule", true);
                        intent.putExtra("original_appointment_id", item.appointment.getId());
                        intent.putExtra("clinic_id", template.clinicId);
                        intent.putExtra("clinic_name", template.clinicName);
                        intent.putExtra("doctor_name", template.doctorName);
                        intent.putExtra("patient_name", template.patientName);
                        intent.putExtra("patient_phone", template.patientPhone);
                        intent.putExtra("patient_age", template.patientAge);
                        intent.putExtra("patient_gender", template.patientGender);
                        intent.putExtra("symptoms", template.symptoms);
                        intent.putExtra("medical_history", template.medicalHistory);
                        intent.putExtra("appointment_type", template.appointmentType);
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showError("Không thể đổi lịch: " + error);
                    });
                }
            }
        });
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
        loadUpcomingAppointments(); // Refresh when fragment becomes visible
    }
}
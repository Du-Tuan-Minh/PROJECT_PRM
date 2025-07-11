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

        service = HealthcareService.getInstance(getContext());
        appointmentList = new ArrayList<>();

        // Update empty state for cancelled appointments
        updateEmptyStateText(view);
    }

    private void updateEmptyStateText(View view) {
        if (view.findViewById(R.id.tv_empty_title) != null) {
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_title))
                    .setText("Chưa có lịch hẹn bị hủy");
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_description))
                    .setText("Các lịch hẹn đã hủy sẽ hiển thị ở đây");
        }
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, AppointmentAdapter.TYPE_CANCELLED);

        adapter.setOnActionClickListener(new AppointmentAdapter.OnActionClickListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not applicable for cancelled appointments
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not applicable for cancelled appointments
            }

            @Override
            public void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                bookAgain(item);
            }

            @Override
            public void onReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not applicable for cancelled appointments
            }

            @Override
            public void onItemClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Navigate to Appointment Detail
                viewAppointmentDetail(item);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCancelledAppointments() {
        showLoading(true);

        int userId = getCurrentUserId();

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
                        showError("Không thể tải lịch hẹn đã hủy: " + error);
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
            public void onSuccess(AppointmentHistoryManager.BookAgainTemplate template) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Navigate to booking screen with pre-filled data
                        Intent intent = new Intent(getContext(), AppointmentBookingActivity.class);
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

                        // Flag to indicate this is "book again" from cancelled
                        intent.putExtra("is_book_again", true);
                        intent.putExtra("original_appointment_id", item.appointment.getId());

                        startActivity(intent);

                        Toast.makeText(getContext(), "Thông tin đã được điền sẵn", Toast.LENGTH_SHORT).show();
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
        // Try to get clinic phone from the appointment data
        // Since we don't have direct clinic phone in appointment,
        // we'll show a dialog with clinic info and suggest calling

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Liên hệ phòng khám")
                .setMessage("Phòng khám: " + item.appointment.getClinic() +
                        "\n\nBạn có thể tìm số điện thoại của phòng khám trong danh sách tìm kiếm phòng khám.")
                .setPositiveButton("Tìm phòng khám", (dialog, which) -> {
                    // Navigate to clinic search
                    Intent intent = new Intent(getContext(), com.example.project_prm.ui.SearchScreen.ClinicSearchActivity.class);
                    intent.putExtra("search_query", item.appointment.getClinic());
                    startActivity(intent);
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void viewAppointmentDetail(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Show appointment info with cancellation reason
        String info = String.format("Phòng khám: %s\nBác sĩ: %s\nNgày: %s\nGiờ: %s\nTrạng thái: Đã hủy",
                item.appointment.getClinic(),
                item.appointment.getDoctor(),
                item.appointment.getDate(),
                item.appointment.getTime());

        if (item.appointment.getCancellationReason() != null && !item.appointment.getCancellationReason().isEmpty()) {
            info += "\nLý do hủy: " + item.appointment.getCancellationReason();
        }

        Toast.makeText(getContext(), info, Toast.LENGTH_LONG).show();

        // TODO: Uncomment when AppointmentDetailActivity is created
        /*
        Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
        intent.putExtra("appointment_id", item.appointment.getId());
        startActivity(intent);
        */
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
        loadCancelledAppointments();
    }
}
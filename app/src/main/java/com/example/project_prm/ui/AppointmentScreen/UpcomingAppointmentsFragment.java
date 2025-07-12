// File: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/UpcomingAppointmentsFragment.java
package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hiển thị danh sách các cuộc hẹn sắp tới
 * Chức năng 9: Xem lịch sử đặt lịch khám - Upcoming appointments
 * - Hủy đặt lịch (Cancel) với lý do
 * - Đổi lịch (Reschedule) với phí đổi lịch
 * - Xem chi tiết cuộc hẹn
 */
public class UpcomingAppointmentsFragment extends Fragment {

    // UI Components
    private RecyclerView recyclerView;
    private View progressBar;
    private View emptyStateView;

    // Data & Adapter
    private AppointmentAdapter adapter;
    private List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;

    // Services
    private HealthcareService service;
    private AppointmentHistoryManager historyManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadUpcomingAppointments();
    }

    /**
     * Khởi tạo các view và service
     */
    private void initViews(View view) {
        // Initialize UI components
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyStateView = view.findViewById(R.id.empty_state_view);

        // Initialize services
        service = HealthcareService.getInstance(getContext());
        historyManager = new AppointmentHistoryManager(getContext());

        // Initialize data
        appointmentList = new ArrayList<>();
    }

    /**
     * Thiết lập RecyclerView và Adapter
     */
    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, AppointmentAdapter.TYPE_UPCOMING);

        // Thiết lập listener cho các action buttons
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
                // Không áp dụng cho upcoming appointments
            }

            @Override
            public void onReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Không áp dụng cho upcoming appointments
            }

            @Override
            public void onItemClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                viewAppointmentDetail(item);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Tải danh sách các cuộc hẹn sắp tới
     */
    private void loadUpcomingAppointments() {
        showLoading(true);

        service.getUpcomingAppointments(getCurrentUserId(), new AppointmentHistoryManager.OnHistoryListener() {
            @Override
            public void onSuccess(List<AppointmentHistoryManager.AppointmentHistoryItem> appointments) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        updateAppointmentList(appointments);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Lỗi khi tải danh sách: " + error);
                    });
                }
            }
        });
    }

    /**
     * Cập nhật danh sách cuộc hẹn
     */
    private void updateAppointmentList(List<AppointmentHistoryManager.AppointmentHistoryItem> appointments) {
        appointmentList.clear();
        if (appointments != null) {
            appointmentList.addAll(appointments);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (appointmentList.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
        }
    }

    /**
     * CHỨC NĂNG 9: Hủy cuộc hẹn (Upcoming appointments)
     * Cancel appointment với lý do
     */
    private void cancelAppointment(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Hiển thị dialog xác nhận hủy và chọn lý do
        showCancelConfirmationDialog(item);
    }

    /**
     * Hiển thị dialog xác nhận hủy với lựa chọn lý do
     */
    private void showCancelConfirmationDialog(AppointmentHistoryManager.AppointmentHistoryItem item) {
        String[] reasons = {
                "Có việc đột xuất không thể tham gia",
                "Thay đổi kế hoạch cá nhân",
                "Vấn đề sức khỏe đã được giải quyết",
                "Muốn đổi sang bác sĩ khác",
                "Lý do khác"
        };

        new AlertDialog.Builder(getContext())
                .setTitle("Hủy cuộc hẹn")
                .setMessage("Bạn có chắc muốn hủy cuộc hẹn này?\nChọn lý do hủy:")
                .setSingleChoiceItems(reasons, 0, null)
                .setPositiveButton("Xác nhận hủy", (dialog, which) -> {
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    String selectedReason = reasons[selectedPosition];
                    performCancelAppointment(item, selectedReason);
                })
                .setNegativeButton("Không hủy", null)
                .show();
    }

    /**
     * Thực hiện hủy cuộc hẹn
     */
    private void performCancelAppointment(AppointmentHistoryManager.AppointmentHistoryItem item, String reason) {
        showLoading(true);

        service.cancelAppointment(item.appointmentId, reason, new AppointmentHistoryManager.OnActionListener() {
            @Override
            public void onSuccess(String message) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadUpcomingAppointments(); // Refresh the list
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Lỗi khi hủy cuộc hẹn: " + error);
                    });
                }
            }
        });
    }

    /**
     * CHỨC NĂNG 9: Đổi lịch hẹn (Upcoming appointments)
     * Reschedule appointment với phí đổi lịch
     */
    private void rescheduleAppointment(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Điều hướng đến màn hình đổi lịch (RescheduleActivity)
        navigateToRescheduleScreen(item);
    }

    /**
     * Điều hướng đến màn hình đổi lịch
     */
    private void navigateToRescheduleScreen(AppointmentHistoryManager.AppointmentHistoryItem item) {
        try {
            // Tạo intent cho Reschedule Activity
            Intent intent = new Intent();
            intent.setClassName(getContext(), "com.example.project_prm.ui.RescheduleScreen.RescheduleActivity");

            // Truyền thông tin cuộc hẹn hiện tại
            intent.putExtra("appointment_id", item.appointmentId);
            intent.putExtra("doctor_name", item.doctorName);
            intent.putExtra("clinic_name", item.clinicName);
            intent.putExtra("specialty", item.specialty);
            intent.putExtra("current_date", item.appointmentDate);
            intent.putExtra("current_time", item.appointmentTime);
            intent.putExtra("current_fee", item.fee);

            startActivity(intent);
        } catch (Exception e) {
            // Fallback nếu RescheduleActivity chưa có
            showRescheduleDialog(item);
        }
    }

    /**
     * Hiển thị dialog đổi lịch đơn giản (fallback)
     */
    private void showRescheduleDialog(AppointmentHistoryManager.AppointmentHistoryItem item) {
        new AlertDialog.Builder(getContext())
                .setTitle("Đổi lịch hẹn")
                .setMessage(String.format(
                        "Cuộc hẹn hiện tại:\n• Ngày: %s\n• Giờ: %s\n• Bác sĩ: %s\n\nPhí đổi lịch: 50,000đ\n\nBạn có muốn tiếp tục?",
                        item.appointmentDate, item.appointmentTime, item.doctorName))
                .setPositiveButton("Đồng ý đổi lịch", (dialog, which) -> {
                    // Simple reschedule - chọn ngày mới
                    performSimpleReschedule(item);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    /**
     * Thực hiện đổi lịch đơn giản
     */
    private void performSimpleReschedule(AppointmentHistoryManager.AppointmentHistoryItem item) {
        // Mock new date/time cho demo
        String newDate = "2025-07-20"; // Date after current appointment
        String newTime = "14:00";

        service.rescheduleAppointment(item.appointmentId, newDate, newTime, new AppointmentHistoryManager.OnActionListener() {
            @Override
            public void onSuccess(String message) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        loadUpcomingAppointments(); // Refresh the list
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showError("Lỗi khi đổi lịch: " + error);
                    });
                }
            }
        });
    }

    /**
     * Xem chi tiết cuộc hẹn
     */
    private void viewAppointmentDetail(AppointmentHistoryManager.AppointmentHistoryItem item) {
        Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
        intent.putExtra("appointment_id", item.appointmentId);
        intent.putExtra("appointment_status", "upcoming");
        startActivity(intent);
    }

    /**
     * Lấy User ID hiện tại
     */
    private int getCurrentUserId() {
        if (getContext() != null) {
            SharedPreferences prefs = getContext()
                    .getSharedPreferences("MyAppPrefs", android.content.Context.MODE_PRIVATE);
            return prefs.getInt("userId", 1); // Mặc định là 1 cho demo
        }
        return 1;
    }

    /**
     * Hiển thị/ẩn loading indicator
     */
    private void showLoading(boolean show) {
        if (progressBar != null && recyclerView != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);

            if (emptyStateView != null) {
                emptyStateView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Hiển thị/ẩn empty state
     */
    private void showEmptyState(boolean show) {
        if (emptyStateView != null && recyclerView != null) {
            emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Hiển thị thông báo lỗi
     */
    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Làm mới danh sách khi fragment hiển thị lại
     */
    @Override
    public void onResume() {
        super.onResume();
        loadUpcomingAppointments(); // Refresh data when fragment becomes visible
    }

    /**
     * Dọn dẹp resources khi destroy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (historyManager != null) {
            historyManager.close();
        }
    }
}
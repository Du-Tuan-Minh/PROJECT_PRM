// File: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/CancelledAppointmentsFragment.java
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
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hiển thị danh sách các cuộc hẹn đã bị hủy
 * Chức năng 9: Xem lịch sử đặt lịch khám - Cancelled appointments
 * - Hiển thị lý do hủy
 * - Cho phép đặt lại lịch (Book Again)
 * - Xem chi tiết cuộc hẹn
 */
public class CancelledAppointmentsFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_cancelled_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadCancelledAppointments();
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
        adapter = new AppointmentAdapter(appointmentList, AppointmentAdapter.TYPE_CANCELLED);

        // Thiết lập listener cho các action buttons
        adapter.setOnActionClickListener(new AppointmentAdapter.OnActionClickListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Không áp dụng cho cancelled appointments
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Không áp dụng cho cancelled appointments
            }

            @Override
            public void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                bookAgain(item);
            }

            @Override
            public void onReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Không áp dụng cho cancelled appointments
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
     * Tải danh sách các cuộc hẹn đã hủy
     */
    private void loadCancelledAppointments() {
        showLoading(true);

        service.getCancelledAppointments(getCurrentUserId(), new AppointmentHistoryManager.OnHistoryListener() {
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
     * CHỨC NĂNG 9: Đặt lịch lại từ cancelled appointments
     * Tương ứng với màn hình 55, 56, 57 trong design
     */
    private void bookAgain(AppointmentHistoryManager.AppointmentHistoryItem item) {
        showLoading(true);

        // Lấy template để đặt lại lịch
        service.getBookAgainTemplate(item.appointmentId, new AppointmentHistoryManager.OnBookAgainListener() {
            @Override
            public void onSuccess(AppointmentHistoryManager.BookAgainTemplate template) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        navigateToBookingScreen(template);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getContext() != null && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Không thể tạo đặt lịch mới: " + error);
                    });
                }
            }
        });
    }

    /**
     * Điều hướng đến màn hình đặt lịch với thông tin từ cuộc hẹn đã hủy
     */
    private void navigateToBookingScreen(AppointmentHistoryManager.BookAgainTemplate template) {
        Intent intent = new Intent(getContext(), AppointmentBookingActivity.class);

        // Truyền thông tin từ cuộc hẹn đã hủy
        intent.putExtra("doctor_id", template.doctorId);
        intent.putExtra("doctor_name", template.doctorName);
        intent.putExtra("specialty", template.specialty);
        intent.putExtra("clinic_name", template.clinicName);
        intent.putExtra("package_type", template.packageType);
        intent.putExtra("fee", template.fee);
        intent.putExtra("original_date", template.originalDate);
        intent.putExtra("original_time", template.originalTime);
        intent.putExtra("from_cancelled", true); // Flag để biết đây là đặt lại từ cancelled

        startActivity(intent);

        Toast.makeText(getContext(),
                "Đang chuyển đến màn hình đặt lịch với thông tin cuộc hẹn trước đó...",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Xem chi tiết cuộc hẹn
     */
    private void viewAppointmentDetail(AppointmentHistoryManager.AppointmentHistoryItem item) {
        Intent intent = new Intent(getContext(), AppointmentDetailActivity.class);
        intent.putExtra("appointment_id", item.appointmentId);
        intent.putExtra("appointment_status", "cancelled");
        startActivity(intent);
    }

    // Xóa method không sử dụng showCancellationReason

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
        loadCancelledAppointments(); // Refresh data when fragment becomes visible
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
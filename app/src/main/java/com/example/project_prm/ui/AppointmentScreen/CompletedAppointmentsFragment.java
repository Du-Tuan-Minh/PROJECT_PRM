// NEW FILE: app/src/main/java/com/example/project_prm/UI/AppointmentScreen/CompletedAppointmentsFragment.java
package com.example.project_prm.ui.AppointmentScreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class CompletedAppointmentsFragment extends Fragment {

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
        loadCompletedAppointments();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyStateView = view.findViewById(R.id.empty_state_view);
        progressBar = view.findViewById(R.id.progress_bar);

        service = TungFeaturesService.getInstance(requireContext());
        appointmentList = new ArrayList<>();

        // Update empty state for completed appointments
        view.findViewById(R.id.tv_empty_title).setVisibility(View.VISIBLE);
        ((android.widget.TextView) view.findViewById(R.id.tv_empty_title)).setText("No completed appointments");
        ((android.widget.TextView) view.findViewById(R.id.tv_empty_description)).setText("Your completed appointments will appear here");
    }

    private void setupRecyclerView() {
        adapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in completed
            }

            @Override
            public void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in completed
            }

            @Override
            public void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                bookAgain(item);
            }

            @Override
            public void onLeaveReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                showReviewDialog(item);
            }

            @Override
            public void onContactClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in completed
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCompletedAppointments() {
        showLoading(true);

        int userId = 1; // TODO: Get actual user ID

        service.getCompletedAppointments(userId, new AppointmentHistoryManager.OnHistoryListener() {
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

    private void showReviewDialog(AppointmentHistoryManager.AppointmentHistoryItem item) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_leave_review, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        EditText etReview = dialogView.findViewById(R.id.et_review);

        new AlertDialog.Builder(requireContext())
                .setTitle("Leave a Review")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    int rating = (int) ratingBar.getRating();
                    String review = etReview.getText().toString().trim();

                    if (rating == 0) {
                        Toast.makeText(getContext(), "Please select a rating", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    submitReview(item, rating, review);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void submitReview(AppointmentHistoryManager.AppointmentHistoryItem item, int rating, String review) {
        service.addAppointmentFeedback(item.appointment.getId(), rating, review, new AppointmentHistoryManager.OnActionListener() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                        loadCompletedAppointments(); // Refresh list
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showError("Failed to submit review: " + error);
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
        loadCompletedAppointments();
    }
}
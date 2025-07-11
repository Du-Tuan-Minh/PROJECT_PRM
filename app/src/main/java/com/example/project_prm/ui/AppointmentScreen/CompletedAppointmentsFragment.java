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
        if (view.findViewById(R.id.tv_empty_title) != null) {
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_title)).setText("No completed appointments");
            ((android.widget.TextView) view.findViewById(R.id.tv_empty_description)).setText("Your completed appointments will appear here");
        }
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
                // Navigate to Review Activity
                Intent intent = new Intent(getContext(), ReviewActivity.class);
                intent.putExtra("appointment_id", item.appointment.getId());
                intent.putExtra("doctor_name", item.appointment.getDoctor());
                startActivity(intent);
            }

            @Override
            public void onContactClick(AppointmentHistoryManager.AppointmentHistoryItem item) {
                // Not used in completed
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

    private void loadCompletedAppointments() {
        showLoading(true);

        int userId = 1; // TODO: Get actual user ID

        service.getCompletedAppointments(userId, new AppointmentHistoryManager.OnHistoryListener() {
            @Override
            public void onSuccess(List<AppointmentHistoryManager.AppointmentHistoryItem> items) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        appointmentList.clear();
                        appointmentList.addAll(items);
                        adapter.notifyDataSetChanged();

                        showEmptyState(items.isEmpty());
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        showError("Error loading completed appointments: " + error);
                        showEmptyState(true);
                    });
                }
            }
        });
    }

    private void bookAgain(AppointmentHistoryManager.AppointmentHistoryItem item) {
        service.getBookAgainTemplate(item.appointment.getId(), new AppointmentHistoryManager.OnBookAgainListener() {
            @Override
            public void onSuccess(AppointmentHistoryManager.BookAgainTemplate template) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Navigate to book appointment with pre-filled data
                        Intent intent = new Intent(getContext(), BookAppointmentActivity.class);
                        intent.putExtra("doctor_name", template.doctorName);
                        intent.putExtra("clinic_name", template.clinicName);
                        intent.putExtra("appointment_type", template.appointmentType);
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
                        showError("Cannot create new appointment: " + error);
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
        loadCompletedAppointments(); // Refresh when fragment becomes visible
    }
}
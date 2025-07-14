package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;

import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class CompletedAppointmentsFragment extends Fragment {

    private static final String TAG = "CompletedAppointments";
    
    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<AppointmentModel> appointments;
    
    public static CompletedAppointmentsFragment newInstance() {
        return new CompletedAppointmentsFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_list, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadCompletedAppointments();
        
        return view;
    }
    
    private void initViews(View view) {
        rvAppointments = view.findViewById(R.id.rvAppointments);
    }
    
    private void setupRecyclerView() {
        appointments = new ArrayList<>();
        adapter = new AppointmentAdapter(appointments);
        adapter.setOnAppointmentActionListener(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onAppointmentClick(appointment.getId(), appointment.getStatus());
                }
            }
            
            @Override
            public void onCancelAppointment(AppointmentModel appointment) {
                // Không áp dụng cho completed
            }
            
            @Override
            public void onRescheduleAppointment(AppointmentModel appointment) {
                // Không áp dụng cho completed
            }
            
            @Override
            public void onBookAgain(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onBookAgain(appointment.getDoctorName());
                }
            }
            
            @Override
            public void onLeaveReview(AppointmentModel appointment) {
                // CHỈ cho phép review nếu appointment đã completed và chưa review
                if (appointment.canBeReviewed()) {
                    if (getActivity() instanceof AppointmentHistoryActivity) {
                        ((AppointmentHistoryActivity) getActivity()).onLeaveReview(appointment.getId());
                    }
                } else if (appointment.isReviewed()) {
                    // Đã review rồi, có thể hiển thị review hoặc không làm gì
                    Toast.makeText(getContext(), "Bạn đã đánh giá cuộc hẹn này rồi", Toast.LENGTH_SHORT).show();
                } else {
                    // Chưa completed, không thể review
                    Toast.makeText(getContext(), "Chỉ có thể đánh giá sau khi hoàn thành cuộc hẹn", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAppointments.setAdapter(adapter);
    }
    
    private void loadCompletedAppointments() {
        try {
            appointments.clear();
            appointments.addAll(AppointmentRepository.getInstance().getCompletedAppointments());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading completed appointments: " + e.getMessage());
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadCompletedAppointments();
    }
} 
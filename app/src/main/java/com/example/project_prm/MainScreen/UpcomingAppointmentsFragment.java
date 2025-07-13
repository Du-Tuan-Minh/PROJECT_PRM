package com.example.project_prm.MainScreen;

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
import java.util.ArrayList;
import java.util.List;

public class UpcomingAppointmentsFragment extends Fragment {
    
    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<AppointmentModel> appointments;
    
    public static UpcomingAppointmentsFragment newInstance() {
        return new UpcomingAppointmentsFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_list, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadUpcomingAppointments();
        
        return view;
    }
    
    private void initViews(View view) {
        rvAppointments = view.findViewById(R.id.rvAppointments);
    }
    
    private void setupRecyclerView() {
        appointments = new ArrayList<>();
        adapter = new AppointmentAdapter(appointments, new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onAppointmentClick(appointment.getId(), appointment.getStatus());
                }
            }
            
            @Override
            public void onCancelAppointment(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onCancelAppointment(appointment.getId());
                }
            }
            
            @Override
            public void onRescheduleAppointment(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onRescheduleAppointment(appointment.getId());
                }
            }
            
            @Override
            public void onBookAgain(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onBookAgain(appointment.getDoctorName());
                }
            }
            
            @Override
            public void onLeaveReview(AppointmentModel appointment) {
                // Không áp dụng cho upcoming
            }
        });
        
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAppointments.setAdapter(adapter);
    }
    
    private void loadUpcomingAppointments() {
        appointments.clear();
        for (AppointmentModel a : AppointmentRepository.getInstance().getAppointments()) {
            if ("upcoming".equalsIgnoreCase(a.getStatus())) {
                appointments.add(a);
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadUpcomingAppointments();
    }
} 
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

public class CancelledAppointmentsFragment extends Fragment {
    
    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<AppointmentModel> appointments;
    
    public static CancelledAppointmentsFragment newInstance() {
        return new CancelledAppointmentsFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_list, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadCancelledAppointments();
        
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
                // Không áp dụng cho cancelled
            }
            
            @Override
            public void onRescheduleAppointment(AppointmentModel appointment) {
                // Không áp dụng cho cancelled
            }
            
            @Override
            public void onBookAgain(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onBookAgain(appointment.getDoctorName());
                }
            }
            
            @Override
            public void onLeaveReview(AppointmentModel appointment) {
                // Không áp dụng cho cancelled
            }
        });
        
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAppointments.setAdapter(adapter);
    }
    
    private void loadCancelledAppointments() {
        appointments.clear();
        for (AppointmentModel a : AppointmentRepository.getInstance().getAppointments()) {
            if ("cancelled".equalsIgnoreCase(a.getStatus())) {
                appointments.add(a);
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadCancelledAppointments();
    }
} 
package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;

public class RescheduleDateTimeFragment extends Fragment {
    
    private CalendarView calendarView;
    private RecyclerView rvTimeSlots;
    private MaterialButton btnConfirm;
    private TimeSlotAdapter adapter;
    
    private String selectedDate = "";
    private String selectedTime = "";
    private OnDateTimeSelectedListener listener;
    
    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(String date, String time);
    }
    
    public void setOnDateTimeSelectedListener(OnDateTimeSelectedListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reschedule_datetime, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupListeners();
        loadTimeSlots();
    }
    
    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        rvTimeSlots = view.findViewById(R.id.rvTimeSlots);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        
        rvTimeSlots.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }
    
    private void setupListeners() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            updateConfirmButton();
        });
        
        btnConfirm.setOnClickListener(v -> {
            if (!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
                if (listener != null) {
                    listener.onDateTimeSelected(selectedDate, selectedTime);
                }
            }
        });
    }
    
    private void loadTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        timeSlots.add("09:00 AM");
        timeSlots.add("09:30 AM");
        timeSlots.add("10:00 AM");
        timeSlots.add("10:30 AM");
        timeSlots.add("11:00 AM");
        timeSlots.add("11:30 AM");
        timeSlots.add("02:00 PM");
        timeSlots.add("02:30 PM");
        timeSlots.add("03:00 PM");
        timeSlots.add("03:30 PM");
        timeSlots.add("04:00 PM");
        timeSlots.add("04:30 PM");
        
        adapter = new TimeSlotAdapter(timeSlots, new TimeSlotAdapter.OnTimeSlotClickListener() {
            @Override
            public void onTimeSlotClick(String timeSlot) {
                selectedTime = timeSlot;
                updateConfirmButton();
            }
        });
        rvTimeSlots.setAdapter(adapter);
    }
    
    private void updateConfirmButton() {
        btnConfirm.setEnabled(!selectedDate.isEmpty() && !selectedTime.isEmpty());
    }
} 
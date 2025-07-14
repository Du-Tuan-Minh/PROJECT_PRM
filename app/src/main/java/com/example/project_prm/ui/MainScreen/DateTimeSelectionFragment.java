package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeSelectionFragment extends Fragment {
    
    private CalendarView calendarView;
    private RecyclerView rvTimeSlots;
    private MaterialButton btnNext;
    private TextView tvSelectedDate;
    
    private String selectedDate = "";
    private String selectedTime = "";
    private TimeSlotAdapter timeSlotAdapter;
    private List<TimeSlot> timeSlots;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datetime_selection, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupCalendar();
        setupTimeSlots();
        setupListeners();
    }
    
    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        rvTimeSlots = view.findViewById(R.id.rvTimeSlots);
        btnNext = view.findViewById(R.id.btnNext);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        
        // Set minimum date to today
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());
        
        // Set initial date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        tvSelectedDate.setText("Ngày đã chọn: " + selectedDate);
    }
    
    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDate = sdf.format(calendar.getTime());
            tvSelectedDate.setText("Ngày đã chọn: " + selectedDate);
            updateTimeSlots();
        });
    }
    
    private void setupTimeSlots() {
        timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot("08:00", true));
        timeSlots.add(new TimeSlot("09:00", true));
        timeSlots.add(new TimeSlot("10:00", true));
        timeSlots.add(new TimeSlot("11:00", false));
        timeSlots.add(new TimeSlot("14:00", true));
        timeSlots.add(new TimeSlot("15:00", true));
        timeSlots.add(new TimeSlot("16:00", true));
        timeSlots.add(new TimeSlot("17:00", false));
        
        timeSlotAdapter = new TimeSlotAdapter(timeSlots, new TimeSlotAdapter.OnTimeSlotClickListener() {
            @Override
            public void onTimeSlotClick(TimeSlot timeSlot) {
                if (timeSlot.isAvailable) {
                    selectedTime = timeSlot.time;
                    timeSlotAdapter.setSelectedTime(selectedTime);
                    updateNextButton();
                }
            }
        });
        
        rvTimeSlots.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvTimeSlots.setAdapter(timeSlotAdapter);
    }
    
    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
                BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
                if (activity != null) {
                    activity.onDateTimeSelected(selectedDate, selectedTime);
                }
            }
        });
        
        updateNextButton();
    }
    
    private void updateTimeSlots() {
        // Update available time slots based on selected date
        // This is a simple implementation - in real app, you'd check against booked appointments
        timeSlotAdapter.notifyDataSetChanged();
    }
    
    private void updateNextButton() {
        btnNext.setEnabled(!selectedDate.isEmpty() && !selectedTime.isEmpty());
    }
    
    public static class TimeSlot {
        public String time;
        public boolean isAvailable;
        
        public TimeSlot(String time, boolean isAvailable) {
            this.time = time;
            this.isAvailable = isAvailable;
        }
    }
    
    public static class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
        private List<TimeSlot> timeSlots;
        private OnTimeSlotClickListener listener;
        private String selectedTime = "";
        
        public interface OnTimeSlotClickListener {
            void onTimeSlotClick(TimeSlot timeSlot);
        }
        
        public TimeSlotAdapter(List<TimeSlot> timeSlots, OnTimeSlotClickListener listener) {
            this.timeSlots = timeSlots;
            this.listener = listener;
        }
        
        @NonNull
        @Override
        public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_time_slot, parent, false);
            return new TimeSlotViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
            TimeSlot timeSlot = timeSlots.get(position);
            holder.bind(timeSlot);
        }
        
        @Override
        public int getItemCount() {
            return timeSlots.size();
        }
        
        public void setSelectedTime(String time) {
            this.selectedTime = time;
            notifyDataSetChanged();
        }
        
        class TimeSlotViewHolder extends RecyclerView.ViewHolder {
            private MaterialButton btnTimeSlot;
            
            public TimeSlotViewHolder(@NonNull View itemView) {
                super(itemView);
                btnTimeSlot = itemView.findViewById(R.id.btnTimeSlot);
            }
            
            public void bind(TimeSlot timeSlot) {
                btnTimeSlot.setText(timeSlot.time);
                
                if (timeSlot.isAvailable) {
                    btnTimeSlot.setEnabled(true);
                    if (timeSlot.time.equals(selectedTime)) {
                        btnTimeSlot.setBackgroundResource(R.drawable.bg_button_primary);
                        btnTimeSlot.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                    } else {
                        btnTimeSlot.setBackgroundResource(R.drawable.bg_button_outline);
                        btnTimeSlot.setTextColor(itemView.getContext().getResources().getColor(R.color.primary_blue));
                    }
                } else {
                    btnTimeSlot.setEnabled(false);
                    btnTimeSlot.setBackgroundResource(R.drawable.bg_button_outline);
                    btnTimeSlot.setTextColor(itemView.getContext().getResources().getColor(R.color.text_gray));
                }
                
                btnTimeSlot.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onTimeSlotClick(timeSlot);
                    }
                });
            }
        }
    }
} 
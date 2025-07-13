package com.example.project_prm.MainScreen;

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
import java.util.Calendar;
import java.util.List;
import android.widget.TextView;

public class DateTimeSelectionFragment extends Fragment {
    
    private CalendarView calendarView;
    private RecyclerView rvTimeSlots;
    private MaterialButton btnNext;
    private String selectedDate = "";
    private String selectedTime = "";
    private TimeSlotAdapter timeSlotAdapter;
    
    public static DateTimeSelectionFragment newInstance() {
        return new DateTimeSelectionFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_datetime_selection, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupListeners();
        setupTimeSlots();
    }
    
    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        rvTimeSlots = view.findViewById(R.id.rvTimeSlots);
        btnNext = view.findViewById(R.id.btnNext);
    }
    
    private void setupListeners() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
            updateNextButton();
        });
        
        btnNext.setOnClickListener(v -> {
            if (!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
                BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
                if (activity != null) {
                    activity.onDateTimeSelected(selectedDate, selectedTime);
                }
            }
        });
    }
    
    private void setupTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        // 09:00 AM đến 05:30 PM, cách nhau 30 phút
        String[] slots = {"09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "15:00 PM", "15:30 PM", "16:00 PM", "16:30 PM", "17:00 PM", "17:30 PM"};
        for (String slot : slots) timeSlots.add(slot);
        timeSlotAdapter = new TimeSlotAdapter(timeSlots, selectedTime -> {
            this.selectedTime = selectedTime;
            updateNextButton();
        });
        rvTimeSlots.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvTimeSlots.setAdapter(timeSlotAdapter);
    }
    
    private void updateNextButton() {
        btnNext.setEnabled(!selectedDate.isEmpty() && !selectedTime.isEmpty());
    }
    
    // TimeSlotAdapter inner class
    private static class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {
        private List<String> timeSlots;
        private OnTimeSlotClickListener listener;
        private int selectedPosition = -1;
        
        public interface OnTimeSlotClickListener {
            void onTimeSlotSelected(String time);
        }
        
        public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotClickListener listener) {
            this.timeSlots = timeSlots;
            this.listener = listener;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String time = timeSlots.get(position);
            holder.tvTime.setText(time);
            boolean isSelected = position == selectedPosition;
            holder.itemView.setSelected(isSelected);
            holder.tvTime.setSelected(isSelected);
            holder.itemView.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    int previousSelected = selectedPosition;
                    selectedPosition = adapterPosition;
                    notifyItemChanged(previousSelected);
                    notifyItemChanged(selectedPosition);
                    if (listener != null) {
                        listener.onTimeSlotSelected(time);
                    }
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return timeSlots.size();
        }
        
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTime;
            
            ViewHolder(View itemView) {
                super(itemView);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
        }
    }
} 
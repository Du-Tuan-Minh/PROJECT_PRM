package com.example.project_prm.ui.BookingScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;

import java.util.List;

/**
 * Adapter for Time Slot selection in Appointment Booking
 * Chức năng 8: Đặt lịch khám - Time slot selection
 */
public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<String> timeSlots;
    private String selectedTimeSlot = "";
    private OnTimeSlotSelectedListener listener;

    public interface OnTimeSlotSelectedListener {
        void onTimeSlotSelected(String timeSlot);
    }

    public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotSelectedListener listener) {
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
        String timeSlot = timeSlots.get(position);
        holder.bind(timeSlot);
    }

    @Override
    public int getItemCount() {
        return timeSlots != null ? timeSlots.size() : 0;
    }

    public void updateTimeSlots(List<String> newTimeSlots) {
        this.timeSlots = newTimeSlots;
        this.selectedTimeSlot = ""; // Reset selection
        notifyDataSetChanged();
    }

    public String getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void clearSelection() {
        selectedTimeSlot = "";
        notifyDataSetChanged();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
        }

        public void bind(String timeSlot) {
            tvTimeSlot.setText(timeSlot);

            // Set appearance based on selection
            boolean isSelected = timeSlot.equals(selectedTimeSlot);
            if (isSelected) {
                tvTimeSlot.setBackgroundResource(R.drawable.bg_time_slot_selected);
                tvTimeSlot.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            } else {
                tvTimeSlot.setBackgroundResource(R.drawable.bg_time_slot_unselected);
                tvTimeSlot.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_black));
            }

            // Handle click
            itemView.setOnClickListener(v -> {
                selectedTimeSlot = timeSlot;
                notifyDataSetChanged(); // Refresh all items to update selection

                if (listener != null) {
                    listener.onTimeSlotSelected(timeSlot);
                }
            });
        }
    }
}
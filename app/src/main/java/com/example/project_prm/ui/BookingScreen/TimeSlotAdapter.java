// NEW FILE: app/src/main/java/com/example/project_prm/ui/BookingScreen/TimeSlotAdapter.java
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

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private final List<String> timeSlots;
    private final OnTimeSlotClickListener listener;
    private int selectedPosition = -1;

    public interface OnTimeSlotClickListener {
        void onTimeSlotSelected(String timeSlot);
    }

    public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotClickListener listener) {
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
        holder.bind(timeSlot, position == selectedPosition, position, listener);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;

        if (previousPosition != -1) {
            notifyItemChanged(previousPosition);
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
        }

        public void bind(String timeSlot, boolean isSelected, int position, OnTimeSlotClickListener listener) {
            tvTimeSlot.setText(timeSlot);

            // Update appearance based on selection
            if (isSelected) {
                tvTimeSlot.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.primary_blue));
                tvTimeSlot.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            } else {
                tvTimeSlot.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.background_light));
                tvTimeSlot.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_black));
            }

            itemView.setOnClickListener(v -> {
                setSelectedPosition(position);
                if (listener != null) {
                    listener.onTimeSlotSelected(timeSlot);
                }
            });
        }
    }
}
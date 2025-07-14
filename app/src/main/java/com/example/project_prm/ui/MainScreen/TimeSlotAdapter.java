package com.example.project_prm.ui.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    
    private List<String> timeSlots;
    private OnTimeSlotClickListener listener;
    private int selectedPosition = -1;

    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(String timeSlot);
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
        holder.bind(timeSlot, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private MaterialButton btnTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            btnTimeSlot = itemView.findViewById(R.id.btnTimeSlot);
        }

        public void bind(String timeSlot, boolean isSelected) {
            btnTimeSlot.setText(timeSlot);
            
            if (isSelected) {
                btnTimeSlot.setBackgroundResource(R.drawable.bg_button_primary);
                btnTimeSlot.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
            } else {
                btnTimeSlot.setBackgroundResource(R.drawable.bg_button_outline);
                btnTimeSlot.setTextColor(itemView.getContext().getResources().getColor(R.color.primary_blue));
            }

            btnTimeSlot.setOnClickListener(v -> {
                int previousSelected = selectedPosition;
                selectedPosition = getAdapterPosition();
                
                // Update previous selection
                if (previousSelected != -1) {
                    notifyItemChanged(previousSelected);
                }
                
                // Update new selection
                notifyItemChanged(selectedPosition);
                
                if (listener != null) {
                    listener.onTimeSlotClick(timeSlot);
                }
            });
        }
    }
} 
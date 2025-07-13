package com.example.project_prm.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    private List<String> timeSlots;
    private OnTimeSlotClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private Context context;

    // Colors
    private static final String GREEN_COLOR = "#4CAF50"; // Material Green
    private static final String WHITE_COLOR = "#FFFFFF";
    private static final String BLACK_COLOR = "#000000";
    private static final String GRAY_BORDER = "#E0E0E0";

    public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String timeSlot = timeSlots.get(position);
        holder.bind(timeSlot, position);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
        }

        public void bind(String timeSlot, int position) {
            tvTimeSlot.setText(timeSlot);

            // Set appearance based on selection
            if (selectedPosition == position) {
                // Selected state - GREEN background, WHITE text
                itemView.setBackgroundResource(R.drawable.bg_time_slot_selected);
                tvTimeSlot.setTextColor(Color.parseColor(WHITE_COLOR));
            } else {
                // Unselected state - WHITE background, BLACK text
                itemView.setBackgroundResource(R.drawable.bg_time_slot_unselected);
                tvTimeSlot.setTextColor(Color.parseColor(BLACK_COLOR));
            }

            // Click listener
            itemView.setOnClickListener(v -> {
                int oldPosition = selectedPosition;
                selectedPosition = getAdapterPosition();

                // Notify changes for smooth animation
                if (oldPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(oldPosition);
                }
                notifyItemChanged(selectedPosition);

                // Callback to parent
                if (listener != null) {
                    listener.onTimeSlotClick(timeSlot, selectedPosition);
                }
            });
        }
    }

    // Interface for click callbacks
    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(String timeSlot, int position);
    }

    // Public methods for external control
    public void clearSelection() {
        int oldPosition = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        if (oldPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPosition);
        }
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;

        if (oldPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPosition);
        }
        if (selectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPosition);
        }
    }

    public String getSelectedTimeSlot() {
        if (selectedPosition != RecyclerView.NO_POSITION &&
                selectedPosition < timeSlots.size()) {
            return timeSlots.get(selectedPosition);
        }
        return null;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void updateTimeSlots(List<String> newTimeSlots) {
        this.timeSlots.clear();
        this.timeSlots.addAll(newTimeSlots);
        this.selectedPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }
}
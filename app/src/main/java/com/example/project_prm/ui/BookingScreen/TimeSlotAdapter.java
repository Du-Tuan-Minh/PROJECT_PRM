package com.example.project_prm.ui.BookingScreen;

import android.content.Context;
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

    public interface OnTimeSlotClickListener {
        void onTimeSlotSelected(String timeSlot, int position);
    }

    private final List<String> timeSlots;
    private final OnTimeSlotClickListener listener;
    private int selectedPosition = -1;
    private Context context;

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
        holder.tvTimeSlot.setText(timeSlot);

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_time_slot_selected);
            holder.tvTimeSlot.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_time_slot_unselected);
            holder.tvTimeSlot.setTextColor(ContextCompat.getColor(context, R.color.text_black));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                if (selectedPosition != holder.getAdapterPosition()) {
                    int previousSelectedPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(previousSelectedPosition);
                    notifyItemChanged(selectedPosition);
                    listener.onTimeSlotSelected(timeSlot, selectedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots != null ? timeSlots.size() : 0;
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
        }
    }
}
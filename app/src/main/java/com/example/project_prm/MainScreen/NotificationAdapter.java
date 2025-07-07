package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications;
    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.tvNotification.setText(notification.getContent());
        // Hiển thị thời gian nếu muốn
        if (holder.tvTime != null) {
            String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(notification.getTimestamp()));
            holder.tvTime.setText(time);
        }
    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotification, tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotification = itemView.findViewById(R.id.tvNotification);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
} 
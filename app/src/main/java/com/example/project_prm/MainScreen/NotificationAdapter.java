package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.tvTitle.setText(notification.getTitle());
        holder.tvDescription.setText(notification.getDescription());
        holder.tvTime.setText(formatTime(notification.getTimestamp()));
        holder.tvNew.setVisibility(notification.isNew() ? View.VISIBLE : View.GONE);
        int iconRes = R.drawable.ic_calendar;
        int bgRes = R.drawable.bg_circle_lightblue;
        switch (notification.getIconType()) {
            case "cancel":
                iconRes = R.drawable.ic_close;
                bgRes = R.drawable.bg_circle_red;
                break;
            case "calendar":
                iconRes = R.drawable.ic_calendar;
                bgRes = R.drawable.bg_circle_green;
                break;
            case "success":
                iconRes = R.drawable.ic_check;
                bgRes = R.drawable.bg_circle_blue;
                break;
            case "service":
                iconRes = R.drawable.ic_service;
                bgRes = R.drawable.bg_circle_orange;
                break;
            case "credit":
                iconRes = R.drawable.ic_credit;
                bgRes = R.drawable.bg_circle_blue;
                break;
        }
        holder.ivIcon.setImageResource(iconRes);
        holder.ivIcon.setBackgroundResource(bgRes);
    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvDescription, tvTime, tvNew;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNew = itemView.findViewById(R.id.tvNew);
        }
    }
    private String formatTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date now = new Date();
        long diff = now.getTime() - date.getTime();
        if (diff < 24 * 60 * 60 * 1000 && now.getDate() == date.getDate()) {
            return "Today  |  " + sdfTime.format(date);
        } else if (diff < 48 * 60 * 60 * 1000 && now.getDate() - date.getDate() == 1) {
            return "Yesterday  |  " + sdfTime.format(date);
        } else {
            return sdfDate.format(date) + "  |  " + sdfTime.format(date);
        }
    }
} 
// NEW FILE: app/src/main/java/com/example/project_prm/ui/SearchScreen/ClinicSearchAdapter.java
package com.example.project_prm.ui.SearchScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.SearchManager.ClinicSearchManager;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.List;

public class ClinicSearchAdapter extends RecyclerView.Adapter<ClinicSearchAdapter.ClinicViewHolder> {

    private final List<ClinicSearchManager.ClinicSearchResult> clinicList;
    private final OnClinicClickListener listener;

    public interface OnClinicClickListener {
        void onClinicClick(ClinicSearchManager.ClinicSearchResult clinic);
        void onBookAppointmentClick(ClinicSearchManager.ClinicSearchResult clinic);
    }

    public ClinicSearchAdapter(List<ClinicSearchManager.ClinicSearchResult> clinicList, OnClinicClickListener listener) {
        this.clinicList = clinicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clinic_search, parent, false);
        return new ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        ClinicSearchManager.ClinicSearchResult clinic = clinicList.get(position);
        holder.bind(clinic, listener);
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    static class ClinicViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivClinicImage;
        private final TextView tvClinicName;
        private final TextView tvSpecialty;
        private final TextView tvAddress;
        private final TextView tvDistance;
        private final RatingBar ratingBar;
        private final TextView tvRating;
        private final TextView tvReviewCount;
        private final TextView tvOpenStatus;
        private final MaterialButton btnBookAppointment;

        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClinicImage = itemView.findViewById(R.id.iv_clinic_image);
            tvClinicName = itemView.findViewById(R.id.tv_clinic_name);
            tvSpecialty = itemView.findViewById(R.id.tv_specialty);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviewCount = itemView.findViewById(R.id.tv_review_count);
            tvOpenStatus = itemView.findViewById(R.id.tv_open_status);
            btnBookAppointment = itemView.findViewById(R.id.btn_book_appointment);
        }

        public void bind(ClinicSearchManager.ClinicSearchResult clinic, OnClinicClickListener listener) {
            // Clinic name
            tvClinicName.setText(clinic.name);

            // Specialty
            tvSpecialty.setText(clinic.specialties != null ? clinic.specialties : "Khám tổng quát");

            // Address
            tvAddress.setText(clinic.address != null ? clinic.address : "Địa chỉ không có sẵn");

            // Distance (if available)
            if (clinic.distance > 0) {
                tvDistance.setText(String.format("%.1f km", clinic.distance));
                tvDistance.setVisibility(View.VISIBLE);
            } else {
                tvDistance.setVisibility(View.GONE);
            }

            // Rating
            ratingBar.setRating((float) clinic.rating);
            tvRating.setText(String.format("%.1f", clinic.rating));
            tvReviewCount.setText(String.format("(%d reviews)", clinic.totalReviews));

            // Open status
            boolean isOpen = isClinicOpen(clinic);
            tvOpenStatus.setText(isOpen ? "Đang mở cửa" : "Đã đóng cửa");
            tvOpenStatus.setTextColor(itemView.getContext().getColor(
                    isOpen ? R.color.success_green : R.color.error_red
            ));

            // Clinic image (use default for now)
            ivClinicImage.setImageResource(R.drawable.default_clinic_image);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClinicClick(clinic);
                }
            });

            btnBookAppointment.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookAppointmentClick(clinic);
                }
            });
        }

        private boolean isClinicOpen(ClinicSearchManager.ClinicSearchResult clinic) {
            // Get current day and time
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String currentTime = String.format("%02d:%02d", hour, minute);

            // Convert day of week to string
            String[] days = {"", "sun", "mon", "tue", "wed", "thu", "fri", "sat"};
            String currentDay = days[dayOfWeek];

            // Check if clinic is open
            return clinic.isOpen(currentDay, currentTime);
        }
    }
}
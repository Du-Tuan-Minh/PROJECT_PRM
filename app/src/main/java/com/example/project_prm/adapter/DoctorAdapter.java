package com.example.project_prm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm.Model.DoctorModel;
import com.example.project_prm.R;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private List<DoctorModel> doctors;
    private Context context;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(DoctorModel doctor);
    }

    public DoctorAdapter(Context context, List<DoctorModel> doctors, OnDoctorClickListener listener) {
        this.context = context;
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorModel doctor = doctors.get(position);
        holder.bind(doctor);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public void updateDoctors(List<DoctorModel> newDoctors) {
        this.doctors = newDoctors;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDoctorImage;
        private TextView tvDoctorName;
        private TextView tvDoctorSpecialty;
        private TextView tvDoctorLocation;
        private TextView tvDoctorRating;
        private TextView tvDoctorReviewCount;
        // Không có tvConsultationFee và ratingBar trong layout

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDoctorImage = itemView.findViewById(R.id.ivDoctorImage);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialty = itemView.findViewById(R.id.tvDoctorSpecialty);
            tvDoctorLocation = itemView.findViewById(R.id.tvDoctorLocation);
            tvDoctorRating = itemView.findViewById(R.id.tvDoctorRating);
            tvDoctorReviewCount = itemView.findViewById(R.id.tvDoctorReviewCount);
            // Không có tvConsultationFee và ratingBar

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDoctorClick(doctors.get(position));
                }
            });
        }

        public void bind(DoctorModel doctor) {
            tvDoctorName.setText(doctor.getName());
            tvDoctorSpecialty.setText(doctor.getSpecialty());
            tvDoctorLocation.setText(doctor.getLocation());
            tvDoctorRating.setText(String.format("%.1f", doctor.getRating()));
            tvDoctorReviewCount.setText("(" + doctor.getReviewCount() + " đánh giá)");
            // Load doctor image using Glide
            if (doctor.getImage() != null && !doctor.getImage().isEmpty()) {
                Glide.with(context)
                        .load(doctor.getImage())
                        .placeholder(R.drawable.ic_doctor)
                        .error(R.drawable.ic_doctor)
                        .into(ivDoctorImage);
            } else {
                ivDoctorImage.setImageResource(R.drawable.ic_doctor);
            }
        }
    }
} 
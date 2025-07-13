package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;
import java.util.List;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ClinicViewHolder> {
    
    private List<ClinicModel> clinics;
    private OnClinicClickListener listener;
    
    public interface OnClinicClickListener {
        void onClinicClick(ClinicModel clinic);
        void onCallClick(ClinicModel clinic);
        void onDirectionClick(ClinicModel clinic);
    }
    
    public ClinicAdapter(List<ClinicModel> clinics, OnClinicClickListener listener) {
        this.clinics = clinics;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false);
        return new ClinicViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        holder.bind(clinics.get(position));
    }
    
    @Override
    public int getItemCount() {
        return clinics.size();
    }
    
    public void updateClinics(List<ClinicModel> newClinics) {
        this.clinics = newClinics;
        notifyDataSetChanged();
    }
    
    class ClinicViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivClinicImage;
        private TextView tvClinicName, tvAddress, tvSpecialties, tvRating, tvReviewCount, tvDistance, tvWorkingHours;
        private MaterialButton btnCall, btnDirection;
        private View layoutOpenStatus;
        
        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivClinicImage = itemView.findViewById(R.id.ivClinicImage);
            tvClinicName = itemView.findViewById(R.id.tvClinicName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvSpecialties = itemView.findViewById(R.id.tvSpecialties);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvWorkingHours = itemView.findViewById(R.id.tvWorkingHours);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnDirection = itemView.findViewById(R.id.btnDirection);
            layoutOpenStatus = itemView.findViewById(R.id.layoutOpenStatus);
        }
        
        public void bind(ClinicModel clinic) {
            // Set basic info
            tvClinicName.setText(clinic.getName());
            tvAddress.setText(clinic.getAddress());
            tvSpecialties.setText(clinic.getSpecialties());
            tvRating.setText(clinic.getFormattedRating());
            tvReviewCount.setText(clinic.getFormattedReviewCount());
            tvDistance.setText(clinic.getFormattedDistance());
            tvWorkingHours.setText(clinic.getWorkingHours());
            
            // Load clinic image
            if (clinic.getImageUrl() != null && !clinic.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                    .load(clinic.getImageUrl())
                    .placeholder(R.drawable.ic_general)
                    .error(R.drawable.ic_general)
                    .into(ivClinicImage);
            } else {
                ivClinicImage.setImageResource(R.drawable.ic_general);
            }
            
            // Set open/closed status
            if (clinic.isOpen()) {
                layoutOpenStatus.setBackgroundResource(R.drawable.bg_circle_green);
            } else {
                layoutOpenStatus.setBackgroundResource(R.drawable.bg_circle_red);
            }
            
            // Setup click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClinicClick(clinic);
                }
            });
            
            btnCall.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCallClick(clinic);
                }
            });
            
            btnDirection.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDirectionClick(clinic);
                }
            });
        }
    }
} 
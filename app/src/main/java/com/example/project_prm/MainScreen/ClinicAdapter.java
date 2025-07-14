package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clinic, parent, false);
        return new ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        ClinicModel clinic = clinics.get(position);
        holder.bind(clinic);
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
        private TextView tvClinicName, tvClinicAddress, tvClinicRating, tvClinicReviewCount;
        private TextView tvClinicSpecialties, tvClinicWorkingHours;
        private ImageView ivCall, ivDirection;

        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClinicImage = itemView.findViewById(R.id.ivClinicImage);
            tvClinicName = itemView.findViewById(R.id.tvClinicName);
            tvClinicAddress = itemView.findViewById(R.id.tvClinicAddress);
            tvClinicRating = itemView.findViewById(R.id.tvClinicRating);
            tvClinicReviewCount = itemView.findViewById(R.id.tvClinicReviewCount);
            tvClinicSpecialties = itemView.findViewById(R.id.tvClinicSpecialties);
            tvClinicWorkingHours = itemView.findViewById(R.id.tvClinicWorkingHours);
            ivCall = itemView.findViewById(R.id.ivCall);
            ivDirection = itemView.findViewById(R.id.ivDirection);
        }

        public void bind(ClinicModel clinic) {
            tvClinicName.setText(clinic.getName());
            tvClinicAddress.setText(clinic.getAddress());
            tvClinicRating.setText(String.format("%.1f", clinic.getRating()));
            tvClinicReviewCount.setText("(" + clinic.getReviewCount() + " đánh giá)");
            tvClinicSpecialties.setText(clinic.getSpecialties());
            tvClinicWorkingHours.setText(clinic.getWorkingHours());

            // Load clinic image
            Glide.with(itemView.getContext())
                    .load(R.drawable.ic_general) // Default image
                    .into(ivClinicImage);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClinicClick(clinic);
                }
            });

            ivCall.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCallClick(clinic);
                }
            });

            ivDirection.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDirectionClick(clinic);
                }
            });
        }
    }
} 
package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    
    private List<PackageModel> packages;
    private OnPackageClickListener listener;
    private int selectedPosition = -1;

    public interface OnPackageClickListener {
        void onPackageClick(PackageModel packageModel);
    }

    public PackageAdapter(List<PackageModel> packages, OnPackageClickListener listener) {
        this.packages = packages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        PackageModel packageModel = packages.get(position);
        holder.bind(packageModel, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    class PackageViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDescription, tvAmount, tvDuration;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }

        public void bind(PackageModel packageModel, boolean isSelected) {
            tvName.setText(packageModel.getName());
            tvDescription.setText(packageModel.getDescription());
            tvAmount.setText(packageModel.getFormattedPrice());
            tvDuration.setText(packageModel.getDuration());
            
            if (isSelected) {
                itemView.setBackgroundResource(R.drawable.bg_filter_selected);
            } else {
                itemView.setBackgroundResource(R.drawable.bg_filter_unselected);
            }

            itemView.setOnClickListener(v -> {
                int previousSelected = selectedPosition;
                selectedPosition = getAdapterPosition();
                
                // Update previous selection
                if (previousSelected != -1) {
                    notifyItemChanged(previousSelected);
                }
                
                // Update new selection
                notifyItemChanged(selectedPosition);
                
                if (listener != null) {
                    listener.onPackageClick(packageModel);
                }
            });
        }
    }
} 
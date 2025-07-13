package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {
    
    private List<EnhancedDiseaseModel> diseases;
    private OnDiseaseClickListener listener;
    
    public interface OnDiseaseClickListener {
        void onDiseaseClick(EnhancedDiseaseModel disease);
        void onBookmarkClick(EnhancedDiseaseModel disease);
    }
    
    public DiseaseAdapter(List<EnhancedDiseaseModel> diseases, OnDiseaseClickListener listener) {
        this.diseases = diseases;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        EnhancedDiseaseModel disease = diseases.get(position);
        holder.bind(disease);
    }
    
    @Override
    public int getItemCount() {
        return diseases.size();
    }
    
    public void updateDiseases(List<EnhancedDiseaseModel> newDiseases) {
        this.diseases = newDiseases;
        notifyDataSetChanged();
    }
    
    class DiseaseViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvDiseaseName, tvEnglishName, tvCategory, tvDescription, tvSeverity;
        private ImageView ivBookmark, ivSeverityIcon;
        
        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvDiseaseName = itemView.findViewById(R.id.tvDiseaseName);
            tvEnglishName = itemView.findViewById(R.id.tvEnglishName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
            ivBookmark = itemView.findViewById(R.id.ivBookmark);
            ivSeverityIcon = itemView.findViewById(R.id.ivSeverityIcon);
        }
        
        public void bind(EnhancedDiseaseModel disease) {
            tvDiseaseName.setText(disease.name);
            tvEnglishName.setText(disease.englishName);
            tvCategory.setText(disease.category);
            tvDescription.setText(disease.description);
            tvSeverity.setText(disease.getSeverityText());
            
            // Set bookmark icon
            ivBookmark.setImageResource(disease.isBookmarked ? 
                R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
            
            // Set severity color
            tvSeverity.setTextColor(android.graphics.Color.parseColor(disease.getSeverityColor()));
            
            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDiseaseClick(disease);
                }
            });
            
            ivBookmark.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookmarkClick(disease);
                }
            });
        }
    }
} 
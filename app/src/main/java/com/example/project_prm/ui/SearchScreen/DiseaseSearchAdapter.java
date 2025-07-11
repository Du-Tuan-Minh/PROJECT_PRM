// NEW FILE: app/src/main/java/com/example/project_prm/ui/SearchScreen/DiseaseSearchAdapter.java
package com.example.project_prm.ui.SearchScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Disease;
import com.example.project_prm.R;

import java.util.List;

public class DiseaseSearchAdapter extends RecyclerView.Adapter<DiseaseSearchAdapter.DiseaseViewHolder> {

    private final List<Disease> diseaseList;
    private final OnDiseaseClickListener listener;

    public interface OnDiseaseClickListener {
        void onDiseaseClick(Disease disease);
    }

    public DiseaseSearchAdapter(List<Disease> diseaseList, OnDiseaseClickListener listener) {
        this.diseaseList = diseaseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease_search, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease disease = diseaseList.get(position);
        holder.bind(disease, listener);
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDiseaseName;
        private final TextView tvDiseaseSymptoms;
        private final TextView tvDiseaseCauses;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiseaseName = itemView.findViewById(R.id.tv_disease_name);
            tvDiseaseSymptoms = itemView.findViewById(R.id.tv_disease_symptoms);
            tvDiseaseCauses = itemView.findViewById(R.id.tv_disease_causes);
        }

        public void bind(Disease disease, OnDiseaseClickListener listener) {
            tvDiseaseName.setText(disease.getName());

            // Show symptoms preview (first 100 characters)
            String symptoms = disease.getSymptoms();
            if (symptoms != null && symptoms.length() > 100) {
                symptoms = symptoms.substring(0, 100) + "...";
            }
            tvDiseaseSymptoms.setText("Triệu chứng: " + (symptoms != null ? symptoms : "Không có thông tin"));

            // Show causes preview (first 80 characters)
            String causes = disease.getCauses();
            if (causes != null && causes.length() > 80) {
                causes = causes.substring(0, 80) + "...";
            }
            tvDiseaseCauses.setText("Nguyên nhân: " + (causes != null ? causes : "Không có thông tin"));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDiseaseClick(disease);
                }
            });
        }
    }
}
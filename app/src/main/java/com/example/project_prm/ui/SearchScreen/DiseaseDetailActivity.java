// File: app/src/main/java/com/example/project_prm/ui/SearchScreen/DiseaseDetailActivity.java
package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Disease;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class DiseaseDetailActivity extends AppCompatActivity {

    private TextView tvDiseaseName, tvDiseaseSymptoms, tvDiseaseCauses, tvDiseaseTreatment;
    private RecyclerView rvRelatedDiseases;
    private MaterialButton btnFindClinics;
    private View progressBar;

    private Disease currentDisease;
    private RelatedDiseaseAdapter relatedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        // Get disease data from intent
        int diseaseId = getIntent().getIntExtra("disease_id", -1);
        String diseaseName = getIntent().getStringExtra("disease_name");

        if (diseaseId == -1) {
            Toast.makeText(this, "Invalid disease data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadDiseaseDetails(diseaseId, diseaseName);
        setupClickListeners();
    }

    private void initViews() {
        tvDiseaseName = findViewById(R.id.tv_disease_name);
        tvDiseaseSymptoms = findViewById(R.id.tv_disease_symptoms);
        tvDiseaseCauses = findViewById(R.id.tv_disease_causes);
        tvDiseaseTreatment = findViewById(R.id.tv_disease_treatment);
        rvRelatedDiseases = findViewById(R.id.rv_related_diseases);
        btnFindClinics = findViewById(R.id.btn_find_clinics);
        progressBar = findViewById(R.id.progress_bar);

        // Setup back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        btnFindClinics.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClinicSearchActivity.class);
            intent.putExtra("disease_name", currentDisease != null ? currentDisease.getName() : "");
            startActivity(intent);
        });
    }

    private void loadDiseaseDetails(int diseaseId, String diseaseName) {
        showProgressBar(true);

        // Mock disease details
        new android.os.Handler().postDelayed(() -> {
            currentDisease = mockGetDiseaseDetails(diseaseId, diseaseName);
            displayDiseaseDetails(currentDisease);
            loadRelatedDiseases(diseaseId);
            showProgressBar(false);
        }, 500);
    }

    private void displayDiseaseDetails(Disease disease) {
        tvDiseaseName.setText(disease.getName());
        tvDiseaseSymptoms.setText("Triệu chứng: " + disease.getSymptoms());
        tvDiseaseCauses.setText("Nguyên nhân: " + disease.getCauses());
        tvDiseaseTreatment.setText("Điều trị: " + disease.getTreatment());
    }

    private void loadRelatedDiseases(int diseaseId) {
        List<Disease> relatedDiseases = mockGetRelatedDiseases(diseaseId);

        relatedAdapter = new RelatedDiseaseAdapter(relatedDiseases, this::onRelatedDiseaseClick);
        rvRelatedDiseases.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRelatedDiseases.setAdapter(relatedAdapter);
    }

    private void onRelatedDiseaseClick(Disease disease) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra("disease_id", disease.getId());
        intent.putExtra("disease_name", disease.getName());
        startActivity(intent);
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Mock data methods
    private Disease mockGetDiseaseDetails(int diseaseId, String diseaseName) {
        switch (diseaseId) {
            case 1:
                return new Disease(1, "Cảm cúm",
                        "Sốt, đau đầu, ho, chảy nước mũi, đau cơ",
                        "Virus cúm A, B hoặc C",
                        "Nghỉ ngơi, uống nhiều nước, thuốc hạ sốt");
            case 2:
                return new Disease(2, "Đau đầu",
                        "Đau âm ỉ hoặc nhói ở vùng đầu, có thể kèm buồn nôn",
                        "Căng thẳng, thiếu ngủ, tăng huyết áp",
                        "Nghỉ ngơi, massage, thuốc giảm đau");
            case 3:
                return new Disease(3, "Đau bụng",
                        "Đau ở vùng bụng, có thể kèm buồn nôn, tiêu chảy",
                        "Ăn uống không hợp lý, nhiễm khuẩn tiêu hóa",
                        "Ăn nhẹ, uống nhiều nước, thuốc kháng sinh nếu cần");
            default:
                return new Disease(diseaseId, diseaseName != null ? diseaseName : "Unknown",
                        "Triệu chứng chưa xác định",
                        "Nguyên nhân chưa xác định",
                        "Cần thăm khám để xác định");
        }
    }

    private List<Disease> mockGetRelatedDiseases(int diseaseId) {
        List<Disease> related = new ArrayList<>();

        if (diseaseId == 1) { // Cảm cúm
            related.add(new Disease(4, "Viêm họng", "Đau rát cổ họng", "Virus, vi khuẩn"));
            related.add(new Disease(5, "Viêm xoang", "Đau đầu, nghẹt mũi", "Nhiễm trùng xoang"));
        } else if (diseaseId == 2) { // Đau đầu
            related.add(new Disease(6, "Migraine", "Đau đầu dữ dội", "Rối loạn thần kinh"));
            related.add(new Disease(7, "Căng thẳng", "Stress, lo âu", "Áp lực cuộc sống"));
        } else if (diseaseId == 3) { // Đau bụng
            related.add(new Disease(8, "Viêm dạ dày", "Đau thượng vị", "H.pylori, stress"));
            related.add(new Disease(9, "Rối loạn tiêu hóa", "Khó tiêu", "Chế độ ăn không tốt"));
        }

        return related;
    }

    // Related disease adapter
    private static class RelatedDiseaseAdapter extends RecyclerView.Adapter<RelatedDiseaseAdapter.ViewHolder> {
        private List<Disease> diseases;
        private OnDiseaseClickListener listener;

        interface OnDiseaseClickListener {
            void onDiseaseClick(Disease disease);
        }

        public RelatedDiseaseAdapter(List<Disease> diseases, OnDiseaseClickListener listener) {
            this.diseases = diseases;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.widget.LinearLayout layout = new android.widget.LinearLayout(parent.getContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(12, 12, 12, 12);
            layout.setMinimumWidth(150);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Disease disease = diseases.get(position);
            holder.bind(disease, listener);
        }

        @Override
        public int getItemCount() {
            return diseases.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private android.widget.TextView tvName;

            public ViewHolder(android.view.View itemView) {
                super(itemView);
                android.widget.LinearLayout layout = (android.widget.LinearLayout) itemView;

                tvName = new android.widget.TextView(itemView.getContext());
                tvName.setTextSize(14);
                tvName.setTextColor(0xFF2196F3);
                tvName.setPadding(8, 8, 8, 8);
                layout.addView(tvName);
            }

            public void bind(Disease disease, OnDiseaseClickListener listener) {
                tvName.setText(disease.getName());

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDiseaseClick(disease);
                    }
                });
            }
        }
    }
}
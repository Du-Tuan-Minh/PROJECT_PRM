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

/**
 * Activity hiển thị chi tiết bệnh
 * Chức năng 6: Search - Disease Detail
 * Hiển thị triệu chứng, nguyên nhân, điều trị và bệnh liên quan
 */
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
            try {
                Intent intent = new Intent();
                intent.setClassName(this, "com.example.project_prm.ui.SearchScreen.ClinicSearchActivity");
                intent.putExtra("disease_name", currentDisease != null ? currentDisease.getName() : "");
                intent.putExtra("search_specialty", getSpecialtyFromDisease(currentDisease));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Chức năng tìm phòng khám đang được phát triển", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDiseaseDetails(int diseaseId, String diseaseName) {
        showProgressBar(true);

        // Mock disease details - simulate async loading
        new android.os.Handler().postDelayed(() -> {
            currentDisease = mockGetDiseaseDetails(diseaseId, diseaseName);
            displayDiseaseDetails(currentDisease);
            loadRelatedDiseases(diseaseId);
            showProgressBar(false);
        }, 500);
    }

    private void displayDiseaseDetails(Disease disease) {
        if (tvDiseaseName != null) {
            tvDiseaseName.setText(disease.getName());
        }

        if (tvDiseaseSymptoms != null) {
            tvDiseaseSymptoms.setText("Triệu chứng: " + disease.getSymptoms());
        }

        if (tvDiseaseCauses != null) {
            tvDiseaseCauses.setText("Nguyên nhân: " + disease.getCauses());
        }

        if (tvDiseaseTreatment != null) {
            tvDiseaseTreatment.setText("Điều trị: " + disease.getTreatment());
        }
    }

    private void loadRelatedDiseases(int diseaseId) {
        List<Disease> relatedDiseases = mockGetRelatedDiseases(diseaseId);

        relatedAdapter = new RelatedDiseaseAdapter(relatedDiseases, this::onRelatedDiseaseClick);

        if (rvRelatedDiseases != null) {
            rvRelatedDiseases.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvRelatedDiseases.setAdapter(relatedAdapter);
        }
    }

    private void onRelatedDiseaseClick(Disease disease) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra("disease_id", disease.getId());
        intent.putExtra("disease_name", disease.getName());
        startActivity(intent);
    }

    private void showProgressBar(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private String getSpecialtyFromDisease(Disease disease) {
        if (disease == null) return "general";

        String name = disease.getName().toLowerCase();
        if (name.contains("da") || name.contains("skin")) {
            return "dermatology";
        } else if (name.contains("tim") || name.contains("heart")) {
            return "cardiology";
        } else if (name.contains("thần kinh") || name.contains("neuro")) {
            return "neurology";
        } else if (name.contains("nhi") || name.contains("trẻ em")) {
            return "pediatrics";
        } else {
            return "general";
        }
    }

    // FIXED: Mock data methods using setters instead of constructor
    private Disease mockGetDiseaseDetails(int diseaseId, String diseaseName) {
        Disease disease = new Disease();

        switch (diseaseId) {
            case 1:
                disease.setId(1);
                disease.setName("Cảm cúm");
                disease.setSymptoms("Sốt, đau đầu, ho, chảy nước mũi, đau cơ");
                disease.setCauses("Virus cúm A, B hoặc C");
                disease.setTreatment("Nghỉ ngơi, uống nhiều nước, thuốc hạ sốt");
                break;
            case 2:
                disease.setId(2);
                disease.setName("Đau đầu");
                disease.setSymptoms("Đau âm ỉ hoặc nhói ở vùng đầu, có thể kèm buồn nôn");
                disease.setCauses("Căng thẳng, thiếu ngủ, tăng huyết áp");
                disease.setTreatment("Nghỉ ngơi, massage, thuốc giảm đau");
                break;
            case 3:
                disease.setId(3);
                disease.setName("Đau bụng");
                disease.setSymptoms("Đau ở vùng bụng, có thể kèm buồn nôn, tiêu chảy");
                disease.setCauses("Ăn uống không hợp lý, nhiễm khuẩn tiêu hóa");
                disease.setTreatment("Ăn nhẹ, uống nhiều nước, thuốc kháng sinh nếu cần");
                break;
            case 4:
                disease.setId(4);
                disease.setName("Viêm họng");
                disease.setSymptoms("Đau rát cổ họng, khó nuốt");
                disease.setCauses("Virus, vi khuẩn");
                disease.setTreatment("Súc miệng nước muối, thuốc kháng sinh");
                break;
            case 5:
                disease.setId(5);
                disease.setName("Viêm xoang");
                disease.setSymptoms("Đau đầu, nghẹt mũi, chảy nước mũi");
                disease.setCauses("Nhiễm trùng xoang");
                disease.setTreatment("Xịt mũi, thuốc kháng sinh");
                break;
            default:
                disease.setId(diseaseId);
                disease.setName(diseaseName != null ? diseaseName : "Bệnh không xác định");
                disease.setSymptoms("Triệu chứng chưa xác định");
                disease.setCauses("Nguyên nhân chưa xác định");
                disease.setTreatment("Cần thăm khám để xác định phương pháp điều trị phù hợp");
                break;
        }

        return disease;
    }

    private List<Disease> mockGetRelatedDiseases(int diseaseId) {
        List<Disease> related = new ArrayList<>();

        if (diseaseId == 1) { // Cảm cúm
            Disease disease1 = new Disease();
            disease1.setId(4);
            disease1.setName("Viêm họng");
            disease1.setSymptoms("Đau rát cổ họng");
            disease1.setCauses("Virus, vi khuẩn");
            disease1.setTreatment("Súc miệng nước muối");
            related.add(disease1);

            Disease disease2 = new Disease();
            disease2.setId(5);
            disease2.setName("Viêm xoang");
            disease2.setSymptoms("Đau đầu, nghẹt mũi");
            disease2.setCauses("Nhiễm trùng xoang");
            disease2.setTreatment("Xịt mũi, thuốc kháng sinh");
            related.add(disease2);

        } else if (diseaseId == 2) { // Đau đầu
            Disease disease1 = new Disease();
            disease1.setId(6);
            disease1.setName("Migraine");
            disease1.setSymptoms("Đau đầu dữ dội");
            disease1.setCauses("Rối loạn thần kinh");
            disease1.setTreatment("Thuốc chuyên biệt cho migraine");
            related.add(disease1);

            Disease disease2 = new Disease();
            disease2.setId(7);
            disease2.setName("Căng thẳng");
            disease2.setSymptoms("Stress, lo âu");
            disease2.setCauses("Áp lực cuộc sống");
            disease2.setTreatment("Thư giãn, tập thể dục");
            related.add(disease2);

        } else if (diseaseId == 3) { // Đau bụng
            Disease disease1 = new Disease();
            disease1.setId(8);
            disease1.setName("Viêm dạ dày");
            disease1.setSymptoms("Đau thượng vị");
            disease1.setCauses("H.pylori, stress");
            disease1.setTreatment("Thuốc kháng acid");
            related.add(disease1);

            Disease disease2 = new Disease();
            disease2.setId(9);
            disease2.setName("Rối loạn tiêu hóa");
            disease2.setSymptoms("Khó tiêu");
            disease2.setCauses("Chế độ ăn không tốt");
            disease2.setTreatment("Thay đổi chế độ ăn");
            related.add(disease2);
        }

        return related;
    }

    // FIXED: Related disease adapter with proper ViewHolder
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
            // Create a simple card view programmatically
            android.widget.LinearLayout layout = new android.widget.LinearLayout(parent.getContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);
            layout.setMinimumWidth(200);

            // Add background and elevation
            try {
                layout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            } catch (Exception e) {
                // Fallback if resource not available
            }

            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Disease disease = diseases.get(position);
            holder.bind(disease, listener);
        }

        @Override
        public int getItemCount() {
            return diseases != null ? diseases.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private android.widget.TextView tvName;
            private android.widget.TextView tvSymptoms;

            public ViewHolder(android.view.View itemView) {
                super(itemView);
                android.widget.LinearLayout layout = (android.widget.LinearLayout) itemView;

                // Disease name
                tvName = new android.widget.TextView(itemView.getContext());
                tvName.setTextSize(16);
                tvName.setTextColor(0xFF2196F3);
                tvName.setPadding(8, 8, 8, 4);
                tvName.setTypeface(null, android.graphics.Typeface.BOLD);
                layout.addView(tvName);

                // Disease symptoms (brief)
                tvSymptoms = new android.widget.TextView(itemView.getContext());
                tvSymptoms.setTextSize(12);
                tvSymptoms.setTextColor(0xFF666666);
                tvSymptoms.setPadding(8, 0, 8, 8);
                tvSymptoms.setMaxLines(2);
                layout.addView(tvSymptoms);
            }

            public void bind(Disease disease, OnDiseaseClickListener listener) {
                tvName.setText(disease.getName());

                String symptoms = disease.getSymptoms();
                if (symptoms != null && symptoms.length() > 50) {
                    symptoms = symptoms.substring(0, 47) + "...";
                }
                tvSymptoms.setText(symptoms);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDiseaseClick(disease);
                    }
                });
            }
        }
    }
}
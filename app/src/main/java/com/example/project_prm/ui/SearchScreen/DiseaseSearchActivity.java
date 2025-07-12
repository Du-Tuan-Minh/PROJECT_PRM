// File: app/src/main/java/com/example/project_prm/ui/SearchScreen/DiseaseSearchActivity.java
package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Disease;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity tìm kiếm bệnh theo triệu chứng
 * Chức năng 6: Search - Disease Search by Symptoms
 * Hiển thị danh sách bệnh phổ biến và kết quả tìm kiếm
 */
public class DiseaseSearchActivity extends AppCompatActivity {

    private TextInputEditText etSearchDisease;
    private RecyclerView rvSearchResults;
    private View emptyStateView;
    private View progressBar;

    private DiseaseSearchAdapter adapter;
    private List<Disease> diseaseList;
    private HealthcareService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_search);

        initViews();
        setupRecyclerView();
        setupSearchListener();
        loadPopularDiseases();
    }

    private void initViews() {
        etSearchDisease = findViewById(R.id.et_search_disease);
        rvSearchResults = findViewById(R.id.rv_search_results);
        emptyStateView = findViewById(R.id.empty_state_view);
        progressBar = findViewById(R.id.progress_bar);

        service = HealthcareService.getInstance(this);
        diseaseList = new ArrayList<>();

        // Setup back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new DiseaseSearchAdapter(diseaseList, this::onDiseaseClick);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchListener() {
        etSearchDisease.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    searchDiseases(query);
                } else if (query.isEmpty()) {
                    loadPopularDiseases();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchDiseases(String query) {
        showProgressBar(true);

        // Mock search implementation - simulate async search
        List<Disease> results = mockSearchDiseases(query);

        new android.os.Handler().postDelayed(() -> {
            showProgressBar(false);
            updateSearchResults(results);
        }, 500);
    }

    private void loadPopularDiseases() {
        showProgressBar(true);

        // Mock popular diseases - simulate loading
        List<Disease> popularDiseases = mockGetPopularDiseases();

        new android.os.Handler().postDelayed(() -> {
            showProgressBar(false);
            updateSearchResults(popularDiseases);
        }, 300);
    }

    private void updateSearchResults(List<Disease> results) {
        diseaseList.clear();
        if (results != null) {
            diseaseList.addAll(results);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (diseaseList.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
        }
    }

    private void onDiseaseClick(Disease disease) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra("disease_id", disease.getId());
        intent.putExtra("disease_name", disease.getName());
        startActivity(intent);
    }

    private void showProgressBar(boolean show) {
        if (progressBar != null && rvSearchResults != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showEmptyState(boolean show) {
        if (emptyStateView != null && rvSearchResults != null) {
            emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
            rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    // FIXED: Mock data methods using setters instead of constructor with parameters
    private List<Disease> mockSearchDiseases(String query) {
        List<Disease> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        if (lowerQuery.contains("đau")) {
            // Đau đầu
            Disease disease1 = new Disease();
            disease1.setId(1);
            disease1.setName("Đau đầu");
            disease1.setSymptoms("Triệu chứng đau ở vùng đầu, có thể kèm buồn nôn");
            disease1.setCauses("Căng thẳng, mệt mỏi, thiếu ngủ");
            disease1.setTreatment("Nghỉ ngơi, massage, thuốc giảm đau");
            results.add(disease1);

            // Đau bụng
            Disease disease2 = new Disease();
            disease2.setId(2);
            disease2.setName("Đau bụng");
            disease2.setSymptoms("Triệu chứng đau ở vùng bụng, có thể kèm buồn nôn");
            disease2.setCauses("Tiêu hóa kém, ăn uống không hợp lý");
            disease2.setTreatment("Ăn nhẹ, uống nhiều nước");
            results.add(disease2);

        } else if (lowerQuery.contains("sốt")) {
            Disease disease = new Disease();
            disease.setId(3);
            disease.setName("Sốt virus");
            disease.setSymptoms("Nhiệt độ cơ thể tăng cao, có thể kèm đau đầu");
            disease.setCauses("Nhiễm virus");
            disease.setTreatment("Nghỉ ngơi, uống nhiều nước, hạ sốt");
            results.add(disease);

        } else if (lowerQuery.contains("ho")) {
            // Ho khan
            Disease disease1 = new Disease();
            disease1.setId(4);
            disease1.setName("Ho khan");
            disease1.setSymptoms("Ho không có đờm, cổ họng khô");
            disease1.setCauses("Dị ứng, viêm họng, không khí khô");
            disease1.setTreatment("Uống nhiều nước, súc miệng nước muối");
            results.add(disease1);

            // Ho có đờm
            Disease disease2 = new Disease();
            disease2.setId(5);
            disease2.setName("Ho có đờm");
            disease2.setSymptoms("Ho kèm đờm, có thể có máu");
            disease2.setCauses("Nhiễm khuẩn đường hô hấp");
            disease2.setTreatment("Thuốc long đờm, kháng sinh nếu cần");
            results.add(disease2);

        } else if (lowerQuery.contains("cảm") || lowerQuery.contains("cúm")) {
            Disease disease = new Disease();
            disease.setId(6);
            disease.setName("Cảm cúm");
            disease.setSymptoms("Sốt, ho, chảy nước mũi, đau cơ");
            disease.setCauses("Virus cúm A, B, C");
            disease.setTreatment("Nghỉ ngơi, uống nhiều nước, thuốc hạ sốt");
            results.add(disease);

        } else if (lowerQuery.contains("viêm")) {
            Disease disease = new Disease();
            disease.setId(7);
            disease.setName("Viêm họng");
            disease.setSymptoms("Đau rát cổ họng, khó nuốt");
            disease.setCauses("Virus, vi khuẩn");
            disease.setTreatment("Súc miệng nước muối, thuốc kháng sinh");
            results.add(disease);
        }

        return results;
    }

    private List<Disease> mockGetPopularDiseases() {
        List<Disease> diseases = new ArrayList<>();

        // Cảm cúm
        Disease disease1 = new Disease();
        disease1.setId(1);
        disease1.setName("Cảm cúm");
        disease1.setSymptoms("Bệnh nhiễm virus phổ biến gây sốt, ho, chảy nước mũi");
        disease1.setCauses("Virus cúm");
        disease1.setTreatment("Nghỉ ngơi, uống nhiều nước");
        diseases.add(disease1);

        // Đau đầu
        Disease disease2 = new Disease();
        disease2.setId(2);
        disease2.setName("Đau đầu");
        disease2.setSymptoms("Triệu chứng đau ở vùng đầu");
        disease2.setCauses("Căng thẳng, thiếu ngủ");
        disease2.setTreatment("Nghỉ ngơi, massage");
        diseases.add(disease2);

        // Đau bụng
        Disease disease3 = new Disease();
        disease3.setId(3);
        disease3.setName("Đau bụng");
        disease3.setSymptoms("Triệu chứng đau ở vùng bụng");
        disease3.setCauses("Tiêu hóa kém");
        disease3.setTreatment("Ăn nhẹ, uống nhiều nước");
        diseases.add(disease3);

        // Sốt
        Disease disease4 = new Disease();
        disease4.setId(4);
        disease4.setName("Sốt");
        disease4.setSymptoms("Nhiệt độ cơ thể tăng cao");
        disease4.setCauses("Nhiễm trùng");
        disease4.setTreatment("Hạ sốt, nghỉ ngơi");
        diseases.add(disease4);

        // Ho
        Disease disease5 = new Disease();
        disease5.setId(5);
        disease5.setName("Ho");
        disease5.setSymptoms("Phản xạ tự nhiên của cơ thể để làm sạch đường thở");
        disease5.setCauses("Dị ứng, nhiễm trùng");
        disease5.setTreatment("Uống nhiều nước, thuốc ho");
        diseases.add(disease5);

        // Viêm họng
        Disease disease6 = new Disease();
        disease6.setId(6);
        disease6.setName("Viêm họng");
        disease6.setSymptoms("Đau rát cổ họng, khó nuốt");
        disease6.setCauses("Virus, vi khuẩn");
        disease6.setTreatment("Súc miệng nước muối");
        diseases.add(disease6);

        return diseases;
    }

    // FIXED: Simple adapter class with proper ViewHolder
    private static class DiseaseSearchAdapter extends RecyclerView.Adapter<DiseaseSearchAdapter.ViewHolder> {
        private List<Disease> diseases;
        private OnDiseaseClickListener listener;

        interface OnDiseaseClickListener {
            void onDiseaseClick(Disease disease);
        }

        public DiseaseSearchAdapter(List<Disease> diseases, OnDiseaseClickListener listener) {
            this.diseases = diseases;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            // Create a card-like layout programmatically
            android.widget.LinearLayout layout = new android.widget.LinearLayout(parent.getContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(16, 12, 16, 12);

            // Add margin between items
            android.widget.LinearLayout.LayoutParams layoutParams = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 8);
            layout.setLayoutParams(layoutParams);

            // Add background
            try {
                layout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            } catch (Exception e) {
                // Fallback if resource not available
                layout.setBackgroundColor(0xFFF5F5F5);
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
            private android.widget.TextView tvName, tvDescription, tvCauses;

            public ViewHolder(android.view.View itemView) {
                super(itemView);
                android.widget.LinearLayout layout = (android.widget.LinearLayout) itemView;

                // Disease name
                tvName = new android.widget.TextView(itemView.getContext());
                tvName.setTextSize(18);
                tvName.setTextColor(0xFF2196F3);
                tvName.setTypeface(null, android.graphics.Typeface.BOLD);
                tvName.setPadding(0, 0, 0, 4);
                layout.addView(tvName);

                // Disease symptoms
                tvDescription = new android.widget.TextView(itemView.getContext());
                tvDescription.setTextSize(14);
                tvDescription.setTextColor(0xFF444444);
                tvDescription.setPadding(0, 0, 0, 4);
                tvDescription.setMaxLines(2);
                layout.addView(tvDescription);

                // Disease causes
                tvCauses = new android.widget.TextView(itemView.getContext());
                tvCauses.setTextSize(12);
                tvCauses.setTextColor(0xFF666666);
                tvCauses.setPadding(0, 0, 0, 0);
                layout.addView(tvCauses);
            }

            public void bind(Disease disease, OnDiseaseClickListener listener) {
                tvName.setText(disease.getName());

                String symptoms = disease.getSymptoms();
                if (symptoms != null && symptoms.length() > 80) {
                    symptoms = symptoms.substring(0, 77) + "...";
                }
                tvDescription.setText(symptoms);

                String causes = disease.getCauses();
                if (causes != null) {
                    tvCauses.setText("Nguyên nhân: " + causes);
                } else {
                    tvCauses.setVisibility(android.view.View.GONE);
                }

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDiseaseClick(disease);
                    }
                });
            }
        }
    }
}
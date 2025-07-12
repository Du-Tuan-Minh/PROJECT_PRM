// File: app/src/main/java/com/example/project_prm/ui/SearchScreen/DiseaseSearchActivity.java
package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Disease;
import com.example.project_prm.DataManager.SearchManager.DiseaseSearchManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

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

        // Mock search implementation
        List<Disease> results = mockSearchDiseases(query);

        new android.os.Handler().postDelayed(() -> {
            showProgressBar(false);
            updateSearchResults(results);
        }, 500);
    }

    private void loadPopularDiseases() {
        showProgressBar(true);

        // Mock popular diseases
        List<Disease> popularDiseases = mockGetPopularDiseases();

        new android.os.Handler().postDelayed(() -> {
            showProgressBar(false);
            updateSearchResults(popularDiseases);
        }, 300);
    }

    private void updateSearchResults(List<Disease> results) {
        diseaseList.clear();
        diseaseList.addAll(results);
        adapter.notifyDataSetChanged();

        if (results.isEmpty()) {
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
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
        rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    // Mock data methods
    private List<Disease> mockSearchDiseases(String query) {
        List<Disease> results = new ArrayList<>();

        if (query.toLowerCase().contains("đau")) {
            results.add(new Disease(1, "Đau đầu", "Triệu chứng đau ở vùng đầu", "Căng thẳng, mệt mỏi"));
            results.add(new Disease(2, "Đau bụng", "Triệu chứng đau ở vùng bụng", "Tiêu hóa kém"));
        } else if (query.toLowerCase().contains("sốt")) {
            results.add(new Disease(3, "Sốt virus", "Nhiệt độ cơ thể tăng cao", "Nhiễm virus"));
        } else if (query.toLowerCase().contains("ho")) {
            results.add(new Disease(4, "Ho khan", "Ho không có đờm", "Dị ứng, viêm họng"));
            results.add(new Disease(5, "Ho có đờm", "Ho kèm đờm", "Nhiễm khuẩn đường hô hấp"));
        }

        return results;
    }

    private List<Disease> mockGetPopularDiseases() {
        List<Disease> diseases = new ArrayList<>();
        diseases.add(new Disease(1, "Cảm cúm", "Bệnh nhiễm virus phổ biến", "Virus cúm"));
        diseases.add(new Disease(2, "Đau đầu", "Triệu chứng đau ở vùng đầu", "Căng thẳng"));
        diseases.add(new Disease(3, "Đau bụng", "Triệu chứng đau ở vùng bụng", "Tiêu hóa"));
        diseases.add(new Disease(4, "Sốt", "Nhiệt độ cơ thể tăng cao", "Nhiễm trùng"));
        diseases.add(new Disease(5, "Ho", "Phản xạ tự nhiên của cơ thể", "Dị ứng"));
        return diseases;
    }

    // Simple adapter class
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
            android.widget.LinearLayout layout = new android.widget.LinearLayout(parent.getContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(16, 16, 16, 16);
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
            private android.widget.TextView tvName, tvDescription;

            public ViewHolder(android.view.View itemView) {
                super(itemView);
                android.widget.LinearLayout layout = (android.widget.LinearLayout) itemView;

                tvName = new android.widget.TextView(itemView.getContext());
                tvName.setTextSize(16);
                tvName.setTextColor(0xFF000000);
                layout.addView(tvName);

                tvDescription = new android.widget.TextView(itemView.getContext());
                tvDescription.setTextSize(14);
                tvDescription.setTextColor(0xFF666666);
                layout.addView(tvDescription);
            }

            public void bind(Disease disease, OnDiseaseClickListener listener) {
                tvName.setText(disease.getName());
                tvDescription.setText(disease.getSymptoms());

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDiseaseClick(disease);
                    }
                });
            }
        }
    }
}
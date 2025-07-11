// NEW FILE: app/src/main/java/com/example/project_prm/ui/SearchScreen/DiseaseSearchActivity.java
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
        adapter = new DiseaseSearchAdapter(diseaseList, new DiseaseSearchAdapter.OnDiseaseClickListener() {
            @Override
            public void onDiseaseClick(Disease disease) {
                // Navigate to disease detail
                Intent intent = new Intent(DiseaseSearchActivity.this, DiseaseDetailActivity.class);
                intent.putExtra("disease_id", disease.getId());
                intent.putExtra("disease_name", disease.getName());
                startActivity(intent);
            }
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchListener() {
        etSearchDisease.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadPopularDiseases();
                } else if (query.length() >= 2) {
                    searchDiseases(query);
                }
            }
        });
    }

    private void searchDiseases(String query) {
        showLoading(true);

        // Search by symptoms (primary search method)
        service.searchDiseasesBySymptoms(query, new DiseaseSearchManager.OnSearchCompleteListener() {
            @Override
            public void onSuccess(List<Disease> diseases) {
                runOnUiThread(() -> {
                    showLoading(false);

                    // Also search by name to get more comprehensive results
                    List<Disease> nameResults = service.searchDiseasesByName(query);

                    // Combine results (avoid duplicates)
                    List<Disease> combinedResults = new ArrayList<>(diseases);
                    for (Disease disease : nameResults) {
                        if (!containsDisease(combinedResults, disease.getId())) {
                            combinedResults.add(disease);
                        }
                    }

                    updateSearchResults(combinedResults);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Search failed: " + error);
                    showEmptyState(true);
                });
            }
        });
    }

    private void loadPopularDiseases() {
        showLoading(true);

        new Thread(() -> {
            try {
                List<Disease> popularDiseases = service.getPopularDiseases(20);
                runOnUiThread(() -> {
                    showLoading(false);
                    updateSearchResults(popularDiseases);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Failed to load diseases: " + e.getMessage());
                });
            }
        }).start();
    }

    private void updateSearchResults(List<Disease> diseases) {
        diseaseList.clear();
        diseaseList.addAll(diseases);
        adapter.notifyDataSetChanged();

        showEmptyState(diseases.isEmpty());
    }

    private boolean containsDisease(List<Disease> diseases, int diseaseId) {
        for (Disease disease : diseases) {
            if (disease.getId() == diseaseId) {
                return true;
            }
        }
        return false;
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
        rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
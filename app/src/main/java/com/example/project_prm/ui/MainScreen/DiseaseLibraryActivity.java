package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;
import com.google.android.material.textfield.TextInputEditText;

public class DiseaseLibraryActivity extends AppCompatActivity {

    private RecyclerView rvDiseaseList;
    private TextInputEditText etSearchDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library);

        // Initialize UI components
        rvDiseaseList = findViewById(R.id.rv_disease_list);
        etSearchDisease = findViewById(R.id.et_search_disease);

        // Setup RecyclerView
        rvDiseaseList.setLayoutManager(new LinearLayoutManager(this));

        // Handle search
        etSearchDisease.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
}
package com.example.project_prm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

public class AllUtilitiesActivity extends AppCompatActivity {
    private String[] utilityNames = {"Thông tin đặt lịch", "Articles", "History", "Chat AI", "Appointment"};
    private int[] utilityIcons = {R.drawable.ic_info_booking, R.drawable.ic_article, R.drawable.ic_history, R.drawable.ic_chat_ai, R.drawable.ic_calendar};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_utilities);

        EditText etSearchAll = findViewById(R.id.etSearchAll);
        GridLayout gridAllUtilities = findViewById(R.id.gridAllUtilities);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivEmptyState = findViewById(R.id.ivEmptyState);
        TextView tvEmptyStateMsg = findViewById(R.id.tvEmptyStateMsg);
        ivBack.setOnClickListener(v -> finish());

        showUtilities(gridAllUtilities, ivEmptyState, tvEmptyStateMsg, "");

        etSearchAll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                showUtilities(gridAllUtilities, ivEmptyState, tvEmptyStateMsg, s.toString().trim().toLowerCase());
            }
        });
    }

    private void showUtilities(GridLayout grid, ImageView emptyState, TextView emptyMsg, String keyword) {
        grid.removeAllViews();
        int count = 0;
        for (int i = 0; i < utilityNames.length; i++) {
            String name = utilityNames[i];
            if (keyword.isEmpty() || name.toLowerCase().contains(keyword)) {
                LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.item_utility, grid, false);
                TextView tv = item.findViewById(R.id.tvUtilityName);
                tv.setText(name);
                item.<android.widget.ImageView>findViewById(R.id.ivUtilityIcon).setImageResource(utilityIcons[i]);
                grid.addView(item);
                count++;
            }
        }
        if (count == 0) {
            grid.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
            emptyMsg.setVisibility(View.VISIBLE);
        } else {
            grid.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            emptyMsg.setVisibility(View.GONE);
        }
    }
} 
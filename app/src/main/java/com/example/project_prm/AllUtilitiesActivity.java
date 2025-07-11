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

public class AllUtilitiesActivity extends AppCompatActivity {
    private String[] utilityNames = {"Thông tin đặt lịch", "Articles", "History", "Chat AI", "Appointment"};
    private int[] utilityIcons = {R.drawable.ic_info_booking, R.drawable.ic_article, R.drawable.ic_history, R.drawable.ic_chat_ai, R.drawable.ic_calendar};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_utilities);

        EditText etSearchAll = findViewById(R.id.etSearchAll);
        GridLayout gridAllUtilities = findViewById(R.id.gridAllUtilities);

        showUtilities(gridAllUtilities, "");

        etSearchAll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                showUtilities(gridAllUtilities, s.toString().trim().toLowerCase());
            }
        });
    }

    private void showUtilities(GridLayout grid, String keyword) {
        grid.removeAllViews();
        for (int i = 0; i < utilityNames.length; i++) {
            String name = utilityNames[i];
            if (keyword.isEmpty() || name.toLowerCase().contains(keyword)) {
                LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.item_utility, grid, false);
                TextView tv = item.findViewById(R.id.tvUtilityName);
                tv.setText(name);
                item.<android.widget.ImageView>findViewById(R.id.ivUtilityIcon).setImageResource(utilityIcons[i]);
                grid.addView(item);
            }
        }
    }
} 
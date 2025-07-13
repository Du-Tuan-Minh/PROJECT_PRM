package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_prm.AllUtilitiesActivity;
import com.example.project_prm.BannerAdapter;
import com.example.project_prm.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    private ViewPager2 bannerViewPager;
    private EditText etSearch;
    private GridLayout gridSpeciality;
    private TextView tvSeeAllSpeciality;
    private ImageView ivNotification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupClickListeners();
        setupSearchFunctionality();
        setupBannerViewPager();
    }

    private void initializeViews(View view) {
        ivNotification = view.findViewById(R.id.ivNotification);
        etSearch = view.findViewById(R.id.etSearch);
        gridSpeciality = view.findViewById(R.id.gridSpeciality);
        tvSeeAllSpeciality = view.findViewById(R.id.tvSeeAllSpeciality);
    }

    private void setupClickListeners() {
        ivNotification.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        tvSeeAllSpeciality.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AllUtilitiesActivity.class));
        });

        // Setup click listeners for new appointment features
        View btnBookAppointment = getView().findViewById(R.id.btnBookAppointment);
        View btnAppointmentHistory = getView().findViewById(R.id.btnAppointmentHistory);
        View btnSearchDoctor = getView().findViewById(R.id.btnSearchDoctor);
        View btnSearchClinic = getView().findViewById(R.id.btnSearchClinic);

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookAppointmentActivity.class);
            startActivity(intent);
        });

        btnAppointmentHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AppointmentHistoryActivity.class);
            startActivity(intent);
        });

        btnSearchDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
            startActivity(intent);
        });

        btnSearchClinic.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FindClinicActivity.class);
            startActivity(intent);
        });

        // Setup click listeners for existing utilities
        View btnBookingInfo = getView().findViewById(R.id.btnBookingInfo);
        View btnArticles = getView().findViewById(R.id.btnArticles);
        View btnChatAI = getView().findViewById(R.id.btnChatAI);

        btnBookingInfo.setOnClickListener(v -> {
            // TODO: Show booking information
            Toast.makeText(getActivity(), "Thông tin đặt lịch khám", Toast.LENGTH_SHORT).show();
        });

        btnArticles.setOnClickListener(v -> {
            // TODO: Navigate to articles
            Toast.makeText(getActivity(), "Tính năng Articles đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnChatAI.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatbotActivity.class);
            startActivity(intent);
        });
    }

    private void setupSearchFunctionality() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim().toLowerCase();
                for (int i = 0; i < gridSpeciality.getChildCount(); i++) {
                    View child = gridSpeciality.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        TextView tv = null;
                        for (int j = 0; j < ((LinearLayout) child).getChildCount(); j++) {
                            View sub = ((LinearLayout) child).getChildAt(j);
                            if (sub instanceof TextView) {
                                tv = (TextView) sub;
                                break;
                            }
                        }
                        if (tv != null) {
                            String name = tv.getText().toString().toLowerCase();
                            if (keyword.isEmpty() || name.contains(keyword)) {
                                child.setVisibility(View.VISIBLE);
                            } else {
                                child.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setupBannerViewPager() {
        bannerViewPager = getView().findViewById(R.id.bannerViewPager);
        BannerAdapter bannerAdapter = new BannerAdapter(Arrays.asList(
            R.layout.banner_kiem_tra_y_te,
            R.layout.banner_artical,
            R.layout.banner_chat_ai
        ));
        bannerViewPager.setAdapter(bannerAdapter);
        
        DotsIndicator bannerIndicator = getView().findViewById(R.id.bannerIndicator);
        bannerIndicator.setViewPager2(bannerViewPager);
    }
} 
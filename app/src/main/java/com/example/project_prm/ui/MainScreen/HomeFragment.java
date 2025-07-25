package com.example.project_prm.ui.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.DataManager.DAO.UserProfileDAO;
import com.example.project_prm.ui.AllUtilitiesActivity;
import com.example.project_prm.ui.adapter.BannerAdapter;
import com.example.project_prm.R;
import com.example.project_prm.ui.Article.ArticlesActivity;
import com.example.project_prm.utils.CurrentUser;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 bannerViewPager;
    private EditText etSearch;
    private GridLayout gridSpeciality;
    private TextView tvSeeAllSpeciality;
    private ImageView ivNotification;
    private View notificationBadge;

    private ImageView btnchat;
    private ImageView btnArticles;

    private TextView tvUserName;


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
        loadUserName();
    }

    private void initializeViews(View view) {
        ivNotification = view.findViewById(R.id.ivNotification);
        notificationBadge = view.findViewById(R.id.notificationBadge);
        etSearch = view.findViewById(R.id.etSearch);
        gridSpeciality = view.findViewById(R.id.gridSpeciality);
        tvSeeAllSpeciality = view.findViewById(R.id.tvSeeAllSpeciality);
        btnchat = view.findViewById(R.id.btnchat);
        btnArticles = view.findViewById(R.id.btnArticles);
        tvUserName = view.findViewById(R.id.tvUserNameHome);


        updateNotificationBadge();
    }

    private void updateNotificationBadge() {
        boolean hasNew = requireActivity().getSharedPreferences("app_prefs", 0)
                .getBoolean("has_new_notification", false);
        if (notificationBadge != null) {
            notificationBadge.setVisibility(hasNew ? View.VISIBLE : View.GONE);
        }
    }

    private void loadUserName() {
        String userId = CurrentUser.getUserId(requireContext());
        if (userId == null) return;

        UserProfileDAO profileDAO = new UserProfileDAO();
        profileDAO.getByUserId(userId).addOnSuccessListener(doc -> {
            if (doc != null && doc.exists()) {
                String name = doc.getString("full_name");
                if (name != null) {
                    tvUserName.setText(name);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("LoadUserName", "Lỗi khi lấy tên người dùng", e);
        });
    }



    private void setupClickListeners() {
        ivNotification.setOnClickListener(v -> {
            // Khi vào NotificationActivity thì ẩn badge
            requireActivity().getSharedPreferences("app_prefs", 0)
                .edit().putBoolean("has_new_notification", false).apply();
            updateNotificationBadge();
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        tvSeeAllSpeciality.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AllUtilitiesActivity.class));
        });

        // Setup click listeners for new appointment features
        View btnBookAppointment = getView().findViewById(R.id.btnBookAppointment);
        View btnAppointmentHistory = getView().findViewById(R.id.btnAppointmentHistory);
        View btnSearchClinic = getView().findViewById(R.id.btnSearchClinic);

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookAppointmentActivity.class);
            startActivity(intent);
        });

        btnAppointmentHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AppointmentHistoryActivity.class);
            startActivity(intent);
        });

        btnSearchClinic.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FindClinicActivity.class);
            startActivity(intent);
        });

        btnchat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatbotActivity.class);
            startActivity(intent);
        });

        btnArticles.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ArticlesActivity.class);
            startActivity(intent);
        });

        // Setup click listeners for existing utilities

        View btnChatAI = getView().findViewById(R.id.btnChatAI);



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

        List<Integer> layoutIds = Arrays.asList(
                Integer.valueOf(R.layout.banner_kiem_tra_y_te),
                Integer.valueOf(R.layout.banner_artical),
                Integer.valueOf(R.layout.banner_chat_ai)
        );

        // Danh sách ID của các view có thể click trong banner
        List<Integer> clickableViewIds = Arrays.asList(
                Integer.valueOf(R.id.btnChat)
                // Nếu không có, bạn có thể bỏ dòng này đi
        );

        // Tạo adapter với callback
        BannerAdapter bannerAdapter = new BannerAdapter(
                layoutIds,
                clickableViewIds,
                (layoutId, position, clickedView) -> {
                    int viewId = clickedView.getId();

                    if (layoutId == R.layout.banner_chat_ai && viewId == R.id.btnChat) {
                        // Mở màn Chatbot
                        Intent intent = new Intent(getActivity(), ChatbotActivity.class);
                        startActivity(intent);
                    }
                }
        );

        // Gán adapter vào ViewPager
        bannerViewPager.setAdapter(bannerAdapter);

        // Gán indicator
        DotsIndicator bannerIndicator = getView().findViewById(R.id.bannerIndicator);
        bannerIndicator.setViewPager2(bannerViewPager);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateNotificationBadge();
        loadUserName();  // Thêm dòng này để cập nhật lại tên người dùng
    }

} 
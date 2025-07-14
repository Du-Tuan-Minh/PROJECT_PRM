package com.example.project_prm.ui.User_Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.project_prm.DataManager.DAO.UserProfileDAO;
import com.example.project_prm.DataManager.Entity.UserProfile;
import com.example.project_prm.MainActivity;
import com.example.project_prm.R;
import com.example.project_prm.ui.auth.SignInActivity;
import com.example.project_prm.utils.CurrentUser;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Tách riêng xử lý nút back
        setupBackToHome(view);
        setupEditProfileButton(view);
        setupDarkModeSwitch(view);

        setupNavigation(view);
        loadUserProfile(view);
        return view;
    }



    // ✅ Hàm riêng xử lý nhấn icon để quay về Home
    private void setupBackToHome(View rootView) {
        ImageView ivBackToHome = rootView.findViewById(R.id.ivBackToHome);
        ivBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void setupEditProfileButton(View rootView) {
        ImageView ivEditProfile = rootView.findViewById(R.id.ivEditProfile);
        ivEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupDarkModeSwitch(View rootView) {
        Switch darkModeSwitch = rootView.findViewById(R.id.dark_mode_switch);
        SharedPreferences prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDark);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }




    private void setupNavigation(View rootView) {
        rootView.findViewById(R.id.edit_profile_item).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        });

        rootView.findViewById(R.id.security_item).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SecurityActivity.class));
        });

        rootView.findViewById(R.id.language_item).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LanguageActivity.class));
        });

        // ✅ Thêm xử lý logout
        rootView.findViewById(R.id.logout_item).setOnClickListener(v -> {
            performLogout();
        });
    }


    // Hàm tải và hiển thị thông tin người dùng hiện tại lên giao diện Profile
    private void loadUserProfile(View rootView) {
        // Lấy userId từ SharedPreferences (hoặc FirebaseAuth tùy bạn dùng)
        String userId = CurrentUser.getUserId(requireContext());
        if (userId == null) {
            // Nếu chưa đăng nhập, thông báo và dừng
            Toast.makeText(requireContext(), "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi DAO để lấy thông tin UserProfile từ Firestore
        UserProfileDAO profileDAO = new UserProfileDAO();
        profileDAO.getByUserId(userId)
                .addOnSuccessListener(doc -> {
                    if (doc != null && doc.exists()) {
                        // Chuyển dữ liệu Firestore thành đối tượng UserProfile
                        UserProfile profile = doc.toObject(UserProfile.class);

                        // Tìm các view hiển thị thông tin
                        TextView tvName = rootView.findViewById(R.id.tvUserName);
                        TextView tvPhone = rootView.findViewById(R.id.tvUserPhone);

                        // Gán dữ liệu từ profile vào UI (check null để tránh crash)
                        if (profile != null) {
                            tvName.setText(profile.getFull_name() != null ? profile.getFull_name() : "Chưa có tên");
                            tvPhone.setText(profile.getEmergency_contact() != null ? profile.getEmergency_contact() : "Chưa có số");
                        } else {
                            Toast.makeText(requireContext(), "Không thể đọc hồ sơ người dùng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Không tìm thấy tài liệu khớp
                        Toast.makeText(requireContext(), "Không tìm thấy hồ sơ người dùng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Context context = getContext(); // hoặc getActivity()
                    if (context != null) {
                        Toast.makeText(context, "Lỗi khi tải hồ sơ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    /**
     * Đăng xuất người dùng hiện tại:
     * - Xoá userId trong SharedPreferences.
     * - Chuyển về màn hình LoginActivity.
     * - Clear back stack để không quay lại được.
     */
    private void performLogout() {
        CurrentUser.logout(requireContext()); // Xoá userId khỏi SharedPreferences

        Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xoá stack
        startActivity(intent);
    }




}


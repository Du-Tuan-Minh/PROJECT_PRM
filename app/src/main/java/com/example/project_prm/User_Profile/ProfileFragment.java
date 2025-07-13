package com.example.project_prm.User_Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.project_prm.MainActivity;
import com.example.project_prm.R;

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

//        rootView.findViewById(R.id.logout_item).setOnClickListener(v -> {
//            // Ví dụ dùng DialogFragment
//            LogoutDialogFragment dialog = new LogoutDialogFragment();
//            dialog.show(getParentFragmentManager(), "LogoutDialog");
//        });
    }


}


package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class PackageSelectionFragment extends Fragment {
    
    private RecyclerView rvPackages;
    private MaterialButton btnNext;
    private PackageAdapter adapter;
    private List<PackageModel> packages;
    private PackageModel selectedPackage;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_package_selection, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupPackages();
        setupListeners();
    }
    
    private void initViews(View view) {
        rvPackages = view.findViewById(R.id.rvPackages);
        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
    }
    
    private void setupPackages() {
        packages = new ArrayList<>();
        packages.add(new PackageModel("Nhắn tin", "Chat với bác sĩ", "30 phút", "200,000đ", "200000"));
        packages.add(new PackageModel("Gọi video", "Gọi video với bác sĩ", "45 phút", "500,000đ", "500000"));
        packages.add(new PackageModel("Gọi thoại", "Gọi thoại với bác sĩ", "30 phút", "300,000đ", "300000"));
        packages.add(new PackageModel("Khám trực tiếp", "Khám trực tiếp tại phòng khám", "60 phút", "800,000đ", "800000"));
        
        adapter = new PackageAdapter(packages, new PackageAdapter.OnPackageClickListener() {
            @Override
            public void onPackageClick(PackageModel packageModel) {
                selectedPackage = packageModel;
                adapter.setSelectedPackage(selectedPackage);
                btnNext.setEnabled(true);
            }
        });
        
        rvPackages.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPackages.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (selectedPackage != null) {
                BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
                if (activity != null) {
                    activity.bookingData.packageType = selectedPackage.name;
                    activity.bookingData.duration = selectedPackage.duration;
                    activity.bookingData.amount = selectedPackage.amount;
                    activity.showStep(2);
                }
            }
        });
    }
    
    public static class PackageModel {
        public String name;
        public String description;
        public String duration;
        public String amount;
        public String amountValue;
        
        public PackageModel(String name, String description, String duration, String amount, String amountValue) {
            this.name = name;
            this.description = description;
            this.duration = duration;
            this.amount = amount;
            this.amountValue = amountValue;
        }
    }
    
    public static class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
        private List<PackageModel> packages;
        private OnPackageClickListener listener;
        private PackageModel selectedPackage;
        
        public interface OnPackageClickListener {
            void onPackageClick(PackageModel packageModel);
        }
        
        public PackageAdapter(List<PackageModel> packages, OnPackageClickListener listener) {
            this.packages = packages;
            this.listener = listener;
        }
        
        @NonNull
        @Override
        public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_package, parent, false);
            return new PackageViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
            PackageModel packageModel = packages.get(position);
            holder.bind(packageModel);
        }
        
        @Override
        public int getItemCount() {
            return packages.size();
        }
        
        public void setSelectedPackage(PackageModel packageModel) {
            this.selectedPackage = packageModel;
            notifyDataSetChanged();
        }
        
        class PackageViewHolder extends RecyclerView.ViewHolder {
            private TextView tvName, tvDescription, tvDuration, tvAmount;
            private View cardView;
            
            public PackageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvDuration = itemView.findViewById(R.id.tvDuration);
                tvAmount = itemView.findViewById(R.id.tvAmount);
                cardView = itemView.findViewById(R.id.cardView);
            }
            
            public void bind(PackageModel packageModel) {
                tvName.setText(packageModel.name);
                tvDescription.setText(packageModel.description);
                tvDuration.setText(packageModel.duration);
                tvAmount.setText(packageModel.amount);
                
                if (packageModel.equals(selectedPackage)) {
                    cardView.setBackgroundResource(R.drawable.bg_button_primary);
                    tvName.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                    tvDescription.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                    tvDuration.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                    tvAmount.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                } else {
                    cardView.setBackgroundResource(R.drawable.bg_button_outline);
                    tvName.setTextColor(itemView.getContext().getResources().getColor(R.color.text_black));
                    tvDescription.setTextColor(itemView.getContext().getResources().getColor(R.color.text_gray));
                    tvDuration.setTextColor(itemView.getContext().getResources().getColor(R.color.text_gray));
                    tvAmount.setTextColor(itemView.getContext().getResources().getColor(R.color.primary_blue));
                }
                
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPackageClick(packageModel);
                    }
                });
            }
        }
    }
} 
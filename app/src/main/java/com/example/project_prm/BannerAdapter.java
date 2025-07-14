package com.example.project_prm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.utils.OnBannerActionListener;

import java.util.List;


public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private List<Integer> layoutIds;
    private OnBannerActionListener listener;
    private List<Integer> clickableViewIds;

    public BannerAdapter(List<Integer> layoutIds, List<Integer> clickableViewIds, OnBannerActionListener listener) {
        this.layoutIds = layoutIds;
        this.clickableViewIds = clickableViewIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutIds.get(viewType), parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        int layoutId = layoutIds.get(position);

//        // Xử lý tùy theo layout cụ thể
//        if (layoutId == R.layout.banner_chat_ai) {
//            Button btnChat = holder.itemView.findViewById(R.id.btnChat);
//            if (btnChat != null) {
//                btnChat.setOnClickListener(v -> {
//                    if (listener != null) {
//                        listener.onAction(layoutId, position, v);
//                    }
//                });
//            }
//        }
        if (listener != null && clickableViewIds != null) {
            for (int viewId : clickableViewIds) {
                View view = holder.itemView.findViewById(viewId);
                if (view != null) {
                    view.setOnClickListener(v -> listener.onAction(layoutId, position, v));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return layoutIds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
} 
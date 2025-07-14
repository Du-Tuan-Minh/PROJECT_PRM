package com.example.project_prm.ui.Article;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm.R;

import java.util.ArrayList;
import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {

    private Context context;
    private List<Article> trendingArticles;
    private OnTrendingClickListener listener;

    public interface OnTrendingClickListener {
        void onTrendingClick(Article article);
    }

    public TrendingAdapter(Context context, OnTrendingClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.trendingArticles = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item_trending, parent, false);
        return new TrendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        Article article = trendingArticles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return trendingArticles.size();
    }

    public void updateArticles(List<Article> newList) {
        trendingArticles.clear();
        trendingArticles.addAll(newList);
        notifyDataSetChanged();
    }

    class TrendingViewHolder extends RecyclerView.ViewHolder {

        private ImageView trendingImage;
        private TextView trendingTitle;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            trendingImage = itemView.findViewById(R.id.trending_image);
            trendingTitle = itemView.findViewById(R.id.trending_title);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTrendingClick(trendingArticles.get(position));
                }
            });
        }

        public void bind(Article article) {
            trendingTitle.setText(article.getTitle());

            // Load ảnh từ URL bằng Glide
            Glide.with(context)
                    .load(article.getImageUrl()) // ← dùng link ảnh
                    .placeholder(R.drawable.loading) // ảnh tạm trong lúc loading
                    .error(R.drawable.loading)       // ảnh khi lỗi
                    .into(trendingImage);
        }

    }
}


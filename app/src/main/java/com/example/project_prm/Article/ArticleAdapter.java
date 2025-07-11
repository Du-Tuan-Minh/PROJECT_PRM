package com.example.project_prm.Article;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.R;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<Article> articles;
    private OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public ArticleAdapter(Context context, OnArticleClickListener listener) {
        this.context = context;
        this.articles = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void updateArticles(List<Article> newArticles) {
        this.articles.clear();
        this.articles.addAll(newArticles);
        notifyDataSetChanged();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private ImageView articleImage;
        private TextView articleTitle;
        private TextView articleDate;
        private TextView articleCategory;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleDate = itemView.findViewById(R.id.article_date);
            articleCategory = itemView.findViewById(R.id.article_category);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onArticleClick(articles.get(position));
                }
            });
        }

        public void bind(Article article) {
            articleTitle.setText(article.getTitle());
            articleDate.setText(article.getDate());
            articleCategory.setText(article.getCategory());

            // Load image
            articleImage.setImageResource(R.drawable.ic_facebook);

            // Set category background color based on category
            setCategoryBackground(article.getCategory());
        }

        private void setCategoryBackground(String category) {
            int colorResId;
            switch (category.toLowerCase()) {
                case "health":
                    colorResId = R.color.category_health;
                    break;
                case "covid-19":
                    colorResId = R.color.category_covid;
                    break;
                case "lifestyle":
                    colorResId = R.color.category_lifestyle;
                    break;
                case "medical":
                    colorResId = R.color.category_medical;
                    break;
                default:
                    colorResId = R.color.category_default;
                    break;
            }

            int color = ContextCompat.getColor(context, colorResId);
            articleCategory.setBackgroundColor(color);
        }

    }
}

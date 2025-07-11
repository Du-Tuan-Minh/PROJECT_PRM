package com.example.project_prm.Article;


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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.stream.Collectors;

public class ArticlesFragment extends Fragment {

    private RecyclerView trendingRecyclerView;
    private RecyclerView articlesRecyclerView;
    private TrendingAdapter trendingAdapter;
    private ArticleAdapter articleAdapter;
    private ChipGroup chipGroup;
    private TextView seeAllTrending;
    private TextView seeAllArticles;
    private String currentCategory = "All";

    private List<Article> allArticles; // Dùng sample data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_fragment_article, container, false);

        initViews(view);
        setupRecyclerViews();
        setupChips();
        loadData();

        return view;
    }

    private void initViews(View view) {
        trendingRecyclerView = view.findViewById(R.id.trending_recycler_view);
        articlesRecyclerView = view.findViewById(R.id.articles_recycler_view);
        chipGroup = view.findViewById(R.id.chip_group);
        seeAllTrending = view.findViewById(R.id.see_all_trending);
        seeAllArticles = view.findViewById(R.id.see_all_articles);

        // Sample Data
        allArticles = FakeArticleData.getSampleArticles();

        // Không xử lý click, chỉ hiển thị giao diện chính
// seeAllTrending.setOnClickListener(v -> {
//     ArticlesListFragment fragment = ArticlesListFragment.newInstance("trending");
//     ((MainActivity) getActivity()).loadFragment(fragment);
// });
//
// seeAllArticles.setOnClickListener(v -> {
//     ArticlesListFragment fragment = ArticlesListFragment.newInstance("all");
//     ((MainActivity) getActivity()).loadFragment(fragment);
// });

    }

    private void setupRecyclerViews() {
        // Trending RecyclerView (Horizontal)
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingAdapter = new TrendingAdapter(getContext(), article -> {
            // KHÔNG xử lý click chuyển trang
            // ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article.getId());
            // ((MainActivity) getActivity()).loadFragment(fragment);
        });
        trendingRecyclerView.setAdapter(trendingAdapter);

        // Articles RecyclerView (Vertical)
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new ArticleAdapter(getContext(), article -> {
            // KHÔNG xử lý click chuyển trang
            // ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article.getId());
            // ((MainActivity) getActivity()).loadFragment(fragment);
        });
        articlesRecyclerView.setAdapter(articleAdapter);
    }


    private void setupChips() {
        String[] categories = {"Newest", "Health", "Covid-19", "Lifestyle", "Medical"};

        for (String category : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setOnClickListener(v -> {
                currentCategory = category;
                loadArticlesByCategory();
            });
            chipGroup.addView(chip);
        }

        // Set first chip as checked
        if (chipGroup.getChildCount() > 0) {
            ((Chip) chipGroup.getChildAt(0)).setChecked(true);
        }
    }

    private void loadData() {
        loadTrendingArticles();
        loadArticlesByCategory();
    }

    private void loadTrendingArticles() {
        List<Article> trendingArticles = allArticles.stream()
                .filter(Article::isTrending)
                .collect(Collectors.toList());
        trendingAdapter.updateArticles(trendingArticles);
    }

    private void loadArticlesByCategory() {
        List<Article> filtered;
        if (currentCategory.equalsIgnoreCase("Newest") || currentCategory.equalsIgnoreCase("All")) {
            filtered = allArticles;
        } else {
            filtered = allArticles.stream()
                    .filter(a -> a.getCategory().equalsIgnoreCase(currentCategory))
                    .collect(Collectors.toList());
        }
        articleAdapter.updateArticles(filtered);
    }
}

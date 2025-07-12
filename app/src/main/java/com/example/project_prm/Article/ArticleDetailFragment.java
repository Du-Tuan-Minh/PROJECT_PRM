package com.example.project_prm.Article;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project_prm.R;

public class ArticleDetailFragment extends Fragment {

    private static final String ARG_ARTICLE_ID = "article_id";
    private int articleId;

    public static ArticleDetailFragment newInstance(int articleId) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticleDetailFragment() {
        // Bắt buộc constructor rỗng
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleId = getArguments().getInt(ARG_ARTICLE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_fragment_article_detail, container, false);

        // Lấy Article theo ID
        Article article = getArticleById(articleId);

        // Ánh xạ view
        TextView titleView = view.findViewById(R.id.article_title);
        TextView dateView = view.findViewById(R.id.article_date);
        TextView contentView = view.findViewById(R.id.article_content);
        TextView categoryView = view.findViewById(R.id.article_category);
        ImageView imageView = view.findViewById(R.id.article_image);
        ImageView backButton = view.findViewById(R.id.back_button); // cần có trong XML

        // Đổ dữ liệu vào UI nếu article tồn tại
        if (article != null) {
            titleView.setText(article.getTitle());
            dateView.setText(article.getDate());
            contentView.setText(article.getContent());
            categoryView.setText(article.getCategory());

            Glide.with(this)
                    .load(article.getImageUrl())  // ← Load từ link
                    .placeholder(R.drawable.ic_facebook) // ảnh tạm trong lúc loading
                    .error(R.drawable.ic_facebook)       // ảnh nếu load lỗi
                    .into(imageView);
        }

        // Xử lý quay lại Fragment trước
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return view;
    }

    private Article getArticleById(int id) {
        for (Article a : FakeArticleData.getSampleArticles()) {
            if (a.getId() == id) return a;
        }
        return null;
    }
}

package com.example.project_prm.ui.Article;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;

public class ArticlesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_main); // chứa fragment_container_article

        // Chỉ load fragment nếu activity mới tạo
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_article, new ArticlesFragment())
                    .commit();
        }
    }
}

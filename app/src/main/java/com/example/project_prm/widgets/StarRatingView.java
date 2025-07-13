package com.example.project_prm.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class StarRatingView extends LinearLayout {
    private static final int STAR_COUNT = 5;
    private ImageView[] stars = new ImageView[STAR_COUNT];
    private int rating = 0;
    private OnRatingChangeListener listener;

    public interface OnRatingChangeListener {
        void onRatingChanged(int rating);
    }

    public StarRatingView(Context context) {
        super(context);
        init();
    }

    public StarRatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        for (int i = 0; i < STAR_COUNT; i++) {
            final int index = i;
            ImageView star = new ImageView(getContext());
            star.setImageResource(android.R.drawable.btn_star_big_off);
            star.setColorFilter(Color.parseColor("#FFD600"));
            star.setPadding(8, 8, 8, 8);
            star.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(index + 1);
                    if (listener != null) listener.onRatingChanged(rating);
                }
            });
            stars[i] = star;
            addView(star);
        }
        updateStars();
    }

    public void setRating(int rating) {
        this.rating = rating;
        updateStars();
    }

    public int getRating() {
        return rating;
    }

    private void updateStars() {
        for (int i = 0; i < STAR_COUNT; i++) {
            if (i < rating) {
                stars[i].setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                stars[i].setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }

    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        this.listener = listener;
    }
} 
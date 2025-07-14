package com.example.project_prm.ui.MainScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.project_prm.R;

public class StarRatingView extends LinearLayout {
    private ImageView[] stars;
    private int currentRating = 0;
    private OnRatingChangedListener listener;

    public interface OnRatingChangedListener {
        void onRatingChanged(int rating);
    }

    public StarRatingView(Context context) {
        super(context);
        init();
    }

    public StarRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarRatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        stars = new ImageView[5];
        
        for (int i = 0; i < 5; i++) {
            stars[i] = new ImageView(getContext());
            stars[i].setImageResource(R.drawable.ic_star);
            stars[i].setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT
            ));
            stars[i].setPadding(8, 0, 8, 0);
            
            final int starIndex = i;
            stars[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(starIndex + 1);
                }
            });
            
            addView(stars[i]);
        }
        
        updateStars();
    }

    public void setRating(int rating) {
        this.currentRating = rating;
        updateStars();
        if (listener != null) {
            listener.onRatingChanged(rating);
        }
    }

    public int getRating() {
        return currentRating;
    }

    private void updateStars() {
        for (int i = 0; i < stars.length; i++) {
            if (i < currentRating) {
                stars[i].setImageResource(R.drawable.ic_star);
                stars[i].setColorFilter(getResources().getColor(R.color.primary_blue));
            } else {
                stars[i].setImageResource(R.drawable.ic_star);
                stars[i].setColorFilter(getResources().getColor(R.color.text_gray));
            }
        }
    }

    public void setOnRatingChangedListener(OnRatingChangedListener listener) {
        this.listener = listener;
    }

    public void setOnRatingChangeListener(OnRatingChangedListener listener) {
        this.listener = listener;
    }
} 
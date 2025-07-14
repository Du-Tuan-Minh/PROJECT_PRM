package com.example.project_prm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Model.ReviewModel;
import com.example.project_prm.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<ReviewModel> reviews;
    private Context context;

    public ReviewAdapter(Context context, List<ReviewModel> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewModel review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void updateReviews(List<ReviewModel> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPatientName;
        private TextView tvReviewText;
        private TextView tvReviewDate;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void bind(ReviewModel review) {
            tvPatientName.setText(review.getPatientName());
            tvReviewText.setText(review.getContent());
            ratingBar.setRating(review.getRating());
            if (review.getDate() != null) {
                tvReviewDate.setText(review.getDate());
            } else {
                tvReviewDate.setText("");
            }
        }
    }
} 
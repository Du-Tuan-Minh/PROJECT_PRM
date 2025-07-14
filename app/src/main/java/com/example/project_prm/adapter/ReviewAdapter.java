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
        private TextView tvComment;
        private TextView tvDate;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvDate = itemView.findViewById(R.id.tv_date);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }

        public void bind(ReviewModel review) {
            tvPatientName.setText(review.getPatientName());
            tvComment.setText(review.getContent());
            ratingBar.setRating(review.getRating());
            if (review.getDate() != null) {
                tvDate.setText(review.getDate());
            } else {
                tvDate.setText("");
            }
        }
    }
} 
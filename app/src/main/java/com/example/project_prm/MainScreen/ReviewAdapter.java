package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.project_prm.R;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    
    private List<ReviewModel> reviews;
    
    public ReviewAdapter(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }
    
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
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
    
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivReviewerAvatar;
        private TextView tvReviewerName, tvReviewText, tvTimeAgo, tvLikes;
        private ImageView[] stars = new ImageView[5];
        
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivReviewerAvatar = itemView.findViewById(R.id.ivReviewerAvatar);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            
            stars[0] = itemView.findViewById(R.id.ivStar1);
            stars[1] = itemView.findViewById(R.id.ivStar2);
            stars[2] = itemView.findViewById(R.id.ivStar3);
            stars[3] = itemView.findViewById(R.id.ivStar4);
            stars[4] = itemView.findViewById(R.id.ivStar5);
        }
        
        public void bind(ReviewModel review) {
            tvReviewerName.setText(review.reviewerName);
            tvReviewText.setText(review.reviewText);
            tvTimeAgo.setText(review.timeAgo);
            tvLikes.setText(String.valueOf(review.likes));
            
            // Set stars
            for (int i = 0; i < stars.length; i++) {
                if (i < review.rating) {
                    stars[i].setImageResource(R.drawable.ic_star);
                    stars[i].setColorFilter(itemView.getContext().getResources().getColor(android.R.color.holo_orange_light));
                } else {
                    stars[i].setImageResource(R.drawable.ic_star_outline);
                    stars[i].setColorFilter(itemView.getContext().getResources().getColor(R.color.text_gray));
                }
            }
            
            // Load avatar
            Glide.with(itemView.getContext())
                .load(R.drawable.ic_general)
                .circleCrop()
                .into(ivReviewerAvatar);
        }
    }
} 
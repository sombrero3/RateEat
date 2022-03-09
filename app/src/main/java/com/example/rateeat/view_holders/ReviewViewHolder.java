package com.example.rateeat.view_holders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.adapters.OnItemClickListener;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewViewHolder extends RecyclerView.ViewHolder{
    TextView restaurantTv, dishTv,userTv,ratingTv;
    ImageView star1,star2,star3,star4,star5;
    List<Review> reviewList;

    public ReviewViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        userTv = itemView.findViewById(R.id.general_list_user_tv);
        restaurantTv = itemView.findViewById(R.id.general_list_restaurant_tv);
        dishTv = itemView.findViewById(R.id.general_list_dish_tv);
        ratingTv = itemView.findViewById(R.id.general_list_rating_tv);
        star1 = itemView.findViewById(R.id.general_list_star1_iv);
        star2 = itemView.findViewById(R.id.general_list_star2_iv);
        star3 = itemView.findViewById(R.id.general_list_star3_iv);
        star4 = itemView.findViewById(R.id.general_list_star4_iv);
        star5 = itemView.findViewById(R.id.general_list_star5_iv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });

        Model.instance.getAllReviews(new Model.GetReviewsListListener() {
            @Override
            public void onComplete(List<Review> reviews) {
                reviewList = new ArrayList<>();
                reviewList.clear();
                reviewList.addAll(reviews);
            }
        });


        userTv.setOnClickListener((v)->{
            int pos = getAdapterPosition();
            String userId = reviewList.get(pos).getUserId();
            Bundle args = new Bundle();
            args.putString("userId", userId);
            Navigation.findNavController(v).navigate(R.id.action_global_myListFragment,args);
        });
    }

    public void bind(Review review) {
        userTv.setText(review.getUserName());
        restaurantTv.setText(review.getRestaurantName());
        dishTv.setText(review.getDishName());
        Model.instance.setStarByRating(review.getRating(), star1,star2,star3,star4,star5,ratingTv);
    }
}




















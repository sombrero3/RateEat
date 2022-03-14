package com.example.rateeat.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.adapters.OnItemClickListener;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.squareup.picasso.Picasso;

public class UserReviewViewHolder extends RecyclerView.ViewHolder{
    TextView restaurantTv, dishTv,ratingTv;
    ImageView star1,star2,star3,star4,star5,img;


    public UserReviewViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        restaurantTv = itemView.findViewById(R.id.my_list_row_restaurant_name_tv);
        dishTv = itemView.findViewById(R.id.my_list_row_dish_name_tv);
        ratingTv = itemView.findViewById(R.id.my_list_row_rating_tv);
        star1 = itemView.findViewById(R.id.my_list_row_star1_iv);
        star2 = itemView.findViewById(R.id.my_list_row_star2_iv);
        star3 = itemView.findViewById(R.id.my_list_row_star3_iv);
        star4 = itemView.findViewById(R.id.my_list_row_star4_iv);
        star5 = itemView.findViewById(R.id.my_list_row_star5_iv);
        img= itemView.findViewById(R.id.my_list_row_img);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }

    public void bind(Review review) {
        restaurantTv.setText(review.getRestaurantName());
        dishTv.setText(review.getDishName());
        img.setImageResource(R.drawable.falafel);
        if(review.getImageUrl()!=null) {
            Picasso.get().load(review.getImageUrl()).into(img);
        }
        Model.instance.setStarByRating(review.getRating(), star1,star2,star3,star4,star5,ratingTv);


    }
}
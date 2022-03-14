package com.example.rateeat.view_holders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.adapters.OnItemClickListener;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_models.GeneralListViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewViewHolder extends RecyclerView.ViewHolder{
    TextView restaurantTv, dishTv,userTv,ratingTv;
    ImageView star1,star2,star3,star4,star5,image;
    LiveData<List<Review>> reviewsList;

    public ReviewViewHolder(@NonNull View itemView, OnItemClickListener listener,LiveData<List<Review>> reviewsList) {
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
        image =itemView.findViewById(R.id.general_list_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
        this.reviewsList = reviewsList;
        userTv.setOnClickListener((v)->{
            String userId = this.reviewsList.getValue().get(getAdapterPosition()).getUserId();
           // itemView.bind(review);
            Bundle args = new Bundle();
            args.putString("userId", userId);
            Navigation.findNavController(v).navigate(R.id.action_global_myListFragment,args);
        });


    }

    public void bind(Review review) {
        userTv.setText(review.getUserName());
        restaurantTv.setText(review.getRestaurantName());
        dishTv.setText(review.getDishName());
        image.setImageResource(R.drawable.falafel);
        if(review.getImageUrl()!=null&&review.getImageUrl()!="") {
            Picasso.get().load(review.getImageUrl()).into(image);
        }
        Model.instance.setStarByRating(review.getRating(), star1, star2, star3, star4, star5, ratingTv);


    }
}


















package com.example.rateeat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_holders.ReviewWithUserNameViewHolder;

import java.util.List;

public class ReviewWithUserNameAdapter extends RecyclerView.Adapter<ReviewWithUserNameViewHolder>{
    List<Review> reviewList;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReviewWithUserNameAdapter(List<Review> reviewList){
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public ReviewWithUserNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_list_row,parent,false);
        ReviewWithUserNameViewHolder holder = new ReviewWithUserNameViewHolder(view,listener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewWithUserNameViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

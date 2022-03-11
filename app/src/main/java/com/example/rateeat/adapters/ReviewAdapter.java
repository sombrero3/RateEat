package com.example.rateeat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_holders.ReviewViewHolder;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>{
    List<Review> reviewList;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReviewAdapter(List<Review> reviewList){
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_list_row,parent,false);
        ReviewViewHolder holder = new ReviewViewHolder(view,listener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if(reviewList==null){
            return 0;
        }
        return reviewList.size();
    }
}

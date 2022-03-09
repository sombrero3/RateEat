package com.example.rateeat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_holders.MyReviewViewHolder;
import com.example.rateeat.view_holders.ReviewViewHolder;

import java.util.List;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewViewHolder>{
    List<Review> reviewList;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public MyReviewAdapter(List<Review> reviewList){
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public MyReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_row,parent,false);
        MyReviewViewHolder holder = new MyReviewViewHolder(view,listener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

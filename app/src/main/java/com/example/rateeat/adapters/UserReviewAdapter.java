package com.example.rateeat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_holders.UserReviewViewHolder;

import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewViewHolder>{
    LiveData<List<Review>> reviewList;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public UserReviewAdapter(LiveData<List<Review>> reviewList){
        this.reviewList = reviewList;
    }
    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_row,parent,false);
        UserReviewViewHolder holder = new UserReviewViewHolder(view,listener);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull UserReviewViewHolder holder, int position) {
        Review review = reviewList.getValue().get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if(reviewList == null || reviewList.getValue()==null){
            return 0;
        }
        return reviewList.getValue().size();
    }
}

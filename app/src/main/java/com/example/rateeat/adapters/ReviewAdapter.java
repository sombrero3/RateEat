package com.example.rateeat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rateeat.R;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_holders.ReviewViewHolder;
import com.example.rateeat.view_models.GeneralListViewModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder>{
    OnItemClickListener listener;
    LiveData<List<Review>> reviewsList;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReviewAdapter(LiveData<List<Review>> reviewsList){
        this.reviewsList = reviewsList;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_list_row,parent,false);
        ReviewViewHolder holder = new ReviewViewHolder(view,listener,reviewsList);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewsList.getValue().get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if(reviewsList.getValue()==null){
            return 0;
        }
        return reviewsList.getValue().size();
    }
}

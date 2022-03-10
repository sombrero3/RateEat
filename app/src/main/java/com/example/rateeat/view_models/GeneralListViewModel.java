package com.example.rateeat.view_models;

import androidx.lifecycle.ViewModel;

import com.example.rateeat.model.Review;

import java.util.ArrayList;
import java.util.List;

public class GeneralListViewModel extends ViewModel{
    List<Review> reviewList = new ArrayList<>();

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
}

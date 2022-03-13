package com.example.rateeat.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;

import java.util.LinkedList;
import java.util.List;

public class UserReviewsViewModel extends ViewModel {
    MutableLiveData<List<Review>> reviewList;

    public UserReviewsViewModel(String id) {

        reviewList = Model.instance.getAllReviewsLiveData();
    }


    public LiveData<List<Review>> getUserReviewList() {
        return reviewList;
    }

    public void setReviewList(String id) {
        List<Review> allreviews = new LinkedList<>();
        for(Review review:Model.instance.getAllReviewsLiveData().getValue()){
            if(review.getUserId().equals(id)){
                allreviews.add(review);
            }
        }
        this.reviewList.setValue(allreviews);
    }
}

package com.example.rateeat.view_models;

import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;

import java.net.HttpCookie;
import java.util.LinkedList;
import java.util.List;

public class UserReviewsViewModel extends ViewModel {
    MutableLiveData<List<Review>> reviewList;

    public UserReviewsViewModel() {
        reviewList = new MutableLiveData<>();

    }

    public LiveData<List<Review>> getUserReviewList() {
        return reviewList;
    }

    public void setReviewList(String id) {
        MutableLiveData<List<Review>> all = Model.instance.getAllReviewsLiveData();
        List<Review> userReviews = new LinkedList<>();
        for(Review review:all.getValue()){
            if(review.getUserId().equals(id)){
                userReviews.add(review);
            }
        }
        reviewList.setValue(userReviews);
    }
}

package com.example.rateeat.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;

import java.util.ArrayList;
import java.util.List;

public class GeneralListViewModel extends ViewModel{
   LiveData<List<Review>> reviewList;

    public static GeneralListViewModel instance = new GeneralListViewModel();
    public GeneralListViewModel() {
        reviewList = Model.instance.getAllReviewsLiveData();
    }

    public LiveData<List<Review>> getReviewList() {
        return reviewList;
    }

}

package com.example.rateeat.view_models;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserReviewViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplication;
    private String mParam;


    public UserReviewViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return  modelClass.cast(new UserReviewViewModelFactory(mApplication, mParam));
    }
}
package com.example.rateeat.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public static final Model instance = new Model();
    public ModelFirebase modelFirebase = new ModelFirebase();
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    MutableLiveData<List<Review>> ReviewsList = new MutableLiveData<>();
    MutableLiveData<ReviewListLoadingState> ListLoadingState = new MutableLiveData<>();

    public enum ReviewListLoadingState {
        loading,
        loaded
    }

    private Model() {
        ListLoadingState.setValue(ReviewListLoadingState.loaded);

    }

}

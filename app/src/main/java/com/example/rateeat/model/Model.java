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
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public MutableLiveData<List<Review>> ReviewsList = new MutableLiveData<>();
    public MutableLiveData<ReviewListLoadingState> ListLoadingState = new MutableLiveData<>();
    public User signedUser;
    public boolean isSignedIn() {
        return true;
        //return modelFireBase.isSignedIn();
    }
    public User getSignedUser() {

        return signedUser;
    }
    public interface setCurrentUserListener {
        void onComplete(User user);
    }
    public void setCurrentUser(setCurrentUserListener listener) {
//        modelFireBase.setCurrentUser(new setCurrentUserListener() {
//            @Override
//            public void onComplete(User user) {
//                signedUser = user;
//                listener.onComplete(user);
//            }
//        });
    }

    public enum ReviewListLoadingState {
        loading,
        loaded
    }

    private Model() {
        ListLoadingState.setValue(ReviewListLoadingState.loaded);

    }

}

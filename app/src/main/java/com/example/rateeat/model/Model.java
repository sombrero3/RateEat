package com.example.rateeat.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public static final Model instance = new Model();
    public ModelFireBase modelFireBase = new ModelFireBase();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public MutableLiveData<List<Review>> ReviewsList = new MutableLiveData<>();
    public MutableLiveData<ReviewListLoadingState> reviewListLoadingState = new MutableLiveData<>();
    public User signedUser;

    public enum ReviewListLoadingState {
        loading,
        loaded
    }


    private Model() {
        reviewListLoadingState.setValue(ReviewListLoadingState.loaded);
    }

    public boolean isSignedIn() {
        return modelFireBase.isSignedIn();
    }
    public User getSignedUser() {

        return signedUser;
    }
    public interface SetCurrentUserListener {
        void onComplete(User user);
    }
    public void setCurrentUser(SetCurrentUserListener listener) {
        modelFireBase.setCurrentUser(new SetCurrentUserListener() {

            @Override
            public void onComplete(User user) {
                signedUser = user;
                listener.onComplete(user);
            }
        });
    }

    public interface getUserByIdListener{
        void onComplete(User user);
    }
    public void getUserById(String id,getUserByIdListener listener){
        modelFireBase.getUserById(id,listener);

    }
    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(User user,AddUserListener listener) throws JsonProcessingException {
        reviewListLoadingState.setValue(ReviewListLoadingState.loaded);
        modelFireBase.addUser(user,listener);
    }


}

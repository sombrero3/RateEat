package com.example.rateeat.model;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.rateeat.R;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
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

    /**
     * Authentication
     */
    public boolean isSignedIn() {
        return modelFireBase.isSignedIn();
    }
    public User getSignedUser() {
        return signedUser;
    }
    public void setCurrentUser(UserListener listener) {
        modelFireBase.setCurrentUser(new UserListener() {

            @Override
            public void onComplete(User user) {
                signedUser = user;
                listener.onComplete(user);
            }
        });
    }

    /**
     * Listeners Interfaces
     */
    public interface VoidListener {
        void onComplete() throws JsonProcessingException;
    }
    public interface UserListener {
        void onComplete(User user);
    }
    public interface UsersListListener {
        void onComplete(List<User> userList);
    }
    public interface ReviewListener {
        void onComplete(Review review);
    }
    public interface ReviewsListListener {
        void onComplete(List<Review> reviewList);
    }

    /**
     *User Methods
     */
    //Read
    public void getAllUsers(UsersListListener listener){
        modelFireBase.getAllUsers(listener);
    }
    public void getUserById(String id, UserListener listener){
        modelFireBase.getUserById(id,listener);

    }
    //Create
    public void addUser(User user, VoidListener listener) throws JsonProcessingException {
        modelFireBase.addUser(user,listener);
    }
    //Update
    public void updateUser(User user, VoidListener listener) throws JsonProcessingException {
        modelFireBase.updateUser(user,listener);
    }

    /**
     *Review Methods
     */
    //Read
    public void getAllReviews(ReviewsListListener listener) {
        modelFireBase.getAllReviews(listener);
    }
    public void getReviewById(String id, ReviewListener listener){
        modelFireBase.getReviewById(id,listener);
    }
    public void getMyReviews(ReviewsListListener listener) {
        modelFireBase.getMyReviews(listener);
    }
    //Create
    public void addReview(Review review,VoidListener listener) throws JsonProcessingException {
        reviewListLoadingState.setValue(ReviewListLoadingState.loading);
        modelFireBase.addReview(review,listener);
    }
    //Update
    public void updateReview(Review review, VoidListener listener) throws JsonProcessingException {
        modelFireBase.updateReview(review,listener);
    }

    /**
     *User & Review Combined Methods
     */
    public void getUserReviews(String userId, ReviewsListListener listener) {
        modelFireBase.getUserReviews(userId,listener);
    }
    public void changeUserNameToReviews(User user, String userNewName, VoidListener listener) {
        modelFireBase.changeUserNameToReviews(user,userNewName,listener);
    }

    /**
     * Rating Methods
     */
    public void setStarByRating(String ratingVal, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5, TextView rateTv){

        if(!ratingVal.equals("No rating yet")){
            rateTv.setVisibility(View.INVISIBLE);
            float rate =Float.parseFloat(ratingVal);
            if(rate==0.5){
                star1.setImageResource(R.drawable.star);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==1){
                star1.setImageResource(R.drawable.star);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);

            }
            else if(rate==1.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==2){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==2.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.half_star);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==3){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==3.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.half_star);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==4){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setVisibility(View.INVISIBLE);
            }
            else if(rate==4.5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.half_star);
            }
            else if(rate==5){
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);
            }
        }
        else{
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);
            star4.setVisibility(View.INVISIBLE);
            star5.setVisibility(View.INVISIBLE);
        }
    }
}

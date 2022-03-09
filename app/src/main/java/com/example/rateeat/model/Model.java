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

    public void updateReview(Review review, AddReviewListener listener) throws JsonProcessingException {
        modelFireBase.updateReview(review,listener);
    }

    public void getUserReviews(String userId, GetReviewsListListener listener) {
        modelFireBase.getUserReviews(userId,listener);
    }

    public void updateUser(User user,AddUserListener listener) throws JsonProcessingException {
        modelFireBase.updateUser(user,listener);
    }


    public void changeUserNameToReviews(User user,String userNewName,AddUserListener listener) {
        modelFireBase.changeUserNameToReviews(user,userNewName,listener);
    }


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
        void onComplete() throws JsonProcessingException;
    }
    public void addUser(User user,AddUserListener listener) throws JsonProcessingException {
        modelFireBase.addUser(user,listener);
    }
    public interface AddReviewListener{
        void onComplete() throws JsonProcessingException;
    }
    public void addReview(Review review,AddReviewListener listener) throws JsonProcessingException {
        reviewListLoadingState.setValue(ReviewListLoadingState.loading);
        modelFireBase.addReview(review,listener);
    }


    public interface GetReviewsListListener {
        void onComplete(List<Review> reviewList);
    }
    public void getAllReviews(GetReviewsListListener listener) {
        modelFireBase.getAllReviews(listener);
    }

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

    public interface GetAllUsersListener {
        void onComplete(List<User> userList);
    }
    public void getAllUsers(GetAllUsersListener listener){
        modelFireBase.getAllUsers(listener);
    }

    public interface GetReviewByIdListener{
        void onComplete(Review review);
    }
    public void getReviewById(String id,GetReviewByIdListener listener){
        modelFireBase.getReviewById(id,listener);
    }


    public void getMyReviews(GetReviewsListListener listener) {
        modelFireBase.getMyReviews(listener);
    }

}

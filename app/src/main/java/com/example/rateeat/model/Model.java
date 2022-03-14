package com.example.rateeat.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.rateeat.MyApplication;
import com.example.rateeat.R;
import com.example.rateeat.feed.DetailsReviewFragmentDirections;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public static final Model instance = new Model();
    public ModelFireBase modelFireBase = new ModelFireBase();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public MutableLiveData<List<Review>> reviewsList = new MutableLiveData<>();

    public MutableLiveData<ReviewListLoadingState> getReviewListLoadingState() {
        return reviewListLoadingState;
    }

    public void setReviewListLoadingState(MutableLiveData<ReviewListLoadingState> reviewListLoadingState) {
        this.reviewListLoadingState = reviewListLoadingState;
    }

    public MutableLiveData<ReviewListLoadingState> reviewListLoadingState = new MutableLiveData<>();
    public User signedUser;

    public interface  SaveImageListener{
         void onComplete(String url) throws JsonProcessingException;
      }
    public void saveImage(Bitmap imageBitmap, String imageName,SaveImageListener Listener) {

        modelFireBase.saveImage(imageBitmap,imageName,Listener);

    }


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
        modelFireBase.setCurrentUser(user -> {
            signedUser = user;
            listener.onComplete(user);
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
//    public void getAllReviews(ReviewsListListener listener) {
//        modelFireBase.getAllReviews(listener);
//    }

    public MutableLiveData<List<Review>> getUserReviewsLiveData(String userId) {
        refreshReviewsList();
        List<Review> list = new LinkedList<>();
        for (Review rev: reviewsList.getValue()) {
            if(rev.getUserId().equals(userId)){
                list.add(rev);
            }
        }

        MutableLiveData<List<Review>> res = new MutableLiveData<>();
        res.setValue(list);
        return res;
    }
    public MutableLiveData<List<Review>> getAllReviewsLiveData(){
        if(reviewsList.getValue()==null){
            refreshReviewsList();
        }
        return reviewsList;
    }
    public void refreshReviewsList(){
        reviewListLoadingState.setValue(ReviewListLoadingState.loading);
        //get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("ReviewsLastUpdateDate",0);
        //firebase get all updates since last local update date
        modelFireBase.getAllReviews(lastUpdateDate,new ReviewsListListener() {
            @Override
            public void onComplete(List<Review> list) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG","firebase returned "+list.size());
                        Long lud = new Long(0);
                        //add records to local db
                        for(Review review:list) {
                            if(review.isDeleted()==true) {
                                AppLocalDb.db.reviewDao().delete(review);
                            }else{
                                AppLocalDb.db.reviewDao().insertAll(review);
                            }
                            if(lud<review.getUpdateDate()){
                                lud=review.getUpdateDate();
                            }
                        }
                        //update last local update of db
                        MyApplication.getContext()
                                .getSharedPreferences("TAG",Context.MODE_PRIVATE)
                                .edit().putLong("ReviewsLastUpdateDate",lud)
                                .commit();
                        //return data
                        List<Review> revList = AppLocalDb.db.reviewDao().getAll();
                        for(Review rev:revList){
                            if(rev.isDeleted==true){
                                AppLocalDb.db.reviewDao().delete(rev);
                                revList.remove(rev);
                            }
                        }
//                        List<Review> y = AppLocalDb.db.reviewDao().getAll();
//                        if(reviewsList.getValue() == null || reviewsList.getValue().size()!=y.size()){
//                            y = AppLocalDb.db.reviewDao().getAll();
//                            for(Review rev:y){
//                                AppLocalDb.db.reviewDao().delete(rev);
//                            }
//                        }
                        reviewsList.postValue(revList);
                        reviewListLoadingState.postValue(ReviewListLoadingState.loaded);

                    }
                });
            }
        });
    }
    public void deleteLeftoverReview(String reviewId,VoidListener listener) {
        Review review1= AppLocalDb.db.reviewDao().getReviewById(reviewId);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppLocalDb.db.reviewDao().delete(review1);
                try {
                    listener.onComplete();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public Review getReviewById(String id){
        //modelFireBase.getReviewById(id,listener);
        refreshReviewsList();
        Review res = new Review();
        for (Review rev: reviewsList.getValue()) {
            if(rev.getId().equals(id)){
                res = rev;
                break;
            }
        }
        return res;
    }
    public List<Review> getMyReviews(String userId,ReviewsListListener listener) {
        modelFireBase.getMyReviews(listener);
        List<Review> res = new LinkedList<>();
        for (Review rev: reviewsList.getValue()) {
            if(rev.getUserId().equals(userId)){
                res.add(rev);
            }
        }
        return res;
    }
    //Create
    public void addReview(Review review,VoidListener listener) throws JsonProcessingException {
        reviewListLoadingState.setValue(ReviewListLoadingState.loading);

        modelFireBase.addReview(review, new VoidListener() {
            @Override
            public void onComplete() throws JsonProcessingException {
                refreshReviewsList();
                listener.onComplete();
            }
        });
    }
    //Update
    public void updateReview(Review review, VoidListener listener) throws JsonProcessingException {
        modelFireBase.updateReview(review, new VoidListener() {
            @Override
            public void onComplete() throws JsonProcessingException {
                refreshReviewsList();
                listener.onComplete();
            }
        });
    }

    /**
     *User & Review Combined Methods
     */
    public List<Review> getUserReviews(String userId) {
        //modelFireBase.getUserReviews(userId,listener);
        //modelFireBase.getMyReviews(listener);
        refreshReviewsList();
        List<Review> res = new LinkedList<>();
        for (Review rev: reviewsList.getValue()) {
            if(rev.getUserId().equals(userId)){
                res.add(rev);
            }
        }
        return res;
    }
    public void changeUserNameToReviews(User user, String userNewName, VoidListener listener) {
        modelFireBase.changeUserNameToReviews(user, userNewName, new VoidListener() {
            @Override
            public void onComplete() throws JsonProcessingException {
                refreshReviewsList();
                listener.onComplete();
            }
        });
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

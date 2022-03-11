package com.example.rateeat.model;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
//import com.google.firebase.firestore.FieldValue;

public class Review {
    String id;
    String userId;
    String userName;
    String restaurantName;
    String dishName;
    String description;
    String rating;
    String imageUrl;
    boolean isDeleted = false;
    Long updateDate = new Long(0);

    public Review(String userId,String userName, String restaurantName, String dishName, String rating, String description) {
        this.userId = userId;
        this.userName = userName;
        this.restaurantName = restaurantName;
        this.dishName = dishName;
        this.rating = rating;
        this.description = description;
        imageUrl="";
    }

    public Review(){}

    public Review(Review rev) {
        this.id = rev.id;
         this.userId = rev.userId;
        this.userName = rev.userName;
        this.restaurantName = rev.restaurantName;
        this.dishName = rev.dishName;
        this.description = rev.description;
        this.rating = rev.rating;
        this.imageUrl = rev.imageUrl;
        this.isDeleted = rev.isDeleted;
        this.updateDate = rev.updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("userId", userId);
        result.put("userName", userName);
        result.put("restaurantName",restaurantName);
        result.put("dishName", dishName);
        result.put("description", description);
        result.put("rating",rating);
        result.put("imageUrl", imageUrl);
        result.put("deleted", isDeleted);
        result.put("updateDate", FieldValue.serverTimestamp());

        return result;
    }

    public void fromMap(Map<String, Object> map){
        id = (String)map.get("id");
        userId = (String)map.get("userId");
        userName = (String)map.get("userName");
        restaurantName = (String)map.get("restaurantName");
        dishName = (String) map.get("dishName");
        description = (String) map.get("description");
        rating= (String) map.get("rating");
        imageUrl = (String)map.get("imageUrl");
        isDeleted = (Boolean) map.get("deleted");
        updateDate = (Long)map.get("updateDate");
        //----------------
        //Timestamp ts = (Timestamp)map.get("lastUpdated");
        //lastUpdated = ts.getSeconds();
        //--------

    }


    public void setAvatarUrl(String url) {


    }

    public void setDishUrl(String url) {

    }
}

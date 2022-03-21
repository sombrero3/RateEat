package com.example.rateeat.model;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class User {
    String email;
    String id;
    String firstName;
    String lastName;
    String password;
    String imageUrl;
    String numOfReview;
    boolean isDeleted = false;
    Long lastUpdated = new Long(0);

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        numOfReview = "0";
    }

    public User(){}

    public User(User user){
        this.email = user.getEmail();
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.imageUrl = user.getImageUrl();
        this.numOfReview = user.getNumOfReview();
        isDeleted = user.isDeleted();
        this.lastUpdated = user.getLastUpdated();
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getNumOfReview() {
        return numOfReview;
    }

    public void setNumOfReview(String numOfReview) {
        this.numOfReview = numOfReview;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("email", email);
        result.put("firstName",firstName);
        result.put("lastName", lastName);
        result.put("password", password);
        result.put("imageUrl", imageUrl);
        result.put("numOfReview", numOfReview);
        result.put("deleted", isDeleted);
        result.put("lastUpdated", FieldValue.serverTimestamp());

        return result;
    }

    public void fromMap(Map<String, Object> map){
        id = (String)map.get("id");
        email = (String)map.get("email");
        firstName = (String)map.get("firstName");
        lastName = (String) map.get("lastName");
        password = (String) map.get("password");
        imageUrl = (String)map.get("imageUrl");
        numOfReview = (String)map.get("numOfReview");
        isDeleted = (Boolean)map.get("deleted");
        lastUpdated = (Long)map.get("lastUpdated");
        //       Timestamp ts = (Timestamp)map.get("lastUpdated");
//        lastUpdated = ts.getSeconds();


    }
}

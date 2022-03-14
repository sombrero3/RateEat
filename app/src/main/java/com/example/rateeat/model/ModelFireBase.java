package com.example.rateeat.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {

    FirebaseAuth currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public ModelFireBase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    /**
     * Authentication
     */
    public boolean isSignedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }
    public void signIn(String email, String password, String firstName, String lastName, ProgressBar progressBar) {
        Log.d("TAG", "signIn: " + email + " " + password + " " + mAuth);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "onComplete: succeed" + email + " " + password + " " + mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(progressBar.getContext(), "Failed To Registered 2", Toast.LENGTH_LONG).show();

                            Log.d("TAG", "onComplete failed: " + email + " " + password + " " + mAuth + " " + task.getException().toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    public void setCurrentUser(Model.UserListener listener) {
        currentUser = FirebaseAuth.getInstance();
        String userUid = currentUser.getCurrentUser().getUid();
        getUserById(userUid, new Model.UserListener() {
            @Override
            public void onComplete(User user) {
                listener.onComplete(user);
            }
        });
    }
    private String getSignedUserId() {
        return Model.instance.getSignedUser().getId();
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
     * User Methods
     */
    //Read
    public void getAllUsers(Model.UsersListListener listener) {
        db.collection("users")
                //      .whereEqualTo("deleted",false)
                //  .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User user = null;
                            user.fromMap(doc.getData());
                            if (user != null) {
                                list.add(user);
                            }
                        }
                    }
                    //       Log.d("TAG","Last update date = "+ lastUpdateDate);
                    listener.onComplete(list);
                });
    }
    public void getUserById(String id, Model.UserListener listener) {
        db.collection("users")
                //  .whereEqualTo("id",studentId)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    User user = null;
                    if (task.isSuccessful() && task.getResult() != null) {
                        //  ObjectMapper map = new ObjectMapper();
                        user = new User("","","","");
                        user.fromMap(task.getResult().getData());
                    }
                    listener.onComplete(user);
                });
    }
    //Create
    public void addUser(User user, Model.VoidListener listener) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String userString = objectMapper.writeValueAsString(user);
        Map<String, Object> json = objectMapper.readValue(userString, Map.class);
        db.collection("users")
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(unused -> {
                    try {
                        listener.onComplete();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                })
                .addOnFailureListener(e -> {
                    try {
                        listener.onComplete();
                    } catch (JsonProcessingException jsonProcessingException) {
                        jsonProcessingException.printStackTrace();
                    }
                });
    }
    //Update
    public void updateUser(User user, Model.VoidListener listener) throws JsonProcessingException {
        addUser(user, new Model.VoidListener() {
            @Override
            public void onComplete() throws JsonProcessingException {
                listener.onComplete();
            }
        });
    }

    /**
     *Review Methods
     */
    //Read
    public void getAllReviews(Long lastUpdateDate,Model.ReviewsListListener listener) {
        db.collection("reviews")
                //.whereEqualTo("deleted",false)
                .whereGreaterThanOrEqualTo("latsUpdateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        Log.d("TAG", "Task was successful");
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Review review = new Review();
                            review.fromMap(doc.getData());
                            if (review != null) {
                                list.add(review);
                            }
                        }
                    }
                    Log.d("TAG","Last update date = "+ lastUpdateDate);
                    listener.onComplete(list);
                });
    }
    public void getReviewById(String id, Model.ReviewListener listener) {
        db.collection("reviews")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    Review review = null;
                    if (task.isSuccessful() && task.getResult() != null) {
                        review = new Review();
                        review.fromMap(task.getResult().getData());
                    }
                    listener.onComplete(review);
                });
    }
    public void getMyReviews(Model.ReviewsListListener listener) {
        String userId = Model.instance.getSignedUser().getId();
        db.collection("reviews")
               // .whereEqualTo("deleted",false)
                .whereEqualTo("userId",userId)
                //  .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Review review = new Review();
                            review.fromMap(doc.getData());
                            if (review != null) {
                                list.add(review);
                            }
                        }
                    }
                    //       Log.d("TAG","Last update date = "+ lastUpdateDate);
                    listener.onComplete(list);
                });
    }
    //Create
    public void addReview(Review review, Model.VoidListener listener) throws JsonProcessingException {
        DocumentReference key = db.collection("reviews").document();
        String id = key.getId();
        Log.d("TAG","review id = "+ id);
        review.setId(id);
        db.collection("reviews")
                .document(review.getId())
                .set(review.toMap())
                .addOnSuccessListener(unused -> {
                    try {
                        listener.onComplete();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                })
                .addOnFailureListener(e -> {
                    try {
                        listener.onComplete();
                    } catch (JsonProcessingException jsonProcessingException) {
                        jsonProcessingException.printStackTrace();
                    }
                });
    }
    //Update
    public void updateReview(Review review, Model.VoidListener listener) throws JsonProcessingException {
        db.collection("reviews")
                .document(review.getId())
                .set(review.toMap())
                .addOnSuccessListener(unused -> {
                    try {
                        listener.onComplete();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                })
                .addOnFailureListener(e -> {
                    try {
                        listener.onComplete();
                    } catch (JsonProcessingException jsonProcessingException) {
                        jsonProcessingException.printStackTrace();
                    }
                });
    }
    //Delete

    //
    /**
     * User & Review Combined Methods
     */
    public void getUserReviews(String userId, Model.ReviewsListListener listener) {
        db.collection("reviews")
                .whereEqualTo("deleted",false)
                .whereEqualTo("userId",userId)
                //  .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Review review = new Review();
                            review.fromMap(doc.getData());
                            if (review != null) {
                                list.add(review);
                            }
                        }
                    }
                    //       Log.d("TAG","Last update date = "+ lastUpdateDate);
                    listener.onComplete(list);
                });
    }
    public void changeUserNameToReviews( User user,String userNewName, Model.VoidListener listener) {
        String userId = user.getId();
        db.collection("reviews")
                .whereEqualTo("deleted",false)
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Review review = new Review();
                            review.fromMap(doc.getData());
                            if (review != null) {
                                review.setUserName(userNewName);
                                list.add(review);
                            }
                        }
                    }
                    //       Log.d("TAG","Last update date = "+ lastUpdateDate);

                    updateAllUserReviews(list, new Model.VoidListener() {
                        @Override
                        public void onComplete() throws JsonProcessingException {
                            listener.onComplete();
                        }
                    });
                });
    }

    private void updateAllUserReviews(List<Review> list, Model.VoidListener listener) {
                        for (int i=0;i< list.size();i++) {
                            if(i== list.size()-1) {
                                try {
                                    updateReview(list.get(i), () -> listener.onComplete());
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    updateReview(list.get(i), () -> {});
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
    }
    public void saveImage(Bitmap imageBitmap, String imageName,String collectionName, Model.SaveImageListener listener) {
       StorageReference storageRef=storage.getReference();
        StorageReference imgRef = storageRef.child(collectionName + imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data =baos.toByteArray();

        UploadTask uploadTask =imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            try {
                listener.onComplete(null);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }).addOnSuccessListener(taskSnapshot -> {

            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Uri downloadUrl = uri;
                try {
                    listener.onComplete(downloadUrl.toString());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        });


    }
}

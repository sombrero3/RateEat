package com.example.rateeat.model;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.json.JSONException;

import java.util.Map;

public class ModelFireBase {

    FirebaseAuth currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFireBase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    /**
     * Authentication
     */
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

    public void setCurrentUser(Model.SetCurrentUserListener listener) {
        currentUser = FirebaseAuth.getInstance();
        String userUid = currentUser.getCurrentUser().getUid();
        getUserById(userUid, new Model.getUserByIdListener() {
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
     * User CRUD + Dao
     */
    //Create
    public void addUser(User user, Model.AddUserListener listener) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String userString = objectMapper.writeValueAsString(user);
        Map<String, Object> json = objectMapper.readValue(userString, Map.class);
        db.collection("users")
                .document(user.getId())
                .set(json)
                .addOnSuccessListener(unused -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onComplete();
                });
    }

    //Update
    public void updateUser(User user, Model.AddUserListener listener) throws JsonProcessingException {
        addUser(user, new Model.AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void getUserById(String id, Model.getUserByIdListener listener) {
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

}
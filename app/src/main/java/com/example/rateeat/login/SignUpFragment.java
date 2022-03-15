package com.example.rateeat.login;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rateeat.MainActivity;
import com.example.rateeat.R;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class SignUpFragment extends Fragment {
    ImageView imageIv;
    Button signUp;
    EditText firstNameEt,lastNameEt,passwordEt,emailEt;
    TextView signIn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    static final int REQUEST_IMAGE_CAPTURE = 1,REQUEST_GALLERY =2;
    Bitmap imageBitmap;
    ImageButton cameraBtn,galleryBtn;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUp = view.findViewById(R.id.signup_btn);
        signIn = view.findViewById(R.id.signup_sign_in_tv);
        firstNameEt = view.findViewById(R.id.signup_first_name_et);
        lastNameEt = view.findViewById(R.id.signup_last_name_et);
        passwordEt = view.findViewById(R.id.signup_password_et);
        emailEt = view.findViewById(R.id.signup_email_et);
        imageIv = view.findViewById(R.id.signup_image_iv);
        cameraBtn = view.findViewById(R.id.signup_camera_ibtn);
        galleryBtn = view.findViewById(R.id.signup_gallery_ibtn);
        progressBar = view.findViewById(R.id.signup_prog);
        progressBar.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener((v)->{
            registration();
        });

        signIn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
        });

        cameraBtn.setOnClickListener(v -> {
            openCamera();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });
        return view;
    }
    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,REQUEST_GALLERY);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE){         //Back from the camera
            if(resultCode==RESULT_OK){                   //It worked
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageIv.setImageBitmap(imageBitmap);
            }
        }
        else if(requestCode==REQUEST_GALLERY){
            if(resultCode==RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream =getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    imageIv.setImageBitmap(imageBitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Failed to select image from gallery",Toast.LENGTH_LONG).show();
                }

            }
        }
    }
    private boolean validation(String email, String password, String firstName, String lastName) {
        if(firstName.isEmpty()){
            firstNameEt.setError("First name is required");
            firstNameEt.requestFocus();
            return false;
        }
        if(lastName.isEmpty()){
            lastNameEt.setError("Last name is required");
            lastNameEt.requestFocus();
            return false;
        }
        if(email.isEmpty()){
            emailEt.setError("Email address is required");
            emailEt.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            passwordEt.setError("Password is required");
            passwordEt.requestFocus();
            return false;
        }
        if(password.length()<6){
            passwordEt.setError("Password must contain 6 or more characters");
            passwordEt.requestFocus();
            return false;
        }
        return true;
    }

    private void registration() {
        String email = emailEt.getEditableText().toString().trim();
        String password =passwordEt.getEditableText().toString().trim();
        String firstName = firstNameEt.getEditableText().toString().trim();
        String lastName = lastNameEt.getEditableText().toString().trim();

        if(validation(email,password,firstName,lastName)) {
            progressBar.setVisibility(View.VISIBLE);
            //  Model.instance.signUp(email,password,firstName,lastName,progressBar);
            mAuth = FirebaseAuth.getInstance();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = new User(email, firstName, lastName,password);
                                user.setId(FirebaseAuth.getInstance().getUid());
                                user.setLastUpdated(Timestamp.now().getSeconds());
                                //addUserToRealTimeDataBase(user);
                                try {
                                    addUserToFireStore(user);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                Toast.makeText(getActivity(), "Failed To Registered", Toast.LENGTH_LONG).show();


                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void addUserToFireStore(User user) throws JsonProcessingException {
        if(imageBitmap!=null){
            Model.instance.saveImage(imageBitmap,  user.getId()+ ".jpg",Model.IMAGE_USER_COLLECTION, url -> {
                user.setImageUrl(url);
                Model.instance.addUser(user, new Model.VoidListener() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getActivity(), "Successfully Registered.", Toast.LENGTH_LONG).show();
                        goToFeedActivity();
                    }
                });
            });
        }else {
            Model.instance.addUser(user, new Model.VoidListener() {
                @Override
                public void onComplete() {
                    Toast.makeText(getActivity(), "Successfully Registered.", Toast.LENGTH_LONG).show();
                    goToFeedActivity();
                }
            });
        }
    }
    private void addUserToRealTimeDataBase(User user) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Successfully Registered.", Toast.LENGTH_LONG).show();
                    goToFeedActivity();
                } else {
                    Toast.makeText(getActivity(), "Failed to add user to db", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void goToFeedActivity(){
        Model.instance.setCurrentUser(new Model.UserListener() {
            @Override
            public void onComplete(User user) {
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
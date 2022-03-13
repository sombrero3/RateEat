package com.example.rateeat.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rateeat.MainActivity;
import com.example.rateeat.R;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    Button loginBtn, signUpBtn;
    TextView forgotTv;
    EditText emailEt, passwordEt;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginBtn = view.findViewById(R.id.login_btn);
        signUpBtn = view.findViewById(R.id.login_signup_btn);
        forgotTv = view.findViewById(R.id.login_forgot_tv);
        emailEt = view.findViewById(R.id.login_email_et);
        passwordEt = view.findViewById(R.id.login_password_et);
        progressBar = view.findViewById(R.id.login_prog);

        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });

        loginBtn.setOnClickListener((v)->{
            login();
        });

        forgotTv.setOnClickListener((v) -> {
            String email = emailEt.getText().toString().trim();
            if(email.isEmpty()){
                email = "";
            }
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment(email));
        });
        return view;
    }

    private void login() {
        String email = emailEt.getEditableText().toString();
        String password =passwordEt.getEditableText().toString();
        if(validation(email,password)){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //  toFeedActivityWithEmailVerification();
                        toFeedActivity();
                    }else{
                        Toast.makeText(getActivity(),"Failed To Login",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private boolean validation(String email,String password) {
        if(email.isEmpty()){
            emailEt.setError("Email address is required");
            emailEt.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Please provide a valid email");
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

    private void toFeedActivity() {
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

    public void toFeedActivityWithEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.isEmailVerified()) {
            toFeedActivity();
        }else{
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Please check your email to verify your account!!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"Something went wrong please try again!!",Toast.LENGTH_LONG).show();//                       progressBar.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
package com.example.rateeat.login;

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
import android.widget.Toast;

import com.example.rateeat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {
    EditText emailEt;
    Button confirmBtn;
    ProgressBar prog;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_forgot_password, container, false);
        String email = ForgotPasswordFragmentArgs.fromBundle(getArguments()).getEmail();
        emailEt = view.findViewById(R.id.forgot_password_email_et);
        confirmBtn = view.findViewById(R.id.forgot_password_btn);
        prog = view.findViewById(R.id.forgot_password_prog);
        prog.setVisibility(View.GONE);

        emailEt.setText(email);
        mAuth = FirebaseAuth.getInstance();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        return view;
    }

    private void resetPassword() {
        String email = emailEt.getText().toString().trim();
        if(email.isEmpty()){
            emailEt.setError("Email address is required");
            emailEt.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Please provide valid email");
            emailEt.requestFocus();
            return;
        }
        prog.setVisibility(View.VISIBLE);
        confirmBtn.setEnabled(false);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Pleas check your email to reset your password",Toast.LENGTH_LONG).show();
                    Navigation.findNavController(prog).navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment());
                }else{
                    Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                    confirmBtn.setEnabled(true);
                }
                prog.setVisibility(View.GONE);
            }
        });
    }
}
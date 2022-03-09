package com.example.rateeat.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rateeat.R;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProfileFragment extends Fragment {
    EditText nameEditEt;
    TextView nameTv,emailTv;
    ProgressBar prog;
    ImageView imageIv,editIv,uploadImageIv,confirmNameIv;
    String userId;
    User user;
    Boolean flagEdit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userId = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();

        nameEditEt = view.findViewById(R.id.user_profile_edit_name_et);
        nameTv = view.findViewById(R.id.user_profile_name_tv);
        emailTv = view.findViewById(R.id.user_profile_email_tv);
        prog = view.findViewById(R.id.user_profile_prog);
        imageIv = view.findViewById(R.id.user_profile_imge_iv);
        editIv = view.findViewById(R.id.user_profile_edit_iv);
        uploadImageIv = view.findViewById(R.id.user_profile_change_image_iv);
        confirmNameIv = view.findViewById(R.id.user_profile_confirm_name_iv);
        confirmNameIv.setEnabled(false);
        confirmNameIv.setVisibility(View.GONE);
        nameEditEt.setVisibility(View.GONE);
        nameEditEt.setEnabled(false);


        editIv.setOnClickListener((v)->{
            editName();
        });

        confirmNameIv.setOnClickListener((v)->{
            try {
                changeName();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        setUI(userId);
        setHasOptionsMenu(true);
        return view;
    }

    private void changeName() throws JsonProcessingException {
        if(!nameEditEt.getText().toString().trim().isEmpty()) {
            String name[] = nameEditEt.getText().toString().trim().split(" ");
            String lastName = "";
            user.setFirstName(name[0]);
            for (int i = 1; i < name.length; i++) {
                lastName += name[i];
            }
            user.setLastName(lastName);

            Model.instance.changeUserNameToReviews(user,user.getFirstName()+ " "+ user.getLastName(), new Model.AddUserListener() {
                @Override
                public void onComplete() throws JsonProcessingException {


                    Model.instance.updateUser(user, new Model.AddUserListener() {
                        @Override
                        public void onComplete() {
                            nameTv.setVisibility(View.VISIBLE);
                            nameTv.setEnabled(true);
                            nameTv.setText(user.getFirstName() + " " + user.getLastName());
                            nameEditEt.setVisibility(View.GONE);
                            nameEditEt.setEnabled(false);
                            confirmNameIv.setVisibility(View.GONE);
                            confirmNameIv.setEnabled(false);
                            editIv.setVisibility(View.VISIBLE);
                            editIv.setEnabled(true);
                        }
                    });
                }
            });


        }else{
            nameEditEt.setError("Name is required");
            nameEditEt.requestFocus();
        }
    }

    private void editName() {
        nameTv.setVisibility(View.GONE);
        nameTv.setEnabled(false);
        nameEditEt.setVisibility(View.VISIBLE);
        nameEditEt.setEnabled(true);
        confirmNameIv.setVisibility(View.VISIBLE);
        confirmNameIv.setEnabled(true);
        editIv.setVisibility(View.GONE);
        editIv.setEnabled(false);
    }

    private void setUI(String userId) {
        if(!userId.equals(Model.instance.getSignedUser().getId())){
            editIv.setEnabled(false);
            editIv.setVisibility(View.GONE);
            uploadImageIv.setEnabled(false);
            uploadImageIv.setVisibility(View.GONE);
        }

        Model.instance.getUserById(userId, new Model.getUserByIdListener() {
            @Override
            public void onComplete(User u) {
                user = new User(u);
                nameTv.setText(user.getFirstName() + " " + user.getLastName());
                emailTv.setText(user.getEmail());
                setFavoriteDishes();
            }
        });

    }

    private void setFavoriteDishes() {
        prog.setVisibility(View.GONE);

    }
    public void onPrepareOptionsMenu (Menu menu) {
        if(userId.equals(Model.instance.getSignedUser().getId())) {
            menu.findItem(R.id.feed_menu_profile).setEnabled(false);
        }
    }
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.not_home_menu,menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
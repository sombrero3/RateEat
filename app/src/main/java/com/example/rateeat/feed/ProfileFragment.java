package com.example.rateeat.feed;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rateeat.R;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.example.rateeat.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class ProfileFragment extends Fragment {
    EditText nameEditEt;
    TextView nameTv,emailTv;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageIv,editIv,confirmNameIv;
    String userId;
    Button reviewsBtn;
    User user;
    static final int REQUEST_IMAGE_CAPTURE = 1,REQUEST_GALLERY =2;
    Bitmap imageBitmap;
    ImageButton cameraBtn,galleryBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        userId = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();

        nameEditEt = view.findViewById(R.id.user_profile_edit_name_et);
        nameTv = view.findViewById(R.id.user_profile_name_tv);
        emailTv = view.findViewById(R.id.user_profile_email_tv);
        swipeRefreshLayout = view.findViewById(R.id.profile_swipe_refresh);
        imageIv = view.findViewById(R.id.user_profile_imge_iv);
        editIv = view.findViewById(R.id.user_profile_edit_iv);
        reviewsBtn = view.findViewById(R.id.user_profile_all_reviews_btn);
        cameraBtn = view.findViewById(R.id.user_profile_change_image_ibtn);
        galleryBtn = view.findViewById(R.id.user_profile_gallery_ibtn);
        confirmNameIv = view.findViewById(R.id.user_profile_confirm_name_iv);
        confirmNameIv.setEnabled(false);
        confirmNameIv.setVisibility(View.GONE);
        nameEditEt.setVisibility(View.GONE);
        nameEditEt.setEnabled(false);
        reviewsBtn.setEnabled(false);

        editIv.setOnClickListener((v)->{
            editNameUI();
        });

        confirmNameIv.setOnClickListener((v)->{
            try {
                changeName();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        reviewsBtn.setOnClickListener((v)->{
            Bundle args = new Bundle();
            args.putString("userId", userId);
            Navigation.findNavController(v).navigate(R.id.action_global_myListFragment,args);
        });

        cameraBtn.setOnClickListener(v -> {
            openCamera();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        swipeRefreshLayout.setOnRefreshListener(()-> refreshUI(userId));
        refreshUI(userId);
        setHasOptionsMenu(true);
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
                Model.instance.saveImage(imageBitmap,  user.getId()+ ".jpg",Model.IMAGE_USER_COLLECTION, url -> {
                    user.setImageUrl(url);
                    Model.instance.updateUser(user, new Model.VoidListener() {
                        @Override
                        public void onComplete() throws JsonProcessingException {

                        }
                    });
                });

            }
        }
        else if(requestCode==REQUEST_GALLERY){
            if(resultCode==RESULT_OK){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream =getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    imageIv.setImageBitmap(imageBitmap);
                    Model.instance.saveImage(imageBitmap,  user.getId()+ ".jpg",Model.IMAGE_USER_COLLECTION, url -> {
                        user.setImageUrl(url);
                        Model.instance.updateUser(user, new Model.VoidListener() {
                            @Override
                            public void onComplete() throws JsonProcessingException {
                            }
                        });
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Failed to select image from gallery",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void changeName() throws JsonProcessingException {
        if(!nameEditEt.getText().toString().trim().isEmpty()) {
            String name[] = nameEditEt.getText().toString().trim().split(" ");
            String lastName = "";
            user.setFirstName(name[0]);
            for (int i = 1; i < name.length; i++) {
                if(i==1){
                    lastName += name[i];
                }else{
                    lastName += " "+name[i];
                }
            }
            user.setLastName(lastName);
            Model.instance.changeUserNameToReviews(user,user.getFirstName()+ " "+ user.getLastName(), new Model.VoidListener() {
                @Override
                public void onComplete() throws JsonProcessingException {
                    Model.instance.updateUser(user, new Model.VoidListener() {
                        @Override
                        public void onComplete() {
                            defaultUI();
                        }
                    });
                }
            });


        }else{
            nameEditEt.setError("Name is required");
            nameEditEt.requestFocus();
        }
    }

    private void editNameUI() {
        nameTv.setVisibility(View.GONE);
        nameTv.setEnabled(false);
        nameEditEt.setVisibility(View.VISIBLE);
        nameEditEt.setText(user.getFirstName()+" "+user.getLastName());
        nameEditEt.setEnabled(true);
        confirmNameIv.setVisibility(View.VISIBLE);
        confirmNameIv.setEnabled(true);
        editIv.setVisibility(View.GONE);
        editIv.setEnabled(false);
    }
    private void defaultUI(){
        nameTv.setVisibility(View.VISIBLE);
        nameTv.setEnabled(true);
        nameTv.setText(user.getFirstName() + " " + user.getLastName());
        nameEditEt.setVisibility(View.GONE);
        nameEditEt.setEnabled(false);
        confirmNameIv.setVisibility(View.GONE);
        confirmNameIv.setEnabled(false);
        editIv.setVisibility(View.VISIBLE);
        editIv.setEnabled(true);
        reviewsBtn.setText("To "+user.getFirstName()+"'s Reviews");
        reviewsBtn.setEnabled(true);
    }


    private void refreshUI(String userId) {
        swipeRefreshLayout.setRefreshing(true);
        if(!userId.equals(Model.instance.getSignedUser().getId())){
            editIv.setEnabled(false);
            editIv.setVisibility(View.GONE);
            cameraBtn.setEnabled(false);
            cameraBtn.setVisibility(View.GONE);
            galleryBtn.setEnabled(false);
            galleryBtn.setVisibility(View.GONE);
        }

        Model.instance.getUserById(userId, new Model.UserListener() {
            @Override
            public void onComplete(User u) {
                user = new User(u);
                nameTv.setText(user.getFirstName() + " " + user.getLastName());
                emailTv.setText(user.getEmail());
                reviewsBtn.setText("To "+user.getFirstName()+"'s Reviews");
                imageIv.setImageResource(R.drawable.falafel);
                if(user.getImageUrl()!=null) {
                    Picasso.get().load(user.getImageUrl()).into(imageIv);
                }
                reviewsBtn.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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
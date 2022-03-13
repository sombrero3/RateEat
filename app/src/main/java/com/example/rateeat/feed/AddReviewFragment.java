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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rateeat.R;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.example.rateeat.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddReviewFragment extends Fragment {
    EditText restaurantEt, dishEt, descriptionEt;
    Button postReviewBtn, uploadImgBtn;
    TextView locationTv, ratingTv;
    ImageView locationIv, image, star1, star2, star3, star4, star5,addImage;
    ProgressBar prog;
    boolean flagStar1, flagStar2, flagStar3, flagStar4, flagStar5;
    static final int REQUEST_IMAGE_CAPTURE = 1,REQUEST_GALLERY =2;
    Bitmap imageBitmap;
    ImageButton cameraBtn,galleryBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_review, container, false);
        postReviewBtn = view.findViewById(R.id.add_review_postReview_btn);
        uploadImgBtn = view.findViewById(R.id.add_review_uploadImg_btn);
        restaurantEt = view.findViewById(R.id.add_review_restaurant_et);
        dishEt = view.findViewById(R.id.add_review_dish_et);
        descriptionEt = view.findViewById(R.id.add_review_description_et);
        locationIv = view.findViewById(R.id.user_row_img);
        locationTv = view.findViewById(R.id.new_review_location_tv);
        ratingTv = view.findViewById(R.id.add_review_rating_tv);
//        cameraBtn = view.findViewById(R.id.add_review_uploadImg_btn);
        prog = view.findViewById(R.id.add_review_prog);
        //image = view.findViewById(R.id.add_review_img_iv);
        star1 = view.findViewById(R.id.add_review_star1_iv);
        star2 = view.findViewById(R.id.add_review_star2_iv);
        star3 = view.findViewById(R.id.add_review_star3_iv);
        star4 = view.findViewById(R.id.add_review_star4_iv);
        star5 = view.findViewById(R.id.add_review_star5_iv);
        addImage = view.findViewById(R.id.add_review_img_iv);
        prog.setVisibility(View.GONE);

        postReviewBtn.setOnClickListener((v)->{
            try {
                addReview();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        uploadImgBtn.setOnClickListener(v -> {
            //openCamera();
        });

//        galleryBtn.setOnClickListener(v -> {
//            openGallery();
//        });
        setStarsOnClick();
        setHasOptionsMenu(true);
        return view;
    }

    private void addReview() throws JsonProcessingException {
        String restaurant = restaurantEt.getText().toString().trim();
        String dish = dishEt.getText().toString().trim();
        String description = descriptionEt.getText().toString().trim();
        String rating = ratingTv.getText().toString().trim();
        if(validation(restaurant,dish,description)){
            prog.setVisibility(View.VISIBLE);
            disableButtons();
            User user = Model.instance.getSignedUser();
            Review review = new Review(user.getId(), user.getFirstName()+ " " + user.getLastName(), restaurant, dish, rating, description );
            Model.instance.addReview(review, new Model.VoidListener() {
                @Override
                public void onComplete() {
                    prog.setVisibility(View.GONE);
                    Navigation.findNavController(prog).navigate(AddReviewFragmentDirections.actionAddReviewFragmentToMyListFragment(user.getId()));
                }
            });
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==REQUEST_IMAGE_CAPTURE){         //Back from the camera
//            if(resultCode==RESULT_OK){                   //It worked
//                Bundle extras = data.getExtras();
//                imageBitmap = (Bitmap) extras.get("data");
//                addImage.setImageBitmap(imageBitmap);
//
//            }
//        }else if(requestCode == REQUEST_GALLERY){
//            if(resultCode == RESULT_OK){
//                try{
//                    final Uri imageUri = data.getData();
//                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
//                    imageBitmap = BitmapFactory.decodeStream(imageStream);
//                    addImage.setImageBitmap(imageBitmap);
//                }catch(FileNotFoundException e){
//                    e.printStackTrace();
//                    Toast.makeText(getContext(),"Failed to select image from gallery", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//    private void openGallery() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent,REQUEST_GALLERY);
//    }
//
//
//    private void openCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
//    }

    private void disableButtons() {
        postReviewBtn.setEnabled(false);
        uploadImgBtn.setEnabled(false);
    }
    public void enableButtons(){
        postReviewBtn.setEnabled(true);
        uploadImgBtn.setEnabled(true);
    }

    private boolean validation(String restaurantName,String dishName,String description) {
        if(restaurantName.isEmpty()){
            restaurantEt.setError("Restaurant is required");
            restaurantEt.requestFocus();
            return false;
        }
        if(dishName.isEmpty()){
            dishEt.setError("Dish is required");
            dishEt.requestFocus();
            return false;
        }
        if(description.isEmpty()){
            descriptionEt.setError("Description is required");
            descriptionEt.requestFocus();
            return false;
        }

        return true;
    }

    public void setStarsOnClick() {
        flagStar1 = false;
        star1.setOnClickListener((v) -> {
            if (!flagStar1) {
                ratingTv.setText("1");
                flagStar1 = true;
            } else {
                ratingTv.setText("0.5");
                flagStar1 = false;
            }
        });

        flagStar2 = false;
        star2.setOnClickListener((v) -> {
            if (!flagStar2) {
                ratingTv.setText("2");
                flagStar2 = true;
            } else {
                ratingTv.setText("1.5");
                flagStar2 = false;
            }
        });

        flagStar3 = false;
        star3.setOnClickListener((v) -> {
            if (!flagStar3) {
                ratingTv.setText("3");
                flagStar3 = true;
            } else {
                ratingTv.setText("2.5");
                flagStar3 = false;
            }
        });

        flagStar4 = false;
        star4.setOnClickListener((v) -> {
            if (!flagStar4) {
                ratingTv.setText("4");
                flagStar4 = true;
            } else {
                ratingTv.setText("3.5");
                flagStar4 = false;
            }
        });

        flagStar5 = false;
        star5.setOnClickListener((v) -> {
            if (!flagStar5) {
                ratingTv.setText("5");
                flagStar5 = true;
            } else {
                ratingTv.setText("4.5");
                flagStar5 = false;
            }
        });
    }



    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.feed_menu_add_review).setEnabled(false);
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
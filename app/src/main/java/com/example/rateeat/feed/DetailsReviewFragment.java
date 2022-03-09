package com.example.rateeat.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rateeat.R;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.fasterxml.jackson.core.JsonProcessingException;

public class DetailsReviewFragment extends Fragment {
    ImageView imageIv,editIv,deleteIv,star1,star2,star3,star4,star5;
    TextView dishTv,userNameTv,descriptionTv,restaurantTv,ratingTv;
    String reviewId;
    ProgressBar prog;
    Review review;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details_review, container, false);

        reviewId = DetailsReviewFragmentArgs.fromBundle(getArguments()).getReviewId();

        prog = view.findViewById(R.id.review_details_prog);
        prog.setVisibility(View.VISIBLE);
        editIv = view.findViewById(R.id.review_details_edit_iv);
        deleteIv = view.findViewById(R.id.review_details_delete_iv);
        restaurantTv = view.findViewById(R.id.review_details_restaurant_name_tv);
        dishTv = view.findViewById(R.id.review_details_dish_name_tv);
        userNameTv = view.findViewById(R.id.review_details_user_name_tv);
        descriptionTv = view.findViewById(R.id.review_details_description_tv);
        ratingTv = view.findViewById(R.id.review_details_rating_tv);
        imageIv = view.findViewById(R.id.review_details_dish_img);
        star1 = view.findViewById(R.id.review_details_star1_iv);
        star2 = view.findViewById(R.id.review_details_star2_iv);
        star3 = view.findViewById(R.id.review_details_star3_iv);
        star4 = view.findViewById(R.id.review_details_star4_iv);
        star5 = view.findViewById(R.id.review_details_star5_iv);

        setReview();

        editIv.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(DetailsReviewFragmentDirections.actionDetailsReviewFragmentToEditReviewFragment(reviewId));
        });
        deleteIv.setOnClickListener((v)->{
            try {
                deleteReview();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    private void setReview() {

        Model.instance.getReviewById(reviewId, new Model.GetReviewByIdListener() {
            @Override
            public void onComplete(Review rev) {
                review = new Review(rev);
                if(!review.getUserId().equals(Model.instance.getSignedUser().getId())){
                    deleteIv.setEnabled(false);
                    deleteIv.setVisibility(View.GONE);
                }
                Model.instance.setStarByRating(review.getRating(),star1,star2,star3,star4,star5,ratingTv);
                restaurantTv.setText(review.getRestaurantName());
                dishTv.setText(review.getDishName());
                userNameTv.setText(review.getUserName());
                descriptionTv.setText(review.getDescription());
                prog.setVisibility(View.GONE);
            }
        });
    }

    private void deleteReview() throws JsonProcessingException {
        review.setDeleted(true);
        Model.instance.updateReview(review, new Model.AddReviewListener() {
            @Override
            public void onComplete() {
                Navigation.findNavController(restaurantTv).navigate(DetailsReviewFragmentDirections.actionDetailsReviewFragmentToGeneralListFragment());
            }
        });
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
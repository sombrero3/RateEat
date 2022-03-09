package com.example.rateeat.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.example.rateeat.adapters.UserReviewAdapter;
import com.example.rateeat.adapters.OnItemClickListener;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.example.rateeat.model.User;

import java.util.LinkedList;
import java.util.List;

public class UserReviewsFragment extends Fragment {
    List<Review> reviewList;
    UserReviewAdapter adapter;
    TextView nameTv, emailTv;
    ImageView image;
    User user;
    ProgressBar prog;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_list, container, false);
        prog = view.findViewById(R.id.my_list_prog);
        userId="";
        userId = UserReviewsFragmentArgs.fromBundle(getArguments()).getUserId();

        reviewList = new LinkedList<>();
        RecyclerView list = view.findViewById(R.id.my_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserReviewAdapter(reviewList);
        list.setAdapter(adapter);
        nameTv = view.findViewById(R.id.my_list_name_tv);
        emailTv = view.findViewById(R.id.my_list_email_tv);
        image = view.findViewById(R.id.my_list_row_img);


        setUI(userId);

        nameTv.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(UserReviewsFragmentDirections.actionGlobalProfileFragment(userId));
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String reviewId = reviewList.get(position).getId();
                Log.d("TAG","review clicked id: " + reviewId);
                Navigation.findNavController(v).navigate(UserReviewsFragmentDirections.actionGlobalDetailsReviewFragment(reviewId));
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    private void setReviewList(String userId) {
        prog.setVisibility(View.VISIBLE);
        Model.instance.getUserReviews(userId,new Model.GetReviewsListListener() {
            @Override
            public void onComplete(List<Review> reviews) {
                reviewList.clear();
                reviewList.addAll(reviews);
                adapter.notifyDataSetChanged();
                prog.setVisibility(View.GONE);
            }
        });

    }

    public void setUI(String userId){

        String signedUserId = Model.instance.getSignedUser().getId();
        if(userId.equals(signedUserId)){
            user = Model.instance.getSignedUser();
            setUserUi(user);
        }else{
            Model.instance.getUserById(userId, new Model.getUserByIdListener() {
                @Override
                public void onComplete(User u) {
                    user = new User(u);
                    setUserUi(user);
                }
            });
        }
    }

    private void setUserUi(User user) {
        nameTv.setText(user.getFirstName() + " " + user.getLastName());
        emailTv.setText(user.getEmail());
        setReviewList(user.getId());
    }

    public void onPrepareOptionsMenu (Menu menu) {
        if(userId.equals(Model.instance.getSignedUser().getId())) {
            menu.findItem(R.id.feed_menu_my_reviews).setEnabled(false);
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
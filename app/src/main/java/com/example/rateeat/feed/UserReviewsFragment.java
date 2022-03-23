package com.example.rateeat.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rateeat.R;
import com.example.rateeat.adapters.UserReviewAdapter;
import com.example.rateeat.adapters.OnItemClickListener;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.example.rateeat.model.User;
import com.example.rateeat.view_models.UserReviewsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserReviewsFragment extends Fragment {
    UserReviewAdapter adapter;
    TextView nameTv, emailTv,titleTv;
    ImageView image;
    SwipeRefreshLayout swipeRefreshLayout;
    String userId;
    UserReviewsViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(UserReviewsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.user_list_swipe_refresh);

        userId="";
        userId = UserReviewsFragmentArgs.fromBundle(getArguments()).getUserId();

        RecyclerView list = view.findViewById(R.id.user_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserReviewAdapter(viewModel.getUserReviewList());
        list.setAdapter(adapter);
        nameTv = view.findViewById(R.id.user_list_name_tv);
        emailTv = view.findViewById(R.id.user_list_email_tv);
        image = view.findViewById(R.id.user_list_img_iv);
        titleTv = view.findViewById(R.id.user_list_reviews_title_tv);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String reviewId = viewModel.getUserReviewList().getValue().get(position).getId();
                Log.d("TAG","review clicked id: " + reviewId);
                Navigation.findNavController(v).navigate(UserReviewsFragmentDirections.actionGlobalDetailsReviewFragment(reviewId));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(()->{
            refresh(userId);
        });

        viewModel.getUserReviewList().observe(getViewLifecycleOwner(), (Observer<List<Review>>) reviews -> {
            adapter.notifyDataSetChanged();
        });

        swipeRefreshLayout.setRefreshing(Model.instance.getReviewListLoadingState().getValue()==Model.ReviewListLoadingState.loading);
        Model.instance.getReviewListLoadingState().observe(getViewLifecycleOwner(), reviewListLoadingState -> {
            if(reviewListLoadingState== Model.ReviewListLoadingState.loading){
                swipeRefreshLayout.setRefreshing(true);
            }else{
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        nameTv.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(UserReviewsFragmentDirections.actionGlobalProfileFragment(userId));
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Model.instance.refreshReviewsList();
        refresh(userId);
    }

    public void refresh(String userId){
        viewModel.setReviewList(userId);
        refreshUI(userId);
         //adapter.notifyDataSetChanged();
    }
    public void refreshUI(String userId){
        swipeRefreshLayout.setRefreshing(true);
        Model.instance.getUserById(userId, new Model.UserListener() {
            @Override
            public void onComplete(User user) {
                setUserUi(user);
            }
        });
    }
    private void setUserUi(User user) {
        nameTv.setText(user.getFirstName() + " " + user.getLastName());
        emailTv.setText(user.getEmail());
        titleTv.setText("All "+user.getFirstName()+"'s reviews :");
        image.setImageResource(R.drawable.falafel);
        if(user.getImageUrl()!=null) {
            Picasso.get().load(user.getImageUrl()).into(image);
        }
        setReviewList(user.getId());
    }
    private void setReviewList(String userId) {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
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
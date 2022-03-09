package com.example.rateeat.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rateeat.R;
import com.example.rateeat.adapters.MyReviewAdapter;
import com.example.rateeat.adapters.ReviewAdapter;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;

import java.util.LinkedList;
import java.util.List;

public class MyListFragment extends Fragment {
    List<Review> reviewList;
    MyReviewAdapter adapter;
    TextView nameTv, emailTv;
    ImageView image;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_list, container, false);

        reviewList = new LinkedList<>();
        RecyclerView list = view.findViewById(R.id.my_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyReviewAdapter(reviewList);
        list.setAdapter(adapter);
        nameTv = view.findViewById(R.id.my_list_name_tv);
        emailTv = view.findViewById(R.id.my_list_email_tv);
        image = view.findViewById(R.id.my_list_img);


        setUser();
        setReviewList();
        return view;
    }
    public void setUser(){
        user = Model.instance.getSignedUser();

        nameTv.setText();
    }
}
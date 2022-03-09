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
import android.widget.ProgressBar;

import com.example.rateeat.R;
import com.example.rateeat.adapters.OnItemClickListener;
import com.example.rateeat.adapters.ReviewWithUserNameAdapter;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.Review;
import com.example.rateeat.view_holders.ReviewWithUserNameViewHolder;

import java.util.LinkedList;
import java.util.List;


public class GeneralListFragment extends Fragment {
    List<Review> reviewList;
    ReviewWithUserNameAdapter adapter;
    ProgressBar prog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_list, container, false);


        prog = view.findViewById(R.id.general_list_prog);
        reviewList = new LinkedList<>();
        RecyclerView list = view.findViewById(R.id.general_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReviewWithUserNameAdapter(reviewList);
        list.setAdapter(adapter);

        setReviewList();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String reviewId = reviewList.get(position).getId();
                Log.d("TAG","review clicked id: " + reviewId);
                Navigation.findNavController(v).navigate(GeneralListFragmentDirections.actionGeneralListFragmentToDetailsReviewFragment(reviewId));

            }
        });
        setHasOptionsMenu(true);
        return view;
    }
    private void setReviewList() {
        prog.setVisibility(View.VISIBLE);
        Model.instance.getAllReviews(new Model.GetAllReviewsListListener() {
            @Override
            public void onComplete(List<Review> reviews) {
                reviewList.clear();
                reviewList.addAll(reviews);
                adapter.notifyDataSetChanged();
                prog.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu,menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
package com.example.rateeat.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rateeat.R;
import com.example.rateeat.login.LoginActivity;
import com.example.rateeat.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;
public class FeedActivity extends AppCompatActivity {
    NavController navCtl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.feed_navhost);
        navCtl = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navCtl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.feed_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item)){
            Bundle args = new Bundle();
            switch (item.getItemId()){
                case android.R.id.home:
                    navCtl.navigateUp();
                    return true;
                case R.id.not_home_menu_home:
                    Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                    navCtl.navigate(R.id.action_global_generalListFragment);
                    return true;
                case R.id.feed_menu_profile:
                    Toast.makeText(this,"My Profile",Toast.LENGTH_SHORT).show();
                    args.putString("userId", Model.instance.getSignedUser().getId());
                    navCtl.navigate(R.id.action_global_profileFragment,args);
                    return true;
                case R.id.feed_menu_add_review:
                    Toast.makeText(this,"New Review",Toast.LENGTH_SHORT).show();
//                  args.putString("", "");
                    navCtl.navigate(R.id.action_global_addReviewFragment);
                    return true;
                case R.id.feed_menu_my_reviews:
                    Toast.makeText(this,"My Reviews",Toast.LENGTH_SHORT).show();
                    args.putString("userId", Model.instance.getSignedUser().getId());
                    navCtl.navigate(R.id.action_global_myListFragment,args);
                    return true;
                case R.id.feed_menu_logout:
                    Toast.makeText(this,"Logging Out",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
        }
        else{
            return true;

        }
        return false;
    }

}
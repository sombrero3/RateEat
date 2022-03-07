package com.example.rateeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rateeat.feed.FeedActivity;
import com.example.rateeat.login.LoginActivity;
import com.example.rateeat.model.Model;
import com.example.rateeat.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model.instance.executor.execute(()->{
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Model.instance.isSignedIn()){
                Model.instance.mainThread.post(()->{
                    Model.instance.setCurrentUser(new Model.setCurrentUserListener() {
                        @Override
                        public void onComplete(User user) {
                            toFeedActivity();
                        }
                    });
                });
            }else{
                Model.instance.mainThread.post(()-> {
                    toLoginActivity();
                });
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.stockapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * SPLASH ACTIVITY class : It is the splash screen that is displayed upon starting the application.
 * After 3 seconds, it passes the intent to the MAIN ACTIVITY class.
 */
public class SplashActivity extends AppCompatActivity {

    private TextView mTextView;
    //setting it for 3 seconds
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //hiding the action bar
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {

            boolean connected = false;

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
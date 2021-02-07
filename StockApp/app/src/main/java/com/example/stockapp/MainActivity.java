package com.example.stockapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
/**
This is the main activity class which sets the activity_main.xml as the layout.
 First it checks whether the phone is connected to the internet or not, If not, it will display a snackbar
 prompting the user to connect to internet and then try again.

 The activity_main.xml has the layout of content_main.xml set which then makes FirstFragment
 as the first main activity.
 Structure => activity_main.xml -> content_main.xml -> fragment_first.xml

 */
public class MainActivity extends AppCompatActivity {


    /*
    Checking initially, if the internet connection is there on the mobile phone or not.
    If there isn't, a snack bar is displayed.
    If there is, application runs normally.
     */
    public boolean internetConnection(){
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking Internet Connection
        if(internetConnection()) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        /*
        Else, showing a snack bar, if there isn't any internet connection and the user turns on
        the internet and presses try again button, it sets the layout.
         */

        else
        {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    "No internet connection.",
                    Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.design_default_color_error));
            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(internetConnection()) {
                        setContentView(R.layout.activity_main);
                        Toolbar toolbar = findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                    }
                }
            }).show();
        }


    }

    /*
    This option is kept here just to expand the application functionality in the future.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    This option is kept here just to expand the application functionality in the future.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
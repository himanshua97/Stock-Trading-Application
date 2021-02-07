package com.example.stockapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

/**
 * This is the first fragment class which is the main class that is loaded in the MainActivity
 * after the Splash Screen. On this page, there are three buttons and a search bar [Edit Text]
 * and a text View allowing the user to add the ticker for the company.
 * Upon clicking on any of the buttons, they are being redirected to their respective fragments.
 */
public class FirstFragment extends Fragment {

    //Buttons
    Button companyInfo;
    Button stockInfo;
    Button marketNews;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        {// Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_first, container, false);
        }
    }

    //After creating of View
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final FirstFragment ma = this;

        super.onViewCreated(view, savedInstanceState);

        //Finding buttons on the screen
        companyInfo = (Button) view.findViewById(R.id.companyInfo);
        stockInfo = (Button) view.findViewById(R.id.stockData);
        marketNews = (Button) view.findViewById(R.id.marketNews);

        /*
        Button Listener on companyInfo button, gets the ticker entered by the user from the editText
        and then, puts up that in the Bundle and then sends it to the companyFragment
         */
        companyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting ticker entered by the user
                String searchTerm = ((EditText) getView().findViewById(R.id.searchTicker)).getText().toString();
                if(searchTerm == null || searchTerm == "")
                {

                }
                else {
                    Bundle args = new Bundle();
                    args.putString("searchWord", searchTerm);
                    SecondFragment ldf = new SecondFragment();
                    ldf.setArguments(args);
                    //Navigating to company Fragment
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_companyFragment, args);
                }
            }
        });
         /*
        Button Listener on stockInfo button, gets the ticker entered by the user from the editText
        and then, puts up that in the Bundle and then sends it to the stock Fragment
         */
        stockInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String searchTerm = ((EditText) getView().findViewById(R.id.searchTicker)).getText().toString();
                    Bundle args = new Bundle();
                    args.putString("searchWord", searchTerm);
                    SecondFragment ldf = new SecondFragment();
                    ldf.setArguments(args);
                    //Navigating to Second Fragment which is Stocks Fragment
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment, args);
                }

        });

        /*
        Button Listener on marketNews button, gets the ticker entered by the user from the editText
        and then, puts up that in the Bundle and then sends it to the News Sentiment Fragment
         */
        marketNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting ticker entered by the user
                String searchTerm = ((EditText) getView().findViewById(R.id.searchTicker)).getText().toString();
                Bundle args = new Bundle();
                args.putString("searchWord", searchTerm);
                SecondFragment ldf = new SecondFragment();
                ldf.setArguments(args);
                //Navigating to News Sentiment Fragment from this fragment
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_newsSentiment, args);
            }
        });
    }
}
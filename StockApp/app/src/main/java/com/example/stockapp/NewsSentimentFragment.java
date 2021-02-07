package com.example.stockapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 This is the News Sentiment Fragment for displaying the news sentiment information. It's layout is fragment_news_sentiment
 which is being set in the onCreateView method. The searchWord is passed from the first fragment
 which is then passed to the search method for retrieving the data for this searchWord.
 From the getMarketSentiment class, setNewsSentiment is being called from their which then sets the data
 into the EXPANDABLE LIST VIEW and sets the adapter for the change.
 */

public class NewsSentimentFragment extends Fragment {


    /*
    Used expandable list view here with header and Expandable List Adapter
     */
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_sentiment, container, false);
    }

    /*
    Receives the searchWord as a bundle and then calls search method on the getMarketSentiment Class.
    Also has the back button and onClickListener for that
    */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //NewsSentimentFragment object
        final NewsSentimentFragment ip = this;
        super.onViewCreated(view, savedInstanceState);
        //getting arguments
        Bundle bundle = getArguments();
        String searchWord = (String) bundle.getCharSequence("searchWord");

        //Expandable List View
        expListView = (ExpandableListView) view.findViewById(R.id.expView);
        getMarketSentiment g = new getMarketSentiment();
        g.search(searchWord, ip) ;

        //Handling back button
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NewsSentimentFragment.this)
                        .navigate(R.id.action_newsSentiment_to_FirstFragment);
            }
        });

    }

    /*
    This is the setNewsSentiment method which is called from the getMarketSentiment class.
    It receives the data from the user and puts up the expandable list view headers in the
    ListHeader array List and then makes separate array list for each header and binds them together
    in the listDataChild map.
     */
    public void setNewsSentiment(String data)
    {

        if(!data.equals("DATA NOT FOUND!!!! Please Enter correct Ticker.")) {
            //for headers
            listDataHeader = new ArrayList<String>();
            //for binding headers with array list
            listDataChild = new HashMap<String, List<String>>();

            //Receives the data, parses it and puts them into separate lists
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(data);

            //Checking if wrong input or the api doesnt have data
            if (!(jo.get("companyNewsScore").toString()).equals("null") || !(jo.get("sectorAverageBullishPercent").toString()).equals("null") || !(jo.get("sectorAverageNewsScore").toString()).equals("null")) {
                System.out.println("Data = " + data);
                TextView t = (TextView) getView().findViewById(R.id.Name);
                t.setText("Company Symbol : " + jo.get("symbol").toString());
                listDataHeader.clear();
                listDataChild.clear();

                listDataHeader.add("Buzz");
                listDataHeader.add("Sentiment");
                listDataHeader.add("General");

                //Receives the data, parses it and puts them into separate lists
                /*
                THREE DIFFERENT LIST VIEWS ARE MADE HERE:
                1. BUZZ
                2. SENTIMENT
                3. GENERAL
                these three can be seen when data is received in the application.
                 */
                JsonObject jo1 = (JsonObject) jo.get("buzz");
                List<String> buzz = new ArrayList<String>();
                buzz.add("Articles in Last Week : " + jo1.get("articlesInLastWeek").toString());
                buzz.add("Buzz : " + jo1.get("buzz").toString());
                buzz.add("Weekly Average : " + jo1.get("weeklyAverage").toString());

                JsonObject jo2 = (JsonObject) jo.get("sentiment");
                List<String> sentiment = new ArrayList<String>();
                sentiment.add("Bearish Percent : " + jo2.get("bearishPercent").toString());
                sentiment.add("Bullish Percent : " + jo2.get("bullishPercent").toString());

                List<String> general = new ArrayList<String>();
                general.add("Company Ticker : " + jo.get("symbol").toString());
                general.add("Company News Score : " + jo.get("companyNewsScore").toString());
                general.add("Sector Avg Bullish % : " + jo.get("sectorAverageBullishPercent").toString());
                general.add(" Sector Avg News Score % : " + jo.get("sectorAverageNewsScore").toString());

                //Binding them into map
                listDataChild.put(listDataHeader.get(2), general);
                listDataChild.put(listDataHeader.get(1), sentiment);
                listDataChild.put(listDataHeader.get(0), buzz);

                //setting the adapter
                listAdapter = new ExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
            } else {
                TextView t = (TextView) getView().findViewById(R.id.Name);
                t.setText("DATA NOT FOUND! Please enter again!");
            }
        }
        else
        {
            TextView t = (TextView) getView().findViewById(R.id.Name);
            t.setText("DATA NOT FOUND! Please enter again!");
        }

    }


}
package com.example.stockapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
/**
 This is the Second Fragment for displaying the STOCK DATA information. It's layout is fragment_second
 which is being set in the onCreateView method. The searchWord is passed from the first fragment
 which is then passed to the search method for retrieving the data for this searchWord.
 From the getStockData class, fetchStockData is being called from their which then sets the data
 into the LIST VIEW and sets the adapter for the change.
 */
public class SecondFragment extends Fragment {

    //ListView and arrayList declared here to show the incoming data
    ListView stockListView;
    ArrayList<String> stockPrice;
    ArrayAdapter<String> adapter;
    //stockListView
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }
    /*
    Receives the searchWord as a bundle and then calls search method on the getStcokData Class.
    Also has the back button and onClickListener for that
    */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Second Fragment object
        final SecondFragment ip = this;
        super.onViewCreated(view, savedInstanceState);

        //Getting the arguments
        Bundle bundle = getArguments();
        String searchWord = (String) bundle.getCharSequence("searchWord");

        //finding listView and initialising array list
        stockListView = (ListView) view.findViewById(R.id.stockListView);
        stockPrice = new ArrayList<>();
        adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1,stockPrice);
        stockListView.setAdapter(adapter);
        getStockData g = new getStockData();
        g.search(searchWord, ip) ;

        //Back button handled here
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

    }

    /*
    this method is called from the getStock Method which passes in the data to be set into the list view
    It parses the data into Json format and then adds the data to the array List which is then
    displayed with the simple_list_item1 adapter attached.
     */
    public void fetchStockData(String data)
    {
        System.out.println(data);
        System.out.println();
        if(!data.equals("DATA NOT FOUND!!!! Please Enter correct Ticker.")) {
            //parsing the data
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(data);
            //checking if wrong input by the user or the data isnt avaialable on API
            if (!(jo.get("h").toString()).equals("0")) {
                stockPrice.clear();
                stockPrice.add("Current Price: " + jo.get("c").toString());
                stockPrice.add("High Price: " + jo.get("h").toString());
                stockPrice.add("Low Price: " + jo.get("l").toString());
                stockPrice.add("Open Price of the day: " + jo.get("o").toString());
                stockPrice.add("Previous Close Price: " + jo.get("pc").toString());

                System.out.println("Stock Data " + stockPrice.toString());

                //Notifying the adapter that the new data is added to the array list so show it
                adapter.notifyDataSetChanged();
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
package com.example.stockapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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

/**
 This is the company Fragment for displaying the company information. It's layout is fragment_company_info
 which is being set in the onCreateView method. The searchWord is passed from the first fragment
 which is then passed to the getCompanyInfo method for retrieving the data for this searchWord.
 From the getCompanyInfo(class), showCompanyInfo is being called from their which then sets the data
 into the companyInfoList and notifies the adapter for the change.
 */
public class CompanyFragment extends Fragment {

    ListView companyInfoList;
    ArrayList<String> infoList;
    ArrayAdapter<String> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_info, container, false);
    }
/*
    Receives the searchWord as a bundle and then calls search method on the getCompanyInfo Class.
    Also has the back button and onClickListener for that
 */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //getting current Fragment's object
        final CompanyFragment ip = this;
        super.onViewCreated(view, savedInstanceState);
        //Getting Bundle arguments
        Bundle bundle = getArguments();
        String searchWord = (String) bundle.getCharSequence("searchWord");

        //Initialising ArrayList and adapter for the list element
        companyInfoList = (ListView) view.findViewById(R.id.companyInfoList);
        infoList = new ArrayList<>();
        //Simple layout Adapter
        adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, infoList);
        companyInfoList.setAdapter(adapter);
        getCompanyInfo g = new getCompanyInfo();
        //Calling search which will retrieve the data from the server
        g.search(searchWord, ip);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        //On click listener for the back button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CompanyFragment.this)
                        .navigate(R.id.action_companyFragment_to_FirstFragment);
            }
        });
    }

    /*
    This function is called from the getCompanyInfo class onPostExecute method.
    Receives the data from the server, parses it into Json Format and then, adds all of that into the
    infoList arrayList which is then displayed as a list on the android application.
     */
    public void showCompanyInfo(String data) {

        if(!data.equals("DATA NOT FOUND!!!! Please Enter correct Ticker.")) {
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(data);
            System.out.println("Data " + data.isEmpty());

        /*
        2 conditions checked here: 1. data!=null 2. data.length() > 2 [When empty array, the data returned
        is {} which is of length 2 so checking this.]

         */
            if (data != null && data.length() > 2) {
                if (!(jo.get("country")).equals("null")) {
                    infoList.clear();
                    infoList.add("Country : " + jo.get("country").toString());
                    infoList.add("Currency : " + jo.get("currency").toString());
                    infoList.add("Exchange : " + jo.get("exchange").toString());
                    infoList.add("Industry : " + jo.get("finnhubIndustry").toString());
                    infoList.add("Ipo : " + jo.get("ipo").toString());
                    infoList.add("logo : " + jo.get("logo").toString());
                    infoList.add("Market Capitalization : " + jo.get("marketCapitalization").toString());
                    infoList.add("Name : " + jo.get("name").toString());
                    infoList.add("Phone : " + jo.get("phone").toString());
                    infoList.add("Share Outstanding : " + jo.get("shareOutstanding").toString());
                    infoList.add("Ticker : " + jo.get("ticker").toString());
                    infoList.add("Web url : " + jo.get("weburl").toString());

                    adapter.notifyDataSetChanged();
                } else {
                    //Else, displaying Data not found!
                    TextView t = (TextView) getView().findViewById(R.id.Name);
                    t.setText("DATA NOT FOUND! Please enter again!");
                }
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

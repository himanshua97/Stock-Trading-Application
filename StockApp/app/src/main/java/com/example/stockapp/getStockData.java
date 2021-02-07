package com.example.stockapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 This is the getStockData class which is an AsyncTask and is called from the SecondFragment.
 Upon receiving the data from the web page (post execute), it calls the fetchStockData function in the
 Second Fragment.
 */
public class getStockData {
	SecondFragment ip = null;
	
	/*
	 * search is the public getStock method.  Its arguments are the search term, and the SecondFragment object that called it.  This provides a callback
	 * path such that the fetchStockData method in that object is called when the response is received.
	 */
	public void search(String searchTerm, SecondFragment ip) {
		this.ip = ip;
		new AsyncFlickrSearch().execute(searchTerm);
	}

	/*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
    private class AsyncFlickrSearch extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return getStockData(urls[0]);
		}

		protected void onPostExecute(String data) {
			ip.fetchStockData(data);
		}
		/*
          This method is calling the fetch function and returning the response of the link provided
        */
		private String getStockData(String ticker) {
			//encoding ticker
//			ticker = URLEncoder.encode(ticker, "UTF-8");

			String response = "";

			System.out.println(" Stock Model");
			//creating the stockURL
            /*
            This is the URL for the first project without dashboard
             */
//			String stockURL =
//					"https://shrouded-river-88620.herokuapp.com/getStockData?searchWord=" + ticker;

			/*
            This is the URL for the second project with dashboard
             */
			String stockURL =
					"https://young-savannah-60458.herokuapp.com/getStockData?searchWord=" + ticker;

			// Fetch the page
			response = fetch(stockURL);
			return response;
		}
		/*
        This fetch function takes a url String and makes an HTTP request and returns the response of
        the web page.
         */
		private String fetch(String urlString) {
			String response = "";
			try {
				URL url = new URL(urlString);
				/*
				 * Create an HttpURLConnection.  This is useful for setting headers
				 * and for getting the path of the resource that is returned (which
				 * may be different than the URL above if redirected).
				 * HttpsURLConnection (with an "s") can be used if required by the site.
				 */
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				// Read all the text returned by the server
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String str;
				// Read each line of "in" until done, adding each to "response"
				while ((str = in.readLine()) != null) {
					// str is one line of text readLine() strips newline characters
					response += str;
				}
				in.close();
			} catch (IOException e) {
				System.out.println("Eeek, an exception");
				// Do something reasonable.  This is left for students to do.
			}
			return response;
		}
	}
}

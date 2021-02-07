import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FinancialData {
    /*
    Makes the HTTP connection and retrieves the response from the web page and then returns it back
     */
    public static String fetch(String urlString) {
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

    /*
    Called from the servlet class and receives a ticker from there.
    Upon receiving the ticker, it appends that ticker to the api link and then calls the fetch function.
    Upon receiving the response, returns the response of the web page.
     */
    public String getMetricsData(String ticker) throws UnsupportedEncodingException {

        //encoding ticker
        ticker = URLEncoder.encode(ticker, "UTF-8");

        String response = "";

        System.out.println("Financial Data Model");
        //creating the stockURL

        String stockURL =
                "https://finnhub.io/api/v1/stock/metric?symbol="
                        + ticker
                        + "&metric=all&token=bue425v48v6vkac96rf0";

        // Fetch the page
        response = fetch(stockURL);
        return response;
    }

    /*
    Called from the servlet class and receives a ticker from there.
    Upon receiving the ticker, it appends that ticker to the api link and then calls the fetch function.
    Upon receiving the response, returns the response of the web page.
     */
    public String getStockData(String ticker) throws UnsupportedEncodingException {
        //encoding ticker
        ticker = URLEncoder.encode(ticker, "UTF-8");

        String response = "";

        System.out.println(" Stock Model");
        //creating the stockURL

        String stockURL =
                "https://finnhub.io/api/v1/quote?symbol="
                        + ticker
                        + "&token=bue425v48v6vkac96rf0";

        // Fetch the page
        response = fetch(stockURL);
        return response;
    }

    /*
    Called from the servlet class and receives a ticker from there.
    Upon receiving the ticker, it appends that ticker to the api link and then calls the fetch function.
    Upon receiving the response, returns the response of the web page.
     */
    //Company Profile
    public String getCompanyProfile(String ticker) throws UnsupportedEncodingException {
        //encoding ticker
        ticker = URLEncoder.encode(ticker, "UTF-8");

        String response = "";

        System.out.println("Company Profile");
        //creating the stockURL

        String profileURL =
                "https://finnhub.io/api/v1/stock/profile2?symbol="
                        + ticker
                        + "&token=bue425v48v6vkac96rf0";

        // Fetch the page
        response = fetch(profileURL);
        return response;
    }

    /*
    Called from the servlet class and receives a ticker from there.
    Upon receiving the ticker, it appends that ticker to the api link and then calls the fetch function.
    Upon receiving the response, returns the response of the web page.
     */
    //Company News Sentiment
    public String getNewsSentiment(String ticker) throws UnsupportedEncodingException {
        //encoding ticker
        ticker = URLEncoder.encode(ticker, "UTF-8");

        String response = "";

        System.out.println("News sentiment");
        //creating the stockURL
        String financialsURL =
                "https://finnhub.io/api/v1/news-sentiment?symbol="
                        + ticker
                        + "&token=bue425v48v6vkac96rf0";

        // Fetch the page
        response = fetch(financialsURL);
        return response;
    }
}

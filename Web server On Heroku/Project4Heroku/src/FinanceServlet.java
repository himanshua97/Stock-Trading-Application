import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/*
This is the main servlet of the web dashboard. It handles the 4 different links as you can see below.
Main idea: For each link: get the data from the api response, parse it into Json and then, outputs the data received.
4 links:

getMetrics : getting the metrics data from the api and filtering it out [NOT USED IN MY APPLICATION]
getStockData : get the stock prices of the ticker of the company searched
getCompanyInfo: get the company information about the ticker searched
getNewsSentiment: get the company's new sentiment like buzz, and other details
 */

@WebServlet(name = "FinanceServlet",
        urlPatterns = {"/getMetrics", "/getStockData", "/getCompanyInfo", "/getNewsSentiment"})
public class FinanceServlet extends javax.servlet.http.HttpServlet {
    FinancialData fn = null;  // The "business model" for this app

    @Override
    public void init() {
        fn = new FinancialData();
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    /*
    Receives the HTTP request and provides separate functionality according to the link requested.
    Links:
    1. /getStockData : getting stock Data
    2. /getMetrics : getting company Metrics
    3. /getCompanyInfo : getting company information
    4. /getNewsSentiment : getting news sentiment
     */
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        //Getting the ticker sent to this request
        String search = request.getParameter("searchWord");
        String nextView;
        String data = null;
        String apiResponse = null;

        /*
           Handled the getMetrics link here.
           Receiving the data from the getMetricsData function in the Financial Data Model, parses it and then
           displays it on the web page.
         */
        if ((request.getServletPath()).equals("/getMetrics")) {
            apiResponse = fn.getMetricsData(search);
            System.out.println("ApiResponse " + apiResponse);
            Gson g = new Gson();
            Map map = g.fromJson(apiResponse, Map.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); // pretty print
            String metrics = map.get("metric").toString();
            //Checking if not empty list
            if (metrics.length() != 2) {
                JsonParser jsonParser = new JsonParser();
                JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

                //Filtering out metric
                System.out.println("Metrics " + jo.get("metric"));
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jo.get("metric"));
                out.flush();
            } else {
                //DATA NOT FOUND
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String notFound = "DATA NOT FOUND!!!! Please Enter correct Ticker.";
                out.print(notFound);
            }
        }
        /*
         Handled the getStockData link here.
         Receiving the data from the getStockData function in the Financial Data Model, parses it and then
         displays it on the web page.
        */
        else if ((request.getServletPath()).equals("/getStockData")) {
            apiResponse = fn.getStockData(search);
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

            //Checking if data not found on API
            if(!(jo.get("c").toString().equals("0"))) {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jo);
                out.flush();
            }
            else
            {
                //DATA NOT FOUND
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String notFound = "DATA NOT FOUND!!!! Please Enter correct Ticker.";
                out.print(notFound);
            }

        }
        /*
           Handled the getCompanyInfo link here.
           Receiving the data from the getCompanyProfile function in the Financial Data Model, parses it and then
           displays it on the web page.
        */
        else if ((request.getServletPath()).equals("/getCompanyInfo")) {

            apiResponse = fn.getCompanyProfile(search);
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

            //Checking if not empty list
            if(!(jo.toString().equals("{}"))) {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jo);
                out.flush();
            }
            else
            {
                //DATA NOT FOUND
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String notFound = "DATA NOT FOUND!!!! Please Enter correct Ticker.";
                out.print(notFound);
            }

        }

        /*
         Handled the getNewsSentiment link here.
         Receiving the data from the getNewsSentiment function in the Financial Data Model, parses it and then
         displays it on the web page.
        */
        else if ((request.getServletPath()).equals("/getNewsSentiment")) {
            apiResponse = fn.getNewsSentiment(search);
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

            if(!(jo.get("buzz").toString().equals("null"))) {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jo);
                out.flush();
            }
            else
            {
                //DATA NOT FOUND
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String notFound = "DATA NOT FOUND!!!! Please Enter correct Ticker.";
                out.print(notFound);
            }

        }
        else
        {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String notFound = "Please enter correct Link";
            out.print(notFound);
        }
    }
}


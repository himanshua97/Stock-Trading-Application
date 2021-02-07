import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/*
This is the main servlet of the web dashboard. It handles the 5 different links as you can see below.
Main idea: For each link: get the data from the api response, parse it into Json and then, outputs the data received.

5 links:
getMetrics : getting the metrics data from the api and filtering it out [NOT USED IN MY APPLICATION]
getStockData : get the stock prices of the ticker of the company searched
getCompanyInfo: get the company information about the ticker searched
getNewsSentiment: get the company's new sentiment like buzz, and other details
getServer: my dashboard

Functionality added: Connected MongoDb in the beginning, getting the database and for each time API is hit,
6 things are stored into the mongo document object which is then written to the database.
6 things are:
1. Header: information about device used to conenct
2. Company Ticker: Company Ticker searched.
3. Api Latency: Difference between response time and request time of the request
4. Request time: timestamp of getting the HTTP request
5. Response Time: timestamp of sending out the data upon receiving request.
6. Metrics Requested : Storing which URL has been called from the user.
 */

@WebServlet(name = "FinanceServlet",
        urlPatterns = {"/getMetrics", "/getStockData", "/getCompanyInfo", "/getNewsSentiment" , "/getServer"})
public class FinanceServlet extends javax.servlet.http.HttpServlet {

    FinancialData fn = null;  // The "business model" for this app

    /*
    Connecting the MongoDb Atlas
     */
    MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://himanshua97:Johncena123@cluster0.ceriy.mongodb.net/<test>?retryWrites=true&w=majority");
    MongoClient mongoClient = new MongoClient(uri);
    //getting the database
    MongoDatabase database = mongoClient.getDatabase("logDs");
    //getting the collection
    MongoCollection<Document> collection = database.getCollection("data");
    @Override
    public void init() {
        fn = new FinancialData();
    }

    /*
    This function is used to calculate the most frequent word in an Array List. It receives an array List
    and then simply returns the most frequent string in the array List.
     */
    public String mostRepeatedWord(ArrayList<String> words)
    {
        String mostRepeatedWord
                = words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();

        return mostRepeatedWord;
    }


    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    /*
    Receives the HTTP request and provides separate functionality according to the link requested.
    Links:
    1. /getServer : our server dashboard
    2. /getStockData : getting stock Data
    3. /getMetrics : getting company Metrics
    4. /getCompanyInfo : getting company information
    5. /getNewsSentiment : getting news sentiment
     */
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String reqTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        Timestamp req = new Timestamp(System.currentTimeMillis());
        System.out.println("Request Timestamp = " + reqTime);
        Timestamp resp = new Timestamp(System.currentTimeMillis());

        String nextView;
        String data = null;
        String apiResponse = null;

        /*
        Handled the getServer link here, upon calling: It reads the documents form the MongoDB ATLAS
        and then fetches the data into different arrayLists for analytics as well as logsData which is an arrayList
        of HashMaps and every HashMap is a document received from MongoDb Atlas.

        Latency Array List : adding latency from each document
        Ticker Array List : adding ticker from each document
        Metrics : contains URLS hit by the user from the document
         */
        if ((request.getServletPath()).equals("/getServer")) {
            HashMap<String, String> logData = new HashMap<>();
            Gson gson = new Gson();
            ArrayList<HashMap> logDocs = new ArrayList<>();
            ArrayList<String> Latency = new ArrayList<>();
            ArrayList<String> ticker = new ArrayList<>();
            ArrayList<String> metrics = new ArrayList<>();
            int count = 0;
            int sum = 0;

            //Iterating over the documents
            MongoCursor<Document> cursor = collection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    //putting every document into a MAP
                    logData = gson.fromJson(cursor.next().toJson(), HashMap.class);
                    Latency.add(logData.get("Api Latency : "));
                    ticker.add(logData.get("Company Ticker : "));
                    metrics.add(logData.get("Link requested : "));
                    logDocs.add(logData);
                    count++;
                }
                System.out.println("LogDocs" + logDocs.toString());
                System.out.println("Most repeated Ticker" + mostRepeatedWord(ticker));
                System.out.println("Most URL requested" + mostRepeatedWord(metrics));

                // Calculating average Latency
                for(int i=0;i<Latency.size();i++)
                {
                    sum = sum + Integer.valueOf(Latency.get(i));
                }
                float avg = sum/count;
                //Finding most frequent ticker
                String mostTicker = mostRepeatedWord(ticker);
                //Finding most frequent URL requested
                String mostURL = mostRepeatedWord(metrics);

                //Attributes set for showing it on the dashboard
                request.setAttribute("avgLatency", "" + avg);
                request.setAttribute("mostTicker", mostTicker);
                request.setAttribute("mostURL", mostURL);
                request.setAttribute("logDocs", logDocs);

            } finally {
                cursor.close();
            }
            //Transferring view
            nextView = "index.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        } else {
            String search = request.getParameter("searchWord");
            String resTime = "";
            String linkType = "";
            String companyTicker = search;
            String header = "";
            long apiLatency = 0;
            Date d1, d2;

            /*
            Handled the getMetrics link here.
            Receiving the data from the getMetricsData function in the Financial Data Model, parses it and then
            displays it on the web page.
             */
            if ((request.getServletPath()).equals("/getMetrics")) {

                linkType = "MetricsURL";
                apiResponse = fn.getMetricsData(search);
                System.out.println("ApiResponse " + apiResponse);

                Gson g = new Gson();
                Map map = g.fromJson(apiResponse, Map.class);
                Gson gson = new GsonBuilder().setPrettyPrinting().create(); // pretty print
                String metrics = map.get("metric").toString();
                //Checking if not empty list
                if(metrics.length()!=2) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

                    //Filtering out metric
                    System.out.println("Metrics " + jo.get("metric"));
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.print(jo.get("metric"));
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
                //response time calculated here and latency which is request - response time.
                resp = new Timestamp(System.currentTimeMillis());
                apiLatency = resp.getTime() - req.getTime();
            }
           /*
            Handled the getStockData link here.
            Receiving the data from the getStockData function in the Financial Data Model, parses it and then
            displays it on the web page.
             */
            else if ((request.getServletPath()).equals("/getStockData")) {

                linkType = "StocksURL";
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
                //response time calculated here and latency which is request - response time.
                resp = new Timestamp(System.currentTimeMillis());
                apiLatency = resp.getTime() - req.getTime();

            }
            /*
            Handled the getCompanyInfo link here.
            Receiving the data from the getCompanyProfile function in the Financial Data Model, parses it and then
            displays it on the web page.
             */
            else if ((request.getServletPath()).equals("/getCompanyInfo")) {

                linkType = "CompanyURL";
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
                //response time calculated here and latency which is request - response time.
                resp = new Timestamp(System.currentTimeMillis());
                apiLatency = resp.getTime() - req.getTime();
            }
            /*
            Handled the getNewsSentiment link here.
            Receiving the data from the getNewsSentiment function in the Financial Data Model, parses it and then
            displays it on the web page.
             */
            else if ((request.getServletPath()).equals("/getNewsSentiment")) {
                linkType = "NewsURL";
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
                //response time calculated here and latency which is request - response time.
                resp = new Timestamp(System.currentTimeMillis());
                apiLatency = resp.getTime() - req.getTime();
            }
            /*
            If wrong link requested by the user on the web page:
            asking the user to enter correct link
             */
            else
            {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String notFound = "Please enter correct Link";
                out.print(notFound);
            }

            //Handling mobile part and setting attribute as docType accordingly
            boolean mobile;
            header = request.getHeader("User-Agent");

            //Appending the data into the document
            Document doc = new Document("Company Ticker : ", companyTicker)
                    .append("Link requested : ", linkType)
                    .append("Header : ", header)
                    .append("Request timestamp : ", req.toString())
                    .append("Response timestamp : ", resp.toString())
                    .append("Api Latency : ", "" + apiLatency);
            //Inserting the document into the collection
            collection.insertOne(doc);

        }
    }
}

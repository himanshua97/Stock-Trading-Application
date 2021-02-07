STOCK TRACK ANDROID APPLICATION – HIMANSHU ARORA
Whole Project Diagram:

See Project_Diagram_Android-Web image in the repository.

Project Description:
The Application displays financial information about different companies listed at New York Stock Exchange. It displays different types of stock prices of the day, previous week, company information searched and news sentiment of the stocks of the company which would help an investor to take better decisions where to invest or not. 
As of now, the functionality is limited, and the user needs to know the ticker of the companies to search for. List of tickers can be found here: http://eoddata.com/symbols.aspx
Also, In the description below, I have used mostly ticker “AMZN” representing Amazon but you can try with other tickers like “MSFT”(Microsoft) / “BAC” (Bank of America) etc. 
3 fragments:
Second fragment: Launches on clicking “Company Stocks” button.
CompanyFragment: Launches on clicking “Company Info” button.
NewsSentimentFragment: Launches on clicking “Market News” button.




1.1 3 views:
My application has Text Views, Edit Text(Search Bar), List View, Expandable List View, Splash Screen. 
Some screenshots of the application:
     
1.2 Requires Input from the user
- See screenshot 2nd from left. 

1.3 Makes an HTTP request:
My application makes a HTTP request to the web service deployed on Heroku:
Link of web service: (Example of AMZN (Amazon Ticker))
https://young-savannah-60458.herokuapp.com/getStockData?searchWord=AMZN

It returns the data fetched form the third party API and after parsing it into Json format, passes the data to my application and then data is displayed to the user.

1.4 Receives and parses a JSON formatted reply from the server:
In all the links: In the code You can see that I have parsed the apiResponse with these 2 lines:
JsonParser jsonParser = new JsonParser();
JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

For example:
/getStockData:
apiResponse = fn.getStockData(search);
JsonParser jsonParser = new JsonParser();
JsonObject jo = (JsonObject) jsonParser.parse(apiResponse);

Reply on the server screenshot:
 
Similarly, for other links as well. 
1.5 Displays information to the user:
Check screenshots, different buttons do different things. 
First Fragment has 3 buttons and upon clicking on each fragment, its been redirected to their respective fragments showing different types of views. 

1.6 Is Repeatable:
You can see a green button in the screenshots (Right 3) which redirects every fragment to return to the first fragment upon clicking that button. On the first fragment, there is no button as it is the main screen but there is 1 button on each of the 3 fragments. 

WEB SERVICE:
1.  implement a web service: Heroku [More Links can be found at last page]
•	Link : https://shrouded-river-88620.herokuapp.com/getStockData?searchWord=AMZN
•	Project directory Name: Project4Heroku

1.1. Using an HTTP servlet to implement a simple API:
•	Model: FinancialData.java
•	View: index.jsp
•	Controller: FinancialServlet.java

1.2 Receives an HTTP request from my native Android application:
FinancialServlet.java receives the HTTP GET request with the argument “searchWord” which then passes it to the model “Financial Data”.

1.3 Business Logic:
FinancialData.java then makes a request to https://finnhub.io/docs/api with the API key included.
Example of a call: (More Links can be found at last)
https://finnhub.io/api/v1/quote?symbol=AAPL&token=bue425v48v6vkac96rf0

1.4 Response to the Android application:
Fetches the data from the Heroku link and then parse the data into Json format. My application doesn’t deal with the third part API directly, it calls the Heroku link of the webservice which interacts with the third party API. 
There is a lot of data to show here on how my android application is setting the data. 

An example of getting Stock data:

The application first opens “First Fragment” which has a button “Company Stocks”. The user enters the ticker in the editText and then presses the button “Company Stocks”. Now, Upon clicking “Company Stocks”, the ticker is bundled and then sent to “SecondFragment” class which upon receiving the ticker calls its search method which lies in “getStockData” class and then fetchStockData function (in Second Fragment) is called on post execute which adds all the data into the array List and displays all in the List view in the android application.
The getStockData fetches the data from the Heroku link.

In the similar way, other fragments are working:
Binding:
Second Fragment <-> getStockData
NewsSentimentFragment <-> getMarketSentiment
CompanyFragment <-> getCompanyInfo

LINKS for TASK 1 Server: Normally deployed on Heroku without Dashboard:
Here are the links for ticker “AMZN”. You can change the tickers according to the company you want. Just remove “AMZN” and you can put “MSFT” etc. More tickers can be found at: http://eoddata.com/symbols.aspx
•	Stock Data : 
https://shrouded-river-88620.herokuapp.com/getStockData?searchWord=AMZN
•	Company Info: 
https://shrouded-river-88620.herokuapp.com/getCompanyInfo?searchWord=AMZN
•	Metrics Data: 
https://shrouded-river-88620.herokuapp.com/getMetrics?searchWord=AMZN
•	News Sentiment: 
https://shrouded-river-88620.herokuapp.com/getNewsSentiment?searchWord=AMZN

Links for server for Task 2: [Deployed on Heroku and currently used in Android application, includes Dashboard] (CURRENTLY USING IN ANDROID)
Here are the links for ticker “AMZN”. You can change the tickers according to the company you want. Just remove “AMZN” and you can put “MSFT” etc. More tickers can be found at: http://eoddata.com/symbols.aspx
•	Server: https://young-savannah-60458.herokuapp.com/getServer
•	Stock Data: 
https://young-savannah-60458.herokuapp.com/getStockData?searchWord=AMZN
•	Company Info: 
https://young-savannah-60458.herokuapp.com/getCompanyInfo?searchWord=AMZN
•	News Sentiment: 
https://young-savannah-60458.herokuapp.com/getNewsSentiment?searchWord=AMZN
•	Metrics Data:
https://young-savannah-60458.herokuapp.com/getMetrics?searchWord=AMZN


























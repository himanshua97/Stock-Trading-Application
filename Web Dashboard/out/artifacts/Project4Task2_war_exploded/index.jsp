<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%--
  Created by IntelliJ IDEA.
  User: himanshu
  Date: 2020-11-13
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body style="background-color: lightblue;">

<h1 style="font-family: arial;
           font-size: 22pt;
           color: blue;
           text-align: center;">Stock Application Dashboard</h1>

<h2 style="font-family: arial;
           font-size: 16pt;
           color: darkred;
           text-align: center;">Analytics : </h2>
<% if (request.getAttribute("avgLatency") != null) {%>
<h3 style="text-align: center">Average Latency : <%=request.getAttribute("avgLatency")%>
</h3>
<%}%>
<% if (request.getAttribute("mostTicker") != null) {%>
<h3 style="text-align: center">Most Common Ticker Searched in the Application : <%=request.getAttribute("mostTicker")%>
</h3>
<%}%>
<% if (request.getAttribute("mostURL") != null) {%>
<h3 style="text-align: center">Most metrics searched by the user : <%=request.getAttribute("mostURL")%>
</h3>
<%}%>

<table border=4 align=center style="text-align:center; background-color: aqua; border-color: red blue gold teal;">
    <thead>
    <tr>
        <th>Device Used</th>
        <th>Company Ticker</th>
        <th>Metrics Requested</th>
        <th>Request Time</th>
        <th>Response Time</th>
        <th>Latency</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (request.getAttribute("logDocs") != null) {
            ArrayList<HashMap<String, String>> logDocs = (ArrayList<HashMap<String, String>>) request.getAttribute("logDocs");
            for (int i = 0; i < logDocs.size(); i++) {
    %>
    <tr>
        <td><% if (logDocs.get(i).get("Header : ") != null) {%>
            <%=logDocs.get(i).get("Header : ")%>
            <% } else {%> Response not found<%}%>
        </td>

        <td><% if (logDocs.get(i).get("Company Ticker : ") != null) {%>
            <%=logDocs.get(i).get("Company Ticker : ")%>
            <% } else {%> Response not found<%}%>
        </td>

        <td><% if (logDocs.get(i).get("Link requested : ") != null) {%>
            <%=logDocs.get(i).get("Link requested : ")%>
            <% } else {%> Response not found<%}%>
        </td>

        <td><% if (logDocs.get(i).get("Request timestamp : ") != null) {%>
            <%=logDocs.get(i).get("Request timestamp : ")%>
            <% } else {%> Response not found<%}%>
        </td>

        <td><% if (logDocs.get(i).get("Response timestamp : ") != null) {%>
            <%=logDocs.get(i).get("Response timestamp : ")%>
            <% } else {%> Response not found<%}%>
        </td>

        <td><% if (logDocs.get(i).get("Api Latency : ") != null) {%>
            <%=logDocs.get(i).get("Api Latency : ")%>
            <% } else {%> Response not found<%}%>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <h1>Data not found</h1> <% }%>
    </tbody>
</table>
<br>

</body>
</html>

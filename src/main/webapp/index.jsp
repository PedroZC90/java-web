<%@ page import="utils.AppUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    final String base_url = AppUtils.getBaseUrl(request);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="A simple HTML5 Template for new projects.">
        <meta name="author" content="Hello World">
        <meta http-equiv="refresh" content="0; URL='<%= base_url %>/dashboard/index.jsp'" />
        <title>Java Web: Project 2</title>
        <%--<link rel="icon" href="/favicon.ico">--%>
        <%--<link rel="icon" href="/favicon.svg" type="image/svg+xml">--%>
        <%--<link rel="apple-touch-icon" href="/apple-touch-icon.png">--%>
        <link rel="stylesheet" href="<%= base_url %>/assets/css/global.css">
        <link rel="stylesheet" href="<%= base_url %>/assets/css/styles.css">
    </head>
    <body>
        <h1><%= "Hello World!" %></h1>
        <ul>
            <li><a href="hello-servlet">Hello Servlet</a></li>
            <li><a href="dashboard/index.jsp">Dashboard</a></li>
            <li><a href="costumers/index.jsp">Clientes</a></li>
            <li><a href="services/index.jsp">Serviços</a></li>
        </ul>
    </body>
</html>

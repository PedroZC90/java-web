<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="A simple HTML5 Template for new projects.">
        <meta name="author" content="Hello World">
        <meta http-equiv="refresh" content="0; URL='<%= request.getContextPath() %>/dashboard'" />
        <title>Hello World</title>
        <%--<link rel="icon" href="/favicon.ico">--%>
        <%--<link rel="icon" href="/favicon.svg" type="image/svg+xml">--%>
        <%--<link rel="apple-touch-icon" href="/apple-touch-icon.png">--%>
        <link rel="stylesheet" href="assets/css/global.css">
        <link rel="stylesheet" href="assets/css/styles.css">
    </head>
    <body>
        <h1><%= "Hello World!" %></h1>
        <ul>
            <li><a href="hello-servlet">Hello Servlet</a></li>
            <li><a href="dashboard">Dashboard</a></li>
            <li><a href="costumers">Clientes</a></li>
            <li><a href="services">Serviços</a></li>
        </ul>
    </body>
</html>

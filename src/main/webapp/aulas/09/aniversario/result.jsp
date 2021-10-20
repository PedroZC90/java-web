<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%!
    public static final Map<Integer, String> months = new TreeMap<Integer, String>() {
        {
            put(1, "Janeiro");
            put(2, "Fevereiro");
            put(3, "Março");
            put(4, "Abril");
            put(5, "Maio");
            put(6, "Junho");
            put(7, "Julho");
            put(8, "Agosto");
            put(9, "Setembro");
            put(10, "Outubro");
            put(11, "Novembro");
            put(12, "Dezembro");
        }
    };
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Java Servlet Sample">
    <meta name="author" content="Aniversario Form">
    <title>INDEX</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
    <body>
    <%
        Integer month = (Integer) session.getAttribute("month");
    %>
    <h1><%= (month != null) ? months.get(month) : "ERROR" %></h1>
    </body>
</html>

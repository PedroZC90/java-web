<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    public static final Map<Integer, String> months = new TreeMap<Integer, String>() {
        {
            put(1, "Jan");
            put(2, "Feb");
            put(3, "Mar");
            put(4, "Apr");
            put(5, "May");
            put(6, "Jun");
            put(7, "Jul");
            put(8, "Aug");
            put(9, "Sep");
            put(10, "Oct");
            put(11, "Nov");
            put(12, "Dec");
        }
    };
%>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Java Servlet Sample">
        <meta name="author" content="Aniversario Form">
        <title>Title</title>
        <link rel="stylesheet" href="../../../assets/css/styles.css">
    </head>
    <body>
        <form method="POST" action="<%= request.getContextPath() %>/test">
            <label for="month">Mês</label>
            <select id="month" name="month">
                <%
                    for (Map.Entry<Integer, String> e : months.entrySet()) {
                        out.println("<option value='" + e.getKey() + "'>" + e.getValue() + "</option>");
                    }
                %>
            </select>
            <button type="submit">Submit</button>
        </form>
    </body>
</html>

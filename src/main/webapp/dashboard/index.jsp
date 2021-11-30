<%@ page import="utils.AppUtils" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="database.CostumerDAO" %>
<%@ page import="database.ServiceDAO" %>
<%@ page import="models.Costumer" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="models.Service" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="javax.swing.text.DateFormatter" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="utils.ParametersUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final String base_url = AppUtils.getBaseUrl(request);

    final LocalDate from = ParametersUtils.asLocalDate(request, "from");
    final LocalDate to = ParametersUtils.asLocalDate(request, "to");

    final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

    final int numberOfCostumers = CostumerDAO.count(db);
    final int numberOfServices = ServiceDAO.count(db);

    final List<Costumer> cotumers = CostumerDAO.select(db, 1, 15);
    final List<Service> services = ServiceDAO.select(db, 1, 15, from, to);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="A simple HTML5 Template for new projects.">
        <meta name="author" content="Hello World">
        <title>Java Web: Project 2</title>
        <link rel="stylesheet" href="<%= base_url %>/assets/css/global.css">
        <link rel="stylesheet" href="<%= base_url %>/assets/css/styles.css">
    </head>
    <body id="dashboard">
        <ul id="menu">
            <li><a href="<%= base_url %>/dashboard/index.jsp">Dashboard</a></li>
            <li><a href="<%= base_url %>/costumers/index.jsp">Clientes</a></li>
            <li><a href="<%= base_url %>/services/index.jsp">Serviços</a></li>
        </ul>
        <h1 class="page-title">DASHBOARD</h1>
        <div class="wrapper">
            <div id="costumers" class="list">
                <div class="list-title">
                    <h2>Clientes</h2>
                    <h2><%= numberOfCostumers %>
                    </h2>
                </div>
                <div class="list-grid">
                    <%
                        for (Costumer c : cotumers) {
                            String path = String.format("%s/costumers/index.jsp?id=%d", base_url, c.getId());
                            StringBuilder sb = new StringBuilder();
                            if (StringUtils.isNotBlank(c.getAddress())) sb.append(c.getAddress());
                            if (sb.length() > 0) sb.append(", ");
                            if (StringUtils.isNotBlank(c.getNumber())) sb.append(c.getNumber());
                            if (sb.length() > 0) sb.append(", ");
                            if (StringUtils.isNotBlank(c.getComplement())) sb.append(c.getComplement());
                            if (sb.length() > 0) sb.append(", ");
                            if (StringUtils.isNotBlank(c.getDistrict())) sb.append(c.getDistrict());
                            if (sb.length() > 0) sb.append(", ");
                            if (StringUtils.isNotBlank(c.getCity())) sb.append(c.getCity());
                            if (sb.length() > 0) sb.append(", ");
                            if (StringUtils.isNotBlank(c.getState())) sb.append(c.getState());

                            out.println("<a class='list-box' href='" + path + "'>");
                            out.println("<p>Nome: " + c.getName() + "</p>");
                            out.println("<p>CPF: " + c.getCpf() + "</p>");
                            out.println("<p>Address : " + sb.toString() + "</p>");
                            out.println("</a>");
                        }
                    %>
                </div>
            </div>
            <div id="services" class="list">
                <div class="list-title">
                    <h2>Serviços</h2>
                    <h2><%= numberOfServices %></h2>
                </div>
                <form class="list-filter" action="<%= base_url %>/dashboard/index.jsp" method="GET">
                    <div class="box">
                        <label for="from">Data Inicial</label>
                        <input id="from" name="from" type="date" value="<%= from %>" />
                    </div>
                    <div class="box">
                        <label for="to">Data Final</label>
                        <input id="to" name="to" type="date" value="<%= to %>" />
                    </div>
                    <button type="submit">Filtrar</button>
                </form>
                <div class="list-grid">
                    <%
                        for (Service s : services) {
                            String css = "";
                            if (s.isCancelled()) css = " cancelled";
                            if (s.isCompleted()) css = " completed";
                            out.println("<a class='list-box" + css +"' href='" + base_url + "/services/index.jsp?id=" + s.getId() + "'>");
                            out.println("<p>#" + s.getId() + "</p>");
                            out.println("<p> Agendamento: " + s.getScheduledTo().format(FORMAT) + "</p>");
                            out.println("</a>");
                        }
                    %>
                </div>
            </div>
        </div>
    </body>
</html>

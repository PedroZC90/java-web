<%@ page import="utils.AppUtils" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="database.ServiceDAO" %>
<%@ page import="database.CostumerDAO" %>
<%@ page import="models.Costumer" %>
<%@ page import="models.Service" %>
<%@ page import="java.util.List" %>
<%@ page import="utils.ParametersUtils" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    final String base_url = AppUtils.getBaseUrl(request);

    final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

    final List<Costumer> cotumers = CostumerDAO.select(db, 1, 15);

    final Long serviceId = ParametersUtils.asLong(request, "id");

    Service service = ServiceDAO.findById(db, serviceId);
    if (service == null) {
        service = new Service();
    }
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
    <body id="service-page">
        <ul id="menu">
            <li><a href="<%= base_url %>/dashboard/index.jsp">Dashboard</a></li>
            <li><a href="<%= base_url %>/costumers/index.jsp">Clientes</a></li>
            <li><a href="<%= base_url %>/services/index.jsp">Serviços</a></li>
        </ul>
        <div class="centralized">
            <div class="form-panel">
                <h1 class="form-header">Service</h1>
                <form class="form-content" action="<%= base_url %>/servlet/services" method="POST">
                    <div class="box">
                        <label for="constumer">Cliente</label>
                        <select id="constumer" name="constumer">
                            <%
                                for (Costumer c : cotumers) {
                                    out.println("<option value='" + c.getCpf() + "'>" + c.getName() + "</option>");
                                }
                            %>
                        </select>
                    </div>
                    <div class="box">
                        <label for="installation">Qtd. p/ Instalar</label>
                        <input id="installation" name="installation" type="number" value="<%= (service.getInstallations() != null) ? service.getInstallations() : 0 %>">
                    </div>
                    <div class="box">
                        <label for="copper_tube">Comprimento de Tubo de Cobre</label>
                        <input id="copper_tube" name="copper_tube" type="number" step="0.01" value="<%= (service.getTubeLength() != null) ? service.getTubeLength() : 0.0 %>">
                    </div>
                    <div class="box">
                        <label for="removal">Qtd. p/ Remover</label>
                        <input id="removal" name="removal" type="number" value="<%= (service.getRemoval() != null) ? service.getRemoval() : 0 %>">
                    </div>
                    <div class="box">
                        <label for="maintenance">Qtd. p/ Manuteção</label>
                        <input id="maintenance" name="maintenance" type="number" value="<%= (service.getMaintenance() != null) ? service.getMaintenance() : 0 %>">
                    </div>
                    <div class="box">
                        <label for="rappel">Necessário Rapel</label>
                        <input id="rappel" name="rappel" type="checkbox" value="<%= service.isRappelRequired() %>">
                    </div>
                    <div class="box">
                        <label for="scheduler">Agendamento</label>
                        <%--"yyyy-MM-ddThh:mm" + ":ss" or ":ss.SSS"--%>
                        <input id="scheduler" name="scheduler" type="datetime-local" step="1" value="<%= ((service.getSchedulingDate() != null) ? service.getSchedulingDate() : LocalDateTime.now()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) %>">
                    </div>
                    <div class="box">
                        <label for="cost">Valor</label>
                        <input id="cost" name="cost" type="text" disabled="true" value="<%= (service.getValue() != null) ? service.getValue() : 0.0 %>">
                    </div>
                    <div class="box">
                        <button name="action" value="save" type="submit">Salvar</button>
                    </div>
                    <%
                        if (service.getId() != null) {
                            out.println("<div class='box'>");
                            out.println("<button name='action' value='complete' type='submit'>Completar</button>");
                            out.println("</div>");
                            out.println("<div class='box'>");
                            out.println("<button name='action' value='cancel' type='submit'>Cancelar</button >");
                            out.println("</div>");
                        }
                    %>
                </form>
            </div>
        </div>
    </body>
</html>
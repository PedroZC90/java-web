<%@ page import="utils.ParametersUtils" %>
<%@ page import="utils.AppUtils" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="database.CostumerDAO" %>
<%@ page import="models.Costumer" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    final String base_url = AppUtils.getBaseUrl(request);
    final Long id = ParametersUtils.asLong(request, "id");

    final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

    Costumer costumer = CostumerDAO.findById(db, id);
    if (costumer == null) {
        costumer = new Costumer();
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
    <body id="costumer-page">
        <ul id="menu">
            <li><a href="<%= base_url %>/dashboard/index.jsp">Dashboard</a></li>
            <li><a href="<%= base_url %>/costumers/index.jsp">Clientes</a></li>
            <li><a href="<%= base_url %>/services/index.jsp">Serviços</a></li>
        </ul>
        <div class="centralized">
            <div class="form-panel">
                <h1 class="form-header">Cliente</h1>
                <form class="form-content" action="<%= base_url %>/servlet/costumers/create<%= (id != null) ? "?id=" + id : "" %>" method="POST">
                    <div class="box">
                        <label for="cpf">CPF</label>
                        <input id="cpf" name="cpf" type="text" value="<%= (StringUtils.isNotBlank(costumer.getCpf())) ? costumer.getCpf() : "" %>">
                    </div>
                    <div class="box">
                        <label for="name">Nome</label>
                        <input id="name" name="name" type="text" value="<%= (StringUtils.isNotBlank(costumer.getName())) ? costumer.getName() : "" %>">
                    </div>
                    <div class="box">
                        <label for="phone">Telefone</label>
                        <input id="phone" name="phone" type="text" value="<%= (StringUtils.isNotBlank(costumer.getPhone())) ? costumer.getPhone() : "" %>">
                    </div>
                    <div class="box">
                        <label for="email">Email</label>
                        <input id="email" name="email" type="email"  value="<%= (StringUtils.isNotBlank(costumer.getEmail())) ? costumer.getEmail() : "" %>">
                    </div>
                    <div class="box">
                        <label for="address">Endereço</label>
                        <input id="address" name="address" type="text" value="<%= (StringUtils.isNotBlank(costumer.getAddress())) ? costumer.getAddress() : "" %>">
                    </div>
                    <div class="box">
                        <label for="number">Número</label>
                        <input id="number" name="number" type="text" value="<%= (StringUtils.isNotBlank(costumer.getNumber())) ? costumer.getNumber() : "" %>">
                    </div>
                    <div class="box">
                        <label for="complement">Complemento</label>
                        <input id="complement" name="complement" type="text" value="<%= (StringUtils.isNotBlank(costumer.getComplement())) ? costumer.getComplement() : "" %>">
                    </div>
                    <div class="box">
                        <label for="reference">Referência</label>
                        <input id="reference" name="reference" type="text" value="<%= (StringUtils.isNotBlank(costumer.getReference())) ? costumer.getReference() : "" %>">
                    </div>
                    <div class="box">
                        <label for="zip_code">CEP</label>
                        <input id="zip_code" name="zip_code" type="text" value="<%= (StringUtils.isNotBlank(costumer.getZipCode())) ? costumer.getZipCode() : "" %>">
                    </div>
                    <div class="box">
                        <label for="district">Bairro</label>
                        <input id="district" name="district" type="text" value="<%= (StringUtils.isNotBlank(costumer.getDistrict())) ? costumer.getDistrict() : "" %>">
                    </div>
                    <div class="box">
                        <label for="city">Cidade</label>
                        <input id="city" name="city" type="text" value="<%= (StringUtils.isNotBlank(costumer.getCity())) ? costumer.getCity() : "" %>">
                    </div>
                    <div class="box">
                        <label for="state">Estado</label>
                        <input id="state" name="state" type="text" value="<%= (StringUtils.isNotBlank(costumer.getState())) ? costumer.getState() : "" %>">
                    </div>
                    <div class="box">
                        <label for="country">País</label>
                        <input id="country" name="country" type="text" value="<%= (StringUtils.isNotBlank(costumer.getCountry())) ? costumer.getCountry() : "" %>">
                    </div>
                    <div class="box">
                        <button type="submit">Salvar</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>

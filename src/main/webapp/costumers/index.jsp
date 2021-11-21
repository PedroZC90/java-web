<%@ page import="utils.ParametersUtils" %>
<%@ page import="utils.AppUtils" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="database.CostumerDAO" %>
<%@ page import="models.Costumer" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    final String base_url = AppUtils.getBaseUrl(request);
    final String cpf = ParametersUtils.asString(request, "cpf");

    final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

    Costumer costumer = CostumerDAO.findByCpf(db, cpf);
    if (cpf == null) costumer = new Costumer();
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
                <form class="form-content" action="<%= base_url %>/servlet/costumers/create" method="POST">
                    <div class="box">
                        <label for="cpf">CPF</label>
                        <input id="cpf" name="cpf" type="text" readonly="<%= StringUtils.isNotBlank(costumer.getCpf()) %>" value="<%= (StringUtils.isNotBlank(costumer.getCpf())) ? costumer.getCpf() : "" %>">
                    </div>
                    <div class="box">
                        <label for="nome">Nome</label>
                        <input id="nome" name="nome" type="text" value="<%= (StringUtils.isNotBlank(costumer.getName())) ? costumer.getName() : "" %>">
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
                        <label for="numero">Número</label>
                        <input id="numero" name="numero" type="text" value="<%= (StringUtils.isNotBlank(costumer.getNumber())) ? costumer.getNumber() : "" %>">
                    </div>
                    <div class="box">
                        <label for="complemento">Complemento</label>
                        <input id="complemento" name="complemento" type="text" value="<%= (StringUtils.isNotBlank(costumer.getComplement())) ? costumer.getComplement() : "" %>">
                    </div>
                    <div class="box">
                        <label for="referencia">Referência</label>
                        <input id="referencia" name="referencia" type="text" value="<%= (StringUtils.isNotBlank(costumer.getReference())) ? costumer.getReference() : "" %>">
                    </div>
                    <div class="box">
                        <label for="cep">CEP</label>
                        <input id="cep" name="cep" type="text" value="<%= (StringUtils.isNotBlank(costumer.getZipCode())) ? costumer.getZipCode() : "" %>">
                    </div>
                    <div class="box">
                        <label for="bairro">Bairro</label>
                        <input id="bairro" name="bairro" type="text" value="<%= (StringUtils.isNotBlank(costumer.getDistrict())) ? costumer.getDistrict() : "" %>">
                    </div>
                    <div class="box">
                        <label for="cidade">Cidade</label>
                        <input id="cidade" name="cidade" type="text" value="<%= (StringUtils.isNotBlank(costumer.getCity())) ? costumer.getCity() : "" %>">
                    </div>
                    <div class="box">
                        <label for="estado">Estado</label>
                        <input id="estado" name="estado" type="text" value="<%= (StringUtils.isNotBlank(costumer.getState())) ? costumer.getState() : "" %>">
                    </div>
                    <div class="box">
                        <label for="pais">País</label>
                        <input id="pais" name="pais" type="text" value="<%= (StringUtils.isNotBlank(costumer.getCountry())) ? costumer.getCountry() : "" %>">
                    </div>
                    <div class="box">
                        <button type="submit">Salvar</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>

package controllers;

import models.Costumer;
import org.apache.commons.lang3.StringUtils;
import utils.AppUtils;
import utils.HtmlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@WebServlet(name = "CostumerServlet", urlPatterns = { "/costumers", "/costumers/*" })
public class CostumerServlet extends HttpServlet {

    private static String input(final String tag, final String value) {
        return input(tag, "text", value);
    }

    private static String input(final String tag, final String type, final String value) {
        StringBuilder sb = new StringBuilder("<input ")
            .append(" id='").append(tag).append("'")
            .append(" name='").append(tag).append("'")
            .append(" type='").append(type).append("'");
        if (StringUtils.isNotBlank(value)) {
            sb.append(" value='").append(value).append("'");
        }
        return sb.append(" />").toString();
    }

    private static void form(final HttpServletResponse response, final Costumer costumer) throws IOException {
        HtmlUtils.build(response.getWriter(), "costumer-page", "centralized", (out) -> {
            out.println("<div class='form-panel'>");
            out.println("<h1 class='form-header'>Cliente</h1>");
            out.println("<form class='form-content'>");

            out.println("<div class='box'>");
            out.println("<label for='cpf'>CPF</label>");
            out.println(input("cpf", costumer.getCpf()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='nome'>Nome</label>");
            out.println(input("nome", costumer.getName()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='phone'>Telefone</label>");
            out.println(input("phone", costumer.getPhone()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='email'>Email</label>");
            out.println(input("email", "email", costumer.getEmail()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='address'>Endereço</label>");
            out.println(input("address", costumer.getAddress()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='numero'>Número</label>");
            out.println(input("numero", costumer.getNumber()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='complemento'>Complemento</label>");
            out.println(input("complemento", costumer.getComplement()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='referencia'>Referência</label>");
            out.println(input("referencia", costumer.getReference()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='cep'>CEP</label>");
            out.println(input("cep", costumer.getZipCode()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='bairro'>Bairro</label>");
            out.println(input("bairro", costumer.getDistrict()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='cidade'>Cidade</label>");
            out.println(input("bairro", costumer.getDistrict()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='estado'>Estado</label>");
            out.println(input("estado", costumer.getState()));
            out.println("</div>");

            out.println("<div class='box'>");
            out.println("<label for='pais'>País</label>");
            out.println(input("pais", costumer.getCountry()));
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
        });
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String costumerId = Arrays.stream(request.getRequestURI().split("/"))
            .reduce((first, second) -> second)
            .orElse(null);

        response.setContentType("text/html");

        // Hello
        form(response, new Costumer());
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // Costumer costumer = request.getInputStream()
    }

    public void destroy() {
        // destroy stuff here
    }

}

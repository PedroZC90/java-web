package controllers;

import models.Costumer;
import org.apache.commons.lang3.StringUtils;
import utils.AppUtils;
import utils.HtmlUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@WebServlet(name = "CostumerServlet", urlPatterns = {"/costumers", "/costumers/*"})
public class CostumerServlet extends HttpServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String cpf = getQueryParameters(request);

        final Costumer costumer = SessionUtils.findCostumersByCpf(request.getSession(), cpf);
        if (StringUtils.isNotBlank(cpf) && costumer == null) {
            response.setStatus(400);
            response.sendRedirect(request.getContextPath());
            return;
        }

        response.setContentType("text/html");

        form(request, response, (costumer != null) ? costumer : new Costumer());
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final Costumer costumer = readForm(request);
        final String cpf = AppUtils.removeCpfMask(costumer.getCpf());
        if (StringUtils.isBlank(cpf)) {
            response.setStatus(400);
            return;
        }

        final HttpSession session = request.getSession();

        final List<Costumer> costumers = SessionUtils.getCostumers(session);

        int index = IntStream.range(0, costumers.size())
                .filter((i) -> {
                    Costumer c = costumers.get(i);
                    if (c == null) return false;
                    return AppUtils.compareCnpj(c.getCpf(), cpf);
                }).findFirst()
                .orElse(-1);

        // remove old value
        if (index > -1) costumers.remove(index);

        // insert updated value
        costumers.add(costumer);

        SessionUtils.setCostumers(session, costumers);

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/costumers", cpf);
        response.sendRedirect(absoluteUrl);
    }

    public void destroy() {
        // destroy stuff here
    }

    // UTILITIES
    public static String getQueryParameters(final HttpServletRequest request) {
        return Arrays.stream(request.getRequestURI().split("/"))
                .filter((v) -> v.length() > 0 &&
                        !StringUtils.equals(v, request.getContextPath().replaceAll("/", "")) &&
                        !StringUtils.equals(v, "costumers"))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private static String input(final String tag, final String value) {
        return input(tag, "text", value);
    }

    private static String input(final String tag, final String type, final String value) {
        return input(tag, type, false, false, value);
    }

    private static String input(final String tag, final String type, final boolean disable, final boolean readonly, final String value) {
        StringBuilder sb = new StringBuilder("<input ")
                .append(" id='").append(tag).append("'")
                .append(" name='").append(tag).append("'")
                .append(" type='").append(type).append("'");
        if (disable) sb.append(" disabled='true'");
        if (readonly) sb.append(" readonly");
        if (StringUtils.isNotBlank(value)) sb.append(" value='").append(value).append("'");
        return sb.append(" />").toString();
    }

    private static void form(final HttpServletRequest request, final HttpServletResponse response, final Costumer costumer) throws IOException {
        final String baseUrl = AppUtils.getBaseUrl(request);
        final boolean readonly = StringUtils.isNotBlank(costumer.getCpf());

        HtmlUtils.build(request, response, "costumer-page", (out) -> {
            HtmlUtils.buildMenu(request, out);

            out.println("<div class='centralized'>");
            out.println("<div class='form-panel'>");
            out.println("<h1 class='form-header'>Cliente</h1>");

            String form = "<form" +
                    " class='form-content'" +
                    " action='" + AppUtils.url(baseUrl, "/costumers") + "'" +
                    " method='POST'" +
                    ">";
            out.println(form);

            out.println("<div class='box'>");
            out.println("<label for='cpf'>CPF</label>");
            out.println(input("cpf", "text", false, readonly, costumer.getCpf()));
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

            out.println("<div class='box'>");
            out.println("<button type='submit'>Salvar</button>");
            out.println("</div>");

            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
        });
    }

    private static Costumer readForm(final HttpServletRequest request) {
        Costumer costumer = new Costumer();
        costumer.setCpf(AppUtils.addCpfMask(request.getParameter("cpf")));
        costumer.setName(request.getParameter("nome"));
        costumer.setPhone(request.getParameter("phone"));
        costumer.setEmail(request.getParameter("email"));
        costumer.setAddress(request.getParameter("address"));
        costumer.setNumber(request.getParameter("number"));
        costumer.setComplement(request.getParameter("complemento"));
        costumer.setReference(request.getParameter("referencia"));
        costumer.setZipCode(request.getParameter("cep"));
        costumer.setDistrict(request.getParameter("bairro"));
        costumer.setCity(request.getParameter("cidade"));
        costumer.setState(request.getParameter("estado"));
        costumer.setCountry(request.getParameter("pais"));
        return costumer;
    }

}

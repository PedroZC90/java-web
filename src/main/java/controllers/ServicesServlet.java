package controllers;

import models.Service;
import org.apache.commons.lang3.StringUtils;
import utils.AppUtils;
import utils.HtmlUtils;
import utils.ParametersUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@WebServlet(name = "ServicesServlet", urlPatterns = {"/services", "/services/*"})
public class ServicesServlet extends HttpServlet {

    private static final Pattern CANCEL_REGEXP = Pattern.compile(".*/services/\\d+/cancel");
    private static final Pattern COMPLETE_REGEXP = Pattern.compile(".*/services/\\d+/complete");
    private static final Pattern REGEXP = Pattern.compile(".*/services/(\\d+)");

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Long serviceId = getQueryParameters(request.getRequestURI());

        final Service service = SessionUtils.findServicesById(request.getSession(), serviceId);
        if (serviceId != null && service == null) {
            response.setStatus(400);
            response.sendRedirect(request.getContextPath());
            return;
        }

        response.setContentType("text/html");

        form(request, response, (service != null) ? service : new Service());
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String uri= request.getRequestURI();
        if (COMPLETE_REGEXP.matcher(uri).matches()) {
            complete(request, response);
        } else if (CANCEL_REGEXP.matcher(uri).matches()) {
            cancel(request, response);
        } else {
            save(request, response);
        }
    }

    protected void complete(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Long serviceId = getQueryParameters(request.getRequestURI());
        if (serviceId == null) {
            final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/dashboard");
            response.sendRedirect(absoluteUrl);
            return;
        }

        final HttpSession session = request.getSession();

        final List<Service> services = SessionUtils.getServices(session);

        int index = IntStream.range(0, services.size())
                .filter((i) -> {
                    Service s = services.get(i);
                    return Objects.equals(s.getId(), serviceId);
                })
                .findFirst()
                .orElse(-1);

        // remove old value
        if (index > -1) {
            services.get(index).setCompleted(true);
            SessionUtils.setServices(session, services);
        } else {
            response.setStatus(400);
        }

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/services", Long.toString(serviceId));
        response.sendRedirect(absoluteUrl);
    }

    protected void cancel(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Long serviceId = getQueryParameters(request.getRequestURI());
        if (serviceId == null) {
            final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/dashboard");
            response.sendRedirect(absoluteUrl);
            return;
        }

        final HttpSession session = request.getSession();

        final List<Service> services = SessionUtils.getServices(session);

        int index = IntStream.range(0, services.size())
                .filter((i) -> {
                    Service s = services.get(i);
                    return Objects.equals(s.getId(), serviceId);
                })
                .findFirst()
                .orElse(-1);

        // remove old value
        if (index > -1) {
            services.get(index).setCancelled(true);
            SessionUtils.setServices(session, services);
        } else {
            response.setStatus(400);
        }

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/services", Long.toString(serviceId));
        response.sendRedirect(absoluteUrl);
    }

    protected void save(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final Service service = readForm(request);

        final HttpSession session = request.getSession();

        final List<Service> services = SessionUtils.getServices(session);

        int index = IntStream.range(0, services.size())
                .filter((i) -> {
                    Service s = services.get(i);
                    return Objects.equals(s.getId(), service.getId());
                }).findFirst()
                .orElse(-1);

        // remove old value
        if (index > -1) {
            services.remove(index);
        } else {
            final Long sequence = SessionUtils.getServiceSequence(session);
            service.setId(sequence);
        }

        // insert updated value
        services.add(service);

        SessionUtils.setServices(session, services);

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/services", service.getId().toString());
        response.sendRedirect(absoluteUrl);
    }

    public void destroy() {
        // destroy stuff here
    }

    // UTILITIES
    public static Long getQueryParameters(final String uri) {
        Matcher matcher = REGEXP.matcher(uri);
        if (!matcher.find()) return null;
        return Long.parseLong(matcher.group(1));
    }

    private static String input(final String tag, final String value) {
        return input(tag, "text", value);
    }

    private static String input(final String tag, final String type, final String value) {
        return input(tag, type, false, false, value);
    }

    private static String input(final String tag, final String type, final boolean disable, final boolean readonly, final LocalDateTime dt) {
        String s = (dt != null) ? dt.minusNanos(dt.getNano()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
        return input(tag, type, disable, readonly, s);
    }

    private static String input(final String tag, final String type, final boolean disable, final boolean readonly, final Integer value) {
        String s = (value != null) ? Integer.toString(value) : "0";
        return input(tag, type, disable, readonly, s);
    }

    private static String input(final String tag, final String type, final boolean disable, final boolean readonly, final Double value) {
        String s = (value != null) ? Double.toString(value) : "0.0";
        return input(tag, type, disable, readonly, s);
    }

    private static String input(final String tag, final String type, final boolean disable, final boolean readonly, final boolean value) {
        return input(tag, type, disable, readonly, Boolean.toString(value));
    }

    private static String input(final String tag, final String type, final boolean disable, final boolean readonly, final String value) {
        StringBuilder sb = new StringBuilder("<input ")
                .append(" id='").append(tag).append("'")
                .append(" name='").append(tag).append("'");
        if (StringUtils.equals(type, "datetime-local")) {
            sb.append(" type='").append(type).append("'")
                    .append(" step='1'");
        } else if (StringUtils.equals(type, "double")) {
                sb.append(" type='number'").append(" step='0.01'");
        } else {
                sb.append(" type='").append(type).append("'");
        }
        if (disable) sb.append(" disabled='true'");
        if (readonly) sb.append(" readonly");
        if (StringUtils.isNotBlank(value)) sb.append(" value='").append(value).append("'");
        return sb.append(" />").toString();
    }

    private static void form(final HttpServletRequest request, final HttpServletResponse response, final Service service) throws IOException {
        final String baseUrl = AppUtils.getBaseUrl(request);
        final boolean exists = (service.getId() != null);

        HtmlUtils.build(request, response, "service-page", (out) -> {
                    HtmlUtils.buildMenu(request, out);

                    out.println("<div class='centralized'>");
                    out.println("<div class='form-panel'>");
                    out.println("<h1 class='form-header'>Service</h1>");

                    String form = "<form" +
                            " class='form-content'" +
                            " action='" + AppUtils.url(baseUrl, "/services") + "'" +
                            " method='POST'" +
                            ">";
                    out.println(form);

                    out.println("<div class='box'>");
                    out.println("<label for='constumer'>Cliente</label>");
                    out.println("<select id='constumer' name='constumer'>");
                    SessionUtils.getCostumers(request.getSession()).forEach((c) -> {
                        out.println("<option value='" + AppUtils.removeCpfMask(c.getCpf()) + "'>" +
                                (c.getName() + " (" + c.getCpf() + ")") +
                                "</option>");
                    });
                    out.println("</select>");
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<label for='installation'>Qtd. p/ Instalar</label>");
                    out.println(input("installation", "number", false, false, service.getInstallations()));
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<label for='copper_tube'>Comprimento de Tubo de Cobre</label>");
                    out.println(input("copper_tube", "double", false, false, service.getTubeLength()));
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<label for='removal'>Qtd. p/ Remover</label>");
                    out.println(input("removal", "number", false, false, service.getRemoval()));
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<label for='maintenance'>Qtd. p/ Manuteção</label>");
                    out.println(input("maintenance", "number", false, false, service.getMaintenance()));
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<label for='rappel'>Necessário Rapel</label>");
                    out.println(input("rappel", "checkbox", false, false, service.isRappelRequired()));
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<label for='scheduler'>Agendamento</label>");
                    out.println(input("scheduler", "datetime-local", false, false, service.getSchedulingDate()));
                    out.println("</div>");

                    if (service.getCreatedAt() != null) {
                        out.println("<div class='box'>");
                        out.println("<label for='date'>Criação</label>");
                        out.println(input("created_at", "datetime-local", false, true, service.getCreatedAt()));
                        out.println("</div>");
                    }

                    out.println("<div class='box'>");
                    out.println("<label for='cost'>Valor</label>");
                    out.println(input("cost", "text", true, false, service.getValue()));
                    out.println("</div>");

                    out.println("<div class='box'>");
                    out.println("<button type='submit'>Salvar</button>");
                    out.println("</div>");

                    out.println("</form>");
                    out.println("</div>");

                    // CANCEL
                    if (exists) {
                    out.println("<div class='form-panel'>");
                    out.println("<h1 class='form-header'>Cancelar</h1>");

                        String cancelForm = "<form" +
                                " class='form-content'" +
                                " action='" + AppUtils.url(baseUrl, "/services", service.getId().toString(), "/cancel") + "'" +
                                " method='POST'" +
                                ">";
                        out.println(cancelForm);

                        out.println("<button type='submit' value='cancel'>Submit</button>");

                        out.println("</form>");
                    out.println("</div>");

                    // COMPLETE
                    out.println("<div class='form-panel'>");
                    out.println("<h1 class='form-header'>Concluir</h1>");

                    String completeForm = "<form" +
                            " class='form-content'" +
                            " action='" + AppUtils.url(baseUrl, "/services", service.getId().toString(), "/complete") + "'" +
                            " method='POST'" +
                            ">";
                    out.println(completeForm);

                    out.println("<button type='submit' value='complete'>Submit</button>");

                    out.println("</form>");
                    out.println("</div>");
                }

            out.println("</div>");
        });
    }

    private static Service readForm(final HttpServletRequest request) {
        LocalDateTime created_at = ParametersUtils.asLocalDateTime(request, "created_at");

        Long serviceId = getQueryParameters(request.getRequestURI());
        if (serviceId == null) {
            final HttpSession session = request.getSession();
            serviceId = SessionUtils.getServiceSequence(session);
            SessionUtils.setServiceSequence(session, serviceId++);
            created_at = LocalDateTime.now();
        }

        Service service = new Service();
        service.setId(serviceId);
        service.setCostumerCpf(ParametersUtils.asString(request, "costumer", AppUtils::addCpfMask));
        service.setInstallations(ParametersUtils.asInteger(request, "installation"));
        service.setTubeLength(ParametersUtils.asDouble(request, "copper_tube"));
        service.setRemoval(ParametersUtils.asInteger(request, "removal"));
        service.setMaintenance(ParametersUtils.asInteger(request, "maintenance"));
        service.setRappelRequired(ParametersUtils.asBoolean(request, "rappel"));
        service.setSchedulingDate(ParametersUtils.asLocalDateTime(request, "scheduler"));
        service.setCreatedAt(created_at);
        // service.setCancelled();
        // service.setCompleted();

        double value = 0.0;
        value += service.getInstallations() * 100;
        value += service.getRemoval() * 75;
        value += service.getMaintenance() * 125;
        if (service.isRappelRequired()) {
            value += 200;
        }
        value += service.getTubeLength() * Service.COPPER_TUBE_PRICE_BY_LENGTH;

        service.setValue(value);

        return service;
    }

}

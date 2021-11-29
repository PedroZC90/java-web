package controllers;

import database.CostumerDAO;
import database.ServiceDAO;
import models.Costumer;
import models.Service;
import utils.AppUtils;
import utils.ParametersUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ServicesServlet", urlPatterns = {"/servlet/services", "/servlet/services/*"})
public class ServicesServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ServicesServlet.class.getSimpleName());

    private static final Pattern CANCEL_REGEXP = Pattern.compile(".*/services/\\d+/cancel");
    private static final Pattern COMPLETE_REGEXP = Pattern.compile(".*/services/\\d+/complete");
    private static final Pattern REGEXP = Pattern.compile(".*/services/(\\d+)");

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String action = ParametersUtils.asString(request, "action");
        final Long serviceId = getQueryParameters(request);

        //String uri= request.getRequestURI();
        //if (COMPLETE_REGEXP.matcher(uri).matches()) {
        //    complete(request, response);
        //} else if (CANCEL_REGEXP.matcher(uri).matches()) {
        //    cancel(request, response);
        //} else {
        //    save(request, response);
        //}
        if (action == null) {
            response.setStatus(400);
            return;
        }

        switch (action) {
            case "save":
                save(request, response, serviceId);
                break;
            case "complete":
                complete(request, response, serviceId);
                break;
            case "cancel":
                cancel(request, response, serviceId);
                break;
            default:
                LOGGER.severe("Unknown action '" + action + "'");
        }

//        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/services/index.jsp?id=" + serviceId);
//        response.sendRedirect(absoluteUrl);
    }

    private void dashboard(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/dashboard/index.jsp");
        response.sendRedirect(absoluteUrl);
    }

    private void redirect(final HttpServletRequest request, final HttpServletResponse response, final Long serviceId) throws IOException {
        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/services/index.jsp?id=" + serviceId);
        response.sendRedirect(absoluteUrl);
    }

    protected void complete(final HttpServletRequest request, final HttpServletResponse response, final Long serviceId) throws IOException {
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        final Service service = ServiceDAO.findById(db, serviceId);
        if (service == null) {
            response.setStatus(400);
            return;
        }

        boolean success = ServiceDAO.complete(db, service.getId());
        if (success) {
            dashboard(request, response);
        }
    }

    protected void cancel(final HttpServletRequest request, final HttpServletResponse response, final Long serviceId) throws IOException {
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        final Service service = ServiceDAO.findById(db, serviceId);
        if (service == null) {
            response.setStatus(400);
            return;
        }

        boolean success = ServiceDAO.cancel(db, service.getId());
        if (success) {
            dashboard(request, response);
        }
    }

    protected void save(final HttpServletRequest request, final HttpServletResponse response, final Long serviceId) throws IOException {
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        Service service = ServiceDAO.findById(db, serviceId);
        if (service == null) {
            service = readForm(request);
            service = ServiceDAO.insert(db, service);
            if (service == null) {
                response.setStatus(400);
                return;
            }
        } else {
            merge(request, service);
            boolean success = ServiceDAO.update(db, service);
            if (!success) {
                response.setStatus(400);
                return;
            }
        }

        // final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/services", service.getId().toString());
        // response.sendRedirect(absoluteUrl);
        redirect(request, response, service.getId());
    }

    public void destroy() {
        // destroy stuff here
    }

    // UTILITIES
    public static Long getQueryParameters(final HttpServletRequest request) {
        return getQueryParameters(request.getRequestURL().toString());
    }

    public static Long getQueryParameters(final String uri) {
        Matcher matcher = REGEXP.matcher(uri);
        if (!matcher.find()) return null;
        return Long.parseLong(matcher.group(1));
    }

    private static Service readForm(final HttpServletRequest request) {
        Service s = new Service();
        s.setCostumerId(ParametersUtils.asLong(request, "costumer"));
        s.setInstallations(ParametersUtils.asInteger(request, "installation"));
        s.setTubeLength(ParametersUtils.asDouble(request, "copper_tube"));
        s.setRemoval(ParametersUtils.asInteger(request, "removal"));
        s.setMaintenance(ParametersUtils.asInteger(request, "maintenance"));
        s.setRappelRequired(ParametersUtils.asBoolean(request, "rappel"));
        s.setScheduledTo(ParametersUtils.asLocalDateTime(request, "scheduled_to"));

        LocalDateTime created_at = ParametersUtils.asLocalDateTime(request, "created_at");
        s.setCreatedAt((created_at != null) ? created_at : LocalDateTime.now());

        double value = calculateValue(s);
        s.setValue(value);

        return s;
    }

    private static Service merge(final HttpServletRequest request, final Service s) {
        if (ParametersUtils.has(request, "installation")) {
            s.setInstallations(ParametersUtils.asInteger(request, "installation"));
        }
        if (ParametersUtils.has(request, "copper_tube")) {
            s.setTubeLength(ParametersUtils.asDouble(request, "copper_tube"));
        }
        if (ParametersUtils.has(request, "removal")) {
            s.setRemoval(ParametersUtils.asInteger(request, "removal"));
        }
        if (ParametersUtils.has(request, "maintenance")) {
            s.setMaintenance(ParametersUtils.asInteger(request, "maintenance"));
        }
        if (ParametersUtils.has(request, "rappel")) {
            s.setRappelRequired(ParametersUtils.asBoolean(request, "rappel"));
        }
        if (ParametersUtils.has(request, "scheduled_to")) {
            s.setScheduledTo(ParametersUtils.asLocalDateTime(request, "scheduled_to"));
        }
        if (ParametersUtils.has(request, "created_at")) {
            s.setCreatedAt(ParametersUtils.asLocalDateTime(request, "created_at"));
        }

        if (ParametersUtils.has(request, "costumer")) {
            Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);
            final Long costumer_id = ParametersUtils.asLong(request, "costumer");
            Costumer costumer = CostumerDAO.findById(db, costumer_id);
            if (costumer != null) {
                s.setCostumer(costumer);
            }
        }

        double value = calculateValue(s);
        s.setValue(value);

        return s;
    }

    private static double calculateValue(final Service service) {
        double value = 0.0;
        value += service.getInstallations() * 100;
        value += service.getRemoval() * 75;
        value += service.getMaintenance() * 125;
        if (service.isRappelRequired()) {
            value += 200;
        }
        value += service.getTubeLength() * Service.COPPER_TUBE_PRICE_BY_LENGTH;
        return value;
    }

}

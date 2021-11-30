package controllers;

import database.CostumerDAO;
import database.ServiceDAO;
import models.Costumer;
import org.apache.commons.lang3.StringUtils;
import utils.AppUtils;
import utils.ParametersUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "CostumerServlet", urlPatterns = {"/servlet/costumers", "/servlet/costumers/*"})
public class CostumerServlet extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final Long id = ParametersUtils.asLong(request, "id");
        final String action = ParametersUtils.asString(request, "action");

        if (StringUtils.isBlank(action)) return;

        switch (action) {
            case "save":
                save(request, response, id);
                break;
            case "remove":
                remove(request, response, id);
                break;
        }
    }

    private static void save(final HttpServletRequest request, final HttpServletResponse response, final Long id) throws IOException {
        final HttpSession session = request.getSession();
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        Costumer costumer = CostumerDAO.findById(db, id);
        if (costumer == null) {
            costumer = readForm(request);
            costumer = CostumerDAO.insert(db, costumer);
            if (costumer == null) {
                session.setAttribute("message", "Unable to insert costumer.");
                return;
            }
        } else {
            costumer = costumer.merge(readForm(request));
            boolean updated = CostumerDAO.update(db, costumer);
            if (!updated) {
                session.setAttribute("message", "Unable to update costumer.");
                return;
            }
        }

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/costumers/index.jsp?id=" + costumer.getId());
        response.sendRedirect(absoluteUrl);
    }

    private static void remove(final HttpServletRequest request, final HttpServletResponse response, final Long id) throws IOException {
        final HttpSession session = request.getSession();
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        Costumer costumer = CostumerDAO.findById(db, id);
        if (costumer == null) {
            session.setAttribute("message", "Unable to insert costumer.");
            return;
        }

        ServiceDAO.select(db, null, null, null, null, id)
                .forEach((s) -> ServiceDAO.delete(db, s));

        CostumerDAO.deleteById(db, id);

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/dashboard/index.jsp");
        response.sendRedirect(absoluteUrl);
    }

    public void destroy() {
        // destroy stuff here
    }

    // UTILITIES
    private static Costumer readForm(final HttpServletRequest request) {
        Costumer costumer = new Costumer();
        costumer.setCpf(ParametersUtils.asString(request,"cpf", AppUtils::addCpfMask));
        costumer.setName(ParametersUtils.asString(request, "name"));
        costumer.setPhone(ParametersUtils.asString(request, "phone"));
        costumer.setEmail(ParametersUtils.asString(request, "email"));
        costumer.setAddress(ParametersUtils.asString(request, "address"));
        costumer.setNumber(ParametersUtils.asString(request, "number"));
        costumer.setComplement(ParametersUtils.asString(request, "complement"));
        costumer.setReference(ParametersUtils.asString(request, "reference"));
        costumer.setZipCode(ParametersUtils.asString(request, "zip_code"));
        costumer.setDistrict(ParametersUtils.asString(request, "district"));
        costumer.setCity(ParametersUtils.asString(request, "city"));
        costumer.setState(ParametersUtils.asString(request, "state"));
        costumer.setCountry(ParametersUtils.asString(request, "country"));
        return costumer;
    }

}

package controllers;

import database.CostumerDAO;
import models.Costumer;
import org.apache.commons.lang3.StringUtils;
import utils.AppUtils;
import utils.HtmlUtils;
import utils.ParametersUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;

@WebServlet(name = "CostumerServlet", urlPatterns = {"/servlet/costumers", "/servlet/costumers/*"})
public class CostumerServlet extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        // final String cpf = ParametersUtils.asString(request,"cpf", AppUtils::addCpfMask);
        final Long id = ParametersUtils.asLong(request,"id");
        // if (id == null) {
        //     response.setStatus(400);
        //     return;
        // }

        final HttpSession session = request.getSession();
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        Costumer costumer = CostumerDAO.findById(db, id);
        if (costumer == null) {
            costumer = readForm(request);
            costumer = CostumerDAO.insert(db, costumer);
            if (costumer == null) {
                response.setStatus(400);
                return;
            }
        } else {
            costumer = costumer.merge(readForm(request));
            boolean updated = CostumerDAO.update(db, costumer);
            if (!updated) {
                response.setStatus(400);
                return;
            }
        }

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/costumers/index.jsp?id=" + costumer.getId());
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

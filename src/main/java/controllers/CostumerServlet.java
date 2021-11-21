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
        final String cpf = ParametersUtils.asString(request,"cpf", AppUtils::addCpfMask);
        if (StringUtils.isBlank(cpf)) {
            response.setStatus(400);
            return;
        }

        final HttpSession session = request.getSession();
        final Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);

        Costumer costumer = CostumerDAO.findByCpf(db, cpf);
        if (costumer == null) {
            costumer = readForm(request);
            boolean inserted = CostumerDAO.insert(db, costumer);
            if (!inserted) {
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

        final String absoluteUrl = AppUtils.url(AppUtils.getBaseUrl(request), "/costumers", cpf);
        response.sendRedirect(absoluteUrl);
    }

    public void destroy() {
        // destroy stuff here
    }

    // UTILITIES
    private static Costumer readForm(final HttpServletRequest request) {
        Costumer costumer = new Costumer();
        costumer.setCpf(ParametersUtils.asString(request,"cpf", AppUtils::addCpfMask));
        costumer.setName(ParametersUtils.asString(request, "nome"));
        costumer.setPhone(ParametersUtils.asString(request, "phone"));
        costumer.setEmail(ParametersUtils.asString(request, "email"));
        costumer.setAddress(ParametersUtils.asString(request, "address"));
        costumer.setNumber(ParametersUtils.asString(request, "number"));
        costumer.setComplement(ParametersUtils.asString(request, "complemento"));
        costumer.setReference(ParametersUtils.asString(request, "referencia"));
        costumer.setZipCode(ParametersUtils.asString(request, "cep"));
        costumer.setDistrict(ParametersUtils.asString(request, "bairro"));
        costumer.setCity(ParametersUtils.asString(request, "cidade"));
        costumer.setState(ParametersUtils.asString(request, "estado"));
        costumer.setCountry(ParametersUtils.asString(request, "pais"));
        return costumer;
    }

}

package controllers;

import models.Costumer;
import models.Service;
import utils.AppUtils;
import utils.HtmlUtils;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {

    public void init() {
        // initialize stuff here
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession();

        final List<Costumer> costumers = SessionUtils.getCostumers(session);

        // DEVELOPMENT
        if (costumers.size() == 0) {
            Costumer c1 = new Costumer();
            c1.setCpf("067.621.419-35");
            c1.setName("Pedro");
            c1.setZipCode("88811-510");
            c1.setDistrict("Pio Correa");
            c1.setCity("CRICIUMA");
            c1.setState("SANTA CATARINA");
            c1.setCountry("BRASIL");

            Costumer c2 = new Costumer();
            c2.setCpf("000.000.000-01");
            c2.setName("Suzana");
            c2.setZipCode("88811-510");
            c2.setDistrict("Pio Correa");
            c2.setCity("CRICIUMA");
            c2.setState("SANTA CATARINA");
            c2.setCountry("BRASIL");

            costumers.add(c1);
            costumers.add(c2);

            SessionUtils.setCostumers(session, costumers);
        }

        final int numberOfCostumers = costumers.size();

        final List<Service> services = SessionUtils.getServices(session);
        final int numberOfServices = services.size();

        final String baseUrl = AppUtils.getBaseUrl(request);

        // build html page
        response.setContentType("text/html");
        HtmlUtils.build(request, response, (out) -> {
            HtmlUtils.buildMenu(request, out);

            out.println("<h1>" + "DASHBOARD" + "</h1>");
            out.println("<p>Number Of Costumers:" + numberOfCostumers + "</p>");
            out.println("<p>Number Of Services:" + numberOfServices + "</p>");

            out.println("<div class='wrapper'>");

            // costumers
            out.println("<div>");
            out.println("<ul>");
            costumers.forEach((c) -> {
                out.println("<li>");
                out.println("<a href='" + AppUtils.url(baseUrl, "/costumers", AppUtils.removeCpfMask(c.getCpf())) + "'>");
                out.println(c.getCpf() + " - " + c.getName());
                out.println("</a>");
                out.println("</li>");
            });
            out.println("</ul>");
            out.println("</div>");

            // services
            out.println("<div>");
            out.println("<ul>");
            services.forEach((s) -> {
                out.println("<li>");
                out.println("<a href='" + AppUtils.url(baseUrl, "/services", s.getId().toString()) + "'>");
                out.println(s.getId());
                out.println("</a>");
                out.println("</li>");
            });
            out.println("</ul>");
            out.println("</div>");

            out.println("</div>");
        });
    }

    public void destroy() {
        // destroy stuff here
    }

}

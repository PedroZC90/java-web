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
import java.time.format.DateTimeFormatter;
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

        final int numberOfCostumers = costumers.size();

        final List<Service> services = SessionUtils.getServices(session);
        final int numberOfServices = services.size();

        final String baseUrl = AppUtils.getBaseUrl(request);

        // build html page
        response.setContentType("text/html");
        HtmlUtils.build(request, response, "dashboard", (out) -> {
            HtmlUtils.buildMenu(request, out);

            out.println("<h1 class='page-title'>DASHBOARD</h1>");

            out.println("<div class='wrapper'>");

            // costumers
            out.println("<div id='costumers' class='list'>");
            out.println("<div class='list-title'>");
            out.println("<h2>Clientes</h2>");
            out.println("<h2>" + numberOfCostumers + "</h2>");
            out.println("</div>");
            out.println("<div class='list-grid'>");
            costumers.forEach((c) -> {
                final String link = AppUtils.url(baseUrl, "/costumers", AppUtils.removeCpfMask(c.getCpf()));
                out.println("<a class='list-box' href='" + link + "'>");
                out.println("<p>Nome: " + c.getName() + "</p>");
                out.println("<p>CPF: " + c.getCpf() + "</p>");
                out.println("<p>Address : " + c.getAddress() + ", N " + c.getNumber() + ", " + c.getComplement() + ", " + c.getDistrict() + ", " + c.getCity() + "</p>");
                out.println("</a>");
            });
            out.println("</div>");
            out.println("</div>");

            // services
            out.println("<div id='services' class='list'>");
            out.println("<div class='list-title'>");
            out.println("<h2>Serviços</h2>");
            out.println("<h2>" + numberOfServices + "</h2>");
            out.println("</div>");
            out.println("<div class='list-grid'>");
            services.stream()
                    .filter((s) -> !s.isCancelled())
                    .forEach((s) -> {
                        final String link = AppUtils.url(baseUrl, "/services", s.getId().toString());
                        out.println("<a class='list-box' href='" + link + "'>");
                        out.println("<p>Service #" + s.getId() + "</p>");
                        out.println("<p>" + s.getSchedulingDate().format(DateTimeFormatter.ISO_ZONED_DATE_TIME) + "</p>");
                        out.println("</a>");
                    });
            out.println("</div>");
            out.println("</div>");

            out.println("</div>");
        });
    }

    public void destroy() {
        // destroy stuff here
    }

}

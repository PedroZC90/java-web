package controllers;

import utils.ParametersUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AniversarioServlet", value = "/test")
public class AniversarioServlet extends HttpServlet {

    public void init() {
        // initialize stuff here
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final Integer month = ParametersUtils.asInteger(request, "month");

        HttpSession session = request.getSession();
        session.setAttribute("month", month);

        RequestDispatcher rd = request.getRequestDispatcher("aulas/09/aniversario/result.jsp");
        rd.forward(request, response);
    }

    public void destroy() {
        // destroy stuff here
    }

}

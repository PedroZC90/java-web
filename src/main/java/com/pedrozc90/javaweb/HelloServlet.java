package com.pedrozc90.javaweb;

import java.io.*;
import java.sql.Connection;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        Connection db = (Connection) request.getServletContext().getAttribute(AppUtils.CONNECTION_KEY);
        TestDAO.select(db);
        TestDAO.inser(db, "bbbbbbbbb");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body>");
        out.println("</html>");
    }

    public void destroy() {
        // destroy stuff here
    }
}

package controllers;

import utils.AppUtils;
import utils.HtmlUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        List<String> cpfs = Arrays.asList("067.621.419-35", "067.621.419", "067621435");

        // Hello
        HtmlUtils.build(response.getWriter(), (out) -> {
            out.println("<h1>" + message + "</h1>");
            cpfs.forEach((s) -> {
                String t = AppUtils.convertCpfToMask(s);
                out.println("<p>" + s + " -> " + t + "</p>");
            });
        });
    }

    public void destroy() {
        // destroy stuff here
    }
}

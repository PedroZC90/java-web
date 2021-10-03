package utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

public class HtmlUtils {

    public static void build(final HttpServletRequest request, final HttpServletResponse response,
                             final Consumer<PrintWriter> consumer) throws IOException {
        build(request, response, null, null, consumer);
    }

    public static void build(final HttpServletRequest request, final HttpServletResponse response,
                             final String id, final String bodyClass, final Consumer<PrintWriter> consumer) throws IOException {
        final String baseUrl = AppUtils.getBaseUrl(request);

        final PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='utf-8'>");
        out.println("<link rel='stylesheet' href='" + AppUtils.url(baseUrl, "assets/css/global.css") + "'>");
        out.println("<link rel='stylesheet' href='" + AppUtils.url(baseUrl, "assets/css/styles.css") + "'>");
        out.println("</head>");

        StringBuilder body = new StringBuilder("<body");
        if (StringUtils.isNotBlank(id)) body.append(" id='").append(id).append("'");
        if (StringUtils.isNotBlank(id)) body.append(" class='").append(bodyClass).append("'");
        body.append(">");

        out.println(body);

        consumer.accept(out);
        out.println("</body>");
        out.println("</html>");
    }

}

package utils;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.util.function.Consumer;

public class HtmlUtils {

    public static void build(PrintWriter out, Consumer<PrintWriter> consumer) {
        build(out, null, null, consumer);
    }

    public static void build(PrintWriter out, String id, String bodyClass, Consumer<PrintWriter> consumer) {
        // PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='utf-8'>");
        out.println("<link rel='stylesheet' href='assets/css/global.css'>");
        out.println("<link rel='stylesheet' href='assets/css/styles.css'>");
        out.println("</head>");

        String i = (StringUtils.isNotBlank(id) ? (" id='" + id + "'") : "");
        String c = (StringUtils.isNotBlank(bodyClass) ? (" class='" + bodyClass + "'") : "");
        out.println("<body" + i + c + ">");
        consumer.accept(out);
        out.println("</body>");
        out.println("</html>");
    }



}

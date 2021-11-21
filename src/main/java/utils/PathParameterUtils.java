package utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParameterUtils {

    public static String getQueryParameters(final HttpServletRequest request) {
        return Arrays.stream(request.getRequestURI().split("/"))
                .filter((v) -> v.length() > 0 &&
                        !StringUtils.equals(v, request.getContextPath().replaceAll("/", "")) &&
                        !StringUtils.equals(v, "costumers"))
                .reduce((first, second) -> second)
                .orElse(null);
    }

    public static String extract(final String pattern, final HttpServletRequest request) {
        return extract(pattern, request.getRequestURI());
    }

    public static String extract(final String pattern, final String path) {
        Matcher m = Pattern.compile(".*" + pattern + "/?(.*)").matcher(path);
        if (!m.find()) return null;
        return m.group(1);
    }

}

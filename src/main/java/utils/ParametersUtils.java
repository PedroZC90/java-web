package utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.function.Function;

public class ParametersUtils {

    public static LocalDateTime asLocalDateTime(final HttpServletRequest request, final String key) {
        try {
            String s = asString(request, key);
            if (StringUtils.isBlank(s)) return null;
            return LocalDateTime.parse(s);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static Boolean asBoolean(final HttpServletRequest request, final String key) {
        return Boolean.parseBoolean(asString(request, key));
    }

    public static Integer asInteger(final HttpServletRequest request, final String key) {
        return Integer.parseInt(asString(request, key));
    }

    public static Double asDouble(final HttpServletRequest request, final String key) {
        return Double.parseDouble(asString(request, key));
    }

    public static Long asLong(final HttpServletRequest request, final String key) {
        try {
            return Long.parseLong(asString(request, key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String asString(final HttpServletRequest request, final String key) {
        return request.getParameter(key);
    }

    public static String asString(final HttpServletRequest request, final String key, final Function<String, String> transform) {
        return transform.apply(asString(request, key));
    }

}

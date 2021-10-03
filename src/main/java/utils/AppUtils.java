package utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    // CPF
    public static String convertCpfNoMask(final String cnpj) {
        if (StringUtils.isBlank(cnpj)) return null;
        // remover all non-digits from string
        String s = cnpj.replaceAll("\\D", "");
        // fill remaining spaces with 0
        return StringUtils.leftPad(s, 11, "0");
    }

    private static List<String> captureCpfNumbers(String digits) {
        if (StringUtils.isBlank(digits)) return null;
        List<String> list = new ArrayList<>();
        Matcher matcher = Pattern.compile("(\\d{0,3})(\\d{0,3})(\\d{0,3})(\\d{0,2})").matcher(digits);
        if (matcher.find()) {
            list.add(matcher.group(1));
            list.add(matcher.group(2));
            list.add(matcher.group(3));
            list.add(matcher.group(4));
        }
        return list;
    }

    public static String convertCpfToMask(final String s) {
        String digits = convertCpfNoMask(s);

        List<String> groups = captureCpfNumbers(digits);
        if (groups == null) return null;

        String cnpj = null;
        if (groups.size() >= 1) cnpj = groups.get(0);
        if (groups.size() >= 2) cnpj += "." + groups.get(1);
        if (groups.size() >= 3) cnpj += "." + groups.get(2);
        if (groups.size() >= 4) cnpj += "-" + groups.get(3);
        return cnpj;
    }

    public static boolean compareCnpj(String s1, String s2) {
        return StringUtils.equals(convertCpfNoMask(s1), convertCpfNoMask(s2));
    }

}

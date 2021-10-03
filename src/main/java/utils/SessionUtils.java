package utils;

import models.Costumer;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class SessionUtils {

    public static List<Costumer> getCostumers(final HttpSession session) {
        ArrayList<Costumer> array = (ArrayList<Costumer>) session.getAttribute("costumers");
        if (array == null) return new ArrayList<>();
        return array;
    }

    public static void setCostumers(final HttpSession session, final List<Costumer> data) {
        session.setAttribute("costumers", data);
    }

    public static Costumer findCostumersByCpf(final HttpSession session, final String cpf) {
        return getCostumers(session)
                .stream().filter((c) -> AppUtils.compareCnpj(c.getCpf(), cpf))
                .findFirst()
                .orElse(null);
    }

}

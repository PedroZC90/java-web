package utils;

import models.Costumer;
import models.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SessionUtils {

    private static final String CONSUMERS_LIST_KEY = "cosumers";
    private static final String SERVICES_LIST_KEY = "services";
    private static final String SERVICE_SEQUENCE_KEY = "service_sequence";

    // COSTUMER LIST
    public static List<Costumer> getCostumers(final HttpSession session) {
        ArrayList<Costumer> array = (ArrayList<Costumer>) session.getAttribute(CONSUMERS_LIST_KEY);
        if (array == null) return new ArrayList<>();
        return array;
    }

    public static void setCostumers(final HttpSession session, final List<Costumer> data) {
        session.setAttribute(CONSUMERS_LIST_KEY, data);
    }

    public static Costumer findCostumersByCpf(final HttpSession session, final String cpf) {
        return getCostumers(session)
                .stream().filter((c) -> AppUtils.compareCnpj(c.getCpf(), cpf))
                .findFirst()
                .orElse(null);
    }

    // SERVICE SEQUENCE
    public static Long getServiceSequence(final HttpSession session) {
        Long sequence = (Long) session.getAttribute(SERVICE_SEQUENCE_KEY);
        if (sequence == null) return 1L;
        return sequence;
    }

    public static void setServiceSequence(final HttpSession session, final Long sequence) {
        session.setAttribute(SERVICE_SEQUENCE_KEY, sequence);
    }

    // SERVICE LIST
    public static List<Service> getServices(final HttpSession session) {
        ArrayList<Service> array = (ArrayList<Service>) session.getAttribute(SERVICES_LIST_KEY);
        if (array == null) return new ArrayList<>();
        return array;
    }

    public static void setServices(final HttpSession session, final List<Service> data) {
        session.setAttribute(SERVICES_LIST_KEY, data);
    }

    public static Service findServicesById(final HttpSession session, final Long serviceId) {
        return getServices(session)
                .stream().filter((s) -> Objects.equals(s.getId(), serviceId))
                .findFirst()
                .orElse(null);
    }

}

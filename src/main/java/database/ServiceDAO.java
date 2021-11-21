package database;

import models.Service;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ServiceDAO {

    private static final Logger log = Logger.getLogger(ServiceDAO.class.getSimpleName());

    private static Service build(final ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setId(rs.getLong("id"));
        s.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        s.setSchedulingDate(rs.getObject("scheduling_date", LocalDateTime.class));
        s.setCancelled(rs.getBoolean("cancelled"));
        s.setCancelled(rs.getBoolean("completed"));
        s.setInstallations(rs.getInt("installations"));
        s.setTubeLength(rs.getDouble("tube_length"));
        s.setRappelRequired(rs.getBoolean("rappel_required"));
        s.setRemoval(rs.getInt("removal"));
        s.setMaintenance(rs.getInt("maintenance"));
        s.setValue(rs.getDouble("value"));
        return s;
    }

    public static List<Service> select(final Connection db, final Integer page, final Integer rpp) {
        final List<Service> list = new ArrayList<>();
        try {
            final String query = "SELECT s.* FROM public.services s ORDER BY s.id ASC OFFSET ? LIMIT ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, rpp * (page - 1));
            st.setInt(2, rpp);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Service s = build(rs);
                list.add(s);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return list;
    }

    public static Service findById(final Connection db, final Long id) {
        if (id == null) return null;

        Service s = null;
        try {
            final String query = "SELECT s.* FROM public.services s WHERE s.id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                s = build(rs);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return s;
    }

    public static int count(final Connection db) {
        int value = 0;
        try {
            final String query = "SELECT count(s.id) FROM public.services s";
            PreparedStatement st = db.prepareStatement(query);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                value = rs.getInt(0);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return value;
    }


    public static boolean insert(final Connection db, final Service s) {
        try {
            final String query = "INSERT INTO public.services (" +
                    "created_at, scheduling_date, cancelled, completed, installations, tube_length, rappel_required, removal, maintenance, value" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int index = 0;
            PreparedStatement st = db.prepareStatement(query);
            st.setObject(++index, s.getCreatedAt());
            st.setObject(++index, s.getSchedulingDate());
            st.setBoolean(++index, s.isCancelled());
            st.setBoolean(++index, s.isCompleted());
            st.setInt(++index, s.getInstallations());
            st.setDouble(++index, s.getTubeLength());
            st.setBoolean(++index, s.isRappelRequired());
            st.setInt(++index, s.getRemoval());
            st.setInt(++index, s.getMaintenance());
            st.setDouble(++index, s.getValue());
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            System.out.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage());
        }
        return false;
    }

    public static boolean update(final Connection db, final Service s) {
        try {
            final String query = "UPDATE public.services s SET " +
                    "created_at = ?, " +
                    "scheduling_date = ?, " +
                    "cancelled = ?, " +
                    "completed = ?, " +
                    "installations = ?, " +
                    "tube_length = ?, " +
                    "rappel_required = ?, " +
                    "removal = ?, " +
                    "maintenance = ?, " +
                    "value = ? " +
                    "WHERE s.id = ?";

            int index = 0;
            PreparedStatement st = db.prepareStatement(query);
            st.setObject(++index, s.getCreatedAt());
            st.setObject(++index, s.getSchedulingDate());
            st.setBoolean(++index, s.isCancelled());
            st.setBoolean(++index, s.isCompleted());
            st.setInt(++index, s.getInstallations());
            st.setDouble(++index, s.getTubeLength());
            st.setBoolean(++index, s.isRappelRequired());
            st.setInt(++index, s.getRemoval());
            st.setInt(++index, s.getMaintenance());
            st.setDouble(++index, s.getValue());
            st.setLong(++index, s.getId());
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    public static boolean delete(final Connection db, final Service s) {
        if (s == null || s.getId() == null) return false;
        return deleteById(db, s.getId());
    }

    public static boolean deleteById(final Connection db, final long id) {
        try {
            final String query = "DELETE FROM public.services WHERE id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setLong(1, id);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return false;
    }

}

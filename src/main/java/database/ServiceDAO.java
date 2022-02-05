package database;

import models.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ServiceDAO {

    private static final Logger log = Logger.getLogger(ServiceDAO.class.getSimpleName());

    public static Service build(final ResultSet rs) throws SQLException {
        return build(rs, 1);
    }

    public static Service build(final ResultSet rs, int index) throws SQLException {
        Service s = new Service();
        s.setId(rs.getLong(index++));
        s.setCreatedAt(rs.getObject(index++, LocalDateTime.class));
        s.setScheduledTo(rs.getObject(index++, LocalDateTime.class));
        s.setCancelled(rs.getBoolean(index++));
        s.setCompleted(rs.getBoolean(index++));
        s.setInstallations(rs.getInt(index++));
        s.setTubeLength(rs.getDouble(index++));
        s.setRappelRequired(rs.getBoolean(index++));
        s.setRemoval(rs.getInt(index++));
        s.setMaintenance(rs.getInt(index++));
        s.setValue(rs.getDouble(index++));
        s.setCostumerId(rs.getLong(index++));
        s.setCostumer(CostumerDAO.build(rs, index));
        return s;
    }

    public static List<Service> select(final Connection db, final Integer page, final Integer rpp,
                                       final LocalDate from, final LocalDate to,
                                       final Long costumerId) {
        final List<Service> list = new ArrayList<>();
        try {
            final StringBuilder query = new StringBuilder("SELECT s.*, c.* FROM public.services s ")
                    .append("LEFT JOIN costumers c ON c.id = s.costumer_id ")
                    .append("WHERE s.cancelled = false ");

            if (from != null || to != null) {
                query.append("AND ");
                if (from != null) {
                    query.append("date(s.scheduled_to) >= ? ");
                }
                if (to != null) {
                    if (from != null) query.append("AND ");
                    query.append("date(s.scheduled_to) <= ? ");
                }
            }

            if (costumerId != null) {
                query.append("AND s.costumer_id = ? ");
            }

            query.append("ORDER BY s.cancelled ASC, ")
                    .append("s.completed ASC, ")
                    .append("s.scheduled_to ASC ");

            if (page != null && rpp != null) {
                query.append("OFFSET ? ").append("LIMIT ?");
            }

            int index = 1;
            PreparedStatement ps = db.prepareStatement(query.toString());
            if (from != null) ps.setObject(index++, from);
            if (to != null) ps.setObject(index++, to);
            if (costumerId != null) ps.setLong(index++, costumerId);
            if (page != null && rpp != null) {
                ps.setInt(index++, rpp * (page - 1));
                ps.setInt(index, rpp);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Service s = build(rs);
                list.add(s);
            }
            ps.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return list;
    }

    public static Service findById(final Connection db, final Long id) {
        if (id == null) return null;

        Service s = null;
        try {
            final String query = "SELECT s.*, c.* FROM public.services s " +
                    "LEFT JOIN costumers c ON c.id = s.costumer_id " +
                    "WHERE s.id = ?";
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
                value = rs.getInt(1);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return value;
    }


    public static Service insert(final Connection db, final Service s) {
        try {
            final String query = "INSERT INTO public.services (" +
                    "created_at, scheduled_to, cancelled, completed, installations, tube_length, rappel_required, removal, maintenance, value, costumer_id " +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int index = 1;
            PreparedStatement ps = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(index++, s.getCreatedAt());
            ps.setObject(index++, s.getScheduledTo());
            ps.setBoolean(index++, s.isCancelled());
            ps.setBoolean(index++, s.isCompleted());
            ps.setInt(index++, s.getInstallations());
            ps.setDouble(index++, s.getTubeLength());
            ps.setBoolean(index++, s.isRappelRequired());
            ps.setInt(index++, s.getRemoval());
            ps.setInt(index++, s.getMaintenance());
            ps.setDouble(index++, s.getValue());
            ps.setLong(index, s.getCostumerId());
            int inserted = ps.executeUpdate();
            if (inserted <= 0) {
                throw new SQLException("Creating service failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    final Long id = rs.getLong(1);
                    s.setId(id);
                    return s;
                } else {
                    throw new SQLException("Creating service failed, no id obtained.");
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage());
        }
        return null;
    }

    public static boolean update(final Connection db, final Service s) {
        try {
            final String query = "UPDATE public.services s SET " +
                    "created_at = ?, " +
                    "scheduled_to = ?, " +
                    "cancelled = ?, " +
                    "completed = ?, " +
                    "installations = ?, " +
                    "tube_length = ?, " +
                    "rappel_required = ?, " +
                    "removal = ?, " +
                    "maintenance = ?, " +
                    "value = ?, " +
                    "costumer_id = ? " +
                    "WHERE s.id = ?";

            int index = 1;
            PreparedStatement st = db.prepareStatement(query);
            st.setObject(index++, s.getCreatedAt());
            st.setObject(index++, s.getScheduledTo());
            st.setBoolean(index++, s.isCancelled());
            st.setBoolean(index++, s.isCompleted());
            st.setInt(index++, s.getInstallations());
            st.setDouble(index++, s.getTubeLength());
            st.setBoolean(index++, s.isRappelRequired());
            st.setInt(index++, s.getRemoval());
            st.setInt(index++, s.getMaintenance());
            st.setDouble(index++, s.getValue());
            st.setLong(index++, s.getCostumerId());
            st.setLong(index, s.getId());
            int updated = st.executeUpdate();
            st.close();
            return updated > 0;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    public static boolean complete(final Connection db, final Long id) {
        if (id == null) return false;
        try {
            final String query = "UPDATE public.services s SET completed = true WHERE s.id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setLong(1, id);
            int updated = st.executeUpdate();
            st.close();
            return updated > 0;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    public static boolean cancel(final Connection db, final Long id) {
        if (id == null) return false;
        try {
            final String query = "UPDATE public.services s SET cancelled = true WHERE s.id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setLong(1, id);
            int updated = st.executeUpdate();
            st.close();
            return updated > 0;
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

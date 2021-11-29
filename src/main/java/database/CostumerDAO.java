package database;

import models.Costumer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CostumerDAO {

    private static final Logger log = Logger.getLogger(CostumerDAO.class.getSimpleName());

    public static Costumer build(final ResultSet rs) throws SQLException {
        return build(rs, 1);
    }

    public static Costumer build(final ResultSet rs, int i) throws SQLException {
        Costumer c = new Costumer();
        c.setId(rs.getLong(i++));
        c.setCpf(rs.getString(i++));
        c.setName(rs.getString(i++));
        c.setPhone(rs.getString(i++));
        c.setEmail(rs.getString(i++));
        c.setAddress(rs.getString(i++));
        c.setNumber(rs.getString(i++));
        c.setComplement(rs.getString(i++));
        c.setReference(rs.getString(i++));
        c.setZipCode(rs.getString(i++));
        c.setDistrict(rs.getString(i++));
        c.setCity(rs.getString(i++));
        c.setState(rs.getString(i++));
        c.setCountry(rs.getString(i));
        return c;
    }

    public static List<Costumer> select(final Connection db, final Integer page, final Integer rpp) {
        final List<Costumer> list = new ArrayList<>();
        try {
            final String query = "SELECT c.* FROM public.costumers c ORDER BY c.cpf ASC OFFSET ? LIMIT ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, rpp * (page - 1));
            st.setInt(2, rpp);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Costumer c = build(rs);
                list.add(c);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return list;
    }

    public static Costumer findById(final Connection db, final Long id) {
        if (id == null) return null;
        Costumer c = null;
        try {
            final String query = "SELECT c.* FROM public.costumers c WHERE c.id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setLong(1, id);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                c = build(rs);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return c;
    }

    public static int count(final Connection db) {
        int value = 0;
        try {
            final String query = "SELECT DISTINCT count(c.cpf) FROM public.costumers c";
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

    public static Costumer insert(final Connection db, final Costumer c) {
        try {
            final String query = "INSERT INTO public.costumers (" +
                    "cpf, name, phone, email, address, number, complement, reference, zip_code, district, city, state, country" +
                    ") VALUES (" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                    ") RETURNING id";
            PreparedStatement ps = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 0;
            ps.setString(++index, c.getCpf());
            ps.setString(++index, c.getName());
            ps.setString(++index, c.getPhone());
            ps.setString(++index, c.getEmail());
            ps.setString(++index, c.getAddress());
            ps.setString(++index, c.getNumber());
            ps.setString(++index, c.getComplement());
            ps.setString(++index, c.getReference());
            ps.setString(++index, c.getZipCode());
            ps.setString(++index, c.getDistrict());
            ps.setString(++index, c.getCity());
            ps.setString(++index, c.getState());
            ps.setString(++index, c.getCountry());
            int inserted = ps.executeUpdate();
            if (inserted <= 0) {
                throw new SQLException("Creating costumer failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    final Long id = rs.getLong(1);
                    c.setId(id);
                    return c;
                } else {
                    throw new SQLException("Creating costumer failed, no id obtained.");
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage());
        }
        return null;
    }

    public static boolean update(final Connection db, final Costumer c) {
        try {
            final String query = "UPDATE public.costumers c SET " +
                    "cpf = ?, " +
                    "name = ?, " +
                    "phone = ?, " +
                    "email = ?, " +
                    "address = ?, " +
                    "number = ?, " +
                    "complement = ?, " +
                    "reference = ?, " +
                    "zip_code = ?, " +
                    "district = ?, " +
                    "city = ?, " +
                    "state = ?, " +
                    "country = ? " +
                    "WHERE c.id = ?";

            int index = 1;
            PreparedStatement st = db.prepareStatement(query);
            st.setString(index++, c.getCpf());
            st.setString(index++, c.getName());
            st.setString(index++, c.getPhone());
            st.setString(index++, c.getEmail());
            st.setString(index++, c.getAddress());
            st.setString(index++, c.getNumber());
            st.setString(index++, c.getComplement());
            st.setString(index++, c.getReference());
            st.setString(index++, c.getZipCode());
            st.setString(index++, c.getDistrict());
            st.setString(index++, c.getCity());
            st.setString(index++, c.getState());
            st.setString(index++, c.getCountry());
            st.setLong(index, c.getId());
            int updated = st.executeUpdate();
            st.close();
            return updated > 0;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    public static boolean deleteById(final Connection db, final Costumer c) {
        if (c == null || c.getId() == null) return false;
        return deleteById(db, c.getId());
    }

    public static boolean deleteById(final Connection db, final Long id) {
        try {
            final String query = "DELETE FROM public.constumers WHERE id = ?";
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

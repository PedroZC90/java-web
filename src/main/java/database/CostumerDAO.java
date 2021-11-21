package database;

import models.Costumer ;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CostumerDAO {

    private static final Logger log = Logger.getLogger(CostumerDAO.class.getSimpleName());

    private static Costumer build(final ResultSet rs) throws SQLException {
        Costumer c = new Costumer();
        c.setCpf(rs.getString("cpf"));
        c.setName(rs.getString("name"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setNumber(rs.getString("number"));
        c.setComplement(rs.getString("complement"));
        c.setReference(rs.getString("reference"));
        c.setZipCode(rs.getString("zip_code"));
        c.setDistrict(rs.getString("district"));
        c.setCity(rs.getString("city"));
        c.setState(rs.getString("state"));
        c.setCountry(rs.getString("country"));
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

    public static Costumer findByCpf(final Connection db, final String cpf) {
        if (StringUtils.isBlank(cpf)) return null;
        Costumer c = null;
        try {
            final String query = "SELECT c.* FROM public.costumers c WHERE c.cpf = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, cpf);

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

    public static boolean insert(final Connection db, final Costumer c) {
        try {
            final String query = "INSERT INTO public.costumers (" +
                    "cpf, name, phone, email, address, number, complement, reference, zip_code, district, city, state, country" +
                    ") VALUES (" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                    ")";
            PreparedStatement st = db.prepareStatement(query);
            int index = 0;
            st.setString(++index, c.getCpf());
            st.setString(++index, c.getName());
            st.setString(++index, c.getPhone());
            st.setString(++index, c.getEmail());
            st.setString(++index, c.getAddress());
            st.setString(++index, c.getNumber());
            st.setString(++index, c.getComplement());
            st.setString(++index, c.getReference());
            st.setString(++index, c.getZipCode());
            st.setString(++index, c.getDistrict());
            st.setString(++index, c.getCity());
            st.setString(++index, c.getState());
            st.setString(++index, c.getCountry());
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            System.out.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage());
        }
        return false;
    }

    public static boolean update(final Connection db, final Costumer c) {
        try {
            final String query = "UPDATE public.costumers c SET " +
                    "name = ? " +
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
                    "WHERE c.cpf = ?";

            int index = 0;
            PreparedStatement st = db.prepareStatement(query);
            st.setString(++index, c.getName());
            st.setString(++index, c.getPhone());
            st.setString(++index, c.getEmail());
            st.setString(++index, c.getAddress());
            st.setString(++index, c.getNumber());
            st.setString(++index, c.getComplement());
            st.setString(++index, c.getReference());
            st.setString(++index, c.getZipCode());
            st.setString(++index, c.getDistrict());
            st.setString(++index, c.getCity());
            st.setString(++index, c.getState());
            st.setString(++index, c.getCountry());
            st.setString(++index, c.getCpf());
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    public static boolean deleteById(final Connection db, final Costumer c) {
        if (c == null || c.getCpf() == null) return false;
        return deleteById(db, c.getCpf());
    }

    public static boolean deleteById(final Connection db, final String cpf) {
        try {
            final String query = "DELETE FROM public.constumers WHERE cpf = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, cpf);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        return false;
    }

}

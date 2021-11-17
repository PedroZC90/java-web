package com.pedrozc90.javaweb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TestDAO {

    private static final Logger log = Logger.getLogger(TestDAO.class.getSimpleName());

    public static Map<Integer, String> select(final Connection db) {
        final Map<Integer, String> map = new HashMap<>();
        try {
            final String query = "SELECT t.* FROM test t ORDER BY t.id ASC";
            PreparedStatement st = db.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                map.put(id, name);
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s\n", e.getErrorCode(), e.getMessage()));
        }
        return map;
    }

    public static String findById(final Connection db, final int id) {
        String name = null;
        try {
            String query = "SELECT t.* FROM test t WHERE t.id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
            st.close();
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s\n", e.getErrorCode(), e.getMessage()));
        }
        return name;
    }

    public static boolean inser(final Connection db, final String name) {
        try {
            String query = "INSERT INTO test (name) VALUES (?)";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, name);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            System.out.format("SQLError (%d): %s\n", e.getErrorCode(), e.getMessage());
        }
        return false;
    }

    public static boolean update(final Connection db, final Integer id, final String name) {
        try {
            String query = "UPDATE test t SET name = ? WHERE t.id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, name);
            st.setInt(2, id);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s\n", e.getErrorCode(), e.getMessage()));
            return false;
        }
    }

    public static boolean deleteById(final Connection db, final int id) {
        try {
            String query = "DELETE FROM test WHERE id = ?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);
            boolean result = st.execute();
            st.close();
            return result;
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s\n", e.getErrorCode(), e.getMessage()));
        }
        return false;
    }

}

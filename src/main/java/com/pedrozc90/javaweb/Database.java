package com.pedrozc90.javaweb;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {

    private static final Logger log = Logger.getLogger(Database.class.getSimpleName());

    /* config: */
    private static final String dbTimezone = "America/Sao_Paulo";
    private static final String dbCharset = "UTF-8";
    /* attributes: */
    private static final String dbHost = "localhost";
    private static final String dbName = "java-web";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "1";
    private static final String dbURL = String.format("jdbc:postgresql://%s/%s", dbHost, dbName);

    /* static connection link */
    private static Connection connection = null;

    /* constructor: */
    public Database() { }

    /* methods: */
    public Connection getConnection() {
        return connection;
    }

    /* connect -- opens connection to mysql database. */
    public void connect() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        } catch (SQLException e) {
            log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
        }
        if (isConnected()) {
            log.info(String.format("Connected to database %s.", dbName));
        }
    }

    /* disconnect -- closes connection to mysql database. */
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                log.info(String.format("Disconnected from database %s.", dbName));
            } catch (SQLException e) {
                log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            }
        } else {
            log.severe("Unable to find database connection.");
        }
    }

    /* getStatus -- check if link is connected. */
    public boolean isConnected() {
        return (connection != null);
    }

}

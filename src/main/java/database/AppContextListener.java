package database;

import utils.AppUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(AppContextListener.class.getSimpleName());

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        try {
            Database db = new Database();
            db.connect();
            if (db.isConnected()) {
                Connection connection = db.getConnection();
                context.setAttribute(AppUtils.CONNECTION_KEY, connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Connection connection = (Connection) context.getAttribute(AppUtils.CONNECTION_KEY);
        if (connection != null) {
            try {
                connection.close();
                log.info("Disconnected from database.");
            } catch (SQLException e) {
                log.severe(String.format("SQLError (%d): %s", e.getErrorCode(), e.getMessage()));
            }
        } else {
            log.severe("Unable to find database connection.");
        }
    }

}

package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides JDBC connections configured via {@link AppConfig}.
 */
public class ConnectionUtil {

    private ConnectionUtil() { /* utility class */ }

    public static Connection getConnection() throws SQLException {
        String driver = AppConfig.getDbDriver();
        if (driver == null || driver.trim().isEmpty()) {
            driver = "com.mysql.jdbc.Driver";
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, "JDBC Driver not found: " + driver, ex);
            throw new SQLException("JDBC Driver not found: " + driver, ex);
        }

        String url = AppConfig.getDbUrl();
        if (url == null || url.trim().isEmpty()) {
            throw new SQLException("Database URL not configured (db.url)");
        }
        String user = AppConfig.getDbUser();
        String password = AppConfig.getDbPassword();

        return DriverManager.getConnection(url, user, password);
    }
}

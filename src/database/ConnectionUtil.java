/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LuisBravo
 */
public class ConnectionUtil {

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

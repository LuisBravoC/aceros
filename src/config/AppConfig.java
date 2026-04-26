package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AppConfig {

    private static final Properties PROPS = new Properties();
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());

    static {
        // Try loading from workspace-relative config/config.properties first
        try (InputStream in = new FileInputStream("config/config.properties")) {
            PROPS.load(in);
            LOGGER.log(Level.CONFIG, "Loaded config from config/config.properties");
        } catch (IOException ex) {
            // Fallback to classpath resource
            try (InputStream in = AppConfig.class.getResourceAsStream("/config/config.properties")) {
                if (in != null) {
                    PROPS.load(in);
                    LOGGER.log(Level.CONFIG, "Loaded config from classpath /config/config.properties");
                } else {
                    LOGGER.log(Level.WARNING, "config/config.properties not found on filesystem or classpath");
                }
            } catch (IOException ex2) {
                LOGGER.log(Level.WARNING, "Failed to load config/config.properties from classpath", ex2);
            }
        }
    }

    private AppConfig() {
        // utility
    }

    public static String get(String key) {
        return PROPS.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }

    public static String getDbDriver() {
        return get("db.driver", "com.mysql.jdbc.Driver");
    }

    public static String getDbUrl() {
        return get("db.url");
    }

    public static String getDbUser() {
        return get("db.user");
    }

    public static String getDbPassword() {
        return get("db.password");
    }
}

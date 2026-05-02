package dao;

import config.ConnectionUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;

/**
 * Base class for DAO integration tests.
 * Redirects AppConfig to use aceros_test before any DAO or DB class is loaded.
 */
public abstract class BaseDaoSupport {

    private static final Logger LOGGER = Logger.getLogger(BaseDaoSupport.class.getName());

    /*
     * This static block runs when BaseDaoTest (or any subclass) is first loaded by the JVM —
     * before AppConfig's static initializer, which runs only when AppConfig is first referenced
     * (i.e., when the first DAO method is called).
     */
    static {
        System.setProperty("app.config", "config/test.config.properties");
    }

    @BeforeClass
    public static void verifyTestConnection() throws SQLException {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String url = conn.getMetaData().getURL();
            if (!url.contains("aceros_test")) {
                throw new IllegalStateException(
                    "Tests must run against 'aceros_test' but connected to: " + url);
            }
        }
    }

    /**
     * Executes a raw SQL statement against the test DB.
     * Use in @Before/@After to set up / clean up test data.
     */
    protected static void execSql(String sql) {
        try (Connection conn = ConnectionUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing test SQL: " + sql, e);
            throw new RuntimeException(e);
        }
    }
}

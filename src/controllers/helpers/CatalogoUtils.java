package controllers.helpers;

import database.ConnectionUtil;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods shared by all catalog CRUD screens
 * (Materiales, Alturas, Calibres, Rombos).
 */
public final class CatalogoUtils {

    private static final Logger LOGGER = Logger.getLogger(CatalogoUtils.class.getName());

    private CatalogoUtils() { /* utility class — no instances */ }

    /**
     * Queries SELECT MAX(id) FROM {tabla} and writes the next available ID
     * into campoCodigo.  Sets "1" when the table is empty.
     *
     * <p>The {@code tabla} argument MUST be a compile-time constant string
     * (e.g. "materiales") — never pass user input.
     */
    public static void cargarSiguienteId(String tabla, TextField campoCodigo) {
        String sql = "SELECT MAX(id) FROM " + tabla;
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getObject(1) != null) {
                campoCodigo.setText(String.valueOf(rs.getInt(1) + 1));
            } else {
                campoCodigo.setText("1");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error cargando siguiente id para tabla: " + tabla, ex);
        }
    }
}

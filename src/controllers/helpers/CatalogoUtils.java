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

    private CatalogoUtils() { /* utility class - no instances */ }

    // -- functional interfaces (two-field) ----------------------------------

    /** Insert operation: (nombre, medida) -> boolean. */
    @FunctionalInterface
    public interface CrudInsert {
        boolean apply(String nombre, String medida);
    }

    /** Update operation: (id, nombre, medida) -> boolean. */
    @FunctionalInterface
    public interface CrudUpdate {
        boolean apply(String id, String nombre, String medida);
    }

    // -- functional interfaces (single-field) -------------------------------

    /** Insert with a single text param (nombre only). */
    @FunctionalInterface
    public interface CrudInsertSingle { boolean apply(String nombre); }

    /** Update with two params: (id, nombre). */
    @FunctionalInterface
    public interface CrudUpdateSingle { boolean apply(String id, String nombre); }

    // -- cargarSiguienteId --------------------------------------------------

    /**
     * Queries SELECT MAX(id) FROM tabla and writes the next available ID
     * into campoCodigo. Sets "1" when the table is empty.
     * tabla MUST be a compile-time constant -- never pass user input.
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

    // -- agregarCatalogo (two-field: nombre + medida) -----------------------

    /**
     * Inserts a catalog entry with two text fields (nombre + medida).
     * Use method reference: AlturasService::insert
     */
    public static void agregarCatalogo(
            CrudInsert insertFn,
            TextField nombreField,
            TextField medidaField,
            Runnable onSuccess) {
        String nombre = safeText(nombreField);
        String medida = safeText(medidaField);
        try {
            LOGGER.log(Level.FINE, "RECORD RUNNING INSIDE!!!");
            boolean ok = insertFn.apply(nombre, medida);
            LOGGER.log(Level.FINE, "RECORD RUNNING POST QUERY");
            if (ok) {
                LOGGER.log(Level.INFO, "RECORD ADDED");
                nombreField.clear();
                medidaField.clear();
                onSuccess.run();
            } else { LOGGER.log(Level.WARNING, "RECORD FAILED"); }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding catalog entry", e);
        }
    }

    // -- agregarCatalogo (single-field: nombre only) ------------------------

    /**
     * Inserts a catalog entry with a single text field (nombre only).
     * Use method reference: MaterialesService::insert
     */
    public static void agregarCatalogo(
            CrudInsertSingle insertFn, TextField nombreField, Runnable onSuccess) {
        String nombre = safeText(nombreField);
        try {
            LOGGER.log(Level.FINE, "RECORD RUNNING INSIDE!!!");
            boolean ok = insertFn.apply(nombre);
            LOGGER.log(Level.FINE, "RECORD RUNNING POST QUERY");
            if (ok) {
                LOGGER.log(Level.INFO, "RECORD ADDED");
                nombreField.clear();
                onSuccess.run();
            } else { LOGGER.log(Level.WARNING, "RECORD FAILED"); }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding entry", e);
        }
    }

    // -- modificarCatalogo (three-field: id, nombre, medida) ----------------

    /**
     * Updates a catalog entry reading from three shared edit fields.
     * Use method reference: AlturasService::update
     */
    public static void modificarCatalogo(
            CrudUpdate updateFn,
            TextField codField,
            TextField nombreField,
            TextField medidaField,
            Runnable onSuccess) {
        String id     = safeText(codField);
        String nombre = safeText(nombreField);
        String medida = safeText(medidaField);
        try {
            LOGGER.log(Level.FINE, "RECORD RUNNING INSIDE!!!");
            boolean ok = updateFn.apply(id, nombre, medida);
            LOGGER.log(Level.FINE, "RECORD RUNNING POST QUERY");
            if (ok) {
                LOGGER.log(Level.INFO, "RECORD UPDATED");
                onSuccess.run();
            } else { LOGGER.log(Level.WARNING, "RECORD FAILED"); }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating catalog entry", e);
        }
    }

    // -- modificarCatalogo (single-field: id, nombre) -----------------------

    /**
     * Updates a catalog entry with two fields: id and nombre.
     * Use method reference: MaterialesService::update
     */
    public static void modificarCatalogo(
            CrudUpdateSingle updateFn, TextField codField, TextField nombreField,
            Runnable onSuccess) {
        String id     = safeText(codField);
        String nombre = safeText(nombreField);
        try {
            LOGGER.log(Level.FINE, "RECORD RUNNING INSIDE!!!");
            boolean ok = updateFn.apply(id, nombre);
            LOGGER.log(Level.FINE, "RECORD RUNNING POST QUERY");
            if (ok) {
                LOGGER.log(Level.INFO, "RECORD UPDATED");
                onSuccess.run();
            } else { LOGGER.log(Level.WARNING, "RECORD FAILED"); }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating entry", e);
        }
    }

    // -- private helpers ----------------------------------------------------

    private static String safeText(TextField tf) {
        String t = tf.getText();
        return (t == null || t.isEmpty()) ? "NULL" : t;
    }
}

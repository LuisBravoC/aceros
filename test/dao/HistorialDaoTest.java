package dao;

import models.Historial;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for HistorialDao.
 * Self-contained: inserts one produccion record (with required catalog entries)
 * in @BeforeClass and removes everything in @AfterClass.
 */
public class HistorialDaoTest extends BaseDaoSupport {

    private static final String AUTOR_ID  = "1";
    private static final String MATERIAL  = "ACERO HIST TEST";
    private static final String CALIBRE   = "12.00";
    private static final String ALTURA    = "6.00";
    private static final String ROMBO     = "4.00";
    private static final String HOY       = LocalDate.now().toString();
    private static final String DE        = LocalDate.now().minusMonths(1).toString();

    @BeforeClass
    public static void seedData() {
        execSql("INSERT IGNORE INTO materiales (nombre) VALUES ('" + MATERIAL + "')");
        execSql("INSERT IGNORE INTO calibres (calibre) VALUES (" + CALIBRE + ")");
        execSql("INSERT IGNORE INTO alturas (altura) VALUES (" + ALTURA + ")");
        execSql("INSERT IGNORE INTO rombos (rombo) VALUES (" + ROMBO + ")");
        // Insert one produccion record with today's date so historial queries return results
        execSql(
            "INSERT INTO produccion (material_id, calibre_id, altura_id, rombo_id, metros, cantidad, autor_id, fecha_registro, dia) " +
            "SELECT m.id, c.id, a.id, r.id, 10, 2, " + AUTOR_ID + ", CURDATE(), 'LUNES' " +
            "FROM materiales m, calibres c, alturas a, rombos r " +
            "WHERE m.nombre = '" + MATERIAL + "' AND c.calibre = " + CALIBRE +
            " AND a.altura = " + ALTURA + " AND r.rombo = " + ROMBO + " LIMIT 1"
        );
    }

    @AfterClass
    public static void cleanData() {
        execSql("DELETE p FROM produccion p JOIN materiales m ON p.material_id = m.id WHERE m.nombre = '" + MATERIAL + "'");
        execSql("DELETE FROM materiales WHERE nombre = '" + MATERIAL + "'");
        execSql("DELETE FROM calibres WHERE calibre = " + CALIBRE);
        execSql("DELETE FROM alturas WHERE altura = " + ALTURA);
        execSql("DELETE FROM rombos WHERE rombo = " + ROMBO);
    }

    @Test
    public void getHistorial_withValidRange_returnsList() {
        ObservableList<Historial> list = HistorialDao.getHistorial(AUTOR_ID, DE, HOY);
        assertNotNull(list);
        assertFalse("Debe haber al menos un registro de historial en el rango", list.isEmpty());
    }

    @Test
    public void getHistorial_nonExistentAuthor_returnsEmptyList() {
        ObservableList<Historial> list = HistorialDao.getHistorial("999999", DE, HOY);
        assertNotNull(list);
        assertTrue("Autor inexistente debe retornar lista vacia", list.isEmpty());
    }

    @Test
    public void getHistorial_futureRange_returnsEmptyList() {
        String futureFrom = LocalDate.now().plusDays(1).toString();
        String futureTo   = LocalDate.now().plusDays(30).toString();
        ObservableList<Historial> list = HistorialDao.getHistorial(AUTOR_ID, futureFrom, futureTo);
        assertNotNull(list);
        assertTrue("Rango futuro no debe tener registros", list.isEmpty());
    }

    @Test
    public void getHistorial_recordsHaveExpectedFields() {
        ObservableList<Historial> list = HistorialDao.getHistorial(AUTOR_ID, DE, HOY);
        assertFalse("Debe tener registros", list.isEmpty());
        Historial first = list.get(0);
        assertNotNull("codigo no debe ser null", first.getTcCodigoHistorial());
        assertNotNull("material no debe ser null", first.getTcMaterialHistorial());
        assertEquals(MATERIAL, first.getTcMaterialHistorial());
    }
}

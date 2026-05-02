package dao;

import models.Calibres;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CalibresDaoTest extends BaseDaoSupport {

    private static final String NOMBRE = "Calibre TEST";
    private static final String MEDIDA = "14";

    @Before
    @After
    public void cleanUp() {
        execSql("DELETE FROM calibres WHERE nombre = '" + NOMBRE + "'");
    }

    @Test
    public void getAll_returnsNonEmptyList() {
        ObservableList<Calibres> list = CalibresDao.getAll();
        assertNotNull(list);
        assertFalse("calibres debe tener datos de seed", list.isEmpty());
    }

    @Test
    public void findById_nonExistent_returnsNull() {
        assertNull(CalibresDao.findById("999999"));
    }

    @Test
    public void insert_thenFindById_matchesValues() {
        assertTrue(CalibresDao.insert(NOMBRE, MEDIDA));

        Calibres found = CalibresDao.getAll().stream()
            .filter(c -> NOMBRE.equals(c.getTcNombreCalibre()))
            .findFirst().orElse(null);

        assertNotNull("El registro insertado debe aparecer en getAll()", found);
        assertEquals(MEDIDA, found.getTcCalibre());

        Calibres byId = CalibresDao.findById(String.valueOf(found.getTcCodigoCalibre()));
        assertNotNull(byId);
        assertEquals(NOMBRE, byId.getTcNombreCalibre());
    }

    @Test
    public void update_changesValue() {
        CalibresDao.insert(NOMBRE, MEDIDA);
        Calibres ins = CalibresDao.getAll().stream()
            .filter(c -> NOMBRE.equals(c.getTcNombreCalibre()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado"));

        String id = String.valueOf(ins.getTcCodigoCalibre());
        assertTrue(CalibresDao.update(id, NOMBRE, "20"));
        assertEquals("20", CalibresDao.findById(id).getTcCalibre());
    }

    @Test
    public void delete_removesRecord() {
        CalibresDao.insert(NOMBRE, MEDIDA);
        Calibres ins = CalibresDao.getAll().stream()
            .filter(c -> NOMBRE.equals(c.getTcNombreCalibre()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado"));

        String id = String.valueOf(ins.getTcCodigoCalibre());
        assertTrue(CalibresDao.delete(id));
        assertNull(CalibresDao.findById(id));
    }
}

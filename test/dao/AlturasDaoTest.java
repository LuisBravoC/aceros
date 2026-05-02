package dao;

import models.Alturas;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for AlturasDao against the aceros_test database.
 */
public class AlturasDaoTest extends BaseDaoSupport {

    private static final String TEST_NOMBRE  = "Altura TEST";
    private static final String TEST_MEDIDA  = "99.9";

    @Before
    public void cleanUp() {
        execSql("DELETE FROM alturas WHERE nombre = '" + TEST_NOMBRE + "'");
    }

    @After
    public void tearDown() {
        execSql("DELETE FROM alturas WHERE nombre = '" + TEST_NOMBRE + "'");
    }

    @Test
    public void getAll_returnsNonEmptyList() {
        ObservableList<Alturas> list = AlturasDao.getAll();
        assertNotNull("getAll() no debe retornar null", list);
        assertFalse("La tabla alturas debe tener al menos un registro de seed", list.isEmpty());
    }

    @Test
    public void insert_thenFindById_returnsInsertedRecord() {
        boolean inserted = AlturasDao.insert(TEST_NOMBRE, TEST_MEDIDA);
        assertTrue("insert() debe retornar true", inserted);

        // Find the just-inserted row by iterating (no getByNombre exists)
        ObservableList<Alturas> all = AlturasDao.getAll();
        Alturas found = all.stream()
            .filter(a -> TEST_NOMBRE.equals(a.getTcNombreAltura()))
            .findFirst()
            .orElse(null);

        assertNotNull("El registro insertado debe encontrarse en getAll()", found);
        assertEquals(TEST_MEDIDA, found.getTcAltura());
    }

    @Test
    public void findById_withInvalidId_returnsNull() {
        Alturas result = AlturasDao.findById("99999");
        assertNull("findById con ID inexistente debe retornar null", result);
    }

    @Test
    public void insert_update_thenVerifyChange() {
        AlturasDao.insert(TEST_NOMBRE, TEST_MEDIDA);
        ObservableList<Alturas> all = AlturasDao.getAll();
        Alturas inserted = all.stream()
            .filter(a -> TEST_NOMBRE.equals(a.getTcNombreAltura()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Registro de prueba no encontrado"));

        String idStr = String.valueOf(inserted.getTcCodigoAltura());
        boolean updated = AlturasDao.update(idStr, TEST_NOMBRE, "55.5");
        assertTrue("update() debe retornar true", updated);

        Alturas afterUpdate = AlturasDao.findById(idStr);
        assertNotNull(afterUpdate);
        assertEquals("55.5", afterUpdate.getTcAltura());
    }

    @Test
    public void insert_thenDelete_removesRecord() {
        AlturasDao.insert(TEST_NOMBRE, TEST_MEDIDA);
        ObservableList<Alturas> all = AlturasDao.getAll();
        Alturas inserted = all.stream()
            .filter(a -> TEST_NOMBRE.equals(a.getTcNombreAltura()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Registro de prueba no encontrado"));

        String idStr = String.valueOf(inserted.getTcCodigoAltura());
        boolean deleted = AlturasDao.delete(idStr);
        assertTrue("delete() debe retornar true", deleted);

        Alturas afterDelete = AlturasDao.findById(idStr);
        assertNull("El registro eliminado no debe encontrarse", afterDelete);
    }
}

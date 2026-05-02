package dao;

import models.Rombos;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RombosDaoTest extends BaseDaoSupport {

    private static final String NOMBRE = "Rombo TEST";
    private static final String MEDIDA = "25";

    @Before
    @After
    public void cleanUp() {
        execSql("DELETE FROM rombos WHERE nombre = '" + NOMBRE + "'");
    }

    @Test
    public void getAll_returnsNonEmptyList() {
        ObservableList<Rombos> list = RombosDao.getAll();
        assertNotNull(list);
        assertFalse("rombos debe tener datos de seed", list.isEmpty());
    }

    @Test
    public void findById_nonExistent_returnsNull() {
        assertNull(RombosDao.findById("999999"));
    }

    @Test
    public void insert_thenFindById_matchesValues() {
        assertTrue(RombosDao.insert(NOMBRE, MEDIDA));

        Rombos found = RombosDao.getAll().stream()
            .filter(r -> NOMBRE.equals(r.getTcNombreRombo()))
            .findFirst().orElse(null);

        assertNotNull("El registro insertado debe aparecer en getAll()", found);
        assertEquals(MEDIDA, found.getTcRombo());

        Rombos byId = RombosDao.findById(String.valueOf(found.getTcCodigoRombo()));
        assertNotNull(byId);
        assertEquals(NOMBRE, byId.getTcNombreRombo());
    }

    @Test
    public void update_changesValue() {
        RombosDao.insert(NOMBRE, MEDIDA);
        Rombos ins = RombosDao.getAll().stream()
            .filter(r -> NOMBRE.equals(r.getTcNombreRombo()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado"));

        String id = String.valueOf(ins.getTcCodigoRombo());
        assertTrue(RombosDao.update(id, NOMBRE, "30"));
        assertEquals("30", RombosDao.findById(id).getTcRombo());
    }

    @Test
    public void delete_removesRecord() {
        RombosDao.insert(NOMBRE, MEDIDA);
        Rombos ins = RombosDao.getAll().stream()
            .filter(r -> NOMBRE.equals(r.getTcNombreRombo()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado"));

        String id = String.valueOf(ins.getTcCodigoRombo());
        assertTrue(RombosDao.delete(id));
        assertNull(RombosDao.findById(id));
    }
}

package dao;

import models.Materiales;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaterialesDaoTest extends BaseDaoSupport {

    private static final String NOMBRE = "Material TEST";

    @Before
    @After
    public void cleanUp() {
        execSql("DELETE FROM materiales WHERE nombre = '" + NOMBRE + "'");
    }

    @Test
    public void getAll_returnsNonEmptyList() {
        ObservableList<Materiales> list = MaterialesDao.getAll();
        assertNotNull(list);
        assertFalse("materiales debe tener datos de seed", list.isEmpty());
    }

    @Test
    public void findById_nonExistent_returnsNull() {
        assertNull(MaterialesDao.findById("999999"));
    }

    @Test
    public void insert_thenFindById_matchesValues() {
        assertTrue(MaterialesDao.insert(NOMBRE));

        Materiales found = MaterialesDao.getAll().stream()
            .filter(m -> NOMBRE.equals(m.getTcNombreMaterial()))
            .findFirst().orElse(null);

        assertNotNull("El registro insertado debe aparecer en getAll()", found);

        Materiales byId = MaterialesDao.findById(String.valueOf(found.getTcCodigoMaterial()));
        assertNotNull(byId);
        assertEquals(NOMBRE, byId.getTcNombreMaterial());
    }

    @Test
    public void update_changesName() {
        MaterialesDao.insert(NOMBRE);
        Materiales ins = MaterialesDao.getAll().stream()
            .filter(m -> NOMBRE.equals(m.getTcNombreMaterial()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado"));

        String id = String.valueOf(ins.getTcCodigoMaterial());
        String nuevoNombre = NOMBRE + "_UPD";
        // Insert the updated name so cleanUp catches it
        execSql("DELETE FROM materiales WHERE nombre = '" + nuevoNombre + "'");
        assertTrue(MaterialesDao.update(id, nuevoNombre));
        assertEquals(nuevoNombre, MaterialesDao.findById(id).getTcNombreMaterial());
        execSql("DELETE FROM materiales WHERE nombre = '" + nuevoNombre + "'");
    }

    @Test
    public void delete_removesRecord() {
        MaterialesDao.insert(NOMBRE);
        Materiales ins = MaterialesDao.getAll().stream()
            .filter(m -> NOMBRE.equals(m.getTcNombreMaterial()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado"));

        String id = String.valueOf(ins.getTcCodigoMaterial());
        assertTrue(MaterialesDao.delete(id));
        assertNull(MaterialesDao.findById(id));
    }
}

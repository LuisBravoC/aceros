package services;

import dao.BaseDaoSupport;
import models.Calibres;
import models.Materiales;
import models.Rombos;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CalibresService, MaterialesService and RombosService.
 * These services are thin delegates over the corresponding DAOs,
 * so we verify that the delegation is wired correctly.
 */
public class CatalogServicesTest extends BaseDaoSupport {

    private static final String CAL_NOMBRE = "CalibreService TEST";
    private static final String CAL_MEDIDA = "10";
    private static final String ROM_NOMBRE = "RomboService TEST";
    private static final String ROM_MEDIDA = "15";
    private static final String MAT_NOMBRE = "MaterialService TEST";

    @Before
    @After
    public void cleanUp() {
        execSql("DELETE FROM calibres WHERE nombre = '" + CAL_NOMBRE + "'");
        execSql("DELETE FROM rombos WHERE nombre = '" + ROM_NOMBRE + "'");
        execSql("DELETE FROM materiales WHERE nombre = '" + MAT_NOMBRE + "'");
    }

    // ── CalibresService ───────────────────────────────────────────────────────

    @Test
    public void calibresService_getAll_delegatesToDao() {
        ObservableList<Calibres> list = CalibresService.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void calibresService_insert_and_findById() {
        assertTrue(CalibresService.insert(CAL_NOMBRE, CAL_MEDIDA));
        Calibres found = CalibresService.getAll().stream()
            .filter(c -> CAL_NOMBRE.equals(c.getTcNombreCalibre()))
            .findFirst().orElse(null);
        assertNotNull(found);
        Calibres byId = CalibresService.findById(String.valueOf(found.getTcCodigoCalibre()));
        assertNotNull(byId);
        assertEquals(CAL_NOMBRE, byId.getTcNombreCalibre());
    }

    @Test
    public void calibresService_update_and_delete() {
        CalibresService.insert(CAL_NOMBRE, CAL_MEDIDA);
        Calibres ins = CalibresService.getAll().stream()
            .filter(c -> CAL_NOMBRE.equals(c.getTcNombreCalibre()))
            .findFirst().orElseThrow(() -> new AssertionError("no encontrado"));
        String id = String.valueOf(ins.getTcCodigoCalibre());
        assertTrue(CalibresService.update(id, CAL_NOMBRE, "99"));
        assertEquals("99", CalibresService.findById(id).getTcCalibre());
        assertTrue(CalibresService.delete(id));
        assertNull(CalibresService.findById(id));
    }

    // ── RombosService ─────────────────────────────────────────────────────────

    @Test
    public void rombosService_getAll_delegatesToDao() {
        ObservableList<Rombos> list = RombosService.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void rombosService_insert_and_findById() {
        assertTrue(RombosService.insert(ROM_NOMBRE, ROM_MEDIDA));
        Rombos found = RombosService.getAll().stream()
            .filter(r -> ROM_NOMBRE.equals(r.getTcNombreRombo()))
            .findFirst().orElse(null);
        assertNotNull(found);
        Rombos byId = RombosService.findById(String.valueOf(found.getTcCodigoRombo()));
        assertNotNull(byId);
        assertEquals(ROM_NOMBRE, byId.getTcNombreRombo());
    }

    @Test
    public void rombosService_update_and_delete() {
        RombosService.insert(ROM_NOMBRE, ROM_MEDIDA);
        Rombos ins = RombosService.getAll().stream()
            .filter(r -> ROM_NOMBRE.equals(r.getTcNombreRombo()))
            .findFirst().orElseThrow(() -> new AssertionError("no encontrado"));
        String id = String.valueOf(ins.getTcCodigoRombo());
        assertTrue(RombosService.update(id, ROM_NOMBRE, "88"));
        assertEquals("88", RombosService.findById(id).getTcRombo());
        assertTrue(RombosService.delete(id));
        assertNull(RombosService.findById(id));
    }

    // ── MaterialesService ─────────────────────────────────────────────────────

    @Test
    public void materialesService_getAll_delegatesToDao() {
        ObservableList<Materiales> list = MaterialesService.getAll();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void materialesService_insert_and_findById() {
        assertTrue(MaterialesService.insert(MAT_NOMBRE));
        Materiales found = MaterialesService.getAll().stream()
            .filter(m -> MAT_NOMBRE.equals(m.getTcNombreMaterial()))
            .findFirst().orElse(null);
        assertNotNull(found);
        Materiales byId = MaterialesService.findById(String.valueOf(found.getTcCodigoMaterial()));
        assertNotNull(byId);
        assertEquals(MAT_NOMBRE, byId.getTcNombreMaterial());
    }

    @Test
    public void materialesService_delete() {
        MaterialesService.insert(MAT_NOMBRE);
        Materiales ins = MaterialesService.getAll().stream()
            .filter(m -> MAT_NOMBRE.equals(m.getTcNombreMaterial()))
            .findFirst().orElseThrow(() -> new AssertionError("no encontrado"));
        String id = String.valueOf(ins.getTcCodigoMaterial());
        assertTrue(MaterialesService.delete(id));
        assertNull(MaterialesService.findById(id));
    }
}

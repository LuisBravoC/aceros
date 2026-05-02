package dao;

import models.ProduccionSemanal;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for ProduccionDao against aceros_test.
 * Uses autor_id=1 (seed user in aceros_v2.sql).
 * Inserts test catalog entries (material/calibre/altura/rombo) in @BeforeClass
 * and removes them in @AfterClass to keep tests self-contained.
 */
public class ProduccionDaoTest extends BaseDaoSupport {

    private static final String AUTOR_ID  = "1";
    private static final String MATERIAL  = "ACERO TEST";
    private static final String CALIBRE   = "10.00";
    private static final String ALTURA    = "5.00";
    private static final String ROMBO     = "3.00";
    private static final String METROS    = "50";
    private static final String CANTIDAD  = "3";
    private static final String DIA       = "LUNES";
    private static final String FECHA     = LocalDate.now().toString();

    @BeforeClass
    public static void seedCatalog() {
        execSql("INSERT IGNORE INTO materiales (nombre) VALUES ('" + MATERIAL + "')");
        execSql("INSERT IGNORE INTO calibres (calibre) VALUES (" + CALIBRE + ")");
        execSql("INSERT IGNORE INTO alturas (altura) VALUES (" + ALTURA + ")");
        execSql("INSERT IGNORE INTO rombos (rombo) VALUES (" + ROMBO + ")");
    }

    @AfterClass
    public static void cleanCatalog() {
        // clean up all produccion records using our test material
        execSql("DELETE p FROM produccion p JOIN materiales m ON p.material_id = m.id WHERE m.nombre = '" + MATERIAL + "'");
        execSql("DELETE FROM materiales WHERE nombre = '" + MATERIAL + "'");
        execSql("DELETE FROM calibres WHERE calibre = " + CALIBRE);
        execSql("DELETE FROM alturas WHERE altura = " + ALTURA);
        execSql("DELETE FROM rombos WHERE rombo = " + ROMBO);
    }

    @Test
    public void getProduccionSemana_nonExistentAuthor_returnsEmptyList() {
        ObservableList<ProduccionSemanal> list = ProduccionDao.getProduccionSemana("999999");
        assertNotNull(list);
        assertTrue("Autor inexistente debe retornar lista vacia", list.isEmpty());
    }

    @Test
    public void insert_appearsInGetProduccionSemana() {
        assertTrue(ProduccionDao.insert(MATERIAL, CALIBRE, ALTURA, ROMBO, METROS, CANTIDAD, AUTOR_ID, FECHA, DIA));
        try {
            ObservableList<ProduccionSemanal> list = ProduccionDao.getProduccionSemana(AUTOR_ID);
            boolean found = list.stream().anyMatch(p -> MATERIAL.equals(p.getTcMaterialeS()));
            assertTrue("El registro insertado debe aparecer en getProduccionSemana()", found);
        } finally {
            execSql("DELETE p FROM produccion p JOIN materiales m ON p.material_id = m.id WHERE m.nombre = '" + MATERIAL + "' AND p.autor_id = " + AUTOR_ID);
        }
    }

    @Test
    public void insert_thenUpdate_thenDelete() {
        assertTrue(ProduccionDao.insert(MATERIAL, CALIBRE, ALTURA, ROMBO, METROS, CANTIDAD, AUTOR_ID, FECHA, DIA));

        ProduccionSemanal ins = ProduccionDao.getProduccionSemana(AUTOR_ID).stream()
                .filter(p -> MATERIAL.equals(p.getTcMaterialeS()))
                .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado tras insert"));

        String id = ins.getTcCodigoS();
        try {
            // Update
            assertTrue(ProduccionDao.update(id, MATERIAL, CALIBRE, ALTURA, ROMBO, "99", "9", FECHA, DIA));

            ProduccionSemanal updated = ProduccionDao.getProduccionSemana(AUTOR_ID).stream()
                    .filter(p -> id.equals(p.getTcCodigoS()))
                    .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado tras update"));
            assertEquals("99", updated.getTcMetrosS());

            // Delete
            assertTrue(ProduccionDao.delete(id));
            boolean stillExists = ProduccionDao.getProduccionSemana(AUTOR_ID).stream()
                    .anyMatch(p -> id.equals(p.getTcCodigoS()));
            assertFalse("El registro eliminado no debe existir", stillExists);
        } finally {
            // cleanup in case test failed before delete
            ProduccionDao.delete(id);
        }
    }
}

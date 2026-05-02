package dao;

import models.ProduccionSemanal;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for ProduccionDao against aceros_test.
 * Uses autor_id=1 which is guaranteed by seed data in AcerosSQL_fixed.sql.
 */
public class ProduccionDaoTest extends BaseDaoSupport {

    private static final String AUTOR_ID    = "1";
    private static final String MATERIAL    = "Acero TEST";
    private static final String CALIBRE     = "C-TEST";
    private static final String ALTURA      = "A-TEST";
    private static final String ROMBO       = "R-TEST";
    private static final String METROS      = "50";
    private static final String CANTIDAD    = "3";
    private static final String DIA         = "LUNES";
    private static final String FECHA       = LocalDate.now().toString();

    @Before
    @After
    public void cleanUp() {
        execSql("DELETE FROM produccion WHERE material = '" + MATERIAL + "'");
    }

    @Test
    public void getProduccionSemana_forSeedAuthor_returnsList() {
        ObservableList<ProduccionSemanal> list = ProduccionDao.getProduccionSemana(AUTOR_ID);
        assertNotNull(list);
        // seed data has at least one record for autor_id=1
        assertFalse("Debe haber produccion de seed para autor_id=1", list.isEmpty());
    }

    @Test
    public void getProduccionSemana_nonExistentAuthor_returnsEmptyList() {
        ObservableList<ProduccionSemanal> list = ProduccionDao.getProduccionSemana("999999");
        assertNotNull(list);
        assertTrue("Autor inexistente debe retornar lista vacía", list.isEmpty());
    }

    @Test
    public void insert_appearsInGetProduccionSemana() {
        assertTrue(ProduccionDao.insert(MATERIAL, CALIBRE, ALTURA, ROMBO, METROS, CANTIDAD, AUTOR_ID, FECHA, DIA));

        ObservableList<ProduccionSemanal> list = ProduccionDao.getProduccionSemana(AUTOR_ID);
        boolean found = list.stream()
            .anyMatch(p -> MATERIAL.equals(p.getTcMaterialeS()));
        assertTrue("El registro insertado debe aparecer en getProduccionSemana()", found);
    }

    @Test
    public void insert_thenUpdate_thenDelete() {
        assertTrue(ProduccionDao.insert(MATERIAL, CALIBRE, ALTURA, ROMBO, METROS, CANTIDAD, AUTOR_ID, FECHA, DIA));

        ProduccionSemanal ins = ProduccionDao.getProduccionSemana(AUTOR_ID).stream()
            .filter(p -> MATERIAL.equals(p.getTcMaterialeS()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado tras insert"));

        String id = ins.getTcCodigoS();

        // Update
        assertTrue(ProduccionDao.update(id, MATERIAL, CALIBRE, ALTURA, ROMBO, "99", "9", FECHA, DIA));

        // Verify update (re-query)
        ProduccionSemanal updated = ProduccionDao.getProduccionSemana(AUTOR_ID).stream()
            .filter(p -> id.equals(p.getTcCodigoS()))
            .findFirst().orElseThrow(() -> new AssertionError("registro no encontrado tras update"));
        assertEquals("99", updated.getTcMetrosS());

        // Delete
        assertTrue(ProduccionDao.delete(id));
        boolean stillExists = ProduccionDao.getProduccionSemana(AUTOR_ID).stream()
            .anyMatch(p -> id.equals(p.getTcCodigoS()));
        assertFalse("El registro eliminado no debe existir", stillExists);
    }
}

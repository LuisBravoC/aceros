package dao;

import models.Historial;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for HistorialDao.
 * Relies on the seed produccion record (autor_id=1) from AcerosSQL_fixed.sql.
 */
public class HistorialDaoTest extends BaseDaoSupport {

    private static final String AUTOR_ID = "1";
    private static final String HOY      = LocalDate.now().toString();
    private static final String DE       = LocalDate.now().minusMonths(1).toString();

    @Test
    public void getHistorial_withValidRange_returnsList() {
        ObservableList<Historial> list = HistorialDao.getHistorial(AUTOR_ID, DE, HOY);
        assertNotNull(list);
        // seed has at least one record with CURDATE() as fecha_registro
        assertFalse("Debe haber al menos un registro de historial para autor_id=1 en el rango", list.isEmpty());
    }

    @Test
    public void getHistorial_nonExistentAuthor_returnsEmptyList() {
        ObservableList<Historial> list = HistorialDao.getHistorial("999999", DE, HOY);
        assertNotNull(list);
        assertTrue("Autor inexistente debe retornar lista vacía", list.isEmpty());
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
        if (list.isEmpty()) return; // skip if no seed data matches

        Historial first = list.get(0);
        assertNotNull("codigo no debe ser null", first.getTcCodigoHistorial());
        assertNotNull("material no debe ser null", first.getTcMaterialHistorial());
    }
}

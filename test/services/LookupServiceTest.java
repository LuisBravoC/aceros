package services;

import dao.BaseDaoSupport;
import javafx.collections.ObservableList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for LookupService — verifies delegation to LookupDao is wired correctly.
 */
public class LookupServiceTest extends BaseDaoSupport {

    @Test
    public void getPaises_returnsNonEmpty() {
        ObservableList<String> list = LookupService.getPaises();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void getGeneros_containsExpectedValues() {
        ObservableList<String> list = LookupService.getGeneros();
        assertTrue(list.contains("MASCULINO"));
        assertTrue(list.contains("FEMENINO"));
    }

    @Test
    public void getTipoUsuario_containsEmpleadoGeneral() {
        assertTrue(LookupService.getTipoUsuario().contains("EMPLEADO GENERAL"));
    }

    @Test
    public void getMetodosPago_returnsValues() {
        assertFalse(LookupService.getMetodosPago().isEmpty());
    }

    @Test
    public void getBancos_returnsValues() {
        assertFalse(LookupService.getBancos().isEmpty());
    }

    @Test
    public void getPeriodosPago_returnsValues() {
        assertFalse(LookupService.getPeriodosPago().isEmpty());
    }

    @Test
    public void getContratos_returnsValues() {
        assertFalse(LookupService.getContratos().isEmpty());
    }

    @Test
    public void getMateriales_returnsValues() {
        assertFalse(LookupService.getMateriales().isEmpty());
    }

    @Test
    public void getAlturas_returnsValues() {
        assertFalse(LookupService.getAlturas().isEmpty());
    }

    @Test
    public void getCalibres_returnsValues() {
        assertFalse(LookupService.getCalibres().isEmpty());
    }

    @Test
    public void getRombos_returnsValues() {
        assertFalse(LookupService.getRombos().isEmpty());
    }

    @Test
    public void getEstadosByCountryName_Mexico_returnsNonEmpty() {
        assertFalse(LookupService.getEstadosByCountryName("Mexico").isEmpty());
    }

    @Test
    public void getEstadosByCountryName_nonExistent_returnsEmpty() {
        assertTrue(LookupService.getEstadosByCountryName("ZZZ_NoExiste_999").isEmpty());
    }
}

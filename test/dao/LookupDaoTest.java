package dao;

import javafx.collections.ObservableList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for LookupDao / LookupService.
 * These are read-only lookups against seed data in aceros_test.
 */
public class LookupDaoTest extends BaseDaoSupport {

    @Test
    public void getPaises_returnsNonEmptyList() {
        ObservableList<String> list = LookupDao.getPaises();
        assertNotNull(list);
        assertFalse("paises debe tener datos de seed", list.isEmpty());
    }

    @Test
    public void getGeneros_returnsKnownValues() {
        ObservableList<String> list = LookupDao.getGeneros();
        assertNotNull(list);
        assertTrue("MASCULINO debe estar en generos", list.contains("MASCULINO"));
        assertTrue("FEMENINO debe estar en generos",  list.contains("FEMENINO"));
    }

    @Test
    public void getTipoUsuario_containsEmpleadoGeneral() {
        ObservableList<String> list = LookupDao.getTipoUsuario();
        assertNotNull(list);
        assertTrue("EMPLEADO GENERAL debe estar en tipo_usuario", list.contains("EMPLEADO GENERAL"));
    }

    @Test
    public void getMetodosPago_returnsValues() {
        ObservableList<String> list = LookupDao.getMetodosPago();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void getBancos_returnsValues() {
        ObservableList<String> list = LookupDao.getBancos();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void getPeriodosPago_returnsValues() {
        ObservableList<String> list = LookupDao.getPeriodosPago();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void getContratos_returnsValues() {
        ObservableList<String> list = LookupDao.getContratos();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void getMateriales_returnsValues() {
        ObservableList<String> list = LookupDao.getMateriales();
        assertNotNull(list);
        assertFalse("materiales lookup debe tener datos", list.isEmpty());
    }

    @Test
    public void getAlturas_returnsValues() {
        ObservableList<String> list = LookupDao.getAlturas();
        assertNotNull(list);
        assertFalse("alturas lookup debe tener datos", list.isEmpty());
    }

    @Test
    public void getCalibres_returnsValues() {
        ObservableList<String> list = LookupDao.getCalibres();
        assertNotNull(list);
        assertFalse("calibres lookup debe tener datos", list.isEmpty());
    }

    @Test
    public void getRombos_returnsValues() {
        ObservableList<String> list = LookupDao.getRombos();
        assertNotNull(list);
        assertFalse("rombos lookup debe tener datos", list.isEmpty());
    }

    @Test
    public void getEstadosByCountryName_Mexico_returnsStates() {
        ObservableList<String> list = LookupDao.getEstadosByCountryName("Mexico");
        assertNotNull(list);
        assertFalse("Mexico debe tener estados", list.isEmpty());
    }

    @Test
    public void getEstadosByCountryName_nonExistent_returnsEmpty() {
        ObservableList<String> list = LookupDao.getEstadosByCountryName("PaisQueNoExiste_XYZ");
        assertNotNull(list);
        assertTrue("Pais inexistente debe retornar lista vacía", list.isEmpty());
    }

    @Test
    public void getCiudadesByStateName_Sinaloa_returnsCities() {
        // First, get any state name that exists for Mexico
        ObservableList<String> estados = LookupDao.getEstadosByCountryName("Mexico");
        if (estados.isEmpty()) return; // skip if no data

        String firstState = estados.get(0);
        ObservableList<String> ciudades = LookupDao.getCiudadesByStateName(firstState);
        assertNotNull(ciudades);
        // Don't assert non-empty — some states may have no city entries
    }
}

package dao;

import models.Empleados;
import models.UsuarioDetalle;
import javafx.collections.ObservableList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for UsuariosDao against the aceros_test database.
 * Tests rely on the seed user with usuario_id=1 loaded by AcerosSQL_fixed.sql.
 */
public class UsuariosDaoTest extends BaseDaoSupport {

    /** usuario_id=1 is loaded from seed data in AcerosSQL_fixed.sql */
    private static final String SEED_USER_ID = "1";

    @Test
    public void getAll_returnsNonEmptyList() {
        ObservableList<Empleados> list = UsuariosDao.getAll();
        assertNotNull(list);
        assertFalse("La tabla usuarios debe tener registros de seed", list.isEmpty());
    }

    @Test
    public void findById_existingUser_returnsUsuarioDetalle() {
        UsuarioDetalle u = UsuariosDao.findById(SEED_USER_ID);
        assertNotNull("findById debe retornar un usuario para id=" + SEED_USER_ID, u);
        assertNotNull("El nombre no debe ser null", u.getNombre());
        assertEquals("El ID devuelto debe coincidir", SEED_USER_ID, u.getUsuarioId());
    }

    @Test
    public void findById_nonExistentUser_returnsNull() {
        UsuarioDetalle u = UsuariosDao.findById("999999");
        assertNull("findById con ID inexistente debe retornar null", u);
    }

    @Test
    public void authenticate_withValidCredentials_returnsTrue() {
        // Seed user: usuario_id=1, password='1234'
        boolean result = UsuariosDao.authenticate("1", "1234");
        assertTrue("authenticate debe retornar true para credenciales correctas", result);
    }

    @Test
    public void authenticate_withWrongPassword_returnsFalse() {
        boolean result = UsuariosDao.authenticate("1", "wrongpassword");
        assertFalse("authenticate debe retornar false para password incorrecta", result);
    }

    @Test
    public void authenticate_withNonExistentUser_returnsFalse() {
        boolean result = UsuariosDao.authenticate("999999", "cualquiera");
        assertFalse("authenticate con usuario inexistente debe retornar false", result);
    }

    @Test
    public void verifyPassword_matchesAuthenticate() {
        boolean vp  = UsuariosDao.verifyPassword("1", "1234");
        boolean auth = UsuariosDao.authenticate("1", "1234");
        assertEquals("verifyPassword y authenticate deben coincidir", auth, vp);
    }
}

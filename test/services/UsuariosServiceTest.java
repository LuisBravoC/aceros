package services;

import dao.BaseDaoSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for UsuariosService (password management, first-session flag, tipo de empleado).
 * Uses seed user id=1 (password='1234') from AcerosSQL_fixed.sql.
 */
public class UsuariosServiceTest extends BaseDaoSupport {

    private static final String SEED_ID  = "1";
    private static final String SEED_PWD = "1234";

    @After
    public void restorePassword() {
        // Restore seed password after each test that might change it
        execSql("UPDATE usuarios SET password = '" + SEED_PWD + "', primera_sesion = 0 WHERE usuario_id = " + SEED_ID);
    }

    @Test
    public void getTipoEmpleado_forSeedUser_returnsNonNull() {
        String tipo = UsuariosService.getTipoEmpleado(SEED_ID);
        assertNotNull("getTipoEmpleado no debe retornar null para usuario existente", tipo);
    }

    @Test
    public void getTipoEmpleado_nonExistentUser_returnsNull() {
        String tipo = UsuariosService.getTipoEmpleado("999999");
        assertNull("getTipoEmpleado debe retornar null para usuario inexistente", tipo);
    }

    @Test
    public void verifyPassword_correctPassword_returnsTrue() {
        assertTrue(UsuariosService.verifyPassword(SEED_ID, SEED_PWD));
    }

    @Test
    public void verifyPassword_wrongPassword_returnsFalse() {
        assertFalse(UsuariosService.verifyPassword(SEED_ID, "wrong_password"));
    }

    @Test
    public void changePassword_updatesAndVerifies() {
        String newPwd = "new_test_pwd";
        assertTrue(UsuariosService.changePassword(SEED_ID, newPwd));
        assertTrue("La nueva contraseña debe verificarse correctamente",
            UsuariosService.verifyPassword(SEED_ID, newPwd));
        assertFalse("La contraseña anterior ya no debe ser válida",
            UsuariosService.verifyPassword(SEED_ID, SEED_PWD));
        // restorePassword() will reset it in @After
    }

    @Test
    public void isFirstSession_afterChangePassword_returnsFalse() {
        UsuariosService.changePassword(SEED_ID, "any_pwd");
        assertFalse("Después de cambiar contraseña, isFirstSession debe retornar false",
            UsuariosService.isFirstSession(SEED_ID));
    }
}

package services;

import org.junit.Test;
import static org.junit.Assert.*;
import dao.BaseDaoSupport;

/**
 * Tests for AuthService — covers both pure-logic paths (null/empty inputs)
 * and DB-backed paths (valid/invalid credentials against aceros_test).
 */
public class AuthServiceTest extends BaseDaoSupport {

    @Test
    public void login_nullUserId_returnsEmptyFields() {
        AuthService.LoginResult result = AuthService.login(null, "password");
        assertEquals(AuthService.LoginResult.EMPTY_FIELDS, result);
    }

    @Test
    public void login_emptyUserId_returnsEmptyFields() {
        AuthService.LoginResult result = AuthService.login("  ", "password");
        assertEquals(AuthService.LoginResult.EMPTY_FIELDS, result);
    }

    @Test
    public void login_nullPassword_returnsEmptyFields() {
        AuthService.LoginResult result = AuthService.login("user", null);
        assertEquals(AuthService.LoginResult.EMPTY_FIELDS, result);
    }

    @Test
    public void login_emptyPassword_returnsEmptyFields() {
        AuthService.LoginResult result = AuthService.login("user", "");
        assertEquals(AuthService.LoginResult.EMPTY_FIELDS, result);
    }

    @Test
    public void login_invalidCredentials_returnsInvalidCredentials() {
        AuthService.LoginResult result = AuthService.login("noexiste", "badpass");
        assertEquals(AuthService.LoginResult.INVALID_CREDENTIALS, result);
    }

    @Test
    public void login_validCredentials_returnsSuccess() {
        // Seed user: usuario_id=1, password='1234'
        AuthService.LoginResult result = AuthService.login("1", "1234");
        assertEquals(AuthService.LoginResult.SUCCESS, result);
    }

    @Test
    public void login_success_setsSessionManager() {
        AuthService.login("1", "1234");
        String sessionUser = SessionManager.getInstance().getUserId();
        assertEquals("El SessionManager debe tener el usuario autenticado", "1", sessionUser);
    }
}

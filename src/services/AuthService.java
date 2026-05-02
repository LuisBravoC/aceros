package services;

import dao.UsuariosDao;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio de autenticación.
 * Orquesta la verificación de credenciales y el establecimiento de sesión.
 */
public final class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    public enum LoginResult {
        SUCCESS,
        INVALID_CREDENTIALS,
        EMPTY_FIELDS,
        DB_ERROR
    }

    private AuthService() {}

    /**
     * Intenta autenticar al usuario con las credenciales dadas.
     * Si la autenticación es exitosa establece la sesión en {@link SessionManager}.
     */
    public static LoginResult login(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return LoginResult.EMPTY_FIELDS;
        }
        try {
            boolean ok = UsuariosDao.authenticate(userId, password);
            if (ok) {
                SessionManager.getInstance().setUserId(userId);
                LOGGER.log(Level.INFO, "Login exitoso para usuario: {0}", userId);
                return LoginResult.SUCCESS;
            } else {
                LOGGER.log(Level.INFO, "Credenciales incorrectas para usuario: {0}", userId);
                return LoginResult.INVALID_CREDENTIALS;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error inesperado durante login", ex);
            return LoginResult.DB_ERROR;
        }
    }
}

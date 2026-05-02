package services;

/**
 * Almacena la sesión del usuario autenticado durante la ejecución de la app.
 * Reemplaza el campo estático {@code LoginController.sesion}.
 */
public final class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    private String userId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void clear() {
        this.userId = null;
    }
}

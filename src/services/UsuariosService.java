package services;

import dao.UsuariosDao;
import models.UsuarioDetalle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuariosService {
    private static final Logger LOGGER = Logger.getLogger(UsuariosService.class.getName());

    /**
     * Save or update a usuario.
     * - INSERT: if withImage=true and no file is given, uses the default sin_perfil.png.
     * - UPDATE: if withImage=true and no file is given, image bytes stay null
     *           so the existing DB image is preserved (not overwritten).
     */
    public static boolean saveUsuario(UsuarioDetalle u, String password, boolean withImage, File file) {
        byte[] imageBytes = null;
        boolean isInsert = (u.getUsuarioId() == null || u.getUsuarioId().trim().isEmpty());
        try {
            if (withImage) {
                if (file != null && file.exists() && file.isFile()) {
                    // New file explicitly chosen — use it for both insert and update
                    imageBytes = Files.readAllBytes(file.toPath());
                } else if (isInsert) {
                    // New employee with no photo selected — use default placeholder
                    try (InputStream ris = UsuariosService.class.getResourceAsStream("/icons/sin_perfil.png")) {
                        if (ris != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[8192];
                            int read;
                            while ((read = ris.read(buffer)) != -1) {
                                baos.write(buffer, 0, read);
                            }
                            imageBytes = baos.toByteArray();
                        }
                    }
                }
                // UPDATE with no new file: imageBytes stays null → DAO skips imagen column
            }

            int status = UsuariosDao.save(u, password, imageBytes);
            return status == 1;
        } catch (SQLException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error saving usuario", ex);
            return false;
        }
    }

    public static boolean saveUsuario(UsuarioDetalle u, String password) {
        return saveUsuario(u, password, false, null);
    }

    public static String getTipoEmpleado(String usuario) {
        UsuarioDetalle u = UsuariosDao.findById(usuario);
        return u != null ? u.getTipoEmpleado() : null;
    }

    /** Delega a {@link UsuariosDao#isFirstSession(String)}. */
    public static boolean isFirstSession(String usuario) {
        return UsuariosDao.isFirstSession(usuario);
    }

    /** Delega a {@link UsuariosDao#verifyPassword(String, String)}. */
    public static boolean verifyPassword(String usuario, String password) {
        return UsuariosDao.verifyPassword(usuario, password);
    }

    /** Delega a {@link UsuariosDao#changePassword(String, String)}. */
    public static boolean changePassword(String usuario, String newPassword) {
        return UsuariosDao.changePassword(usuario, newPassword);
    }
}

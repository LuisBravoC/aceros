package services;

import database.ConnectionUtil;
import dao.UsuariosDao;
import models.UsuarioDetalle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuariosService {
    private static final Logger LOGGER = Logger.getLogger(UsuariosService.class.getName());

    /**
     * Save or update a usuario. If usuarioId is empty -> insert; otherwise update.
     * If withImage is true, tries to read bytes from provided file; if file is null, falls back to classpath resource `/icons/sin_perfil.png`.
     */
    public static boolean saveUsuario(UsuarioDetalle u, String password, boolean withImage, File file) {
        byte[] imageBytes = null;
        try {
            if (withImage) {
                if (file != null && file.exists() && file.isFile()) {
                    imageBytes = Files.readAllBytes(file.toPath());
                } else {
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

    public static boolean isFirstSession(String usuario) {
        String sql = "select pimera_sesion from usuarios where usuario_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String val = rs.getString("pimera_sesion");
                    return "0".equals(val);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error checking primer sesion for user: " + usuario, ex);
        }
        return false;
    }

    public static boolean verifyPassword(String usuario, String password) {
        String sql = "select 1 from usuarios where usuario_id = ? and password = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error verifying password for user: " + usuario, ex);
        }
        return false;
    }

    public static boolean changePassword(String usuario, String newPassword) {
        String sql = "update usuarios set password=?, pimera_sesion='1' where usuario_id=?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, usuario);
            int updated = ps.executeUpdate();
            return updated == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error changing password for user: " + usuario, ex);
        }
        return false;
    }

}

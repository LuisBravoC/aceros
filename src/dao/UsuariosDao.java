package dao;

import config.ConnectionUtil;
import models.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import models.UsuarioDetalle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.Period;

public class UsuariosDao {

    private static final Logger LOGGER = Logger.getLogger(UsuariosDao.class.getName());

    // ── Auth ──────────────────────────────────────────────────────────────────

    public static boolean authenticate(String userId, String password) {
        String sql = "SELECT 1 FROM usuarios WHERE usuario_id = ? AND password = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error during authentication for user: " + userId, ex);
        }
        return false;
    }

    /** @return true si el usuario aun no ha cambiado su contrasena por defecto. */
    public static boolean isFirstSession(String userId) {
        String sql = "SELECT primera_sesion FROM usuarios WHERE usuario_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("primera_sesion") == 0;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error checking primera sesion for user: " + userId, ex);
        }
        return false;
    }

    /** @return true si la contrasena coincide con la almacenada en BD. */
    public static boolean verifyPassword(String userId, String password) {
        String sql = "SELECT 1 FROM usuarios WHERE usuario_id = ? AND password = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error verifying password for user: " + userId, ex);
        }
        return false;
    }

    /** Actualiza la contrasena y marca primera_sesion=1. @return true si se actualizo 1 fila. */
    public static boolean changePassword(String userId, String newPassword) {
        String sql = "UPDATE usuarios SET password = ?, primera_sesion = 1 WHERE usuario_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error changing password for user: " + userId, ex);
        }
        return false;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public static ObservableList<Empleados> getAll() {
        ObservableList<Empleados> list = FXCollections.observableArrayList();
        String sql = "SELECT usuario_id, nombre, apellido_paterno, apellido_materno, fecha_nacimiento, sueldo FROM usuarios ORDER BY usuario_id";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String edad = calcularEdad(rs.getString("fecha_nacimiento"));
                String sueldo = rs.getString("sueldo");
                String nombre = coalesce(rs.getString("nombre"), "")
                              + " " + coalesce(rs.getString("apellido_paterno"), "")
                              + " " + coalesce(rs.getString("apellido_materno"), "");
                list.add(new Empleados(
                        rs.getInt("usuario_id"),
                        nombre.trim(),
                        edad,
                        sueldo != null ? sueldo : "0"));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /** Loads full user detail with JOINs to resolve FK IDs to human-readable names. */
    public static UsuarioDetalle findById(String id) {
        String sql =
            "SELECT u.*, " +
            "  g.genero        AS genero_nombre, " +
            "  tp.metodo       AS tipo_pago_nombre, " +
            "  b.nombre        AS banco_nombre, " +
            "  pp.periodo      AS periodo_pago_nombre, " +
            "  tc.contrato     AS tipo_contrato_nombre, " +
            "  tu.puesto       AS tipo_usuario_nombre " +
            "FROM usuarios u " +
            "LEFT JOIN genero            g  ON u.genero_id        = g.id " +
            "LEFT JOIN tipo_pago         tp ON u.tipo_pago_id     = tp.id " +
            "LEFT JOIN bancos            b  ON u.banco_id         = b.id " +
            "LEFT JOIN periodicidad_pago pp ON u.periodo_pago_id  = pp.id " +
            "LEFT JOIN tipo_contratos    tc ON u.tipo_contrato_id = tc.id " +
            "LEFT JOIN tipo_usuario      tu ON u.tipo_usuario_id  = tu.id " +
            "WHERE u.usuario_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Save a user: insert when usuarioId is null/empty, otherwise update.
     * FK string values are resolved to IDs before persisting.
     */
    public static int save(UsuarioDetalle u, String password, byte[] image) throws SQLException {
        if (u == null) return 0;
        boolean isInsert = (u.getUsuarioId() == null || u.getUsuarioId().trim().isEmpty());

        Integer generoId       = LookupDao.getGeneroId(u.getGenero());
        Integer tipoPagoId     = LookupDao.getTipoPagoId(u.getMetodoPago());
        Integer bancoId        = LookupDao.getBancoId(u.getBanco());
        Integer periodoPagoId  = LookupDao.getPeriodoPagoId(u.getPeriodoPago());
        Integer tipoContratoId = LookupDao.getTipoContratoId(u.getTipoContrato());
        Integer tipoUsuarioId  = LookupDao.getTipoUsuarioId(u.getTipoEmpleado());
        Integer paisId         = LookupDao.getPaisId(u.getPais());
        Integer estadoId       = LookupDao.getEstadoId(u.getEstado());
        Integer ciudadId       = LookupDao.getCiudadId(u.getCiudad());

        if (isInsert) {
            String sql =
                "INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, curp, rfc, nss, " +
                "fecha_nacimiento, fecha_contratacion, email, genero_id, password, sueldo, tipo_pago_id, banco_id, " +
                "numero_cuenta, periodo_pago_id, tipo_contrato_id, pais_id, estado_id, localidad, colonia, " +
                "numero_exterior, ciudad_id, calle, codigo_postal, numero_interior, tipo_usuario_id, imagen) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (Connection con = ConnectionUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int i = bindUsuario(ps, u, password, image, generoId, tipoPagoId, bancoId,
                        periodoPagoId, tipoContratoId, paisId, estadoId, ciudadId, tipoUsuarioId, 1);
                int status = ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys != null && keys.next()) u.setUsuarioId(String.valueOf(keys.getInt(1)));
                }
                return status;
            }
        } else {
            // On UPDATE: only include imagen column when new bytes are provided.
            // If image==null the existing image in the DB is preserved.
            String sql = image != null
                ? "UPDATE usuarios SET nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, " +
                  "fecha_nacimiento=?, fecha_contratacion=?, email=?, genero_id=?, sueldo=?, tipo_pago_id=?, banco_id=?, " +
                  "numero_cuenta=?, periodo_pago_id=?, tipo_contrato_id=?, pais_id=?, estado_id=?, localidad=?, colonia=?, " +
                  "numero_exterior=?, ciudad_id=?, calle=?, codigo_postal=?, numero_interior=?, tipo_usuario_id=?, imagen=? " +
                  "WHERE usuario_id=?"
                : "UPDATE usuarios SET nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, " +
                  "fecha_nacimiento=?, fecha_contratacion=?, email=?, genero_id=?, sueldo=?, tipo_pago_id=?, banco_id=?, " +
                  "numero_cuenta=?, periodo_pago_id=?, tipo_contrato_id=?, pais_id=?, estado_id=?, localidad=?, colonia=?, " +
                  "numero_exterior=?, ciudad_id=?, calle=?, codigo_postal=?, numero_interior=?, tipo_usuario_id=? " +
                  "WHERE usuario_id=?";
            try (Connection con = ConnectionUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                int i = bindUsuario(ps, u, null, image, generoId, tipoPagoId, bancoId,
                        periodoPagoId, tipoContratoId, paisId, estadoId, ciudadId, tipoUsuarioId, 1);
                ps.setString(i, u.getUsuarioId());
                return ps.executeUpdate();
            }
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Binds all usuario fields. Returns the next parameter index after the last bound. */
    private static int bindUsuario(PreparedStatement ps, UsuarioDetalle u, String password, byte[] image,
            Integer generoId, Integer tipoPagoId, Integer bancoId, Integer periodoPagoId,
            Integer tipoContratoId, Integer paisId, Integer estadoId, Integer ciudadId,
            Integer tipoUsuarioId, int startIdx) throws SQLException {
        int i = startIdx;
        ps.setString(i++, u.getNombre());
        ps.setString(i++, u.getApellidoPaterno());
        ps.setString(i++, u.getApellidoMaterno());
        ps.setString(i++, u.getCurp());
        ps.setString(i++, u.getRfc());
        ps.setString(i++, u.getNss());
        ps.setString(i++, u.getFechaNacimiento());
        ps.setString(i++, u.getFechaContratacion());
        ps.setString(i++, u.getEmail());
        setNullableInt(ps, i++, generoId);
        if (password != null) ps.setString(i++, password); // only on insert
        ps.setString(i++, u.getSueldo());
        setNullableInt(ps, i++, tipoPagoId);
        setNullableInt(ps, i++, bancoId);
        ps.setString(i++, u.getNumeroCuenta());
        setNullableInt(ps, i++, periodoPagoId);
        setNullableInt(ps, i++, tipoContratoId);
        setNullableInt(ps, i++, paisId);
        setNullableInt(ps, i++, estadoId);
        ps.setString(i++, u.getLocalidad());
        ps.setString(i++, u.getColonia());
        ps.setString(i++, u.getNumeroExterior());
        setNullableInt(ps, i++, ciudadId);
        ps.setString(i++, u.getCalle());
        ps.setString(i++, u.getCodigoPostal());
        ps.setString(i++, u.getNumeroInterior());
        setNullableInt(ps, i++, tipoUsuarioId);
        if (image != null) ps.setBytes(i++, image);
        else               ps.setNull(i++, java.sql.Types.BLOB);
        return i;
    }

    private static UsuarioDetalle mapRow(ResultSet rs) throws SQLException {
        UsuarioDetalle u = new UsuarioDetalle();
        u.setUsuarioId(rs.getString("usuario_id"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidoPaterno(rs.getString("apellido_paterno"));
        u.setApellidoMaterno(rs.getString("apellido_materno"));
        u.setCurp(rs.getString("curp"));
        u.setRfc(rs.getString("rfc"));
        u.setNss(rs.getString("nss"));
        u.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        u.setFechaContratacion(rs.getString("fecha_contratacion"));
        u.setEmail(rs.getString("email"));
        u.setGenero(rs.getString("genero_nombre"));
        u.setTipoEmpleado(rs.getString("tipo_usuario_nombre"));
        u.setSueldo(rs.getString("sueldo"));
        u.setMetodoPago(rs.getString("tipo_pago_nombre"));
        u.setBanco(rs.getString("banco_nombre"));
        u.setNumeroCuenta(rs.getString("numero_cuenta"));
        u.setPeriodoPago(rs.getString("periodo_pago_nombre"));
        u.setTipoContrato(rs.getString("tipo_contrato_nombre"));
        u.setLocalidad(rs.getString("localidad"));
        u.setColonia(rs.getString("colonia"));
        u.setNumeroExterior(rs.getString("numero_exterior"));
        u.setCalle(rs.getString("calle"));
        u.setCodigoPostal(rs.getString("codigo_postal"));
        u.setNumeroInterior(rs.getString("numero_interior"));
        u.setCreateTime(rs.getString("created_at"));
        u.setImagen(rs.getBytes("imagen"));
        Integer paisId   = getIntOrNull(rs, "pais_id");
        Integer estadoId = getIntOrNull(rs, "estado_id");
        Integer ciudadId = getIntOrNull(rs, "ciudad_id");
        u.setPais(LookupDao.getNameById("paises",   "name", paisId));
        u.setEstado(LookupDao.getNameById("estados", "name", estadoId));
        u.setCiudad(LookupDao.getNameById("ciudades","name", ciudadId));
        return u;
    }

    private static Integer getIntOrNull(ResultSet rs, String col) throws SQLException {
        int val = rs.getInt(col);
        return rs.wasNull() ? null : val;
    }

    private static void setNullableInt(PreparedStatement ps, int idx, Integer val) throws SQLException {
        if (val != null) ps.setInt(idx, val);
        else             ps.setNull(idx, java.sql.Types.INTEGER);
    }

    private static String calcularEdad(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) return "0";
        try {
            return String.valueOf(Period.between(LocalDate.parse(fechaNacimiento), LocalDate.now()).getYears());
        } catch (Exception ex) {
            return "0";
        }
    }

    /** Returns {@code val} if non-null, otherwise {@code fallback}. */
    private static String coalesce(String val, String fallback) {
        return val != null ? val : fallback;
    }
}

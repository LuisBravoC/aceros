package dao;

import database.ConnectionUtil;
import controllers.Empleados;
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
import java.util.Arrays;
import java.util.List;

public class UsuariosDao {

    private static final Logger LOGGER = Logger.getLogger(UsuariosDao.class.getName());

    public static ObservableList<Empleados> getAll() {
        ObservableList<Empleados> list = FXCollections.observableArrayList();
        String sql = "select * from usuarios";
        try (Connection con = ConnectionUtil.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Empleados(rs.getInt(1), rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno"), rs.getString("edad"), rs.getString("sueldo")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static UsuarioDetalle findById(String id) {
        String sql = "select * from usuarios where usuario_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    u.setGenero(rs.getString("genero"));
                    u.setTipoEmpleado(rs.getString("tipo_empleado"));
                    u.setSueldo(rs.getString("sueldo"));
                    u.setMetodoPago(rs.getString("metodo_pago"));
                    u.setBanco(rs.getString("banco"));
                    u.setNumeroCuenta(rs.getString("numero_cuenta"));
                    u.setPeriodoPago(rs.getString("periodo_pago"));
                    u.setTipoContrato(rs.getString("tipo_contrato"));
                    u.setPais(rs.getString("pais"));
                    u.setEstado(rs.getString("estado"));
                    u.setLocalidad(rs.getString("localidad"));
                    u.setColonia(rs.getString("colonia"));
                    u.setNumeroExterior(rs.getString("numero_exterior"));
                    u.setCiudad(rs.getString("ciudad"));
                    u.setCalle(rs.getString("calle"));
                    u.setCodigoPostal(rs.getString("codigo_postal"));
                    u.setNumeroInterior(rs.getString("numero_interior"));
                    u.setCreateTime(rs.getString("create_time"));
                    u.setImagen(rs.getBytes("imagen"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Save a user: insert when usuarioId is null/empty, otherwise update.
     * Returns number of affected rows (1 on success).
     */
    public static int save(UsuarioDetalle u, String password, byte[] image) throws SQLException {
        if (u == null) return 0;
        boolean isInsert = (u.getUsuarioId() == null || u.getUsuarioId().trim().isEmpty());

        // compute age from fechaNacimiento if available
        String edadStr = "0";
        if (u.getFechaNacimiento() != null && !u.getFechaNacimiento().isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(u.getFechaNacimiento());
                int edad = Period.between(dob, LocalDate.now()).getYears();
                edadStr = String.valueOf(edad);
            } catch (Exception ex) {
                edadStr = "0";
            }
        }

        if (isInsert) {
            // insert: include imagen column always and set it to NULL when no image is provided
            String insertSql = "insert into usuarios (nombre, apellido_paterno, apellido_materno, curp, rfc, nss, edad, fecha_nacimiento, fecha_contratacion, "
                    + "email, genero, password, sueldo, metodo_pago, banco, numero_cuenta, periodo_pago, tipo_contrato, pais, estado, localidad, colonia, numero_exterior, ciudad, "
                    + "calle, codigo_postal, numero_interior, tipo_empleado, imagen) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            List<String> params = Arrays.asList(
                    u.getNombre(), u.getApellidoPaterno(), u.getApellidoMaterno(), u.getCurp(), u.getRfc(), u.getNss(), edadStr,
                    u.getFechaNacimiento(), u.getFechaContratacion(), u.getEmail(), u.getGenero(), (password != null ? password : ""),
                    u.getSueldo(), u.getMetodoPago(), u.getBanco(), u.getNumeroCuenta(), u.getPeriodoPago(), u.getTipoContrato(),
                    u.getPais(), u.getEstado(), u.getLocalidad(), u.getColonia(), u.getNumeroExterior(), u.getCiudad(), u.getCalle(),
                    u.getCodigoPostal(), u.getNumeroInterior(), u.getTipoEmpleado()
            );

            try (Connection con = ConnectionUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                for (String p : params) {
                    ps.setString(i++, p != null ? p : "");
                }

                if (image != null) {
                    ps.setBytes(i++, image);
                } else {
                    ps.setNull(i++, java.sql.Types.BLOB);
                }

                int status = ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys != null && keys.next()) {
                        u.setUsuarioId(String.valueOf(keys.getInt(1)));
                    }
                } catch (Exception ex) {
                    // ignore
                }
                return status;
            }
        } else {
            // update: same columns (except password) and imagen at the end
            String updateSql = "update usuarios set nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, edad=?, fecha_nacimiento=?, "
                    + "fecha_contratacion=?, email=?, genero=?, sueldo=?, metodo_pago=?, banco=?, numero_cuenta=?, periodo_pago=?, tipo_contrato=?, "
                    + "pais=?, estado=?, localidad=?, colonia=?, numero_exterior=?, ciudad=?, calle=?, codigo_postal=?, numero_interior=?, tipo_empleado=?, imagen=? where usuario_id=?";

            List<String> params = Arrays.asList(
                    u.getNombre(), u.getApellidoPaterno(), u.getApellidoMaterno(), u.getCurp(), u.getRfc(), u.getNss(), edadStr,
                    u.getFechaNacimiento(), u.getFechaContratacion(), u.getEmail(), u.getGenero(), u.getSueldo(), u.getMetodoPago(), u.getBanco(),
                    u.getNumeroCuenta(), u.getPeriodoPago(), u.getTipoContrato(), u.getPais(), u.getEstado(), u.getLocalidad(), u.getColonia(),
                    u.getNumeroExterior(), u.getCiudad(), u.getCalle(), u.getCodigoPostal(), u.getNumeroInterior(), u.getTipoEmpleado()
            );

            try (Connection con = ConnectionUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(updateSql)) {
                int i = 1;
                for (String p : params) {
                    ps.setString(i++, p != null ? p : "");
                }

                if (image != null) {
                    ps.setBytes(i++, image);
                } else {
                    ps.setNull(i++, java.sql.Types.BLOB);
                }

                ps.setString(i++, u.getUsuarioId());
                int status = ps.executeUpdate();
                return status;
            }
        }
    }
}

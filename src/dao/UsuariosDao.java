package dao;

import database.ConnectionUtil;
import controllers.Empleados;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import models.UsuarioDetalle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Statement;

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
            String sqlNoImage = "insert into usuarios (nombre, apellido_paterno, apellido_materno, curp, rfc, nss, edad, fecha_nacimiento, fecha_contratacion, "
                    + "email, genero, password, sueldo, metodo_pago, banco, numero_cuenta, periodo_pago, tipo_contrato, pais, estado, localidad, colonia, numero_exterior, ciudad, "
                    + "calle, codigo_postal, numero_interior, tipo_empleado) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            String sqlWithImage = "insert into usuarios (nombre, apellido_paterno, apellido_materno, curp, rfc, nss, edad, fecha_nacimiento, fecha_contratacion, "
                    + "email, genero, password, sueldo, metodo_pago, banco, numero_cuenta, periodo_pago, tipo_contrato, pais, estado, localidad, colonia, numero_exterior, ciudad, "
                    + "calle, codigo_postal, numero_interior, tipo_empleado, imagen) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            String sql = (image != null) ? sqlWithImage : sqlNoImage;

            try (Connection con = ConnectionUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                ps.setString(i++, u.getNombre());
                ps.setString(i++, u.getApellidoPaterno());
                ps.setString(i++, u.getApellidoMaterno());
                ps.setString(i++, u.getCurp());
                ps.setString(i++, u.getRfc());
                ps.setString(i++, u.getNss());
                ps.setString(i++, edadStr);
                ps.setString(i++, u.getFechaNacimiento());
                ps.setString(i++, u.getFechaContratacion());
                ps.setString(i++, u.getEmail());
                ps.setString(i++, u.getGenero());
                ps.setString(i++, password != null ? password : "");
                ps.setString(i++, u.getSueldo());
                ps.setString(i++, u.getMetodoPago());
                ps.setString(i++, u.getBanco());
                ps.setString(i++, u.getNumeroCuenta());
                ps.setString(i++, u.getPeriodoPago());
                ps.setString(i++, u.getTipoContrato());
                ps.setString(i++, u.getPais());
                ps.setString(i++, u.getEstado());
                ps.setString(i++, u.getLocalidad());
                ps.setString(i++, u.getColonia());
                ps.setString(i++, u.getNumeroExterior());
                ps.setString(i++, u.getCiudad());
                ps.setString(i++, u.getCalle());
                ps.setString(i++, u.getCodigoPostal());
                ps.setString(i++, u.getNumeroInterior());
                ps.setString(i++, u.getTipoEmpleado());

                if (image != null) {
                    ps.setBytes(i++, image);
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
            // update
            String sqlNoImage = "update usuarios set nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, edad=?, fecha_nacimiento=?, "
                    + "fecha_contratacion=?, email=?, genero=?, sueldo=?, metodo_pago=?, banco=?, numero_cuenta=?, periodo_pago=?, tipo_contrato=?, "
                    + "pais=?, estado=?, localidad=?, colonia=?, numero_exterior=?, ciudad=?, calle=?, codigo_postal=?, numero_interior=?, tipo_empleado=? where usuario_id=?";

            String sqlWithImage = "update usuarios set nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, edad=?, fecha_nacimiento=?, "
                    + "fecha_contratacion=?, email=?, genero=?, sueldo=?, metodo_pago=?, banco=?, numero_cuenta=?, periodo_pago=?, tipo_contrato=?, "
                    + "pais=?, estado=?, localidad=?, colonia=?, numero_exterior=?, ciudad=?, calle=?, codigo_postal=?, numero_interior=?, tipo_empleado=?, imagen=? where usuario_id=?";

            String sql = (image != null) ? sqlWithImage : sqlNoImage;

            try (Connection con = ConnectionUtil.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                int i = 1;
                ps.setString(i++, u.getNombre());
                ps.setString(i++, u.getApellidoPaterno());
                ps.setString(i++, u.getApellidoMaterno());
                ps.setString(i++, u.getCurp());
                ps.setString(i++, u.getRfc());
                ps.setString(i++, u.getNss());
                ps.setString(i++, edadStr);
                ps.setString(i++, u.getFechaNacimiento());
                ps.setString(i++, u.getFechaContratacion());
                ps.setString(i++, u.getEmail());
                ps.setString(i++, u.getGenero());
                ps.setString(i++, u.getSueldo());
                ps.setString(i++, u.getMetodoPago());
                ps.setString(i++, u.getBanco());
                ps.setString(i++, u.getNumeroCuenta());
                ps.setString(i++, u.getPeriodoPago());
                ps.setString(i++, u.getTipoContrato());
                ps.setString(i++, u.getPais());
                ps.setString(i++, u.getEstado());
                ps.setString(i++, u.getLocalidad());
                ps.setString(i++, u.getColonia());
                ps.setString(i++, u.getNumeroExterior());
                ps.setString(i++, u.getCiudad());
                ps.setString(i++, u.getCalle());
                ps.setString(i++, u.getCodigoPostal());
                ps.setString(i++, u.getNumeroInterior());
                ps.setString(i++, u.getTipoEmpleado());

                if (image != null) {
                    ps.setBytes(i++, image);
                }

                ps.setString(i++, u.getUsuarioId());
                int status = ps.executeUpdate();
                return status;
            }
        }
    }
}

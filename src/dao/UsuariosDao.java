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
}

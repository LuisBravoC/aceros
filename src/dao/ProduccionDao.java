package dao;

import config.ConnectionUtil;
import models.ProduccionSemanal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProduccionDao {

    private static final Logger LOGGER = Logger.getLogger(ProduccionDao.class.getName());

    public static ObservableList<ProduccionSemanal> getProduccionSemana(String s) {
        ObservableList<ProduccionSemanal> list = FXCollections.observableArrayList();
        String sql =
            "SELECT p.id, p.dia, m.nombre AS material, " +
            "  c.calibre AS calibre, " +
            "  a.altura  AS altura, " +
            "  r.rombo   AS rombos, " +
            "  p.metros   AS metros, " +
            "  p.cantidad AS cantidad " +
            "FROM produccion p " +
            "LEFT JOIN materiales m ON p.material_id = m.id " +
            "LEFT JOIN calibres   c ON p.calibre_id  = c.id " +
            "LEFT JOIN alturas    a ON p.altura_id   = a.id " +
            "LEFT JOIN rombos     r ON p.rombo_id    = r.id " +
            "WHERE p.autor_id = ? ORDER BY p.fecha_registro";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ProduccionSemanal(
                            rs.getString("id"),
                            rs.getString("dia"),
                            rs.getString("material"),
                            CalibresDao.decimalToString(rs.getString("calibre")),
                            AlturasDao.decimalToString(rs.getString("altura")),
                            RombosDao.decimalToString(rs.getString("rombos")),
                            RombosDao.decimalToString(rs.getString("metros")),
                            rs.getString("cantidad")));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static boolean insert(String material, String calibre, String altura, String rombos, String metros, String cantidad, String autorId, String fechaRegistro, String dia) {
        String sql = "INSERT INTO produccion (material_id, calibre_id, altura_id, rombo_id, metros, cantidad, autor_id, fecha_registro, dia) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setNullableInt(ps, 1, LookupDao.getMaterialId(material));
            setNullableInt(ps, 2, LookupDao.getCalibreId(calibre));
            setNullableInt(ps, 3, LookupDao.getAlturaId(altura));
            setNullableInt(ps, 4, LookupDao.getRomboId(rombos));
            ps.setString(5, metros  != null ? metros   : "0");
            ps.setString(6, cantidad != null ? cantidad : "0");
            ps.setString(7, autorId);
            ps.setString(8, fechaRegistro != null ? fechaRegistro : "");
            ps.setString(9, dia != null ? dia : "");
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean update(String id, String material, String calibre, String altura, String rombos, String metros, String cantidad, String fechaRegistro, String dia) {
        String sql = "UPDATE produccion SET material_id=?, calibre_id=?, altura_id=?, rombo_id=?, metros=?, cantidad=?, fecha_registro=?, dia=? WHERE id=?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            setNullableInt(ps, 1, LookupDao.getMaterialId(material));
            setNullableInt(ps, 2, LookupDao.getCalibreId(calibre));
            setNullableInt(ps, 3, LookupDao.getAlturaId(altura));
            setNullableInt(ps, 4, LookupDao.getRomboId(rombos));
            ps.setString(5, metros   != null ? metros   : "0");
            ps.setString(6, cantidad != null ? cantidad : "0");
            ps.setString(7, fechaRegistro != null ? fechaRegistro : "");
            ps.setString(8, dia != null ? dia : "");
            ps.setString(9, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private static void setNullableInt(PreparedStatement ps, int idx, Integer val) throws SQLException {
        if (val != null) ps.setInt(idx, val);
        else             ps.setNull(idx, java.sql.Types.INTEGER);
    }

    public static boolean delete(String id) {
        String sql = "delete from produccion where id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }
}

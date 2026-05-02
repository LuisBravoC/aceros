package dao;

import config.ConnectionUtil;
import models.Historial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HistorialDao {

    private static final Logger LOGGER = Logger.getLogger(HistorialDao.class.getName());

    public static ObservableList<Historial> getHistorial(String s, String de, String a) {
        ObservableList<Historial> list = FXCollections.observableArrayList();
        String sql =
            "SELECT p.id, p.fecha_registro, m.nombre AS material, " +
            "  c.calibre AS calibre, " +
            "  a2.altura  AS altura, " +
            "  r.rombo   AS rombos, " +
            "  p.metros   AS metros, " +
            "  p.cantidad AS cantidad " +
            "FROM produccion p " +
            "LEFT JOIN materiales m  ON p.material_id = m.id " +
            "LEFT JOIN calibres   c  ON p.calibre_id  = c.id " +
            "LEFT JOIN alturas    a2 ON p.altura_id   = a2.id " +
            "LEFT JOIN rombos     r  ON p.rombo_id    = r.id " +
            "WHERE p.autor_id = ? AND (p.fecha_registro BETWEEN ? AND ?) ORDER BY p.fecha_registro";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s  != null ? s  : "");
            ps.setString(2, de != null ? de : "");
            ps.setString(3, a  != null ? a  : "");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Historial(
                            rs.getString("id"),
                            rs.getString("fecha_registro"),
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
}

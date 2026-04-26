package dao;

import database.ConnectionUtil;
import controllers.Historial;
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
        String sql = "select * from produccion where autor_id = ? and (fecha_registro BETWEEN ? AND ?) order by fecha_registro";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s);
            ps.setString(2, de);
            ps.setString(3, a);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Historial(rs.getString("id"), rs.getString("fecha_registro"), rs.getString("material"), rs.getString("calibre"), rs.getString("altura"), rs.getString("rombos"), rs.getString("metros"), rs.getString("cantidad")));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }
}

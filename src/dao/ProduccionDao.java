package dao;

import database.ConnectionUtil;
import controllers.ProduccionSemanal;
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
        String sql = "select * from produccion where autor_id = ? order by fecha_registro";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ProduccionSemanal(rs.getString("id"), rs.getString("dia"), rs.getString("material"), rs.getString("calibre"), rs.getString("altura"), rs.getString("rombos"), rs.getString("metros"), rs.getString("cantidad")));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static boolean insert(String material, String calibre, String altura, String rombos, String metros, String cantidad, String autorId, String fechaRegistro, String dia) {
        String sql = "insert into produccion (material, calibre, altura, rombos, metros, cantidad, autor_id, fecha_registro, dia) values(?,?,?,?,?,?,?,?,?)";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, material);
            ps.setString(2, calibre);
            ps.setString(3, altura);
            ps.setString(4, rombos);
            ps.setString(5, metros);
            ps.setString(6, cantidad);
            ps.setString(7, autorId);
            ps.setString(8, fechaRegistro);
            ps.setString(9, dia);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean update(String id, String material, String calibre, String altura, String rombos, String metros, String cantidad, String fechaRegistro, String dia) {
        String sql = "update produccion set material=?, calibre=?, altura=?, rombos=?, metros=?, cantidad=?, fecha_registro=?, dia=? where id=?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, material);
            ps.setString(2, calibre);
            ps.setString(3, altura);
            ps.setString(4, rombos);
            ps.setString(5, metros);
            ps.setString(6, cantidad);
            ps.setString(7, fechaRegistro);
            ps.setString(8, dia);
            ps.setString(9, id);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
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

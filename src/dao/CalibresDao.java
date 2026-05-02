package dao;

import config.ConnectionUtil;
import models.Calibres;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;

public class CalibresDao {

    private static final Logger LOGGER = Logger.getLogger(CalibresDao.class.getName());

    public static ObservableList<Calibres> getAll() {
        ObservableList<Calibres> list = FXCollections.observableArrayList();
        String sql = "select * from calibres";
        try (Connection con = ConnectionUtil.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Calibres(rs.getInt(1), rs.getString("nombre"), rs.getString("calibre")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static Calibres findById(String id) {
        String sql = "select * from calibres where id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Calibres(rs.getInt("id"), rs.getString("nombre"), rs.getString("calibre"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean insert(String nombre, String calibre) {
        String sql = "insert into calibres (nombre, calibre) values(?,?)";
        List<String> params = Arrays.asList(nombre, calibre);
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            for (String p : params) {
                ps.setString(i++, p != null ? p : "");
            }
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean update(String id, String nombre, String calibre) {
        String sql = "update calibres set nombre = ?, calibre = ? where id = ?";
        List<String> params = Arrays.asList(nombre, calibre);
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            for (String p : params) {
                ps.setString(i++, p != null ? p : "");
            }
            ps.setString(i++, id);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean delete(String id) {
        String sql = "delete from calibres where id = ?";
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

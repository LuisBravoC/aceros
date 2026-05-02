package dao;

import database.ConnectionUtil;
import models.Alturas;
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

public class AlturasDao {

    private static final Logger LOGGER = Logger.getLogger(AlturasDao.class.getName());

    public static ObservableList<Alturas> getAll() {
        ObservableList<Alturas> list = FXCollections.observableArrayList();
        String sql = "select * from alturas";
        try (Connection con = ConnectionUtil.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Alturas(rs.getInt(1), rs.getString("nombre"), rs.getString("altura")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static Alturas findById(String id) {
        String sql = "select * from alturas where id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Alturas(rs.getInt("id"), rs.getString("nombre"), rs.getString("altura"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean insert(String nombre, String altura) {
        String sql = "insert into alturas (nombre, altura) values(?,?)";
        List<String> params = Arrays.asList(nombre, altura);
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

    public static boolean update(String id, String nombre, String altura) {
        String sql = "update alturas set nombre = ?, altura = ? where id = ?";
        List<String> params = Arrays.asList(nombre, altura);
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
        String sql = "delete from alturas where id = ?";
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

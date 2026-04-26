package dao;

import database.ConnectionUtil;
import controllers.Alturas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static controllers.Alturas findById(String id) {
        String sql = "select * from alturas where id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new controllers.Alturas(rs.getInt("id"), rs.getString("nombre"), rs.getString("altura"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

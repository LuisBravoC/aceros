package dao;

import database.ConnectionUtil;
import controllers.Calibres;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static controllers.Calibres findById(String id) {
        String sql = "select * from calibres where id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new controllers.Calibres(rs.getInt("id"), rs.getString("nombre"), rs.getString("calibre"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

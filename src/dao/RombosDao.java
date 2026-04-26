package dao;

import database.ConnectionUtil;
import controllers.Rombos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RombosDao {

    private static final Logger LOGGER = Logger.getLogger(RombosDao.class.getName());

    public static ObservableList<Rombos> getAll() {
        ObservableList<Rombos> list = FXCollections.observableArrayList();
        String sql = "select * from rombos";
        try (Connection con = ConnectionUtil.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Rombos(rs.getInt(1), rs.getString("nombre"), rs.getString("rombo")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static controllers.Rombos findById(String id) {
        String sql = "select * from rombos where id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new controllers.Rombos(rs.getInt("id"), rs.getString("nombre"), rs.getString("rombo"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

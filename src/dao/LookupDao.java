package dao;

import config.ConnectionUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LookupDao {
    private static final Logger LOGGER = Logger.getLogger(LookupDao.class.getName());

    private static ObservableList<String> loadSingleColumn(String sql, String column) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(column));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getPaises() {
        return loadSingleColumn("select name from paises", "name");
    }

    public static ObservableList<String> getGeneros() {
        return loadSingleColumn("select genero from genero", "genero");
    }

    public static ObservableList<String> getTipoUsuario() {
        return loadSingleColumn("select puesto from tipo_usuario", "puesto");
    }

    public static ObservableList<String> getMetodosPago() {
        return loadSingleColumn("select metodo from tipo_pago", "metodo");
    }

    public static ObservableList<String> getBancos() {
        return loadSingleColumn("select nombre from bancos", "nombre");
    }

    public static ObservableList<String> getPeriodosPago() {
        return loadSingleColumn("select periodo from periodicidad_pago", "periodo");
    }

    public static ObservableList<String> getContratos() {
        return loadSingleColumn("select contrato from tipo_contratos", "contrato");
    }

    public static ObservableList<String> getMateriales() {
        return loadSingleColumn("select nombre from materiales", "nombre");
    }

    public static ObservableList<String> getAlturas() {
        return loadSingleColumn("select altura from alturas", "altura");
    }

    public static ObservableList<String> getCalibres() {
        return loadSingleColumn("select calibre from calibres", "calibre");
    }

    public static ObservableList<String> getRombos() {
        return loadSingleColumn("select rombo from rombos", "rombo");
    }

    public static ObservableList<String> getEstadosByCountryName(String countryName) {
        ObservableList<String> list = FXCollections.observableArrayList();
        if (countryName == null) return list;
        String sqlPais = "select id from paises where name = ?";
        String sqlEstados = "select name from estados where country_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlPais)) {
            ps.setString(1, countryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String paisId = rs.getString("id");
                    try (PreparedStatement ps2 = con.prepareStatement(sqlEstados)) {
                        ps2.setString(1, paisId);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while (rs2.next()) {
                                list.add(rs2.getString("name"));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getCiudadesByStateName(String stateName) {
        ObservableList<String> list = FXCollections.observableArrayList();
        if (stateName == null) return list;
        String sqlEstado = "select id from estados where name = ?";
        String sqlCiudades = "select name from ciudades where state_id = ?";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlEstado)) {
            ps.setString(1, stateName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String estadoId = rs.getString("id");
                    try (PreparedStatement ps2 = con.prepareStatement(sqlCiudades)) {
                        ps2.setString(1, estadoId);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while (rs2.next()) {
                                list.add(rs2.getString("name"));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }
}

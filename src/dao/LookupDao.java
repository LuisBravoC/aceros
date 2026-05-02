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

    // ── Generic helpers ──────────────────────────────────────────────────────

    private static ObservableList<String> loadSingleColumn(String sql, String column) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String val = rs.getString(column);
                if (val != null) list.add(val);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Returns the integer ID for the row whose {@code nameColumn} equals {@code name}
     * in the given {@code table}. Returns {@code null} if not found.
     */
    public static Integer getIdByName(String table, String nameColumn, String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String sql = "SELECT id FROM `" + table + "` WHERE `" + nameColumn + "` = ? LIMIT 1";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "getIdByName(" + table + ", " + name + ")", ex);
        }
        return null;
    }

    /**
     * Returns the string value of {@code nameColumn} for the row with the given {@code id}.
     * Returns {@code null} if not found.
     */
    public static String getNameById(String table, String nameColumn, Integer id) {
        if (id == null) return null;
        String sql = "SELECT `" + nameColumn + "` FROM `" + table + "` WHERE id = ? LIMIT 1";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(nameColumn);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "getNameById(" + table + ", " + id + ")", ex);
        }
        return null;
    }

    // ── Lookup lists (for ComboBox population) ───────────────────────────────

    public static ObservableList<String> getPaises() {
        return loadSingleColumn("SELECT name FROM paises ORDER BY name", "name");
    }

    public static ObservableList<String> getGeneros() {
        return loadSingleColumn("SELECT genero FROM genero ORDER BY id", "genero");
    }

    public static ObservableList<String> getTipoUsuario() {
        return loadSingleColumn("SELECT puesto FROM tipo_usuario ORDER BY id", "puesto");
    }

    public static ObservableList<String> getMetodosPago() {
        return loadSingleColumn("SELECT metodo FROM tipo_pago ORDER BY id", "metodo");
    }

    public static ObservableList<String> getBancos() {
        return loadSingleColumn("SELECT nombre FROM bancos ORDER BY nombre", "nombre");
    }

    public static ObservableList<String> getPeriodosPago() {
        return loadSingleColumn("SELECT periodo FROM periodicidad_pago ORDER BY id", "periodo");
    }

    public static ObservableList<String> getContratos() {
        return loadSingleColumn("SELECT contrato FROM tipo_contratos ORDER BY id", "contrato");
    }

    public static ObservableList<String> getMateriales() {
        return loadSingleColumn("SELECT nombre FROM materiales ORDER BY nombre", "nombre");
    }

    public static ObservableList<String> getAlturas() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT altura FROM alturas ORDER BY altura";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String val = AlturasDao.decimalToString(rs.getString("altura"));
                if (val != null) list.add(val);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getCalibres() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT calibre FROM calibres ORDER BY calibre";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String val = CalibresDao.decimalToString(rs.getString("calibre"));
                if (val != null) list.add(val);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getRombos() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT rombo FROM rombos ORDER BY rombo";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String val = RombosDao.decimalToString(rs.getString("rombo"));
                if (val != null) list.add(val);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getEstadosByCountryName(String countryName) {
        ObservableList<String> list = FXCollections.observableArrayList();
        if (countryName == null) return list;
        String sql = "SELECT e.name FROM estados e JOIN paises p ON e.country_id = p.id WHERE p.name = ? ORDER BY e.name";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, countryName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static ObservableList<String> getCiudadesByStateName(String stateName) {
        ObservableList<String> list = FXCollections.observableArrayList();
        if (stateName == null) return list;
        String sql = "SELECT c.name FROM ciudades c JOIN estados e ON c.state_id = e.id WHERE e.name = ? ORDER BY c.name";
        try (Connection con = ConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, stateName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    // ── Convenience ID lookups (used by DAOs when writing) ───────────────────

    public static Integer getGeneroId(String nombre)       { return getIdByName("genero",           "genero",   nombre); }
    public static Integer getTipoPagoId(String nombre)     { return getIdByName("tipo_pago",         "metodo",   nombre); }
    public static Integer getBancoId(String nombre)        { return getIdByName("bancos",            "nombre",   nombre); }
    public static Integer getPeriodoPagoId(String nombre)  { return getIdByName("periodicidad_pago", "periodo",  nombre); }
    public static Integer getTipoContratoId(String nombre) { return getIdByName("tipo_contratos",    "contrato", nombre); }
    public static Integer getTipoUsuarioId(String nombre)  { return getIdByName("tipo_usuario",      "puesto",   nombre); }
    public static Integer getPaisId(String nombre)         { return getIdByName("paises",            "name",     nombre); }
    public static Integer getEstadoId(String nombre)       { return getIdByName("estados",           "name",     nombre); }
    public static Integer getCiudadId(String nombre)       { return getIdByName("ciudades",          "name",     nombre); }
    public static Integer getMaterialId(String nombre)     { return getIdByName("materiales",        "nombre",   nombre); }
    public static Integer getAlturaId(String valor)        { return getIdByName("alturas",           "altura",   valor);  }
    public static Integer getCalibreId(String valor)       { return getIdByName("calibres",          "calibre",  valor);  }
    public static Integer getRomboId(String valor)         { return getIdByName("rombos",            "rombo",    valor);  }

    // ── Convenience name lookups (used by DAOs when reading) ─────────────────

    public static String getGeneroNombre(Integer id)       { return getNameById("genero",           "genero",   id); }
    public static String getTipoPagoNombre(Integer id)     { return getNameById("tipo_pago",        "metodo",   id); }
    public static String getBancoNombre(Integer id)        { return getNameById("bancos",           "nombre",   id); }
    public static String getPeriodoPagoNombre(Integer id)  { return getNameById("periodicidad_pago","periodo",  id); }
    public static String getTipoContratoNombre(Integer id) { return getNameById("tipo_contratos",   "contrato", id); }
    public static String getTipoUsuarioNombre(Integer id)  { return getNameById("tipo_usuario",     "puesto",   id); }
    public static String getMaterialNombre(Integer id)     { return getNameById("materiales",       "nombre",   id); }
    public static String getAlturaNombre(Integer id)       { return getNameById("alturas",          "altura",   id); }
    public static String getCalibreNombre(Integer id)      { return getNameById("calibres",         "calibre",  id); }
    public static String getRomboNombre(Integer id)        { return getNameById("rombos",           "rombo",    id); }
}

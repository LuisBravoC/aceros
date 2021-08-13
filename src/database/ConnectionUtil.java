/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import controllers.Alturas;
import controllers.Calibres;
import controllers.DashboardController;
import controllers.Empleados;
import controllers.Historial;
import controllers.Materiales;
import controllers.ProduccionSemanal;
import controllers.Rombos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author LuisBravo
 */
public class ConnectionUtil {

    Connection conn = null;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/acerosytrefilados?autoReconnect=true&useSSL=false", "root", "1234");
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("ConnectionUtil : " + ex.getMessage());
            return null;
        }
    }

    public static ObservableList<Empleados> getDataUsers() {

        Connection con;
        ObservableList<Empleados> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from usuarios");

            while (rs.next()) {
                list.add(new Empleados(rs.getInt(1), rs.getString("nombre") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno"), rs.getString("edad"), rs.getString("sueldo")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static ObservableList<Materiales> getMateriales() {

        Connection con;
        ObservableList<Materiales> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from materiales");

            while (rs.next()) {
                list.add(new Materiales(rs.getInt(1), rs.getString("nombre")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static ObservableList<Alturas> getAlturas() {

        Connection con;
        ObservableList<Alturas> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from alturas");

            while (rs.next()) {
                list.add(new Alturas(rs.getInt(1), rs.getString("nombre"), rs.getString("altura")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static ObservableList<Calibres> getCalibres() {

        Connection con;
        ObservableList<Calibres> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from calibres");

            while (rs.next()) {
                list.add(new Calibres(rs.getInt(1), rs.getString("nombre"), rs.getString("calibre")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static ObservableList<Rombos> getRombos() {

        Connection con;
        ObservableList<Rombos> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from rombos");

            while (rs.next()) {
                list.add(new Rombos(rs.getInt(1), rs.getString("nombre"), rs.getString("rombo")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static ObservableList<ProduccionSemanal> getProduccionSemana(String s) {
        PreparedStatement ps = null;
        Connection con;
        ObservableList<ProduccionSemanal> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ps = con.prepareStatement("select * from produccion where autor_id = ? order by fecha_registro");
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ProduccionSemanal(rs.getString("id"), rs.getString("dia"), rs.getString("material"), rs.getString("calibre"), rs.getString("altura"), rs.getString("rombos"), rs.getString("metros"), rs.getString("cantidad")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static ObservableList<Historial> getHistorial(String s, String de, String a) {
        PreparedStatement ps = null;
        Connection con;
        ObservableList<Historial> list = FXCollections.observableArrayList();
        try {

            con = ConnectionUtil.getConnection();
            ps = con.prepareStatement("select * from produccion where autor_id = ? and (fecha_registro BETWEEN ? AND ?) order by fecha_registro");
            System.out.println("ENTRO");
            System.out.println("Empleado: " + s);
            System.out.println("De: " + de);
            System.out.println("A: " + a);
            ps.setString(1, s);
            ps.setString(2, de);
            ps.setString(3, a);
            System.out.println("SALIENDO");
            ResultSet rs = ps.executeQuery();
            System.out.println("SALGO");
            while (rs.next()) {
                list.add(new Historial(rs.getString("id"), rs.getString("fecha_registro"), rs.getString("material"), rs.getString("calibre"), rs.getString("altura"), rs.getString("rombos"), rs.getString("metros"), rs.getString("cantidad")));
                System.out.println(list);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

}

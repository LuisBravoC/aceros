/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import animatefx.animation.*;
import database.ConnectionUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author LuisBravo
 */
public class LoginController implements Initializable {
        
    private Label label;
    private Circle btnClose;
    @FXML
    private Button btnIngresar;
    @FXML
    public AnchorPane anchRoot;
    @FXML
    private TextField tf_userid;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Label lbConLogin;
    
    private static String sesion;
    
    private static Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    public LoginController() throws SQLException {
        con = ConnectionUtil.getConnection();
    }
    
    double x = 0, y = 0;

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void dragged(MouseEvent event) {

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void login(MouseEvent event) {

    }
    
    @FXML
    void handleButtonAction(ActionEvent event) {
        
        if (event.getSource() == btnIngresar) {
            //Inicio de sesion aqui
            if (logIn().equals("Success")) {
                try {

                    //Pantallas de carga o transiciones
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Parent root = FXMLLoader.load(getClass().getResource("/aceros/dashboard.fxml"));
                    Scene scene = new Scene(root);

                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    new animatefx.animation.ZoomIn(root).play();
                    stage.show();
                    
                    System.out.println("Inicio sesión");

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }else {
                                     
            tf_userid.clear();
            tf_password.clear();

            }
        } 

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (con == null) {
            lbConLogin.setTextFill(Color.TOMATO);
            lbConLogin.setText("ERROR DE CONEXIÓN");
            System.out.println("Error de conexion ");
        } else {
            lbConLogin.setTextFill(Color.GREEN);
            lbConLogin.setText("HAY CONEXIÓN CON EL SERVICIO");
            System.out.println("Conexion establecida");
        }
        
        tf_password.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){ 
                if (logIn().equals("Success")) {
                try {

                    //Pantallas de carga o transiciones
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Parent root = FXMLLoader.load(getClass().getResource("/aceros/dashboard.fxml"));
                    Scene scene = new Scene(root);

                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    new animatefx.animation.ZoomIn(root).play();
                    stage.show();
                    
                    System.out.println("Inicio sesión");

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }else {
                                     
            tf_userid.clear();
            tf_password.clear();

            }
            }
       });
        
    }    
    
        private String logIn() {
        String status = "Success";
        String userid = tf_userid.getText();
        String password = tf_password.getText();
        if(userid.isEmpty() || password.isEmpty()) {
            
            Alert alert = new Alert(AlertType.ERROR);
            //Image image = new Image("/icons/LogoSmall.png");
            alert.setTitle("Uno o mas campos vacios!");
            alert.setHeaderText("Usuario o contraseña estan vacios");
            alert.setContentText("Asegurese ingresar su usuario y contraseña");
            alert.showAndWait();
            System.out.println("Usuario o contraseña estan vacios");
            status = "Error";
            
        } else {
            //query
            String sql = "SELECT * FROM usuarios Where usuario_id = ? and password = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, userid);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                sesion = userid;
                if (!resultSet.next()) {
                    //setLblError(Color.TOMATO, "Enter Correct Email/Password");
                    status = "Error";
                    
                    Alert alert = new Alert(AlertType.ERROR);
                    //Image image = new Image("/icons/LogoSmall.png");
                    alert.setTitle("Datos incorrectos!");
                    alert.setHeaderText("Uno o mas datos son incorrectos");
                    alert.setContentText("El usuario o la contraseña son incorrectos, vuelva a intentarlo de nuevo");
                    alert.showAndWait();
                    System.out.println("Datos incorrectos");
                    
                    System.out.println(status);
                } else {
                    //setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                tf_userid.clear();
                tf_password.clear();
                status = "Exception";
            }
        }
        System.out.println("Usuario: "+userid);
        System.out.println("Contraseña: "+password);
        return status;
    }
        
    public static String getSesion() {
        return sesion;
    }

    public static void setSesion(String sesion) {
        LoginController.sesion = sesion;
    }

    private void handleMouseEvent(MouseEvent event) {
        
    }
    
}

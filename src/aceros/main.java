/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aceros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import animatefx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

/**
 *
 * @author LuisBravo
 */
public class main extends Application {
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        
        scene.setFill(Color.TRANSPARENT);
        
        stage.getIcons().add(new Image("icons/IconBlanco.png"));
        stage.setTitle("Inicio de sesión");
        
        stage.setScene(scene);
        
        stage.initStyle(StageStyle.TRANSPARENT); // Comentar en caso de que se quiera mostrar el Title (Boton de cerrar, minimizar y maximizar
        
        new animatefx.animation.FadeIn(root).play();
        
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Programa iniciado");
        launch(args);
    }
    
}

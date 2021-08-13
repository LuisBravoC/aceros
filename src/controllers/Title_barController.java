/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LuisBravo
 */
public class Title_barController implements Initializable {
    

    @FXML
    void close(MouseEvent event) {
        
        Node node = (Node) event.getSource();
        
        Stage stage = (Stage) node.getScene().getWindow();
        
        stage.close();
        
    }
    
    @FXML
    void max(MouseEvent event) {
        
        Node node = (Node) event.getSource();
        
        Stage stage = (Stage) node.getScene().getWindow();
        
        if(stage.isMaximized()){
            stage.setMaximized(false);
        }else{            
        stage.setMaximized(true);
        }
        
        // FULL SCREEN
        /*
        if(stage.isFullScreen()){
            stage.setFullScreen(false);
        }else{  
                    
        stage.setFullScreen(true);
            
        }
        */
        
        //stage.setFullScreenExitHint(" ");
    }
    
    @FXML
    void min(MouseEvent event) {
        
        Node node = (Node) event.getSource();
        
        Stage stage = (Stage) node.getScene().getWindow();
        
        stage.setIconified(true);

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

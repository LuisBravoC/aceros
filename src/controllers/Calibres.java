/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author LuisBravo
 */
public class Calibres {
    
    private  final SimpleIntegerProperty tcCodigoCalibre;
    private  final SimpleStringProperty tcNombreCalibre;
    private  final SimpleStringProperty tcCalibre;
    
    public Calibres(Integer codigo, String nombre, String calibre)
    {      
       this.tcCodigoCalibre = new SimpleIntegerProperty(codigo);
       this.tcNombreCalibre = new SimpleStringProperty(nombre);
       this.tcCalibre = new SimpleStringProperty(calibre);
    
    }
    
    public int getTcCodigoCalibre() {
        return tcCodigoCalibre.get();
    }

    public void setTcCodigoCalibre(int codigo) {
        this.tcCodigoCalibre.set(codigo);
    }
    
    public String getTcNombreCalibre() {
        return tcNombreCalibre.get();
    }

    public void setTcNombreCalibre(String nombre) {
        tcNombreCalibre.set(nombre);
    }
    
    public String getTcCalibre() {
        return tcCalibre.get();
    }

    public void setTcCalibres(String calibre) {
        tcCalibre.set(calibre);
    }
    
}

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
public class Rombos {
    
    private  final SimpleIntegerProperty tcCodigoRombo;
    private  final SimpleStringProperty tcNombreRombo;
    private  final SimpleStringProperty tcRombo;
    
    public Rombos(Integer codigo, String nombre, String rombo)
    {      
       this.tcCodigoRombo = new SimpleIntegerProperty(codigo);
       this.tcNombreRombo = new SimpleStringProperty(nombre);
       this.tcRombo = new SimpleStringProperty(rombo);
    
    }
    
    public int getTcCodigoRombo() {
        return tcCodigoRombo.get();
    }

    public void setTcCodigoRombo(int codigo) {
        this.tcCodigoRombo.set(codigo);
    }
    
    public String getTcNombreRombo() {
        return tcNombreRombo.get();
    }

    public void setTcNombreRombo(String nombre) {
        tcNombreRombo.set(nombre);
    }
    
    public String getTcRombo() {
        return tcRombo.get();
    }

    public void setTcRombo(String rombo) {
        tcRombo.set(rombo);
    }
    
}

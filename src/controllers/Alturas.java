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
public class Alturas {
    
    private  final SimpleIntegerProperty tcCodigoAltura;
    private  final SimpleStringProperty tcNombreAltura;
    private  final SimpleStringProperty tcAltura;
    
    public Alturas(Integer codigo, String nombre, String altura)
    {      
       this.tcCodigoAltura = new SimpleIntegerProperty(codigo);
       this.tcNombreAltura = new SimpleStringProperty(nombre);
       this.tcAltura = new SimpleStringProperty(altura);
    
    }
    
    public int getTcCodigoAltura() {
        return tcCodigoAltura.get();
    }

    public void setTcCodigoAltura(int codigo) {
        this.tcCodigoAltura.set(codigo);
    }
    
    public String getTcNombreAltura() {
        return tcNombreAltura.get();
    }

    public void setTcNombreAltura(String nombre) {
        tcNombreAltura.set(nombre);
    }
    
    public String getTcAltura() {
        return tcAltura.get();
    }

    public void setTcAltura(String altura) {
        tcAltura.set(altura);
    }
    
}

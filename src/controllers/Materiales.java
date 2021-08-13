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
public class Materiales {
    private  final SimpleIntegerProperty tcCodigoMaterial;
    private  final SimpleStringProperty tcNombreMaterial;
    
    public Materiales(Integer codigo, String material)
    {      
       this.tcCodigoMaterial = new SimpleIntegerProperty(codigo);
       this.tcNombreMaterial = new SimpleStringProperty(material);
    
    }
    
    public int getTcCodigoMaterial() {
        return tcCodigoMaterial.get();
    }

    public void setTcCodigoMaterial(int codigo) {
        this.tcCodigoMaterial.set(codigo);
    }
    
    public String getTcNombreMaterial() {
        return tcNombreMaterial.get();
    }

    public void setTcNombreMaterial(String material) {
        tcNombreMaterial.set(material);
    }
    
}

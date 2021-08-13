/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author LuisBravo
 */
public class Historial {
    
    private  final SimpleStringProperty tcCodigoHistorial;
    private  final SimpleStringProperty tcDiaHistorial;
    private  final SimpleStringProperty tcMaterialHistorial;
    private  final SimpleStringProperty tcCalibreHistorial;
    private  final SimpleStringProperty tcAlturaHistorial;
    private  final SimpleStringProperty tcRomboHistorial;
    private  final SimpleStringProperty tcMetrosHistorial;
    private  final SimpleStringProperty tcCantidadHistorial;
    
    public Historial(String codigo, String dia, String material, String calibre, String altura, String rombo, String metros, String cantidad)
    {    
       this.tcCodigoHistorial = new SimpleStringProperty(codigo);
       this.tcDiaHistorial = new SimpleStringProperty(dia);
       this.tcMaterialHistorial = new SimpleStringProperty(material);
       this.tcCalibreHistorial = new SimpleStringProperty(calibre);
       this.tcAlturaHistorial = new SimpleStringProperty(altura);
       this.tcRomboHistorial = new SimpleStringProperty(rombo);
       this.tcMetrosHistorial = new SimpleStringProperty(metros);
       this.tcCantidadHistorial = new SimpleStringProperty(cantidad);
    
    }
    
    public String getTcCodigoHistorial() {
        return tcCodigoHistorial.get();
    }

    public void setTcCodigoHistorial(String codigo) {
        tcCodigoHistorial.set(codigo);
    }
    
    public String getTcDiaHistorial() {
        return tcDiaHistorial.get();
    }

    public void setTcDiaHistorial(String dia) {
        tcDiaHistorial.set(dia);
    }
    
    public String getTcMaterialHistorial() {
        return tcMaterialHistorial.get();
    }

    public void setTcMaterialHistorial(String material) {
        tcMaterialHistorial.set(material);
    }
    
    public String getTcCalibreHistorial() {
        return tcCalibreHistorial.get();
    }
    
    public void setTcCalibreHistorial(String calibre) {
        tcCalibreHistorial.set(calibre);
    }

    public String getTcAlturaHistorial() {
        return tcAlturaHistorial.get();
    }
    
    public void setTcAlturaHistorial(String altura) {
        tcAlturaHistorial.set(altura);
    }
    
    public String getTcRomboHistorial() {
        return tcRomboHistorial.get();
    }
    
    public void setTcRomboHistorial(String rombo) {
        tcRomboHistorial.set(rombo);
    }

    public String getTcMetrosHistorial() {
        return tcMetrosHistorial.get();
    }

    public void setTcMetrosHistorial(String metros) {
        tcMetrosHistorial.set(metros);
    }
    
    public String getTcCantidadHistorial() {
        return tcCantidadHistorial.get();
    }

    public void setTcCantidadHistorial(String cantidad) {
        tcCantidadHistorial.set(cantidad);
    }
    
}

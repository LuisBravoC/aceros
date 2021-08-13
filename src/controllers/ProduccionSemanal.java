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
public class ProduccionSemanal {
    
    private  final SimpleStringProperty tcCodigoS;
    private  final SimpleStringProperty tcDiaS;
    private  final SimpleStringProperty tcMaterialeS;
    private  final SimpleStringProperty tcCalibreS;
    private  final SimpleStringProperty tcAlturaS;
    private  final SimpleStringProperty tcRomboS;
    private  final SimpleStringProperty tcMetrosS;
    private  final SimpleStringProperty tcCantidadS;
    
    public ProduccionSemanal(String codigo, String dia, String material, String calibre, String altura, String rombo, String metros, String cantidad)
    {
       this.tcCodigoS = new SimpleStringProperty(codigo);
       this.tcDiaS = new SimpleStringProperty(dia);
       this.tcMaterialeS = new SimpleStringProperty(material);
       this.tcCalibreS = new SimpleStringProperty(calibre);
       this.tcAlturaS = new SimpleStringProperty(altura);
       this.tcRomboS = new SimpleStringProperty(rombo);
       this.tcMetrosS = new SimpleStringProperty(metros);
       this.tcCantidadS = new SimpleStringProperty(cantidad);
    
    }
    
    public String getTcCodigoS() {
        return tcCodigoS.get();
    }

    public void setTcCodigoS(String codigo) {
        tcCodigoS.set(codigo);
    }
    
    public String getTcDiaS() {
        return tcDiaS.get();
    }

    public void setTcDiaS(String dia) {
        tcDiaS.set(dia);
    }
    
    public String getTcMaterialeS() {
        return tcMaterialeS.get();
    }

    public void setTcMaterialeS(String material) {
        tcMaterialeS.set(material);
    }
    
    public String getTcCalibreS() {
        return tcCalibreS.get();
    }
    
    public void setTcCalibreS(String calibre) {
        tcCalibreS.set(calibre);
    }

    public String getTcAlturaS() {
        return tcAlturaS.get();
    }
    
    public void setTcAlturaS(String altura) {
        tcAlturaS.set(altura);
    }

    public String getTcRomboS() {
        return tcRomboS.get();
    }

    public void setTcRomboS(String rombo) {
        tcRomboS.set(rombo);
    }
    
    public String getTcMetrosS() {
        return tcMetrosS.get();
    }

    public void setTcMetrosS(String metros) {
        tcMetrosS.set(metros);
    }
    
    public String getTcCantidadS() {
        return tcCantidadS.get();
    }

    public void setTcCantidadS(String cantidad) {
        tcCantidadS.set(cantidad);
    }
    
}

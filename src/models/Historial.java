package models;

import javafx.beans.property.SimpleStringProperty;

public class Historial {
    private final SimpleStringProperty tcCodigoHistorial;
    private final SimpleStringProperty tcDiaHistorial;
    private final SimpleStringProperty tcMaterialHistorial;
    private final SimpleStringProperty tcCalibreHistorial;
    private final SimpleStringProperty tcAlturaHistorial;
    private final SimpleStringProperty tcRomboHistorial;
    private final SimpleStringProperty tcMetrosHistorial;
    private final SimpleStringProperty tcCantidadHistorial;

    public Historial(String codigo, String dia, String material, String calibre,
                     String altura, String rombo, String metros, String cantidad) {
        this.tcCodigoHistorial   = new SimpleStringProperty(codigo);
        this.tcDiaHistorial      = new SimpleStringProperty(dia);
        this.tcMaterialHistorial = new SimpleStringProperty(material);
        this.tcCalibreHistorial  = new SimpleStringProperty(calibre);
        this.tcAlturaHistorial   = new SimpleStringProperty(altura);
        this.tcRomboHistorial    = new SimpleStringProperty(rombo);
        this.tcMetrosHistorial   = new SimpleStringProperty(metros);
        this.tcCantidadHistorial = new SimpleStringProperty(cantidad);
    }

    public String getTcCodigoHistorial()   { return tcCodigoHistorial.get(); }
    public void setTcCodigoHistorial(String v)   { tcCodigoHistorial.set(v); }

    public String getTcDiaHistorial()      { return tcDiaHistorial.get(); }
    public void setTcDiaHistorial(String v)      { tcDiaHistorial.set(v); }

    public String getTcMaterialHistorial() { return tcMaterialHistorial.get(); }
    public void setTcMaterialHistorial(String v) { tcMaterialHistorial.set(v); }

    public String getTcCalibreHistorial()  { return tcCalibreHistorial.get(); }
    public void setTcCalibreHistorial(String v)  { tcCalibreHistorial.set(v); }

    public String getTcAlturaHistorial()   { return tcAlturaHistorial.get(); }
    public void setTcAlturaHistorial(String v)   { tcAlturaHistorial.set(v); }

    public String getTcRomboHistorial()    { return tcRomboHistorial.get(); }
    public void setTcRomboHistorial(String v)    { tcRomboHistorial.set(v); }

    public String getTcMetrosHistorial()   { return tcMetrosHistorial.get(); }
    public void setTcMetrosHistorial(String v)   { tcMetrosHistorial.set(v); }

    public String getTcCantidadHistorial() { return tcCantidadHistorial.get(); }
    public void setTcCantidadHistorial(String v) { tcCantidadHistorial.set(v); }
}

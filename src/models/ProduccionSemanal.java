package models;

import javafx.beans.property.SimpleStringProperty;

public class ProduccionSemanal {
    private final SimpleStringProperty tcCodigoS;
    private final SimpleStringProperty tcDiaS;
    private final SimpleStringProperty tcMaterialeS;
    private final SimpleStringProperty tcCalibreS;
    private final SimpleStringProperty tcAlturaS;
    private final SimpleStringProperty tcRomboS;
    private final SimpleStringProperty tcMetrosS;
    private final SimpleStringProperty tcCantidadS;

    public ProduccionSemanal(String codigo, String dia, String material, String calibre,
                             String altura, String rombo, String metros, String cantidad) {
        this.tcCodigoS   = new SimpleStringProperty(codigo);
        this.tcDiaS      = new SimpleStringProperty(dia);
        this.tcMaterialeS = new SimpleStringProperty(material);
        this.tcCalibreS  = new SimpleStringProperty(calibre);
        this.tcAlturaS   = new SimpleStringProperty(altura);
        this.tcRomboS    = new SimpleStringProperty(rombo);
        this.tcMetrosS   = new SimpleStringProperty(metros);
        this.tcCantidadS = new SimpleStringProperty(cantidad);
    }

    public String getTcCodigoS()    { return tcCodigoS.get(); }
    public void setTcCodigoS(String v)    { tcCodigoS.set(v); }

    public String getTcDiaS()       { return tcDiaS.get(); }
    public void setTcDiaS(String v)       { tcDiaS.set(v); }

    public String getTcMaterialeS() { return tcMaterialeS.get(); }
    public void setTcMaterialeS(String v) { tcMaterialeS.set(v); }

    public String getTcCalibreS()   { return tcCalibreS.get(); }
    public void setTcCalibreS(String v)   { tcCalibreS.set(v); }

    public String getTcAlturaS()    { return tcAlturaS.get(); }
    public void setTcAlturaS(String v)    { tcAlturaS.set(v); }

    public String getTcRomboS()     { return tcRomboS.get(); }
    public void setTcRomboS(String v)     { tcRomboS.set(v); }

    public String getTcMetrosS()    { return tcMetrosS.get(); }
    public void setTcMetrosS(String v)    { tcMetrosS.set(v); }

    public String getTcCantidadS()  { return tcCantidadS.get(); }
    public void setTcCantidadS(String v)  { tcCantidadS.set(v); }
}

package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Rombos {
    private final SimpleIntegerProperty tcCodigoRombo;
    private final SimpleStringProperty tcNombreRombo;
    private final SimpleStringProperty tcRombo;

    public Rombos(Integer codigo, String nombre, String rombo) {
        this.tcCodigoRombo = new SimpleIntegerProperty(codigo);
        this.tcNombreRombo = new SimpleStringProperty(nombre);
        this.tcRombo = new SimpleStringProperty(rombo);
    }

    public int getTcCodigoRombo() { return tcCodigoRombo.get(); }
    public void setTcCodigoRombo(int val) { tcCodigoRombo.set(val); }

    public String getTcNombreRombo() { return tcNombreRombo.get(); }
    public void setTcNombreRombo(String val) { tcNombreRombo.set(val); }

    public String getTcRombo() { return tcRombo.get(); }
    public void setTcRombo(String val) { tcRombo.set(val); }
}

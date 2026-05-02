package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Calibres {
    private final SimpleIntegerProperty tcCodigoCalibre;
    private final SimpleStringProperty tcNombreCalibre;
    private final SimpleStringProperty tcCalibre;

    public Calibres(Integer codigo, String nombre, String calibre) {
        this.tcCodigoCalibre = new SimpleIntegerProperty(codigo);
        this.tcNombreCalibre = new SimpleStringProperty(nombre);
        this.tcCalibre = new SimpleStringProperty(calibre);
    }

    public int getTcCodigoCalibre() { return tcCodigoCalibre.get(); }
    public void setTcCodigoCalibre(int val) { tcCodigoCalibre.set(val); }

    public String getTcNombreCalibre() { return tcNombreCalibre.get(); }
    public void setTcNombreCalibre(String val) { tcNombreCalibre.set(val); }

    public String getTcCalibre() { return tcCalibre.get(); }
    public void setTcCalibres(String val) { tcCalibre.set(val); }
}

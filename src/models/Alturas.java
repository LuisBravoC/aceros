package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Alturas {
    private final SimpleIntegerProperty tcCodigoAltura;
    private final SimpleStringProperty tcNombreAltura;
    private final SimpleStringProperty tcAltura;

    public Alturas(Integer codigo, String nombre, String altura) {
        this.tcCodigoAltura = new SimpleIntegerProperty(codigo);
        this.tcNombreAltura = new SimpleStringProperty(nombre);
        this.tcAltura = new SimpleStringProperty(altura);
    }

    public int getTcCodigoAltura() { return tcCodigoAltura.get(); }
    public void setTcCodigoAltura(int val) { tcCodigoAltura.set(val); }

    public String getTcNombreAltura() { return tcNombreAltura.get(); }
    public void setTcNombreAltura(String val) { tcNombreAltura.set(val); }

    public String getTcAltura() { return tcAltura.get(); }
    public void setTcAltura(String val) { tcAltura.set(val); }
}

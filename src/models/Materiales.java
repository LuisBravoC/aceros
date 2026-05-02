package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Materiales {
    private final SimpleIntegerProperty tcCodigoMaterial;
    private final SimpleStringProperty tcNombreMaterial;

    public Materiales(Integer codigo, String material) {
        this.tcCodigoMaterial = new SimpleIntegerProperty(codigo);
        this.tcNombreMaterial = new SimpleStringProperty(material);
    }

    public int getTcCodigoMaterial() { return tcCodigoMaterial.get(); }
    public void setTcCodigoMaterial(int val) { tcCodigoMaterial.set(val); }

    public String getTcNombreMaterial() { return tcNombreMaterial.get(); }
    public void setTcNombreMaterial(String val) { tcNombreMaterial.set(val); }
}

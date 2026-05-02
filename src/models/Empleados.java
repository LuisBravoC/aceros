package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Empleados {
    private final SimpleIntegerProperty EmpIdUsuario;
    private final SimpleStringProperty empNombre;
    private final SimpleStringProperty empEdad;
    private final SimpleStringProperty empSueldo;

    public Empleados(Integer EmpIdUsuario, String nombre, String edad, String salario) {
        this.EmpIdUsuario = new SimpleIntegerProperty(EmpIdUsuario);
        this.empNombre = new SimpleStringProperty(nombre);
        this.empEdad = new SimpleStringProperty(edad);
        this.empSueldo = new SimpleStringProperty(salario);
    }

    public int getEmpIdUsuario() { return EmpIdUsuario.get(); }
    public void setEmpIdUsuario(int val) { EmpIdUsuario.set(val); }

    public String getEmpNombre() { return empNombre.get(); }
    public void setEmpNombre(String val) { empNombre.set(val); }

    public String getEmpEdad() { return empEdad.get(); }
    public void setEmpEdad(String val) { empEdad.set(val); }

    public String getEmpSueldo() { return empSueldo.get(); }
    public void setEmpSueldo(String val) { empSueldo.set(val); }
}

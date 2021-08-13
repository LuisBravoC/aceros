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
public class Empleados {
    private  final SimpleIntegerProperty EmpIdUsuario;
    private  final SimpleStringProperty empNombre;
    private  final SimpleStringProperty empEdad;
    private  final SimpleStringProperty empSueldo;
    
    public Empleados(Integer EmpIdUsuario, String nombre, String edad, String salario)
    {      
       this.EmpIdUsuario = new SimpleIntegerProperty(EmpIdUsuario);
       this.empNombre = new SimpleStringProperty(nombre);
       this.empEdad =  new SimpleStringProperty(edad);
       this.empSueldo =  new SimpleStringProperty(salario);
    
    }
    
     
    public int getEmpIdUsuario() {
        return EmpIdUsuario.get();
    }

    public void setEmpIdUsuario(int EmpIdUsuario) {
        this.EmpIdUsuario.set(EmpIdUsuario);
    }
    
   
  
    public String getEmpNombre() {
        return empNombre.get();
    }

    public void setEmpNombre(String nombre) {
        empNombre.set(nombre);
    }
    
    

    public String getEmpEdad() {
        return empEdad.get();
    }

    public void setEmpEdad(String edad) {
        empEdad.set(edad);
    }

    

    public String getEmpSueldo() {
        return empSueldo.get();
    }

    public void setEmpSueldo(String salario) {
        this.empSueldo.set(salario);
    }
    
}

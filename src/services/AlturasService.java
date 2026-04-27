package services;

import controllers.Alturas;
import dao.AlturasDao;
import javafx.collections.ObservableList;

public class AlturasService {

    public static ObservableList<Alturas> getAll() {
        return AlturasDao.getAll();
    }

    public static Alturas findById(String id) {
        return AlturasDao.findById(id);
    }

    public static boolean insert(String nombre, String altura) {
        return AlturasDao.insert(nombre, altura);
    }

    public static boolean update(String id, String nombre, String altura) {
        return AlturasDao.update(id, nombre, altura);
    }

    public static boolean delete(String id) {
        return AlturasDao.delete(id);
    }
}

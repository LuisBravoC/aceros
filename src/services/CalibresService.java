package services;

import controllers.Calibres;
import dao.CalibresDao;
import javafx.collections.ObservableList;

public class CalibresService {

    public static ObservableList<Calibres> getAll() {
        return CalibresDao.getAll();
    }

    public static Calibres findById(String id) {
        return CalibresDao.findById(id);
    }

    public static boolean insert(String nombre, String calibre) {
        return CalibresDao.insert(nombre, calibre);
    }

    public static boolean update(String id, String nombre, String calibre) {
        return CalibresDao.update(id, nombre, calibre);
    }

    public static boolean delete(String id) {
        return CalibresDao.delete(id);
    }
}

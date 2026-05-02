package services;

import models.Rombos;
import dao.RombosDao;
import javafx.collections.ObservableList;

public class RombosService {

    public static ObservableList<Rombos> getAll() {
        return RombosDao.getAll();
    }

    public static Rombos findById(String id) {
        return RombosDao.findById(id);
    }

    public static boolean insert(String nombre, String rombo) {
        return RombosDao.insert(nombre, rombo);
    }

    public static boolean update(String id, String nombre, String rombo) {
        return RombosDao.update(id, nombre, rombo);
    }

    public static boolean delete(String id) {
        return RombosDao.delete(id);
    }
}

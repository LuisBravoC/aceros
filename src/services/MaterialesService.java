package services;

import models.Materiales;
import dao.MaterialesDao;
import javafx.collections.ObservableList;

public class MaterialesService {

    public static ObservableList<Materiales> getAll() {
        return MaterialesDao.getAll();
    }

    public static Materiales findById(String id) {
        return MaterialesDao.findById(id);
    }

    public static boolean insert(String nombre) {
        return MaterialesDao.insert(nombre);
    }

    public static boolean update(String id, String nombre) {
        return MaterialesDao.update(id, nombre);
    }

    public static boolean delete(String id) {
        return MaterialesDao.delete(id);
    }
}

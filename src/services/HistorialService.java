package services;

import models.Historial;
import dao.HistorialDao;
import javafx.collections.ObservableList;

public class HistorialService {

    public static ObservableList<Historial> getHistorial(String s, String de, String a) {
        return HistorialDao.getHistorial(s, de, a);
    }
}

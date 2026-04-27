package services;

import dao.LookupDao;
import javafx.collections.ObservableList;

public class LookupService {

    public static ObservableList<String> getPaises() { return LookupDao.getPaises(); }
    public static ObservableList<String> getGeneros() { return LookupDao.getGeneros(); }
    public static ObservableList<String> getTipoUsuario() { return LookupDao.getTipoUsuario(); }
    public static ObservableList<String> getMetodosPago() { return LookupDao.getMetodosPago(); }
    public static ObservableList<String> getBancos() { return LookupDao.getBancos(); }
    public static ObservableList<String> getPeriodosPago() { return LookupDao.getPeriodosPago(); }
    public static ObservableList<String> getContratos() { return LookupDao.getContratos(); }

    public static ObservableList<String> getMateriales() { return LookupDao.getMateriales(); }
    public static ObservableList<String> getAlturas() { return LookupDao.getAlturas(); }
    public static ObservableList<String> getCalibres() { return LookupDao.getCalibres(); }
    public static ObservableList<String> getRombos() { return LookupDao.getRombos(); }

    public static ObservableList<String> getEstadosByCountryName(String countryName) { return LookupDao.getEstadosByCountryName(countryName); }
    public static ObservableList<String> getCiudadesByStateName(String stateName) { return LookupDao.getCiudadesByStateName(stateName); }
}

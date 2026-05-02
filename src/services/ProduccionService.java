package services;

import dao.ProduccionDao;
import models.ProduccionSemanal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for production-related operations.
 * Delegates persistence to {@link ProduccionDao}.
 */
public class ProduccionService {
    private static final Logger LOGGER = Logger.getLogger(ProduccionService.class.getName());

    private ProduccionService() { /* utility class */ }

    public static boolean insertProduccion(String material, String calibre, String altura, String rombos,
                                           String metros, String cantidad, String autorId, LocalDate fecha) {
        if (fecha == null) fecha = LocalDate.now();
        String fecha_registro = fecha.toString();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        String dia = fecha.format(format).toUpperCase();
        try {
            return ProduccionDao.insert(
                    material == null ? "NULL" : material,
                    calibre == null ? "NULL" : calibre,
                    altura == null ? "NULL" : altura,
                    rombos == null ? "NULL" : rombos,
                    metros == null ? "NULL" : metros,
                    cantidad == null ? "NULL" : cantidad,
                    autorId == null ? "" : autorId,
                    fecha_registro,
                    dia
            );
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error inserting produccion", ex);
            return false;
        }
    }

    public static boolean updateProduccion(String id, String material, String calibre, String altura, String rombos,
                                           String metros, String cantidad, LocalDate fecha) {
        if (id == null || id.trim().isEmpty()) return false;
        if (fecha == null) fecha = LocalDate.now();
        String fecha_registro = fecha.toString();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        String dia = fecha.format(format).toUpperCase();
        try {
            return ProduccionDao.update(
                    id,
                    material == null ? "NULL" : material,
                    calibre == null ? "NULL" : calibre,
                    altura == null ? "NULL" : altura,
                    rombos == null ? "NULL" : rombos,
                    metros == null ? "NULL" : metros,
                    cantidad == null ? "NULL" : cantidad,
                    fecha_registro,
                    dia
            );
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error updating produccion id=" + id, ex);
            return false;
        }
    }

    public static ObservableList<ProduccionSemanal> getProduccionSemana(String autor) {
        try {
            return ProduccionDao.getProduccionSemana(autor);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error fetching produccion semana for autor=" + autor, ex);
            return FXCollections.observableArrayList();
        }
    }
}

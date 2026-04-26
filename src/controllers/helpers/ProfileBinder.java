package controllers.helpers;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.UsuarioDetalle;
import util.DateUtils;
import util.ImageUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileBinder {

    private static final Logger LOGGER = Logger.getLogger(ProfileBinder.class.getName());

    public static void bindProfile(UsuarioDetalle u,
                                   Label lbCodigoUsuario,
                                   Label lbNombreEmpleado,
                                   Label lbAPaternoEmpleado,
                                   Label lbAMaternoEmpleado,
                                   Label lbCurp,
                                   Label lbRfc,
                                   Label lbNss,
                                   Label lbHFecha,
                                   Label lbFechaNacimiento,
                                   Label lbFechaContratacio,
                                   Label lbEmailEmpleado,
                                   Label lbGenero,
                                   Label lbTipoUsuario,
                                   Label lbSueldoEmpleado,
                                   Label lbMetodoPago,
                                   Label lbBanco,
                                   Label lbNCuenta,
                                   Label lbPeriodoPago,
                                   Label lbContrato,
                                   Label lbPais,
                                   Label lbEstado,
                                   Label lbLocalidad,
                                   Label lbColonia,
                                   Label lbNExterior,
                                   Label lbCiudad,
                                   Label lbCalle,
                                   Label lbCodigoPostal,
                                   Label lbNInterior,
                                   ImageView imgPerfil,
                                   Image backup,
                                   Image sinperfil) {
        try {
            if (u != null) {
                lbCodigoUsuario.setText(nonNull(u.getUsuarioId()));
                lbNombreEmpleado.setText(nonNull(u.getNombre()));
                lbAPaternoEmpleado.setText(nonNull(u.getApellidoPaterno()));
                lbAMaternoEmpleado.setText(nonNull(u.getApellidoMaterno()));
                lbCurp.setText(nonNull(u.getCurp()));
                lbRfc.setText(nonNull(u.getRfc()));
                lbNss.setText(nonNull(u.getNss()));

                String timestamp = u.getFechaNacimiento();
                if (timestamp != null && timestamp.length() >= 10) {
                    lbHFecha.setText(DateUtils.formatLongDate(timestamp, true));
                    lbFechaNacimiento.setText(DateUtils.formatLongDate(timestamp, false));
                } else {
                    lbHFecha.setText("");
                    lbFechaNacimiento.setText("");
                }

                String fechaContr = u.getFechaContratacion();
                if (fechaContr != null && fechaContr.length() >= 10) {
                    lbFechaContratacio.setText(DateUtils.formatLongDate(fechaContr, false));
                } else {
                    lbFechaContratacio.setText("");
                }

                lbEmailEmpleado.setText(nonNull(u.getEmail()));
                lbGenero.setText(nonNull(u.getGenero()));
                lbTipoUsuario.setText(nonNull(u.getTipoEmpleado()));
                lbSueldoEmpleado.setText(nonNull(u.getSueldo()));
                lbMetodoPago.setText(nonNull(u.getMetodoPago()));
                lbBanco.setText(nonNull(u.getBanco()));
                lbNCuenta.setText(nonNull(u.getNumeroCuenta()));
                lbPeriodoPago.setText(nonNull(u.getPeriodoPago()));
                lbContrato.setText(nonNull(u.getTipoContrato()));

                lbPais.setText(nonNull(u.getPais()));
                lbEstado.setText(nonNull(u.getEstado()));
                lbLocalidad.setText(nonNull(u.getLocalidad()));
                lbColonia.setText(nonNull(u.getColonia()));
                lbNExterior.setText(nonNull(u.getNumeroExterior()));

                lbCiudad.setText(nonNull(u.getCiudad()));
                lbCalle.setText(nonNull(u.getCalle()));
                lbCodigoPostal.setText(nonNull(u.getCodigoPostal()));
                lbNInterior.setText(nonNull(u.getNumeroInterior()));

                try {
                    imgPerfil.setImage(ImageUtils.fromBytesOrDefault(u.getImagen(), backup));
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Error converting profile image", ex);
                    imgPerfil.setImage(sinperfil);
                }
            } else {
                LOGGER.log(Level.FINE, "No hay informacion de domicilio");
                // Clear fields
                lbCodigoUsuario.setText("");
                lbNombreEmpleado.setText("");
                lbAPaternoEmpleado.setText("");
                lbAMaternoEmpleado.setText("");
                lbCurp.setText("");
                lbRfc.setText("");
                lbNss.setText("");
                lbHFecha.setText("");
                lbFechaNacimiento.setText("");
                lbFechaContratacio.setText("");
                lbEmailEmpleado.setText("");
                lbGenero.setText("");
                lbTipoUsuario.setText("");
                lbSueldoEmpleado.setText("");
                lbMetodoPago.setText("");
                lbBanco.setText("");
                lbNCuenta.setText("");
                lbPeriodoPago.setText("");
                lbContrato.setText("");
                lbPais.setText("");
                lbEstado.setText("");
                lbLocalidad.setText("");
                lbColonia.setText("");
                lbNExterior.setText("");
                lbCiudad.setText("");
                lbCalle.setText("");
                lbCodigoPostal.setText("");
                lbNInterior.setText("");
                imgPerfil.setImage(sinperfil);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error binding profile", e);
        }
    }

    private static String nonNull(String s) {
        return s == null ? "" : s;
    }
}

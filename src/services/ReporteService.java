package services;

import config.ConnectionUtil;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Handles JasperReports generation for the Historial screen.
 */
public class ReporteService {

    private static final Logger LOGGER = Logger.getLogger(ReporteService.class.getName());

    private ReporteService() {}

    /**
     * Builds and displays a JasperViewer report for production history.
     *
     * @param autor  usuario_id to filter by (required)
     * @param de     start date string "yyyy-MM-dd" (may be empty — no lower bound)
     * @param a      end   date string "yyyy-MM-dd" (may be empty — no upper bound)
     * @throws Exception if the report cannot be compiled or filled
     */
    public static void imprimirHistorial(String autor, String de, String a) throws Exception {
        try (InputStream reportStream =
                ReporteService.class.getResourceAsStream("/reports/report.jrxml")) {

            if (reportStream == null) {
                throw new IllegalStateException(
                    "report.jrxml not found on classpath /reports/report.jrxml");
            }

            JasperDesign jdesign = JRXmlLoader.load(reportStream);

            StringBuilder sql = new StringBuilder(
                "select p.id, p.fecha_registro, p.material, p.calibre, p.altura, p.rombos, "
                + "p.metros, p.cantidad, p.dia, p.autor_id, "
                + "concat(u.nombre,' ',u.apellido_paterno,' ',u.apellido_materno) as autor "
                + "from produccion p left join usuarios u on p.autor_id = u.usuario_id "
                + "where p.autor_id = ?");

            List<String> params = new ArrayList<>();
            params.add(autor);
            if (!de.isEmpty() && !a.isEmpty()) {
                sql.append(" and (p.fecha_registro BETWEEN ? AND ?)");
                params.add(de);
                params.add(a);
            }
            sql.append(" order by p.fecha_registro");

            LOGGER.log(Level.FINE, "ReporteService sql: {0}", sql);

            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            try (Connection conn = ConnectionUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql.toString())) {

                for (int i = 0; i < params.size(); i++) {
                    ps.setString(i + 1, params.get(i));
                }
                try (ResultSet rs = ps.executeQuery();
                     InputStream logoStream =
                         ReporteService.class.getResourceAsStream("/icons/LogoInicio.png")) {

                    Map<String, Object> reportParams = new HashMap<>();
                    if (logoStream != null) {
                        reportParams.put("LOGO", logoStream);
                    } else {
                        LOGGER.log(Level.WARNING, "Logo '/icons/LogoInicio.png' not found");
                    }

                    net.sf.jasperreports.engine.JRResultSetDataSource ds =
                        new net.sf.jasperreports.engine.JRResultSetDataSource(rs);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, reportParams, ds);
                    JasperViewer.viewReport(jprint, false);
                }
            }
        }
    }
}

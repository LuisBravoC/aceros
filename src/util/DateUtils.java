package util;

public final class DateUtils {

    private DateUtils() {}

    public static String formatLongDate(String isoDate, boolean includeDel) {
        if (isoDate == null || isoDate.length() < 10) return "";
        String año = isoDate.substring(0, 4);
        String dia = isoDate.substring(8, 10);
        String mes = isoDate.substring(5, 7);
        String mesn;
        switch (mes) {
            case "01": mesn = "ENERO"; break;
            case "02": mesn = "FEBRERO"; break;
            case "03": mesn = "MARZO"; break;
            case "04": mesn = "ABRIL"; break;
            case "05": mesn = "MAYO"; break;
            case "06": mesn = "JUNIO"; break;
            case "07": mesn = "JULIO"; break;
            case "08": mesn = "AGOSTO"; break;
            case "09": mesn = "SEPTIEMBRE"; break;
            case "10": mesn = "OCTUBRE"; break;
            case "11": mesn = "NOVIEMBRE"; break;
            case "12": mesn = "DICIEMBRE"; break;
            default: mesn = mes; break;
        }
        if (includeDel) {
            return dia + " DE " + mesn + " DEL " + año;
        } else {
            return dia + " DE " + mesn + " DE " + año;
        }
    }

}

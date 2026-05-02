package util;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public final class ImageUtils {
    private static final Logger LOGGER = Logger.getLogger(ImageUtils.class.getName());

    private ImageUtils() {}

    public static Image fromBytes(byte[] data) {
        if (data == null || data.length == 0) return null;
        return new Image(new ByteArrayInputStream(data));
    }

    public static Image fromBytesOrDefault(byte[] data, Image defaultImage) {
        if (data == null || data.length == 0) return defaultImage;
        try {
            Image img = fromBytes(data);
            return img != null ? img : defaultImage;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Failed to convert image bytes to Image", ex);
            return defaultImage;
        }
    }
}

package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public final class ImageUtils {
    private static final Logger LOGGER = Logger.getLogger(ImageUtils.class.getName());

    private ImageUtils() {}

    public static Image fromBytes(byte[] data) throws IOException {
        if (data == null) return null;
        Path tmp = Files.createTempFile("profile-", ".png");
        Files.write(tmp, data);
        return new Image(tmp.toUri().toString());
    }

    public static Image fromBytesOrDefault(byte[] data, Image defaultImage) {
        if (data == null) return defaultImage;
        try {
            Image img = fromBytes(data);
            return img != null ? img : defaultImage;
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Failed to convert image bytes to Image", ex);
            return defaultImage;
        }
    }
}

package images;

import util.HashMap;
import util.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageManager {
    private static final HashMap<String, BufferedImage> cache = new HashMap<>();

    private ImageManager() {
    }

    public static BufferedImage getImage(String img) {
        if (cache.get(img) == null) {
            try {
                cache.put(img, ImageIO.read(Objects.requireNonNull(ImageManager.class.getResourceAsStream(img))));
            } catch (IOException e) {
                Logger.log("could not load image \"" + img + "\"");
                e.printStackTrace();
            }
        }
        return cache.get(img);
    }
}

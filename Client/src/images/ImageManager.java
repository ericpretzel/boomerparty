package images;

import util.HashMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageManager {
    private static final HashMap<String, BufferedImage> map = new HashMap<>();
    public static String FULL_HEART = "full_heart.png";
    public static String EMPTY_HEART = "empty_heart.png";

    private ImageManager() {
    }

    public static BufferedImage getImage(String img) {
        if (map.get(img) == null) {
            try {
                map.put(img, ImageIO.read(Objects.requireNonNull(ImageManager.class.getResourceAsStream(img))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map.get(img).get(0);
    }
}

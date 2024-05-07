package circusofplates.Shape;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class imageObject {
    private static final String[] path = {"plateRed.png", "plateBlue.png", "plateYellow.png", "bomb.png", "heart.png", "Ball.png", "clown.png","background.jpg"};

    private static final BufferedImage[] images = new BufferedImage[path.length];

    static {
        for (int i = 0; i < path.length; i++) {
            try {
                images[i] = ImageIO.read(imageObject.class.getClassLoader().getResourceAsStream(path[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedImage getImage(int index) {
        if (index >= 0 && index < images.length) {
            return images[index];
        }
        return null;
    }
}
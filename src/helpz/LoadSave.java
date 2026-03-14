package helpz;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

//tai lai file save
public class LoadSave {
    public static BufferedImage getSpriteAtlas(String fileName){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getClassLoader().getResourceAsStream(fileName);

        try {
            img = ImageIO.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}

package Manager;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AssetManager {

    private static AssetManager instance;

    public BufferedImage backGround;
    public BufferedImage logo;

    private AssetManager() {
        loadAssets();
    }

    public static AssetManager getInstance() {
        if(instance == null){
            instance = new AssetManager();
        }
        return instance;
    }

    private void loadAssets(){
        try {
            backGround = ImageIO.read(getClass().getResourceAsStream("/background.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
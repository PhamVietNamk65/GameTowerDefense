package Manager;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AssetManager {

    private static AssetManager instance;

    public BufferedImage backGround;
    public BufferedImage logo;

    BufferedImage[] menuButtonsNormol = new BufferedImage[3];

    private AssetManager() {
    }

    public static AssetManager getInstance() {
        if(instance == null){
            instance = new AssetManager();
        }
        return instance;
    }

    public void loadMenuAssets() {
        try {
            // 1. Load ảnh gốc chứa cả 3 nút
            BufferedImage atlas = ImageIO.read(getClass().getResourceAsStream("/menu/buttons_normal.png"));
            
            menuButtonsNormol = new BufferedImage[3];
            
            // 2. Cắt ảnh dựa trên tọa độ đã phân tích
            menuButtonsNormol[0] = atlas.getSubimage(250, 15, 593, 120);  // LEVEL
            menuButtonsNormol[1] = atlas.getSubimage(277, 210, 542, 142); // SETTING
            menuButtonsNormol[2] = atlas.getSubimage(282, 424, 532, 123); // QUIT
            
            // Các ảnh khác
            backGround = ImageIO.read(getClass().getResourceAsStream("/menu/background.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/menu/logo.png"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
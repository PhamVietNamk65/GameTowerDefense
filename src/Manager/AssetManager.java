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
            BufferedImage atlas = ImageIO.read(getClass().getResourceAsStream("/menu/button_normal.png"));
            
            menuButtonsNormol = new BufferedImage[3];
            
            // 2. Cắt ảnh dựa trên tọa độ đã phân tích
            menuButtonsNormol[0] = atlas.getSubimage(40, 60, 995, 200);  // LEVEL
            menuButtonsNormol[1] = atlas.getSubimage(40, 390, 995, 235); // SETTING
            menuButtonsNormol[2] = atlas.getSubimage(40, 730, 995, 230); // QUIT
            
            // Các ảnh khác
            backGround = ImageIO.read(getClass().getResourceAsStream("/menu/background.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/menu/logo.png"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package utils;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class AssetManager {

    private static AssetManager instance;

    public static BufferedImage[] tiles;

    public BufferedImage backGround;
    public BufferedImage logo;

    public BufferedImage[] menuButtonsNormol = new BufferedImage[3];
    public BufferedImage[] menuButtonsOver = new BufferedImage[3];
    public BufferedImage[] menuButtonsPressed = new BufferedImage[3];

    private AssetManager() {
    }

    public static AssetManager getInstance() {
        if(instance == null){
            instance = new AssetManager();
        }
        return instance;
    }
    public void loadAllAssets(){
        loadMenuAssets();
        loadLevelAssets();
        loadPlayingAssets();
    }

    public void loadMenuAssets() {
        try {
            //Load ảnh gốc chứa cả 3 nút
            BufferedImage atlas1 = ImageIO.read(getClass().getResourceAsStream("/menu/button_normal.png"));
            BufferedImage atlas2 = ImageIO.read(getClass().getResourceAsStream("/menu/button_moved.png"));
            BufferedImage atlas3 = ImageIO.read(getClass().getResourceAsStream("/menu/button_pressed.png"));

            //Cắt ảnh dựa trên tọa độ đã phân tích
            // nut binh thuong
            menuButtonsNormol[0] = atlas1.getSubimage(300, 0, 740, 180);  // PLAYING
            menuButtonsNormol[1] = atlas1.getSubimage(300, 310, 740, 180); // SETTING
            menuButtonsNormol[2] = atlas1.getSubimage(300, 619, 740, 180); // QUIT
            //nut khi con chuot chi vao
            menuButtonsOver[0] = atlas2.getSubimage(300, 0,740, 180);  // PLAYING
            menuButtonsOver[1] = atlas2.getSubimage(300, 310, 740, 180); // SETTING
            menuButtonsOver[2] = atlas2.getSubimage(300, 619, 740, 180); // QUIT
            //nut khi nhan
            menuButtonsPressed[0] = atlas3.getSubimage(300, 0, 740, 180);  // PLAYING
            menuButtonsPressed[1] = atlas3.getSubimage(300, 300, 740, 180); // SETTING
            menuButtonsPressed[2] = atlas3.getSubimage(300, 609, 740, 180); // QUIT
            // Các ảnh khác
            backGround = ImageIO.read(getClass().getResourceAsStream("/menu/background.png"));
            logo = ImageIO.read(getClass().getResourceAsStream("/menu/logo.png")).getSubimage(5, 80, 1330, 635 );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadLevelAssets() { 
    }
    private void loadPlayingAssets(){

    }
}
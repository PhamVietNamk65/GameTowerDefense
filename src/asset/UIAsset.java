package asset;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import helpz.LoadSave;

public class UIAsset {

    private static UIAsset instance; 

    public static BufferedImage backGround;
    public static BufferedImage logo;
    public static BufferedImage logoTH;
    public static BufferedImage frame;
    public static BufferedImage heart;
    public static BufferedImage coin;
    public static BufferedImage victory;
    public static BufferedImage defeat;
    public static BufferedImage quitFrame;
    public static BufferedImage[] menuButtonsNormol = new BufferedImage[3];
    public static BufferedImage[] menuButtonsOver = new BufferedImage[3];
    public static BufferedImage[] menuButtonsPressed = new BufferedImage[3];

    public static BufferedImage backGround_levelSelect ;
    public static BufferedImage[] levelSelect = new BufferedImage[6];

    public static BufferedImage[] PauseIcon = new BufferedImage[3];

    public static Map<Integer, BufferedImage[]> levelIcons = new HashMap<>();

    public static BufferedImage[] quit = new BufferedImage[3];
    public static BufferedImage[] restart = new BufferedImage[3];
    public static BufferedImage[] option = new BufferedImage[3];
    public static BufferedImage[] nextLevel = new BufferedImage[3];
    public static BufferedImage[] cancel = new BufferedImage[3];
    public static BufferedImage[] levelLock = new BufferedImage[3];
    public static BufferedImage[] yesButton = new BufferedImage[3];
    public static BufferedImage[] noButton = new BufferedImage[3];

    private UIAsset() {
    }

    public static UIAsset getInstance() {
        if(instance == null){
            instance = new UIAsset();
        }
        return instance;
    }
    public void load(){
        loadMenuAssets();
        loadLevelAssets();
        loadMenuStatus();
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
            logoTH = ImageIO.read(getClass().getResourceAsStream("/menu/logoTH.png"));
            frame = ImageIO.read(getClass().getResourceAsStream("/icon/f.png"));
            heart = ImageIO.read(getClass().getResource("/icon/heart.png"));
            coin = LoadSave.getSprite("/icon/coin.png");
            victory = ImageIO.read(getClass().getResource("/menu/victory.png")).getSubimage(355, 300, 637, 193);
            defeat = ImageIO.read(getClass().getResource("/menu/defeat.png")).getSubimage(345, 300, 655, 195);
            quitFrame = ImageIO.read(getClass().getResource("/menu/quitFrame.png")).getSubimage(330, 205, 680, 405);
            //icon
            atlas1 = ImageIO.read(getClass().getResourceAsStream("/ui/icon.png"));
            for( int i = 0 ; i < 3; i++ )
            PauseIcon[i] = atlas1.getSubimage( (3 + i) * 32, 8 * 32, 32, 32);

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadLevelAssets() { 
        try {
            backGround_levelSelect = ImageIO.read(getClass().getResource("/menu/backGround_levelSelect.png"));
            BufferedImage atlas1 = ImageIO.read(getClass().getResourceAsStream("/ui/icon.png"));
            for( int i = 1; i < 10; i++){
                BufferedImage[] icons = new BufferedImage[3];
                for(int j = 0 ; j < 3; j++ ){
                    icons[j] = atlas1.getSubimage(( 12 + j )* 32, i * 32, 32, 32);
                }
                levelIcons.put(i, icons);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenuStatus(){
        try {
            BufferedImage atlas = ImageIO.read(getClass().getResourceAsStream("/ui/icon.png"));
            BufferedImage atlas2 = ImageIO.read(getClass().getResourceAsStream("/ui/text.png"));
            for(int i = 0 ; i < 3; i++){
                quit[i] = atlas.getSubimage( (9 + i) * 32, 0, 32, 32) ;
                restart[i] = atlas.getSubimage( ( 3 + i) * 32, 1 * 32, 32, 32);
                option[i] = atlas.getSubimage((9 + i) * 32, 32,  32, 32);
                nextLevel[i] = atlas.getSubimage(( 3 + i) * 32, 4 * 32, 32, 32);
                cancel[i] = atlas.getSubimage((6 + i) * 32, 3 * 32, 32, 32);
                levelLock[i] = atlas.getSubimage(( 9 + i) * 32, 4 * 32, 32, 32);
                yesButton[i] = atlas2.getSubimage(( 3 + i) * 64, 32, 64, 32);
                noButton[i] = atlas2.getSubimage((3 + i) * 64, 0, 64, 32);
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        
    }
    

}
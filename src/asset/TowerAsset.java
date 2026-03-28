package asset;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Tower;
import helpz.LoadSave;

public class TowerAsset {
    private static TowerAsset instance;

    public static BufferedImage[][] towerFrames;
    public static BufferedImage placeTower;
    public static int[] towerDrawW;
    public static BufferedImage[][][] archerAnimations;

    public static final int MAX_LEVEL = 7;
    // Sprite sheet info per level — frame width đều 70px cho tất cả level
    private static final int[] FRAME_COUNTS = {1, 4, 4, 6, 6, 6, 6};
    private static final int[] FRAME_WIDTHS = {70, 70, 70, 70, 70, 70, 70};
    private static final int   FRAME_HEIGHT = 130;

    // drawW = 70 * 96 / 130 = 51 cho tất cả level
    public static final int DRAW_H = 96;

    public static TowerAsset getInstance() {
        if(instance == null) {
            instance = new TowerAsset();
        }
        return instance;
    }
    
    public void load(){
        loadTowerFrames();
        loadArcherAnimations();
        loadPlaceBuildTower();
    }

    private void loadTowerFrames() {
        towerFrames = new BufferedImage[MAX_LEVEL][];
        towerDrawW  = new int[MAX_LEVEL];

        for (int lv = 0; lv < 7; lv++) {
            String path = "tower/2 Idle/" + (lv + 1) + ".png";
            BufferedImage sheet = LoadSave.getSprite(path);
            if (sheet == null) {
                System.out.println("Missing: " + path);
                towerFrames[lv] = new BufferedImage[0];
                continue;
            }

            int n  = FRAME_COUNTS[lv];
            int fw = FRAME_WIDTHS[lv];     // kích thước frame gốc
            int fh = FRAME_HEIGHT;

            // Tính draw width giữ tỉ lệ với DRAW_H
            towerDrawW[lv] = Math.max(1, fw * DRAW_H / fh);

            towerFrames[lv] = new BufferedImage[n];
            for (int f = 0; f < n; f++) {
                // Đảm bảo không vượt quá chiều rộng sheet
                int x0 = f * fw;
                int x1 = Math.min(x0 + fw, sheet.getWidth());
                if (x0 >= sheet.getWidth()) {
                    towerFrames[lv][f] = null;
                    continue;
                }
                towerFrames[lv][f] = sheet.getSubimage(x0, 0, x1 - x0, fh);
            }

            System.out.printf("Level %d: %d frames, frame=%dx%d, drawW=%d%n",
                    lv+1, n, fw, fh, towerDrawW[lv]);
        }
    }
    private void loadPlaceBuildTower(){
        try {
            placeTower = ImageIO.read(getClass().getResourceAsStream("/2 Objects/PlaceForTower1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadArcherAnimations() {
        archerAnimations = new BufferedImage[3][3][];
        archerAnimations[Tower.SIDE][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/S_Idle.png",      48, 48);
        archerAnimations[Tower.SIDE][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/S_Preattack.png", 48, 48);
        archerAnimations[Tower.SIDE][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/S_Attack.png",    48, 48);
        archerAnimations[Tower.UP  ][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/U_Idle.png",      48, 48);
        archerAnimations[Tower.UP  ][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/U_Preattack.png", 48, 48);
        archerAnimations[Tower.UP  ][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/U_Attack.png",    48, 48);
        archerAnimations[Tower.DOWN][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/D_Idle.png",      48, 48);
        archerAnimations[Tower.DOWN][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/D_Preattack.png", 48, 48);
        archerAnimations[Tower.DOWN][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/D_Attack.png",    48, 48);
    }
}

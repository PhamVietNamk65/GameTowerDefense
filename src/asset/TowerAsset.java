package asset;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.tower.Tower;
import helpz.LoadSave;

public class TowerAsset {
    private static TowerAsset instance;
    // Mỗi level có 1 mảng frame riêng, mỗi frame có kích thước gốc fw x fh, khi vẽ sẽ scale về drawW x DRAW_H
    public static BufferedImage[][] towerFrames;
    public static BufferedImage placeTower;
    public static int[] towerDrawW;
    public static BufferedImage[][][] archerAnimations;
    public static BufferedImage[] arrowFrames;

    public static final int MAX_LEVEL = 7;
    private static final int[] FRAME_COUNTS = {1, 4, 4, 6, 6, 6, 6};
    private static final int[] FRAME_WIDTHS = {70, 70, 70, 70, 70, 70, 70};
    private static final int   FRAME_HEIGHT = 130;

    public static final int DRAW_H = 96;

    public static TowerAsset getInstance() {
        if (instance == null) {
            instance = new TowerAsset();
        }
        return instance;
    }

    public void load() {
        loadTowerFrames();
        loadArcherAnimations();
        loadPlaceBuildTower();
        loadArrowFrames();
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
            int fw = FRAME_WIDTHS[lv];
            int fh = FRAME_HEIGHT;

            towerDrawW[lv] = Math.max(1, fw * DRAW_H / fh);
            towerFrames[lv] = new BufferedImage[n];

            for (int f = 0; f < n; f++) {
                int x0 = f * fw;
                int x1 = Math.min(x0 + fw, sheet.getWidth());
                if (x0 >= sheet.getWidth()) {
                    towerFrames[lv][f] = null;
                    continue;
                }
                towerFrames[lv][f] = sheet.getSubimage(x0, 0, x1 - x0, fh);
            }

            System.out.printf("Level %d: %d frames, frame=%dx%d, drawW=%d%n",
                    lv + 1, n, fw, fh, towerDrawW[lv]);
        }
    }

    private void loadPlaceBuildTower() {
        try {
            placeTower = ImageIO.read(getClass().getResourceAsStream("/tile/2 Objects/PlaceForTower1.png"));
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

    // FIX: Dùng LoadSave.getSprite() — hàm này tự tìm cả classpath lẫn filesystem
    // Chỉ load 1 frame duy nhất (1.png), ArrowRenderer sẽ xoay theo hướng bay
    private void loadArrowFrames() {
        // Thử path chuẩn trước
        BufferedImage arrow = LoadSave.getSprite("tower/3 Units/Arrow/1.png");

        if (arrow != null) {
            arrowFrames = new BufferedImage[]{ arrow };
            System.out.println("Arrow loaded OK: " + arrow.getWidth() + "x" + arrow.getHeight());
        } else {
            // Fallback: thử path không có subfolder số
            arrow = LoadSave.getSprite("tower/3 Units/Arrow/1.png");
            if (arrow != null) {
                arrowFrames = new BufferedImage[]{ arrow };
                System.out.println("Arrow loaded (fallback): " + arrow.getWidth() + "x" + arrow.getHeight());
            } else {
                System.out.println("ERROR: Could not load arrow sprite at 'tower/3 Units/Arrow/1.png'");
                System.out.println("Working dir: " + System.getProperty("user.dir"));
                arrowFrames = new BufferedImage[0];
            }
        }
    }
}
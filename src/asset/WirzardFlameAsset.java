package asset;

import helpz.LoadSave;
import java.awt.image.BufferedImage;

/**
 * characters.png : 6 chars ngang, Wizard = index 4 (thứ 5).
 * towers.png     : 3 cols x 3 rows; sprites 7-9 = row 2 (0-indexed).
 * Flame/1..5.png : frame 0=đạn bay, frame 1-4=explosion animation.
 */
public class WirzardFlameAsset {

    private static WirzardFlameAsset instance;
    public static WirzardFlameAsset getInstance() {
        if (instance == null) instance = new WirzardFlameAsset();
        return instance;
    }

    public static BufferedImage[] towerFrames;   // 3 frames (3 level)
    public static BufferedImage   wizardSprite;
    public static BufferedImage[] wizardSprites = new BufferedImage[3];       // nhân vật thứ 5 (dùng chung)
    /** wizardSprites[0]=lv1, [1]=lv2, [2]=lv3 — cùng sprite nhưng mảng để renderer dễ dùng */
    public static BufferedImage[] flameFrames;   // 5 frames: [0]=đạn, [1-4]=explosion

    public void load() {
        loadTowerFrames();
        loadWizardSprite();
        loadFlameFrames();
    }

    private void loadTowerFrames() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/towers.png");
        if (sheet == null) {
            System.err.println("[WirzardFlameAsset] Missing towers.png");
            towerFrames = makePlaceholder(3, 64, 64, new java.awt.Color(220, 80, 0));
            return;
        }
        int cols = 3, rows = 3;
        int fw = sheet.getWidth() / cols;
        int fh = sheet.getHeight() / rows;
        int row = 0; // sprites 1-3 = hàng thứ 1 (Flame lv1,2,3)
        towerFrames = new BufferedImage[cols];
        for (int c = 0; c < cols; c++) {
            towerFrames[c] = sheet.getSubimage(c * fw, row * fh, fw, fh);
        }
        System.out.println("[WirzardFlameAsset] towerFrames loaded from row0 of towers.png");
    }

    private void loadWizardSprite() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/characters.png");
        if (sheet == null) {
            System.err.println("[WirzardFlameAsset] Missing characters.png");
            wizardSprite = makePlaceholder(1, 48, 48, new java.awt.Color(200, 60, 0))[0];
            return;
        }
        int fw = sheet.getWidth() / 6; // 6 nhân vật ngang
        wizardSprite = sheet.getSubimage(5 * fw, 0, fw, sheet.getHeight()); 
        System.out.println("[WirzardFlameAsset] wizardSprite loaded (index 4)");
        // Fill mảng 3 level — cùng sprite
        for (int i = 0; i < 3; i++) wizardSprites[i] = wizardSprite;
    }

    private void loadFlameFrames() {
        flameFrames = new BufferedImage[5];
        for (int i = 1; i <= 5; i++) {
            String path = "tower/3 Units/Wirzard/Flame/" + i + ".png";
            BufferedImage img = LoadSave.getSprite(path);
            flameFrames[i - 1] = (img != null) ? img
                : makePlaceholder(1, 32, 32, new java.awt.Color(255, 100, 0))[0];
        }
        System.out.println("[WirzardFlameAsset] flameFrames[0-4] loaded");
    }

    private static BufferedImage[] makePlaceholder(int count, int w, int h, java.awt.Color color) {
        BufferedImage[] arr = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(color); g.fillRoundRect(4,4,w,h,10,10);
            g.setColor(java.awt.Color.WHITE); g.drawRoundRect(4,4,w-8,h-8,10,10);
            g.dispose(); arr[i] = img;
        }
        return arr;
    }
}
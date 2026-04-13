package asset;

import helpz.LoadSave;
import java.awt.image.BufferedImage;

/**
 * towers.png     : 3 cols x 3 rows; row 2 = Lightning lv1,2,3.
 * Lightning/1.png: sprite sheet NGANG, mỗi frame vuông (height = frame width).
 *                  Slice theo chiều ngang để lấy từng frame animation hit.
 */
public class WirzardLightningAsset {

    private static WirzardLightningAsset instance;
    public static WirzardLightningAsset getInstance() {
        if (instance == null) instance = new WirzardLightningAsset();
        return instance;
    }

    public static BufferedImage[] towerFrames;
    public static BufferedImage   wizardSprite;
    public static BufferedImage[] wizardSprites = new BufferedImage[3];
    public static BufferedImage[] lightningHitFrames;

    public void load() {
        loadTowerFrames();
        loadWizardSprite();
        loadLightningHitFrames();
    }

    private void loadTowerFrames() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/towers.png");
        if (sheet == null) {
            System.err.println("[WirzardLightningAsset] Missing towers.png");
            towerFrames = makePlaceholder(3, 64, 64, new java.awt.Color(255, 255, 80));
            return;
        }
        int cols = 3, rows = 3;
        int fw = sheet.getWidth()  / cols;
        int fh = sheet.getHeight() / rows;
        int row = 2; // Lightning: hàng thứ 3
        towerFrames = new BufferedImage[cols];
        for (int c = 0; c < cols; c++)
            towerFrames[c] = sheet.getSubimage(c * fw, row * fh, fw, fh);
        System.out.println("[WirzardLightningAsset] towerFrames loaded (row2)");
    }

    private void loadWizardSprite() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/characters.png");
        if (sheet == null) {
            System.err.println("[WirzardLightningAsset] Missing characters.png");
            wizardSprite = makePlaceholder(1, 32, 32, new java.awt.Color(255, 255, 80))[0];
            for (int i = 0; i < 3; i++) wizardSprites[i] = wizardSprite;
            return;
        }
        int fw = sheet.getWidth() / 6;
        wizardSprite = sheet.getSubimage(5 * fw, 0, fw, sheet.getHeight());
        System.out.println("[WirzardLightningAsset] wizardSprite loaded (index 5)");
        for (int i = 0; i < 3; i++) wizardSprites[i] = wizardSprite;
    }

    /**
     * 1.png là sprite sheet NGANG: mỗi frame 64x160 (không vuông).
     * Số frame = sheet.width / FRAME_W.
     * Slice từ trái sang phải.
     */
    private static final int FRAME_W = 64;
    private static final int FRAME_H = 160;

    private void loadLightningHitFrames() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/Lightning/1.png");
        if (sheet == null) {
            System.err.println("[WirzardLightningAsset] Missing Lightning/1.png");
            lightningHitFrames = makePlaceholder(4, FRAME_W, FRAME_H,
                    new java.awt.Color(255, 255, 100));
            return;
        }

        int totalFrames = sheet.getWidth() / FRAME_W;
        if (totalFrames <= 0) {
            System.err.println("[WirzardLightningAsset] 1.png quá nhỏ để slice (w="
                    + sheet.getWidth() + ")");
            lightningHitFrames = makePlaceholder(1, FRAME_W, FRAME_H,
                    new java.awt.Color(255, 255, 100));
            return;
        }

        lightningHitFrames = new BufferedImage[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            int srcY = Math.max(0, (sheet.getHeight() - FRAME_H) / 2); // căn giữa dọc nếu sheet cao hơn
            int srcH = Math.min(FRAME_H, sheet.getHeight());
            lightningHitFrames[i] = sheet.getSubimage(i * FRAME_W, srcY, FRAME_W, srcH);
        }
        System.out.println("[WirzardLightningAsset] lightningHitFrames sliced: "
                + totalFrames + " frames (each " + FRAME_W + "x" + FRAME_H + ") from 1.png ("
                + sheet.getWidth() + "x" + sheet.getHeight() + ")");
    }

    private static BufferedImage[] makePlaceholder(int count, int w, int h, java.awt.Color color) {
        BufferedImage[] arr = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(color); g.fillRoundRect(4, 4, w - 8, h - 8, 10, 10);
            g.setColor(java.awt.Color.WHITE); g.drawRoundRect(4, 4, w - 8, h - 8, 10, 10);
            g.dispose();
            arr[i] = img;
        }
        return arr;
    }
}
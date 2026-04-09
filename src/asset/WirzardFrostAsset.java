package asset;

import helpz.LoadSave;
import java.awt.image.BufferedImage;

/**
 * characters.png    : 6 chars ngang, Wizard = index 4 (thứ 5) — dùng chung.
 * towers.png        : 3 cols x 3 rows; sprites 7-9 = row 2 (0-indexed).
 * Frost/spriteatlas : sprite sheet nhiều hàng, mỗi sprite 32x32.
 *   - Đạn bay   = sprite thứ 19 (0-indexed=18): row=1, col=8 (nếu 10 cols/row)
 *   - Hit effect = sprite thứ 30 (0-indexed=29): row=2, col=9
 */
public class WirzardFrostAsset {

    private static WirzardFrostAsset instance;
    public static WirzardFrostAsset getInstance() {
        if (instance == null) instance = new WirzardFrostAsset();
        return instance;
    }

    public static BufferedImage[] towerFrames;   // 3 frames (3 level)
    public static BufferedImage   wizardSprite;
    public static BufferedImage[] wizardSprites = new BufferedImage[3];
    public static BufferedImage   frostProjectile; // sprite 19 = đạn bay
    public static BufferedImage   frostHitEffect;  // sprite 30 = hiệu ứng trúng

    private static final int SPRITE_SIZE = 32; // kích thước mỗi sprite trong atlas
    private static final int ATLAS_COLS  = 10; // số cột trong spriteatlas

    public void load() {
        loadTowerFrames();
        loadWizardSprite();
        loadFrostSprites();
    }

    private void loadTowerFrames() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/towers.png");
        if (sheet == null) {
            System.err.println("[WirzardFrostAsset] Missing towers.png");
            towerFrames = makePlaceholder(3, 64, 64, new java.awt.Color(80, 180, 255));
            return;
        }
        int cols = 3, rows = 3;
        int fw = sheet.getWidth() / cols;
        int fh = sheet.getHeight() / rows;
        int row = 1; // sprites 4-6 = hàng thứ 2 (Frost lv1,2,3)
        towerFrames = new BufferedImage[cols];
        for (int c = 0; c < cols; c++) {
            towerFrames[c] = sheet.getSubimage(c * fw, row * fh, fw, fh);
        }
        System.out.println("[WirzardFrostAsset] towerFrames loaded from row1 of towers.png");
    }

    private void loadWizardSprite() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Wirzard/characters.png");
        if (sheet == null) {
            System.err.println("[WirzardFrostAsset] Missing characters.png");
            wizardSprite = makePlaceholder(1, 32, 32, new java.awt.Color(80, 180, 255))[0];
            return;
        }
        int fw = sheet.getWidth() / 6;
        wizardSprite = sheet.getSubimage(5 * fw, 0, fw, sheet.getHeight()); 
        System.out.println("[WirzardFrostAsset] wizardSprite loaded (index 4)");
        // Fill mảng 3 level — cùng sprite
        for (int i = 0; i < 3; i++) wizardSprites[i] = wizardSprite;
    }

    private void loadFrostSprites() {
        BufferedImage atlas = LoadSave.getSprite("tower/3 Units/Wirzard/Frost/spriteatlas.png");
        if (atlas == null) {
            System.err.println("[WirzardFrostAsset] Missing spriteatlas.png");
            frostProjectile = makePlaceholder(1, 32, 32, new java.awt.Color(160, 220, 255))[0];
            frostHitEffect  = makePlaceholder(1, 32, 32, new java.awt.Color(200, 240, 255))[0];
            return;
        }

        // Tính kích thước thật của từng ô trong atlas
        // atlas có ATLAS_COLS cột, tính fw/fh từ ảnh thật
        int fw = atlas.getWidth() / ATLAS_COLS;
        int fh = fw; // sprite vuông

        // Sprite 19 (1-indexed) => index 18 (0-indexed) => row=1, col=8
        int idx19 = 18;
        int row19 = idx19 / ATLAS_COLS;
        int col19 = idx19 % ATLAS_COLS;
        if (col19 * fw + fw <= atlas.getWidth() && row19 * fh + fh <= atlas.getHeight()) {
            frostProjectile = atlas.getSubimage(col19 * fw, row19 * fh, fw, fh);
            System.out.println("[WirzardFrostAsset] frostProjectile=sprite19 at row=" + row19 + " col=" + col19);
        } else {
            frostProjectile = makePlaceholder(1, 32, 32, new java.awt.Color(160, 220, 255))[0];
        }

        // Sprite 30 (1-indexed) => index 29 (0-indexed) => row=2, col=9
        int idx30 = 29;
        int row30 = idx30 / ATLAS_COLS;
        int col30 = idx30 % ATLAS_COLS;
        if (col30 * fw + fw <= atlas.getWidth() && row30 * fh + fh <= atlas.getHeight()) {
            frostHitEffect = atlas.getSubimage(col30 * fw, row30 * fh, fw, fh);
            System.out.println("[WirzardFrostAsset] frostHitEffect=sprite30 at row=" + row30 + " col=" + col30);
        } else {
            frostHitEffect = makePlaceholder(1, 32, 32, new java.awt.Color(200, 240, 255))[0];
        }
    }

    private static BufferedImage[] makePlaceholder(int count, int w, int h, java.awt.Color color) {
        BufferedImage[] arr = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(color); g.fillRoundRect(4,4,w-8,h-8,10,10);
            g.setColor(java.awt.Color.WHITE); g.drawRoundRect(4,4,w-8,h-8,10,10);
            g.dispose(); arr[i] = img;
        }
        return arr;
    }
}
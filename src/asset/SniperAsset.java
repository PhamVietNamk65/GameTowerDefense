package asset;

import helpz.LoadSave;
import java.awt.image.BufferedImage;

/**
 * SniperAsset
 *
 * towers.png layout (3×3 grid):
 *   Row 0 : blue   bases  → unused
 *   Row 1 : brown  bases  → col 0 = Lv1 | col 1 = Lv2 | col 2 = Lv3
 *   Row 2 : pink   bases  → unused
 *
 * Each column in row 1 is a DIFFERENT LEVEL — no animation on the base.
 *
 * MC-Idle-SpriteSheet.png : 4-frame idle strip (32×32 each)
 * SnipePNGs/Snipe1.png     : bullet in-flight
 * SnipePNGs/Snipe1-6.png   : hit explosion frames
 */
public class SniperAsset {

    private static SniperAsset instance;
    public  static SniperAsset getInstance() {
        if (instance == null) instance = new SniperAsset();
        return instance;
    }
    private SniperAsset() {}

    // ── sprite storage ────────────────────────────────────────────────────────
    /** towerBaseByLevel[0]=Lv1, [1]=Lv2, [2]=Lv3 — static, level picks which sprite */
    public BufferedImage[] towerBaseByLevel;

    /** 4-frame MC idle animation */
    public BufferedImage[] mcIdleFrames;

    /** In-flight bullet sprite */
    public BufferedImage   bulletSprite;

    /** Hit explosion: Snipe1→Snipe6 */
    public BufferedImage[] hitFrames;

    // ── constants ─────────────────────────────────────────────────────────────
    public static final int MAX_LEVEL       = 3;
    public static final int MC_FRAME_W      = 32;
    public static final int MC_FRAME_H      = 32;
    public static final int MC_IDLE_FRAMES  = 4;
    public static final int HIT_FRAME_COUNT = 6;

    // ── load ──────────────────────────────────────────────────────────────────
    public void load() {
        loadTowerBase();
        loadMcIdle();
        loadBullet();
        loadHitFrames();
    }

    private void loadTowerBase() {
        BufferedImage sheet = LoadSave.getSprite("tower/3 Units/Sniper/towers.png");
        towerBaseByLevel = new BufferedImage[MAX_LEVEL];

        if (sheet == null) {
            System.out.println("[SniperAsset] towers.png not found");
            return;
        }

        int cellW = sheet.getWidth()  / 3;
        int cellH = sheet.getHeight() / 3;
        int row   = 1; // row 1 = brown bases

        for (int col = 0; col < MAX_LEVEL; col++) {
            towerBaseByLevel[col] = sheet.getSubimage(col * cellW, row * cellH, cellW, cellH);
        }
    }

    private void loadMcIdle() {
        mcIdleFrames = LoadSave.getSpriteFrames(
                "tower/3 Units/Sniper/MC-Idle-SpriteSheet.png", MC_FRAME_W, MC_FRAME_H);
        if (mcIdleFrames == null || mcIdleFrames.length == 0)
            System.out.println("[SniperAsset] MC-Idle-SpriteSheet.png not found");
    }

    private void loadBullet() {
        bulletSprite = LoadSave.getSprite("tower/3 Units/Sniper/SnipePNGs/Snipe1.png");
        if (bulletSprite == null)
            System.out.println("[SniperAsset] Snipe1.png (bullet) not found");
    }

    private void loadHitFrames() {
        hitFrames = new BufferedImage[HIT_FRAME_COUNT];
        for (int i = 1; i <= HIT_FRAME_COUNT; i++) {
            hitFrames[i - 1] = LoadSave.getSprite(
                    "tower/3 Units/Sniper/SnipePNGs/Snipe" + i + ".png");
            if (hitFrames[i - 1] == null)
                System.out.println("[SniperAsset] Snipe" + i + ".png not found");
        }
    }
}
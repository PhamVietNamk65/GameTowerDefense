package asset;

import helpz.LoadSave;
import java.awt.image.BufferedImage;

/**
 * Quản lý tất cả sprite của Canon tower (4 level turret) và đạn bom.
 *
 * Cấu trúc thư mục res:
 *   res/tower/3 Units/Canon/Blue/Weapons/turret_02_mk1.png  (11 frames ngang)
 *   res/tower/3 Units/Canon/Blue/Weapons/turret_02_mk2.png  (11 frames ngang)
 *   res/tower/3 Units/Canon/Blue/Weapons/turret_02_mk3.png  (11 frames ngang)
 *   res/tower/3 Units/Canon/Blue/Weapons/turret_02_mk4.png  (11 frames ngang)
 *   res/tower/3 Units/Canon/Blue/Towers/towers_walls_blank.png
 *   res/tower/3 Units/Canon/Blue/Bomb/bomb_anim.png       (tuỳ chọn)
 *   res/tower/3 Units/Canon/Blue/Bomb/explosion.png       (tuỳ chọn)
 *
 * Mỗi turret_02_mkX.png: sprite sheet 11 frames nằm ngang.
 * frame_width  = sheet.getWidth() / FRAMES_PER_TURRET   (tự tính, không hardcode).
 * frame_height = sheet.getHeight().
 */
public class CanonAsset {

    private static CanonAsset instance;

    // ── Turret ────────────────────────────────────────────────────────────────
    public static final int MAX_LEVEL         = 4;   // mk1 → mk4
    public static final int FRAMES_PER_TURRET = 11;  // số frame mỗi animation

    /** [level 0..3][frame 0..10] */
    public static BufferedImage[][] turretFrames;

    /** Kích thước vẽ turret trên màn hình (scale lên tile 64×64) */
    public static final int TURRET_DRAW_W = 56;
    public static final int TURRET_DRAW_H = 56;

    // ── Base (chân tháp) ──────────────────────────────────────────────────────
    public static BufferedImage canonBase;

    public static final int BASE_DRAW_W = 64;
    public static final int BASE_DRAW_H = 64;

    /** Ô đầu tiên trong towers_walls_blank (128×128 vì ảnh vẽ ở scale 2×) */
    private static final int BASE_CELL_W = 128;
    private static final int BASE_CELL_H = 128;

    // ── Bomb projectile ───────────────────────────────────────────────────────
    public static BufferedImage[] bombFrames;
    public static final int BOMB_DRAW_W   = 28;
    public static final int BOMB_DRAW_H   = 28;
    private static final int BOMB_FRAME_W = 32;
    private static final int BOMB_FRAME_H = 32;

    // ── Explosion ─────────────────────────────────────────────────────────────
    public static BufferedImage[] explosionFrames;
    public static final int EXPLOSION_DRAW_W   = 80;
    public static final int EXPLOSION_DRAW_H   = 80;
    private static final int EXPLOSION_FRAME_W = 48;
    private static final int EXPLOSION_FRAME_H = 48;

    // ─────────────────────────────────────────────────────────────────────────

    public static CanonAsset getInstance() {
        if (instance == null) instance = new CanonAsset();
        return instance;
    }

    public void load() {
        loadTurretFrames();
        loadBase();
        loadBombFrames();
        loadExplosionFrames();
    }

    // ── Loaders ───────────────────────────────────────────────────────────────

    /**
     * Load 4 turret sprite sheets, mỗi sheet 11 frames nằm ngang.
     * frameWidth = sheet.getWidth() / 11  (tự động theo file thực tế).
     */
    private void loadTurretFrames() {
        turretFrames = new BufferedImage[MAX_LEVEL][];

        for (int lv = 0; lv < MAX_LEVEL; lv++) {
            String path = "tower/3 Units/Canon/Blue/Weapons/turret_02_mk" + (lv + 1) + ".png";
            BufferedImage sheet = LoadSave.getSprite(path);

            if (sheet == null) {
                System.err.println("[CanonAsset] Missing: " + path + " – placeholder");
                turretFrames[lv] = makePlaceholder(FRAMES_PER_TURRET, 48, 48,
                        new java.awt.Color(0, 80 + lv * 50, 200));
                continue;
            }

            int sheetW  = sheet.getWidth();
            int sheetH  = sheet.getHeight();
            int frameW  = sheetW / FRAMES_PER_TURRET;  // tính từ file thực tế
            int frameH  = sheetH;

            turretFrames[lv] = new BufferedImage[FRAMES_PER_TURRET];
            for (int f = 0; f < FRAMES_PER_TURRET; f++) {
                int x0      = f * frameW;
                int actualW = Math.min(frameW, sheetW - x0);
                if (actualW <= 0) {
                    // fallback nếu sheet hẹp hơn dự kiến
                    turretFrames[lv][f] = turretFrames[lv][Math.max(0, f - 1)];
                    continue;
                }
                turretFrames[lv][f] = sheet.getSubimage(x0, 0, actualW, frameH);
            }

            System.out.printf("[CanonAsset] mk%d: sheet=%dx%d, frameW=%d, frameH=%d, frames=%d%n",
                    lv + 1, sheetW, sheetH, frameW, frameH, FRAMES_PER_TURRET);
        }
    }

    /**
     * Load chân tháp từ towers_walls_blank.png.
     * Lấy ô đầu tiên (top-left 128×128).
     */
    private void loadBase() {
        String path = "tower/3 Units/Canon/Blue/Towers/towers_walls_blank.png";
        BufferedImage sheet = LoadSave.getSprite(path);

        if (sheet == null) {
            System.err.println("[CanonAsset] Missing base: " + path);
            canonBase = makePlaceholder(1, BASE_DRAW_W, BASE_DRAW_H,
                    new java.awt.Color(40, 60, 100))[0];
            return;
        }

        int cw = Math.min(BASE_CELL_W, sheet.getWidth());
        int ch = Math.min(BASE_CELL_H, sheet.getHeight());
        canonBase = sheet.getSubimage(0, 0, cw, ch);
        System.out.printf("[CanonAsset] Base OK: cell=%dx%d (sheet=%dx%d)%n",
                cw, ch, sheet.getWidth(), sheet.getHeight());
    }

    private void loadBombFrames() {
        String path = "tower/3 Units/Canon/Blue/Bomb/bomb_anim.png";
        BufferedImage sheet = LoadSave.getSprite(path);

        if (sheet == null) {
            System.err.println("[CanonAsset] Missing bomb: " + path + " – placeholder");
            bombFrames = makePlaceholder(4, BOMB_FRAME_W, BOMB_FRAME_H,
                    new java.awt.Color(200, 60, 0));
            return;
        }

        int count = Math.max(1, sheet.getWidth() / BOMB_FRAME_W);
        bombFrames = new BufferedImage[count];
        for (int f = 0; f < count; f++) {
            int x0 = f * BOMB_FRAME_W;
            bombFrames[f] = sheet.getSubimage(x0, 0,
                    Math.min(BOMB_FRAME_W, sheet.getWidth() - x0), BOMB_FRAME_H);
        }
        System.out.println("[CanonAsset] Bomb frames: " + count);
    }

    private void loadExplosionFrames() {
        String path = "tower/3 Units/Canon/Blue/Bomb/Boom2.png";
        BufferedImage sheet = LoadSave.getSprite(path);

        if (sheet == null) {
            System.err.println("[CanonAsset] Missing explosion: " + path + " – placeholder");
            explosionFrames = makePlaceholder(6, EXPLOSION_FRAME_W, EXPLOSION_FRAME_H,
                    new java.awt.Color(255, 140, 0));
            return;
        }

        int count = Math.max(1, sheet.getWidth() / EXPLOSION_FRAME_W);
        explosionFrames = new BufferedImage[count];
        for (int f = 0; f < count; f++) {
            int x0 = f * EXPLOSION_FRAME_W;
            explosionFrames[f] = sheet.getSubimage(x0, 0,
                    Math.min(EXPLOSION_FRAME_W, sheet.getWidth() - x0), EXPLOSION_FRAME_H);
        }
        System.out.println("[CanonAsset] Explosion frames: " + count);
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    private static BufferedImage[] makePlaceholder(int count, int w, int h, java.awt.Color color) {
        BufferedImage[] arr = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(color);
            g.fillOval(3, 3, w - 6, h - 6);
            g.setColor(java.awt.Color.WHITE);
            g.drawOval(3, 3, w - 6, h - 6);
            g.setColor(java.awt.Color.YELLOW);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, Math.max(8, w / 4)));
            g.drawString(String.valueOf(i), w / 2 - 4, h / 2 + 4);
            g.dispose();
            arr[i] = img;
        }
        return arr;
    }
}
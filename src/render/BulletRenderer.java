package render;

import asset.SniperAsset;
import entity.Projectile.Bullet;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * BulletRenderer
 *
 * Pha bay :
 *   - Vẽ Snipe1.png (toàn bộ 64×64 sprite) xoay theo hướng di chuyển.
 *   - Sprite gốc hướng LÊN (angle = -PI/2), nên cần bù thêm +PI/2 khi xoay.
 *
 * Pha nổ :
 *   - Vẽ Snipe1→Snipe6 tại điểm chạm, không xoay, kích thước lớn hơn.
 */
public class BulletRenderer {

    private final SniperAsset asset = SniperAsset.getInstance();

    /**
     * Snipe1.png hướng LÊN trên (mũi đạn ở đỉnh).
     * atan2 trả về 0 = sang phải, nên cần xoay thêm +PI/2 để căn hướng.
     */
    private static final double SPRITE_ANGLE_OFFSET = Math.PI / 2.0;

    public void render(Graphics2D g2, ArrayList<Bullet> bullets) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);

        for (Bullet b : bullets) {
            if (b.isDone()) continue;
            if (!b.isHit()) drawFlight(g2, b);
            else            drawExplosion(g2, b);
        }
    }

    // ── đạn đang bay ─────────────────────────────────────────────────────────
    private void drawFlight(Graphics2D g2, Bullet b) {
        BufferedImage img = asset.bulletSprite;
        if (img == null) return;

        int w = Bullet.BULLET_DRAW_W;
        int h = Bullet.BULLET_DRAW_H;

        AffineTransform old = g2.getTransform();

        // Dịch về tâm đạn, xoay theo góc bay, bù offset sprite
        g2.translate(b.getX(), b.getY());
        g2.rotate(b.getAngle() + SPRITE_ANGLE_OFFSET);

        // Vẽ sprite căn giữa quanh điểm (0,0)
        g2.drawImage(img, -w / 2, -h / 2, w, h, null);

        g2.setTransform(old);
    }

    // ── explosion khi chạm ───────────────────────────────────────────────────
    private void drawExplosion(Graphics2D g2, Bullet b) {
        int frame = b.getHitFrame();
        if (asset.hitFrames == null || frame >= asset.hitFrames.length) return;

        BufferedImage img = asset.hitFrames[frame];
        if (img == null) return;

        int size = Bullet.HIT_DRAW_SIZE;
        g2.drawImage(img,
                (int) b.getX() - size / 2,
                (int) b.getY() - size / 2,
                size, size, null);
    }
}
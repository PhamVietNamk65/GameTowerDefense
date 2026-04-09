package render;

import asset.CanonAsset;
import entity.Projectile.Bomb;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Vẽ tất cả Bomb (đang bay + đang nổ).
 */
public class BombRenderer {

    // ── Hằng số vẽ ───────────────────────────────────────────────────────────
    private static final int BOMB_DRAW_W      = CanonAsset.BOMB_DRAW_W;      // 32
    private static final int BOMB_DRAW_H      = CanonAsset.BOMB_DRAW_H;      // 32
    private static final int EXPLOSION_DRAW_W = CanonAsset.EXPLOSION_DRAW_W; // 80
    private static final int EXPLOSION_DRAW_H = CanonAsset.EXPLOSION_DRAW_H; // 80

    // ─────────────────────────────────────────────────────────────────────────

    public void render(Graphics2D g2, ArrayList<Bomb> bombs) {
        if (bombs == null || bombs.isEmpty()) return;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (Bomb b : bombs) {
            if (b.isExploding()) {
                drawExplosion(g2, b);
            } else {
                drawBomb(g2, b);
            }
        }
    }

    // ── Vẽ bom đang bay ───────────────────────────────────────────────────────
    private void drawBomb(Graphics2D g2, Bomb b) {
        BufferedImage[] frames = CanonAsset.bombFrames;
        if (frames == null || frames.length == 0) {
            drawFallbackBomb(g2, b);
            return;
        }

        int idx = Math.min(b.getBombFrame(), frames.length - 1);
        BufferedImage img = frames[idx];
        if (img == null) { drawFallbackBomb(g2, b); return; }

        int cx = (int) b.x;
        int cy = (int) b.y;

        // Xoay sprite theo góc bay
        java.awt.geom.AffineTransform old = g2.getTransform();
        g2.translate(cx, cy);
        g2.rotate(b.getAngle());
        g2.drawImage(img,
                -BOMB_DRAW_W / 2, -BOMB_DRAW_H / 2,
                BOMB_DRAW_W, BOMB_DRAW_H, null);
        g2.setTransform(old);
    }

    // ── Vẽ explosion ──────────────────────────────────────────────────────────
    private void drawExplosion(Graphics2D g2, Bomb b) {
        BufferedImage[] frames = CanonAsset.explosionFrames;
        if (frames == null || frames.length == 0) {
            drawFallbackExplosion(g2, b);
            return;
        }

        int idx = Math.min(b.getExplosionFrame(), frames.length - 1);
        BufferedImage img = frames[idx];
        if (img == null) { drawFallbackExplosion(g2, b); return; }

        int cx = (int) b.x - EXPLOSION_DRAW_W / 2;
        int cy = (int) b.y - EXPLOSION_DRAW_H / 2;
        g2.drawImage(img, cx, cy, EXPLOSION_DRAW_W, EXPLOSION_DRAW_H, null);
    }

    // ── Fallback (vẽ hình tròn khi thiếu asset) ───────────────────────────────
    private void drawFallbackBomb(Graphics2D g2, Bomb b) {
        int cx = (int) b.x;
        int cy = (int) b.y;
        g2.setColor(new Color(50, 50, 50));
        g2.fillOval(cx - 8, cy - 8, 16, 16);
        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(cx - 8, cy - 8, 16, 16);
    }

    private void drawFallbackExplosion(Graphics2D g2, Bomb b) {
        int frame = b.getExplosionFrame();
        int maxFrames = CanonAsset.explosionFrames != null ? CanonAsset.explosionFrames.length : 6;
        float progress = (float) frame / Math.max(1, maxFrames - 1);

        int r = (int)(b.getSplashRadius() * 0.6f * progress);
        if (r <= 0) return;

        float alpha = 0.7f * (1f - progress);
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Vòng ngoài (đỏ cam)
        g2.setColor(new Color(255, 80, 0));
        g2.fillOval((int) b.x - r, (int) b.y - r, r * 2, r * 2);

        // Vòng giữa (vàng)
        int r2 = (int)(r * 0.6f);
        g2.setColor(new Color(255, 220, 0));
        g2.fillOval((int) b.x - r2, (int) b.y - r2, r2 * 2, r2 * 2);

        // Viền splash radius (mờ)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setColor(new Color(255, 100, 0));
        g2.setStroke(new BasicStroke(2f));
        int sr = b.getSplashRadius();
        g2.drawOval((int) b.x - sr, (int) b.y - sr, sr * 2, sr * 2);

        g2.setComposite(old);
    }
}
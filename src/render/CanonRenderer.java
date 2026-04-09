package render;

import asset.CanonAsset;
import entity.tower.CanonTower;
import entity.tower.Tower;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import utils.Constants;

/**
 * Vẽ Canon Tower:
 *  1. Base (towers_walls_blank ô đầu tiên) – nền tĩnh.
 *  2. Turret (turret_02_mkX) – quay theo góc nhắm, animation theo frame.
 *  3. Progress bar nâng cấp (kế thừa logic TowerRenderer).
 */
public class CanonRenderer {

    private static final int TILE = Constants.Tiles.TILE_SIZE; // 64

    // Kích thước vẽ base
    private static final int BASE_W = CanonAsset.BASE_DRAW_W; // 64
    private static final int BASE_H = CanonAsset.BASE_DRAW_H; // 64

    // Kích thước vẽ turret
    private static final int TURRET_W = CanonAsset.TURRET_DRAW_W; // 64
    private static final int TURRET_H = CanonAsset.TURRET_DRAW_H; // 64

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Gọi từ TowerRenderer (hoặc PlayingState) để vẽ tất cả canon towers.
     */
    public void draw(Graphics2D g2, ArrayList<Tower> towers, Tower selectedTower) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (Tower t : towers) {
            if (t instanceof CanonTower) {
                drawCanon(g2, (CanonTower) t);
            }
        }

        if (selectedTower instanceof CanonTower) {
            drawRange(g2, selectedTower);
            drawSplashRadius(g2, (CanonTower) selectedTower);
        }
    }

    // ── Draw single Canon ─────────────────────────────────────────────────────
    private void drawCanon(Graphics2D g2, CanonTower t) {
        int tx = t.getX();
        int ty = t.getY();

        // 1. Vẽ base
        drawBase(g2, t, tx, ty);

        // 2. Vẽ progress bar nếu đang upgrade
        if (t.isUpgrading()) {
            drawProgressBar(g2, t, tx, ty);
        }

        // 3. Vẽ turret (quay)
        if (!t.isUpgrading()) {
            drawTurret(g2, t, tx, ty);
        }
    }

    // ── Base ──────────────────────────────────────────────────────────────────
    private void drawBase(Graphics2D g2, CanonTower t, int tx, int ty) {
        BufferedImage base = CanonAsset.canonBase;
        if (base == null) {
            // Fallback: vẽ nền xám
            g2.setColor(new Color(60, 70, 90));
            g2.fillRect(tx, ty, TILE, TILE);
            g2.setColor(new Color(100, 120, 150));
            g2.drawRect(tx, ty, TILE, TILE);
            return;
        }

        int drawX = tx + (TILE - BASE_W) / 2;
        int drawY = ty + (TILE - BASE_H) / 2;

        if (t.isUpgrading() && t.getFlashAlpha() > 0) {
            float bright = 1.0f + (t.getFlashAlpha() / 180f);
            g2.drawImage(applyBrightness(base, bright), drawX, drawY, BASE_W, BASE_H, null);
        } else {
            g2.drawImage(base, drawX, drawY, BASE_W, BASE_H, null);
        }
    }

    // ── Turret ────────────────────────────────────────────────────────────────
    private void drawTurret(Graphics2D g2, CanonTower t, int tx, int ty) {
        int lv = Math.min(t.getCanonLevel(), CanonAsset.MAX_LEVEL - 1);
        if (CanonAsset.turretFrames == null
                || lv >= CanonAsset.turretFrames.length
                || CanonAsset.turretFrames[lv] == null
                || CanonAsset.turretFrames[lv].length == 0) {
            drawFallbackTurret(g2, t, tx, ty);
            return;
        }

        int frameIdx = t.getTurretFrame() % CanonAsset.turretFrames[lv].length;
        BufferedImage img = CanonAsset.turretFrames[lv][frameIdx];
        if (img == null) { drawFallbackTurret(g2, t, tx, ty); return; }

        // Tâm tile
        int cx = tx + TILE / 2;
        int cy = ty + TILE / 2;

        java.awt.geom.AffineTransform old = g2.getTransform();
        g2.translate(cx, cy);
        // turretAngle từ atan2 (0 = phải, PI/2 = xuống).
        // Sprite gốc hướng lên → bù thêm -PI/2 để khi angle=0 nòng chỉ sang phải.
        g2.rotate(t.getTurretAngle() + Math.PI / 2);
        g2.drawImage(img,
                -TURRET_W / 2, -TURRET_H / 2,
                TURRET_W, TURRET_H, null);
        g2.setTransform(old);
    }

    /** Vẽ fallback (hình chữ thập) khi thiếu asset. */
    private void drawFallbackTurret(Graphics2D g2, CanonTower t, int tx, int ty) {
        int cx = tx + TILE / 2;
        int cy = ty + TILE / 2;

        java.awt.geom.AffineTransform old = g2.getTransform();
        g2.translate(cx, cy);
        g2.rotate(t.getTurretAngle() + Math.PI / 2);

        // Thân
        g2.setColor(new Color(180, 180, 200));
        g2.fillRect(-8, -20, 16, 40);
        // Nòng
        g2.setColor(new Color(80, 80, 100));
        g2.fillRect(-4, -28, 8, 20);

        g2.setTransform(old);
    }

    // ── Progress bar ──────────────────────────────────────────────────────────
    private void drawProgressBar(Graphics2D g2, CanonTower t, int drawX, int drawY) {
        int barW = TILE;
        int barH = 5;
        int barX = drawX;
        int barY = drawY - 14;
        float progress = t.getUpgradeProgress();

        g2.setColor(new Color(30, 30, 30, 180));
        g2.fillRoundRect(barX - 1, barY - 1, barW + 2, barH + 2, 3, 3);

        int filled = (int)(barW * progress);
        if (filled > 0) {
            g2.setColor(Color.getHSBColor(0.55f, 0.9f, 1.0f)); // xanh dương cho canon
            g2.fillRoundRect(barX, barY, filled, barH, 2, 2);
        }

        Stroke os = g2.getStroke();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setColor(new Color(180, 180, 180, 100));
        g2.drawRoundRect(barX, barY, barW, barH, 2, 2);
        g2.setStroke(os);

        g2.setFont(new Font("Arial", Font.BOLD, 8));
        g2.setColor(new Color(160, 220, 255));
        String label = "MK" + (t.getCanonLevel()) + "→MK" + (t.getCanonLevel() + 1);
        int tw = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, barX + (barW - tw) / 2, barY - 1);
    }

    // ── Range & splash circles (khi selected) ────────────────────────────────
    private void drawRange(Graphics2D g2, Tower t) {
        int cx     = t.getX() + TILE / 2;
        int cy     = t.getY() + TILE / 2;
        int radius = (int) t.getRange();

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g2.setColor(new Color(0, 150, 255));
        g2.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
        g2.setComposite(old);

        g2.setColor(new Color(0, 180, 255, 160));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawSplashRadius(Graphics2D g2, CanonTower t) {
        int cx = t.getX() + TILE / 2;
        int cy = t.getY() + TILE / 2;
        int sr = t.getSplashRadius();

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        g2.setColor(new Color(255, 100, 0));
        g2.fillOval(cx - sr, cy - sr, sr * 2, sr * 2);
        g2.setComposite(old);

        g2.setColor(new Color(255, 140, 0, 180));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[]{4, 4}, 0));
        g2.drawOval(cx - sr, cy - sr, sr * 2, sr * 2);
        g2.setStroke(new BasicStroke(1f));
    }

    // ── Brightness helper ─────────────────────────────────────────────────────
    private BufferedImage applyBrightness(BufferedImage src, float scale) {
        float[] s = {scale, scale, scale, 1f};
        float[] o = {0f, 0f, 0f, 0f};
        java.awt.image.RescaleOp op = new java.awt.image.RescaleOp(s, o, null);
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = dst.createGraphics();
        bg.drawImage(src, 0, 0, null);
        bg.dispose();
        return op.filter(dst, null);
    }
}
package render;

import asset.WirzardFlameAsset;
import entity.Projectile.Flame;
import entity.monster.Monster;
import entity.tower.FlameTower;
import entity.tower.Tower;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import utils.Constants;

/**
 * Vẽ:
 *  1. Flame Tower base (3 level, không có archer idle animation phía sau)
 *  2. Wizard ngồi trên đỉnh tháp, kích thước tăng theo level
 *  3. Fireball đang bay + explosion
 *  4. Burn effect (ngọn lửa nhỏ) trên quái đang bị đốt
 */
public class FlameRenderer {

    private static final int TILE = Constants.Tiles.TILE_SIZE; // 64

    // ── Kích thước tower base theo level (w, h) ───────────────────────────────
    // lv1: vừa tile, lv2: to hơn chút, lv3: to nhất — giống ảnh tham khảo
    private static final int[] TOWER_W = {52, 58, 64};
    private static final int[] TOWER_H = {48, 54, 60};

    // ── Kích thước wizard theo level ─────────────────────────────────────────
    private static final int[] WIZ_W = {24, 28, 32};
    private static final int[] WIZ_H = {24, 28, 32};
    private static final int[] WIZ_WP = {-12, -8, 2};
    private static final int[] WIZ_HP = {12, 10, 8};

    // ── Draw tower ────────────────────────────────────────────────────────────
    public void drawTower(Graphics2D g2, ArrayList<Tower> towers, Tower selectedTower) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        for (Tower t : towers) {
            if (t instanceof FlameTower ft) {
                drawFlameTower(g2, ft);
            }
        }

        if (selectedTower instanceof FlameTower ft) {
            drawRange(g2, ft);
        }
    }

    private void drawFlameTower(Graphics2D g2, FlameTower t) {
        int lv  = Math.max(1, Math.min(t.getLevel(), 3));
        int idx = lv - 1;

        BufferedImage[] frames = WirzardFlameAsset.towerFrames;
        if (frames == null || frames.length == 0) {
            drawFallbackTower(g2, t, lv);
            drawWizard(g2, t, lv);
            return;
        }

        if (idx >= frames.length) idx = frames.length - 1;
        BufferedImage img = frames[idx];
        if (img == null) {
            drawFallbackTower(g2, t, lv);
            drawWizard(g2, t, lv);
            return;
        }

        // Vẽ tower base: căn giữa theo tile, to dần theo level
        int tw = TOWER_W[idx];
        int th = TOWER_H[idx];
        int tx = t.getX() + (TILE - tw) / 2;
        // Đáy tháp chạm đáy tile (tháp mọc từ dưới lên)
        int ty = t.getY() + (TILE - th);

        g2.drawImage(img, tx, ty, tw, th, null);

        // Wizard ngồi trên đỉnh tháp
        drawWizard(g2, t, lv);
    }

    /**
     * Vẽ wizard căn giữa ngang, đặt gần đỉnh tháp.
     * Kích thước wizard tăng cùng level để trông cân xứng.
     *
     *  lv1: wizard nhỏ, ngồi thấp hơn (tháp thấp)
     *  lv2: wizard vừa
     *  lv3: wizard lớn, ngồi cao hơn (tháp cao hơn)
     */
    private void drawWizard(Graphics2D g2, FlameTower t, int lv) {
        BufferedImage[] sprites = WirzardFlameAsset.wizardSprites;
        if (sprites == null) return;

        int idx = lv - 1;
        int si  = Math.max(0, Math.min(idx, sprites.length - 1));
        BufferedImage wiz = sprites[si];
        if (wiz == null) wiz = WirzardFlameAsset.wizardSprite;
        if (wiz == null) return;

        int ww = WIZ_W[idx];
        int wh = WIZ_H[idx];

        // Căn giữa ngang
        int wx = t.getX() + (TILE - ww) / 2 + WIZ_WP[idx]; 

        // Đỉnh tháp = ty của tháp + một khoảng nhỏ
        // ty của tháp = t.getY() + (TILE - TOWER_H[idx])
        int towerTop = t.getY() + (TILE - TOWER_H[idx]);
        // Wizard ngồi sao cho chân wizard trùng với đỉnh tháp + một chút overlap
        int wy = towerTop - wh + WIZ_HP[idx] + idx * (-2); // lv cao hơn → wizard cao hơn thêm chút

        g2.drawImage(wiz, wx, wy, ww, wh, null);
    }

    private void drawFallbackTower(Graphics2D g2, FlameTower t, int lv) {
        int idx = lv - 1;
        int tw = TOWER_W[idx];
        int th = TOWER_H[idx];
        int tx = t.getX() + (TILE - tw) / 2;
        int ty = t.getY() + (TILE - th);

        g2.setColor(new Color(160, 60, 0));
        g2.fillRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(255, 140, 0));
        g2.drawRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(255, 80, 0));
        int cx = tx + tw / 2;
        g2.fillOval(cx - 8, ty + th / 2 - 8, 16, 20);
        g2.setColor(new Color(255, 200, 0));
        g2.fillOval(cx - 4, ty + th / 2 - 4, 8, 12);
    }

    private void drawRange(Graphics2D g2, FlameTower t) {
        int cx = t.getX() + TILE / 2, cy = t.getY() + TILE / 2;
        int r  = (int) t.getRange();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g2.setColor(new Color(255, 100, 0));
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        g2.setComposite(old);
        g2.setColor(new Color(255, 140, 0, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        g2.setStroke(new BasicStroke(1f));
    }

    // ── Draw fireballs ────────────────────────────────────────────────────────
    public void drawFlames(Graphics2D g2, ArrayList<Flame> flames) {
        if (flames == null || flames.isEmpty()) return;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (Flame f : flames) {
            if (f.isExploding()) drawExplosion(g2, f);
            else                 drawFireball(g2, f);
        }
    }

    private void drawFireball(Graphics2D g2, Flame f) {
        int cx = (int) f.x, cy = (int) f.y;

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        g2.setColor(new Color(255, 120, 0));
        g2.fillOval(cx - 12, cy - 12, 24, 24);
        g2.setComposite(old);

        g2.setColor(new Color(255, 220, 60));
        g2.fillOval(cx - 7, cy - 7, 14, 14);
        g2.setColor(new Color(255, 80, 0));
        g2.fillOval(cx - 4, cy - 4, 8, 8);
    }

    private void drawExplosion(Graphics2D g2, Flame f) {
        int frame = f.getExplodeFrame();
        float progress = frame / 6f;
        int r = (int)(f.getSplashRadius() * 0.7f * progress);
        if (r <= 0) return;

        Composite old = g2.getComposite();
        float alpha = 0.7f * (1f - progress);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));

        g2.setColor(new Color(255, 60, 0));
        g2.fillOval((int) f.x - r, (int) f.y - r, r * 2, r * 2);
        int r2 = (int)(r * 0.55f);
        g2.setColor(new Color(255, 200, 0));
        g2.fillOval((int) f.x - r2, (int) f.y - r2, r2 * 2, r2 * 2);

        g2.setComposite(old);
    }

    // ── Draw burn effect on monsters ──────────────────────────────────────────
    public void drawBurnEffect(Graphics2D g2, ArrayList<Monster> monsters) {
        if (monsters == null) return;
        for (Monster m : monsters) {
            if (m.getStatusEffect().isBurning()) {
                drawBurnOnMonster(g2, m);
            }
        }
    }

    private void drawBurnOnMonster(Graphics2D g2, Monster m) {
        int cx = (int) m.getX() + 16;
        int cy = (int) m.getY() - 4;

        double flicker = Math.sin(System.currentTimeMillis() * 0.015) * 0.3 + 0.7;
        float alpha = (float)(0.85f * flicker);

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                Math.min(1f, Math.max(0f, alpha))));

        g2.setColor(new Color(255, 80, 0));
        g2.fillOval(cx - 8, cy - 12, 16, 16);
        g2.setColor(new Color(255, 220, 0));
        g2.fillOval(cx - 4, cy - 8, 8, 10);

        g2.setComposite(old);

        g2.setColor(new Color(255, 140, 0, 200));
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 8));
        g2.drawString("BURN", cx - 8, cy - 14);
    }
}
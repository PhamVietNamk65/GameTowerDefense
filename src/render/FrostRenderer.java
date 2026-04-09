package render;

import asset.WirzardFrostAsset;
import entity.Projectile.Frost;
import entity.monster.Monster;
import entity.tower.FrostTower;
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
 * Vẽ Frost Tower + Wizard + Frost Orb (sprite 19) + Hit effect (sprite 30) + Slow effect.
 * Tower base 3 level, wizard kích thước tăng theo level.
 */
public class FrostRenderer {

    private static final int TILE = Constants.Tiles.TILE_SIZE;

    // ── Kích thước tower base theo level ─────────────────────────────────────
    private static final int[] TOWER_W = {52, 58, 64};
    private static final int[] TOWER_H = {48, 54, 60};

    // ── Kích thước wizard theo level ─────────────────────────────────────────
    private static final int[] WIZ_W = {24, 28, 32};
    private static final int[] WIZ_H = {24, 28, 32};
    private static final int[] WIZ_WP = {-12, -8, 2};
    private static final int[] WIZ_HP = {20, 16, 8};

    // ── Draw tower ─────────────────────────────────────────────────────────────
    public void drawTower(Graphics2D g2, ArrayList<Tower> towers, Tower selectedTower) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (Tower t : towers) {
            if (t instanceof FrostTower ft) drawFrostTower(g2, ft);
        }
        if (selectedTower instanceof FrostTower ft) drawRange(g2, ft);
    }

    private void drawFrostTower(Graphics2D g2, FrostTower t) {
        int lv  = Math.max(1, Math.min(t.getLevel(), 3));
        int idx = lv - 1;

        BufferedImage[] frames = WirzardFrostAsset.towerFrames;
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

        // Vẽ tower base: căn giữa ngang, đáy chạm đáy tile
        int tw = TOWER_W[idx];
        int th = TOWER_H[idx];
        int tx = t.getX() + (TILE - tw) / 2;
        int ty = t.getY() + (TILE - th);

        g2.drawImage(img, tx, ty, tw, th, null);
        drawWizard(g2, t, lv);
    }

    /**
     * Wizard căn giữa ngang, ngồi trên đỉnh tháp.
     * lv1: nhỏ, gần đáy tile hơn; lv3: lớn hơn, cao hơn.
     */
    private void drawWizard(Graphics2D g2, FrostTower t, int lv) {
        BufferedImage[] sprites = WirzardFrostAsset.wizardSprites;
        if (sprites == null) return;

        int idx = lv - 1;
        int si  = Math.max(0, Math.min(idx, sprites.length - 1));
        BufferedImage wiz = sprites[si];
        if (wiz == null) wiz = WirzardFrostAsset.wizardSprite;
        if (wiz == null) return;

        int ww = WIZ_W[idx];
        int wh = WIZ_H[idx];

        int wx = t.getX() + (TILE - ww) / 2 + WIZ_WP[idx];
        int towerTop = t.getY() + (TILE - TOWER_H[idx]);
        int wy = towerTop - wh + WIZ_HP[idx] + idx * (-2);

        g2.drawImage(wiz, wx, wy, ww, wh, null);
    }

    private void drawFallbackTower(Graphics2D g2, FrostTower t, int lv) {
        int idx = lv - 1;
        int tw = TOWER_W[idx];
        int th = TOWER_H[idx];
        int tx = t.getX() + (TILE - tw) / 2;
        int ty = t.getY() + (TILE - th);

        g2.setColor(new Color(40, 120, 200));
        g2.fillRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(160, 220, 255));
        g2.drawRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(200, 240, 255));
        g2.setStroke(new BasicStroke(2f));
        int cx = tx + tw / 2, cy = ty + th / 2;
        g2.drawLine(cx - 10, cy, cx + 10, cy);
        g2.drawLine(cx, cy - 10, cx, cy + 10);
        g2.drawLine(cx - 7, cy - 7, cx + 7, cy + 7);
        g2.drawLine(cx + 7, cy - 7, cx - 7, cy + 7);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawRange(Graphics2D g2, FrostTower t) {
        int cx = t.getX() + TILE / 2, cy = t.getY() + TILE / 2;
        int r  = (int) t.getRange();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g2.setColor(new Color(80, 180, 255));
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        g2.setComposite(old);
        g2.setColor(new Color(160, 220, 255, 200));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        g2.setStroke(new BasicStroke(1f));
    }

    // ── Draw frost orbs ────────────────────────────────────────────────────────
    public void drawFrosts(Graphics2D g2, ArrayList<Frost> frosts) {
        if (frosts == null || frosts.isEmpty()) return;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (Frost f : frosts) {
            if (f.isBursting()) drawBurst(g2, f);
            else                drawOrb(g2, f);
        }
    }

    private void drawOrb(Graphics2D g2, Frost f) {
        int cx = (int) f.x, cy = (int) f.y;
        BufferedImage proj = WirzardFrostAsset.frostProjectile;

        if (proj != null) {
            int size = 20;
            g2.drawImage(proj, cx - size / 2, cy - size / 2, size, size, null);
        } else {
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f));
            g2.setColor(new Color(160, 220, 255));
            g2.fillOval(cx - 11, cy - 11, 22, 22);
            g2.setComposite(old);
            g2.setColor(new Color(80, 180, 255));
            g2.fillOval(cx - 7, cy - 7, 14, 14);
            g2.setColor(new Color(220, 245, 255));
            g2.fillOval(cx - 3, cy - 5, 5, 5);
        }
    }

    private void drawBurst(Graphics2D g2, Frost f) {
        int frame = f.getBurstFrame();
        float progress = frame / 5f;
        int r = (int)(f.getSplashRadius() * progress);
        if (r <= 0) return;

        BufferedImage hitFx = WirzardFrostAsset.frostHitEffect;
        Composite old = g2.getComposite();
        float alpha = 0.9f * (1f - progress);

        if (hitFx != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    Math.max(0f, alpha)));
            int size = r * 2;
            g2.drawImage(hitFx, (int) f.x - r, (int) f.y - r, size, size, null);
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));
            g2.setColor(new Color(160, 220, 255));
            g2.fillOval((int) f.x - r, (int) f.y - r, r * 2, r * 2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha * 0.6f)));
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval((int) f.x - r, (int) f.y - r, r * 2, r * 2);
            g2.setStroke(new BasicStroke(1f));
        }
        g2.setComposite(old);
    }

    // ── Slow effect trên quái ──────────────────────────────────────────────────
    public void drawSlowEffect(Graphics2D g2, ArrayList<Monster> monsters) {
        if (monsters == null) return;
        for (Monster m : monsters) {
            if (m.getStatusEffect().isSlowed()) drawSlowOnMonster(g2, m);
        }
    }

    private void drawSlowOnMonster(Graphics2D g2, Monster m) {
        int mx = (int) m.getX(), my = (int) m.getY();

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f));
        g2.setColor(new Color(80, 160, 255));
        g2.fillRect(mx, my, 32, 32);
        g2.setComposite(old);

        BufferedImage hitFx = WirzardFrostAsset.frostHitEffect;
        int cx = mx + 16, cy = my - 6;
        if (hitFx != null) {
            g2.drawImage(hitFx, cx - 10, cy - 10, 20, 20, null);
        } else {
            g2.setColor(new Color(200, 235, 255, 220));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(cx - 6, cy, cx + 6, cy);
            g2.drawLine(cx, cy - 6, cx, cy + 6);
            g2.drawLine(cx - 4, cy - 4, cx + 4, cy + 4);
            g2.drawLine(cx + 4, cy - 4, cx - 4, cy + 4);
            g2.setStroke(new BasicStroke(1f));
        }

        g2.setColor(new Color(160, 220, 255, 210));
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 8));
        g2.drawString("SLOW", cx - 8, cy - 12);
    }
}
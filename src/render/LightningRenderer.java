package render;

import asset.WirzardLightningAsset;
import entity.Projectile.Lightning;
import entity.monster.Monster;
import entity.tower.LightningTower;
import entity.tower.Tower;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import utils.Constants;

/**
 * Vẽ Lightning Tower + Wizard + Bolt + Hit animation + Stun effect.
 *
 * ═══ CHỈNH ANIMATION LIGHTNING ═══════════════════════════════════════════
 *
 *  1. Tốc độ chạy frame (ms mỗi frame):
 *       HIT_FRAME_MS = 60   ← giảm số → nhanh hơn, tăng số → chậm hơn
 *
 *  2. Kích thước sprite hit effect trên quái:
 *       HIT_SIZE = 48       ← pixel, tăng để to hơn
 *
 *  3. Kích thước sprite stun effect trên đầu quái:
 *       STUN_SIZE = 32      ← pixel
 *
 *  4. Tốc độ frame stun (ms mỗi frame):
 *       STUN_FRAME_MS = 100 ← giảm → nhanh hơn
 *
 * ═════════════════════════════════════════════════════════════════════════
 */
public class LightningRenderer {

    private static final int TILE = Constants.Tiles.TILE_SIZE;
    private final Random rng = new Random();

    // ── Kích thước tower base theo level ─────────────────────────────────────
    private static final int[] TOWER_W = {52, 58, 64};
    private static final int[] TOWER_H = {48, 54, 60};

    // ── Kích thước wizard theo level ─────────────────────────────────────────
    private static final int[] WIZ_W = {24, 28, 32};
    private static final int[] WIZ_H = {24, 28, 32};
    private static final int[] WIZ_WP = {-12, -8, 2};
    private static final int[] WIZ_HP = {28, 28, 12};

    // ══ CHỈNH ANIMATION TẠI ĐÂY ══════════════════════════════════════════════
    /** Thời gian hiển thị mỗi frame hit effect (milliseconds). Giảm = nhanh hơn. */
    private static final int HIT_FRAME_MS   = 60;

    /** Kích thước sprite hit effect tại điểm trúng quái — giữ đúng tỉ lệ 64x160. */
    private static final int HIT_W          = 64;
    private static final int HIT_H          = 160;

    /** Thời gian mỗi frame stun effect trên đầu quái (ms). */
    private static final int STUN_FRAME_MS  = 100;

    /** Kích thước sprite stun effect trên đầu quái (px). */
    private static final int STUN_SIZE      = 32;
    // ═════════════════════════════════════════════════════════════════════════

    // ── Draw tower ─────────────────────────────────────────────────────────────
    public void drawTower(Graphics2D g2, ArrayList<Tower> towers, Tower selectedTower) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (Tower t : towers) {
            if (t instanceof LightningTower lt) drawLightningTower(g2, lt);
        }
        if (selectedTower instanceof LightningTower lt) drawRange(g2, lt);
    }

    private void drawLightningTower(Graphics2D g2, LightningTower t) {
        int lv  = Math.max(1, Math.min(t.getLevel(), 3));
        int idx = lv - 1;

        BufferedImage[] frames = WirzardLightningAsset.towerFrames;
        if (frames == null || frames.length == 0) {
            drawFallbackTower(g2, t, lv); drawWizard(g2, t, lv); return;
        }
        if (idx >= frames.length) idx = frames.length - 1;
        BufferedImage img = frames[idx];
        if (img == null) { drawFallbackTower(g2, t, lv); drawWizard(g2, t, lv); return; }

        int tw = TOWER_W[idx];
        int th = TOWER_H[idx];
        int tx = t.getX() + (TILE - tw) / 2;
        int ty = t.getY() + (TILE - th);
        g2.drawImage(img, tx, ty, tw, th, null);
        drawWizard(g2, t, lv);
    }

    private void drawWizard(Graphics2D g2, LightningTower t, int lv) {
        BufferedImage[] sprites = WirzardLightningAsset.wizardSprites;
        if (sprites == null) return;
        int idx = lv - 1;
        int si  = Math.max(0, Math.min(idx, sprites.length - 1));
        BufferedImage wiz = sprites[si];
        if (wiz == null) wiz = WirzardLightningAsset.wizardSprite;
        if (wiz == null) return;

        int ww = WIZ_W[idx];
        int wh = WIZ_H[idx];
        int wx = t.getX() + (TILE - ww) / 2 + WIZ_WP[idx];
        int towerTop = t.getY() + (TILE - TOWER_H[idx]);
        int wy = towerTop - wh + WIZ_HP[idx] + idx * (-2);
        g2.drawImage(wiz, wx, wy, ww, wh, null);
    }

    private void drawFallbackTower(Graphics2D g2, LightningTower t, int lv) {
        int idx = lv - 1;
        int tw = TOWER_W[idx], th = TOWER_H[idx];
        int tx = t.getX() + (TILE - tw) / 2;
        int ty = t.getY() + (TILE - th);
        g2.setColor(new Color(60, 60, 20));
        g2.fillRoundRect(tx, ty, tw, th, 10, 10);
        g2.setColor(new Color(255, 255, 80));
        g2.drawRoundRect(tx, ty, tw, th, 10, 10);
        int cx = tx + tw / 2, cy = ty + th / 2;
        g2.setColor(new Color(255, 255, 100));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawLine(cx + 3, cy - 12, cx - 3, cy);
        g2.drawLine(cx - 3, cy, cx + 4, cy + 2);
        g2.drawLine(cx + 4, cy + 2, cx - 3, cy + 14);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawRange(Graphics2D g2, LightningTower t) {
        int cx = t.getX() + TILE / 2, cy = t.getY() + TILE / 2;
        int r  = (int) t.getRange();
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        g2.setColor(new Color(255, 255, 80));
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        g2.setComposite(old);
        g2.setColor(new Color(255, 255, 120, 180));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1f, new float[]{5, 4}, 0));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        g2.setStroke(new BasicStroke(1f));
    }

    // ── Draw lightning bolts ───────────────────────────────────────────────────
    public void drawLightnings(Graphics2D g2, ArrayList<Lightning> lightnings) {
        if (lightnings == null || lightnings.isEmpty()) return;
        for (Lightning l : lightnings) drawBolts(g2, l);
    }

    private void drawBolts(Graphics2D g2, Lightning l) {
        float alpha = l.getFadeAlpha();
        if (alpha <= 0) return;

        BufferedImage[] hitFrames = WirzardLightningAsset.lightningHitFrames;

        // ── Chọn frame dựa theo thời gian thực, KHÔNG phụ thuộc alpha ──────
        // Mỗi HIT_FRAME_MS ms thì đổi sang frame tiếp theo → cycle liên tục
        int frameIdx = 0;
        if (hitFrames != null && hitFrames.length > 0) {
            frameIdx = (int)((System.currentTimeMillis() / HIT_FRAME_MS) % hitFrames.length);
        }

        Composite old = g2.getComposite();

        for (Lightning.Bolt bolt : l.getBolts()) {
            // Tia sét (glow dày)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.4f));
            g2.setColor(new Color(200, 200, 255));
            g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            drawJaggedLine(g2, bolt.x1, bolt.y1, bolt.x2, bolt.y2, 3);

            // Tia sét (core mỏng sáng hơn)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.9f));
            g2.setColor(new Color(255, 255, 180));
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            drawJaggedLine(g2, bolt.x1, bolt.y1, bolt.x2, bolt.y2, 3);

            // Hit effect sprite tại điểm chạm quái
            if (hitFrames != null && hitFrames.length > 0) {
                BufferedImage hitImg = hitFrames[frameIdx];
                if (hitImg != null) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    // Căn giữa ngang, đáy sprite chạm điểm trúng quái
                    g2.drawImage(hitImg,
                            (int) bolt.x2 - HIT_W / 2,
                            (int) bolt.y2 - HIT_H,
                            HIT_W, HIT_H, null);
                }
            } else {
                // Fallback: flash vàng nhỏ
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.7f));
                g2.setColor(new Color(255, 255, 100));
                g2.fillOval((int) bolt.x2 - 6, (int) bolt.y2 - 6, 12, 12);
            }
        }

        g2.setComposite(old);
        g2.setStroke(new BasicStroke(1f));
    }

    private void drawJaggedLine(Graphics2D g2,
                                 float x1, float y1, float x2, float y2,
                                 int segments) {
        float dx = x2 - x1, dy = y2 - y1;
        float nx = -dy, ny = dx;
        float len = (float) Math.sqrt(nx * nx + ny * ny);
        if (len > 0) { nx /= len; ny /= len; }

        float[] px = new float[segments + 1];
        float[] py = new float[segments + 1];
        px[0] = x1; py[0] = y1;
        px[segments] = x2; py[segments] = y2;

        float jitter = len * 0.22f;
        for (int i = 1; i < segments; i++) {
            float t = (float) i / segments;
            px[i] = x1 + dx * t + nx * (rng.nextFloat() - 0.5f) * jitter * 2;
            py[i] = y1 + dy * t + ny * (rng.nextFloat() - 0.5f) * jitter * 2;
        }
        for (int i = 0; i < segments; i++)
            g2.drawLine((int) px[i], (int) py[i], (int) px[i + 1], (int) py[i + 1]);
    }

    // ── Stun effect trên quái ──────────────────────────────────────────────────
    public void drawStunEffect(Graphics2D g2, ArrayList<Monster> monsters) {
        if (monsters == null) return;
        for (Monster m : monsters)
            if (m.getStatusEffect().isStunned()) drawStunOnMonster(g2, m);
    }

    private void drawStunOnMonster(Graphics2D g2, Monster m) {
        int cx = (int) m.getX() + 16;
        int cy = (int) m.getY() - 2;

        BufferedImage[] hitFrames = WirzardLightningAsset.lightningHitFrames;
        Composite old = g2.getComposite();

        if (hitFrames != null && hitFrames.length > 0) {
            // Frame chạy theo thời gian, tốc độ STUN_FRAME_MS
            int frameIdx = (int)((System.currentTimeMillis() / STUN_FRAME_MS) % hitFrames.length);
            BufferedImage hitImg = hitFrames[frameIdx];
            if (hitImg != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                // Vẽ nhỏ hơn trên đầu quái, giữ tỉ lệ 64:160 → STUN_SIZE x (STUN_SIZE*160/64)
                int sw = STUN_SIZE;
                int sh = STUN_SIZE * HIT_H / HIT_W; // giữ tỉ lệ
                g2.drawImage(hitImg,
                        cx - sw / 2,
                        cy - sh,
                        sw, sh, null);
            }
        } else {
            // Fallback: vòng vàng + sao quay
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.80f));
            g2.setColor(new Color(255, 255, 60));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(cx - 10, cy - 10, 20, 20);
            long t = System.currentTimeMillis();
            for (int i = 0; i < 3; i++) {
                double angle = Math.toRadians(i * 120 + (t / 8) % 360);
                int sx = cx + (int)(Math.cos(angle) * 9);
                int sy = cy + (int)(Math.sin(angle) * 9) - 6;
                g2.setColor(new Color(255, 240, 60));
                g2.fillOval(sx - 3, sy - 3, 6, 6);
            }
            g2.setStroke(new BasicStroke(1f));
        }

        g2.setComposite(old);
        g2.setColor(new Color(255, 240, 60, 210));
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 8));
        int labelH = STUN_SIZE * HIT_H / HIT_W;
        g2.drawString("STUN", cx - 8, cy - labelH - 2);
    }
}
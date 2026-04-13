package render;

import asset.SniperAsset;
import entity.tower.SniperTower;
import entity.tower.Tower;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * SniperRenderer
 *
 * Draws each SniperTower in two layers:
 *   1) Tower base  – static sprite chosen by level (towerBaseByLevel[level])
 *   2) MC sniper   – 4-frame idle animation on top of the base
 *
 * MC position is tuned per level via MC_OFFSET_X / MC_OFFSET_Y arrays so the
 * character sits naturally on each base variant.
 *
 * Level 0 (Lv1) → small brown  base (lowest platform)
 * Level 1 (Lv2) → medium brown base (taller)
 * Level 2 (Lv3) → large brown  base (tallest)
 */
public class SniperRenderer {

    private final SniperAsset asset = SniperAsset.getInstance();

    // ── draw sizes ────────────────────────────────────────────────────────────
    private static final int BASE_DRAW_W = 64;   // match tile size
    private static final int BASE_DRAW_H = 64;

    private static final int MC_DRAW_W = 38;
    private static final int MC_DRAW_H = 38;

    /**
     * Per-level X offset of the MC relative to tower top-left corner.
     * Positive = shift right from left edge of tile.
     * Formula: (BASE_DRAW_W - MC_DRAW_W) / 2 = 13 as centre; adjust ±
     */
    private static final int[] MC_OFFSET_X = {
        6,   // Lv1 – small flat base, centred
        12,   // Lv2 – medium base, centred
        26    // Lv3 – large base, centred
    };

    /**
     * Per-level Y offset of the MC relative to tower top-left corner.
     * Smaller number = higher up on screen (sits closer to top of tile).
     * Lv1 base is short so MC is lower; Lv2/Lv3 bases are taller so MC rides higher.
     */
    private static final int[] MC_OFFSET_Y = {
        -6,   // Lv1 – short base, MC sits near bottom half
        -16,   // Lv2 – taller base, MC sits at mid
        -32    // Lv3 – tallest base, MC near top
    };

    // ── draw ─────────────────────────────────────────────────────────────────
    public void draw(Graphics2D g2, ArrayList<Tower> towers, Tower selectedTower) {
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        for (Tower t : towers) {
            if (t instanceof SniperTower sniper) {
                drawSniperTower(g2, sniper);
            }
        }
    }

    private void drawSniperTower(Graphics2D g2, SniperTower s) {
        int tx  = s.getX();
        int ty  = s.getY();
        int lv  = s.getBaseLevel();   // 0, 1, or 2

        // ── 1. Tower base (static per level) ─────────────────────────────────
        if (asset.towerBaseByLevel != null && lv < asset.towerBaseByLevel.length) {
            BufferedImage base = asset.towerBaseByLevel[lv];
            if (base != null)
                g2.drawImage(base, tx, ty, BASE_DRAW_W, BASE_DRAW_H, null);
        }

        // ── 2. Flash overlay khi đang upgrade ───────────────────────────────────
        if (s.isUpgrading() && s.getFlashAlpha() > 0) {
            Composite oldC = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    s.getFlashAlpha() / 255f * 0.5f));
            g2.setColor(new Color(180, 140, 80));
            g2.fillRoundRect(tx, ty, BASE_DRAW_W, BASE_DRAW_H, 6, 6);
            g2.setComposite(oldC);
        }

        // ── 3. MC idle on top of base ─────────────────────────────────────────
        if (asset.mcIdleFrames != null) {
            int frame = s.getMcAnimFrame();
            if (frame < asset.mcIdleFrames.length) {
                BufferedImage mc = asset.mcIdleFrames[frame];
                if (mc != null) {
                    int drawX = tx + MC_OFFSET_X[lv];
                    int drawY = ty + MC_OFFSET_Y[lv];

                    if (s.isFacingLeftSniper()) {
                        // Flip horizontally
                        g2.drawImage(mc,
                                drawX + MC_DRAW_W, drawY,
                                -MC_DRAW_W, MC_DRAW_H, null);
                    } else {
                        g2.drawImage(mc, drawX, drawY, MC_DRAW_W, MC_DRAW_H, null);
                    }
                }
            }
        }

        // ── 4. Thanh tiến độ upgrade ──────────────────────────────────────────
        if (s.isUpgrading()) drawUpgradeBar(g2, s, tx, ty);
    }

    // ── Upgrade progress bar ──────────────────────────────────────────────────
    private void drawUpgradeBar(Graphics2D g2, SniperTower s, int tx, int ty) {
        int barW = BASE_DRAW_W;
        int barH = 5;
        int barX = tx;
        int barY = ty - 40;
        float progress = s.getUpgradeProgress();

        // Nền tối
        g2.setColor(new Color(20, 20, 20, 200));
        g2.fillRoundRect(barX - 1, barY - 1, barW + 2, barH + 2, 3, 3);

        // Thanh fill màu nâu vàng (theme sniper)
        int filled = (int)(barW * progress);
        if (filled > 0) {
            g2.setColor(Color.getHSBColor(0.10f - progress * 0.03f, 0.85f, 0.95f));
            g2.fillRoundRect(barX, barY, filled, barH, 2, 2);
        }

        // Viền
        java.awt.Stroke os = g2.getStroke();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setColor(new Color(200, 200, 200, 120));
        g2.drawRoundRect(barX, barY, barW, barH, 2, 2);
        g2.setStroke(os);

        // Label LV x → LV y
        g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 8));
        g2.setColor(new Color(255, 220, 140));
        String label = "LV" + s.getPrevLevel() + " → LV" + (s.getPrevLevel() + 1);
        int lw = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, barX + (barW - lw) / 2, barY - 2);
    }
}
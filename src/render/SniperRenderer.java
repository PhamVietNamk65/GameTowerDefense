package render;

import asset.SniperAsset;
import entity.tower.SniperTower;
import entity.tower.Tower;
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

        // ── 2. MC idle on top of base ─────────────────────────────────────────
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
    }
}
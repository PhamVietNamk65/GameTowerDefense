package render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import Manager.TowerManager;
import asset.TowerAsset;
import entity.tower.Tower;
import utils.Constants;
import utils.Utilz;

public class TowerRenderer {

    private TowerManager towerManager;

    private static final int ARCHER_W  = 48;
    private static final int ARCHER_H  = 48;

    private static final int[] ARCHER_TOP_Y = {65, 54, 45, 0, 40, 40, 0};

    private static final boolean[] ARCHER_VISIBLE = {true, true, true, false, true, true, false};

    public TowerRenderer(TowerManager towerManager) {
        this.towerManager = towerManager;
    }

    public void draw(Graphics2D g) {
        for (Tower t : towerManager.getTowers()) {
            drawTower(g, t);
        }

        if (towerManager.getSelectedTower() != null) {
            drawSelected(g, towerManager.getSelectedTower());
        }
    }

    private void drawTower(Graphics2D g2, Tower t) {
        int lv    = Utilz.clamp(t.getTowerLevel(), 0, 6);
        int frame = t.getTowerAnimFrame();

        if (TowerAsset.towerFrames[lv] == null || TowerAsset.towerFrames[lv].length == 0) return;
        frame = Utilz.clamp(frame, 0, TowerAsset.towerFrames[lv].length - 1);

        BufferedImage img = TowerAsset.towerFrames[lv][frame];
        if (img == null) return;
        int dw = TowerAsset.towerDrawW[lv] + 35 ;
        int dh = TowerAsset.DRAW_H + 35 ;

        int drawX = t.getX() - (dw - Constants.Tiles.TILE_SIZE ) / 2;
        int drawY = t.getY() + Constants.Tiles.TILE_SIZE - dh;

        // Flash brightness khi upgrade
        if (t.isUpgrading() && t.getFlashAlpha() > 0) {
            float bright = 1.0f + (t.getFlashAlpha() / 180f);
            g2.drawImage(applyBrightness(img, bright), drawX, drawY, dw, dh, null);
        } else {
            g2.drawImage(img, drawX, drawY, dw, dh, null);
        }

        // Progress bar
        if (t.isUpgrading()) drawProgressBar(g2, t, drawX, drawY, dw);

        // Archer (ẩn khi upgrade)
        if (!t.isUpgrading()) drawArcher(g2, t, drawX, drawY);
    }

    private BufferedImage applyBrightness(BufferedImage src, float scale) {
        float[] s = {scale,scale,scale,1f};
        float[] o = {0f,0f,0f,0f};
        java.awt.image.RescaleOp op = new java.awt.image.RescaleOp(s, o, null);
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = dst.createGraphics();
        bg.drawImage(src,0,0,null);
        bg.dispose();
        return op.filter(dst, null);
    }

    private void drawProgressBar(Graphics2D g2, Tower t, int drawX, int drawY, int dw) {
        int barW = dw;
        int barH = 5;
        int barX = drawX;
        int barY = drawY - 14;
        float progress = t.getUpgradeProgress();

        g2.setColor(new Color(30,30,30,180));
        g2.fillRoundRect(barX-1, barY-1, barW+2, barH+2, 3, 3);

        int filled = (int)(barW * progress);
        if (filled > 0) {
            g2.setColor(Color.getHSBColor(0.15f - progress*0.08f, 0.9f, 1.0f));
            g2.fillRoundRect(barX, barY, filled, barH, 2, 2);
        }

        Stroke os = g2.getStroke();
        g2.setStroke(new BasicStroke(0.5f));
        g2.setColor(new Color(180,180,180,100));
        g2.drawRoundRect(barX, barY, barW, barH, 2, 2);
        g2.setStroke(os);

        g2.setFont(new Font("Arial",Font.BOLD,8));
        g2.setColor(new Color(255,240,160));
        String label = "LV"+(t.getTowerLevel()+1)+"→"+(t.getTowerLevel()+2);
        int tw = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, barX+(barW-tw)/2, barY-1);
    }

    private void drawArcher(Graphics2D g2, Tower t, int drawX, int drawY) {
        if (t.getTowerType() != Constants.Towers.ARCHER) return;

        int lv = Utilz.clamp(t.getTowerLevel(), 0, 6);
        if (!ARCHER_VISIBLE[lv]) return;

        BufferedImage[] frames = TowerAsset.archerAnimations[t.getDirection()][t.getAnimState()];
        if (frames == null || frames.length == 0) return;

        BufferedImage img = frames[t.getAnimIndex() % frames.length];

        int ax = drawX + Constants.Tiles.TILE_SIZE / 2 - ARCHER_W / 4;
        int ay = drawY + ARCHER_TOP_Y[lv] ;

        if (t.isFacingLeft()) {
            g2.drawImage(img, ax + ARCHER_W, ay, -ARCHER_W, ARCHER_H, null);
        } else {
            g2.drawImage(img, ax, ay, ARCHER_W, ARCHER_H, null);
        }
    }

    private void drawSelected(Graphics2D g, Tower t) {
        g.setColor(Color.YELLOW);
        g.drawRect(t.getX(), t.getY(), Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
    }
}
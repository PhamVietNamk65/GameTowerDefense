package ui;

import Manager.TowerManager;
import entity.TowerSlot;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import levels.Level;
import utils.Constants;

/**
 * UI popup khi click vào tower slot trống.
 * Hiển thị 6 nút: Archer, Canon, Flame, Frost, Lightning, Sniper.
 */
public class TowerSlotUI {

    private TowerSlot   selectedSlot;
    private boolean     visible;

    private int x, y;
    private int errorTimer = 0;

    private TowerManager towerManager;

    // ── Layout ────────────────────────────────────────────────────────────────
    private static final int PANEL_W  = 160;
    private static final int BTN_W    = 140;
    private static final int BTN_H    = 30;
    private static final int PADDING  = 10;
    private static final int GAP      = 4;
    private static final int NUM_BTNS = 6;   // ← was 5, now 6
    private static final int PANEL_H  = PADDING * 2 + BTN_H * NUM_BTNS + GAP * (NUM_BTNS - 1);

    // ── Tower costs ───────────────────────────────────────────────────────────
    private static final int COST_ARCHER    = 65;
    private static final int COST_CANON     = 120;
    private static final int COST_FLAME     = 100;
    private static final int COST_FROST     = 110;
    private static final int COST_LIGHTNING = 130;
    private static final int COST_SNIPER    = 140;   // ← NEW

    public TowerSlotUI(TowerManager towerManager) {
        this.towerManager = towerManager;
    }

    // ── Open / Close ──────────────────────────────────────────────────────────
    public void open(TowerSlot slot) {
        this.selectedSlot = slot;
        this.visible      = true;
        this.x = slot.getX() * 64 + 64;
        this.y = slot.getY() * 64;

        if (x + PANEL_W > Constants.SCREEN_WIDTH)  x = slot.getX() * 64 - PANEL_W;
        if (y + PANEL_H > Constants.SCREEN_HEIGHT) y = Constants.SCREEN_HEIGHT - PANEL_H;
    }

    public void close() {
        visible      = false;
        selectedSlot = null;
    }

    // ── Update ────────────────────────────────────────────────────────────────
    public void update(int mouseX, int mouseY, boolean click, Level level) {
        if (!visible) return;

        int bx = x + PADDING;
        for (int i = 0; i < NUM_BTNS; i++) {
            int by = y + PADDING + i * (BTN_H + GAP);
            if (isIn(mouseX, mouseY, bx, by, BTN_W, BTN_H) && click) {
                buildTower(getTowerType(i), level);
                return;
            }
        }
    }

    private int getTowerType(int buttonIndex) {
        return switch (buttonIndex) {
            case 0 -> Constants.Towers.ARCHER;
            case 1 -> Constants.Towers.CANNON;
            case 2 -> Constants.Towers.FLAME;
            case 3 -> Constants.Towers.FROST;
            case 4 -> Constants.Towers.LIGHTNING;
            case 5 -> Constants.Towers.SNIPER;   // ← NEW
            default -> -1;
        };
    }

    private void buildTower(int type, Level level) {
        if (selectedSlot == null || type < 0) return;
        if (towerManager.buildTower(selectedSlot, type)) {
            level.removeSlot(selectedSlot);
            selectedSlot.setOccupied(true);
            close();
        } else {
            errorTimer = 80;
        }
    }

    // ── Render ────────────────────────────────────────────────────────────────
    public void render(Graphics g) {
        if (!visible) return;

        g.setColor(new Color(20, 24, 36, 215));
        g.fillRoundRect(x, y, PANEL_W, PANEL_H, 10, 10);
        g.setColor(new Color(80, 120, 180, 200));
        g.drawRoundRect(x, y, PANEL_W, PANEL_H, 10, 10);

        drawButton(g, 0, "Archer",    "Cost: " + COST_ARCHER,    new Color(50, 90, 50),   new Color(120, 210, 120));
        drawButton(g, 1, "Cannon",    "Cost: " + COST_CANON,     new Color(70, 60, 30),   new Color(255, 190, 80));
        drawButton(g, 2, "Flame",     "Cost: " + COST_FLAME,     new Color(100, 40, 10),  new Color(255, 140, 60));
        drawButton(g, 3, "Frost",     "Cost: " + COST_FROST,     new Color(20, 60, 110),  new Color(140, 210, 255));
        drawButton(g, 4, "Lightning", "Cost: " + COST_LIGHTNING, new Color(60, 60, 10),   new Color(255, 255, 100));
        drawButton(g, 5, "Sniper",    "Cost: " + COST_SNIPER,    new Color(30, 30, 60),   new Color(180, 140, 255)); // ← NEW

        if (errorTimer > 0) {
            g.setColor(new Color(255, 80, 80, Math.min(255, errorTimer * 4)));
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("Not enough gold!", x + 6, y - 6);
            errorTimer--;
        }
    }

    private void drawButton(Graphics g, int index, String name, String cost,
                             Color bg, Color accent) {
        int bx = x + PADDING;
        int by = y + PADDING + index * (BTN_H + GAP);

        g.setColor(bg);
        g.fillRoundRect(bx, by, BTN_W, BTN_H, 6, 6);
        g.setColor(accent);
        g.drawRoundRect(bx, by, BTN_W, BTN_H, 6, 6);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString(name, bx + 8, by + 12);

        g.setColor(accent);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString(cost, bx + 8, by + 24);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private boolean isIn(int mx, int my, int bx, int by, int bw, int bh) {
        return mx >= bx && mx < bx + bw && my >= by && my < by + bh;
    }

    public boolean isVisible()              { return visible; }
    public boolean isInside(int mx, int my) { return isIn(mx, my, x, y, PANEL_W, PANEL_H); }
}
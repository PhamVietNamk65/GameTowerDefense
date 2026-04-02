package ui;

import java.awt.Color;
import java.awt.Graphics;

import Manager.TowerManager;
import entity.TowerSlot;
import levels.Level;

public class TowerSlotUI {

    private TowerSlot selectedSlot;
    private boolean visible;

    private int x, y; // vị trí UI

    private TowerManager towerManager;

    public TowerSlotUI(TowerManager towerManager) {
        this.towerManager = towerManager;
    }

    // ===== GỌI KHI CLICK SLOT =====
    public void open(TowerSlot slot) {
        this.selectedSlot = slot;
        this.visible = true;

        // đặt UI gần slot
        this.x = slot.getX() * 64 + 64;
        this.y = slot.getY() * 64;
    }

    public void close() {
        visible = false;
        selectedSlot = null;
    }

    public void update(int mouseX, int mouseY, boolean click,Level level) {
        if (!visible) return;

        // Button 1: Archer
        if (isIn(mouseX, mouseY, x, y, 120, 30)) {
            if (click) {
                level.removeSlot(selectedSlot);
                buildTower(1);
                
                close();
            }
        }

        // Button 2: Cannon
        if (isIn(mouseX, mouseY, x, y + 40, 120, 30)) {
            if (click) {
                level.removeSlot(selectedSlot);
                buildTower(2);
                
                close();
            }
        }
    }

    private void buildTower(int type) {
        if (selectedSlot == null) return;

        towerManager.buildTower(selectedSlot, type);
        selectedSlot.setOccupied(true);

        close();
    }

    public void render(Graphics g) {
        if (!visible) return;

        // nền UI
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(x, y, 140, 100);

        // button Archer
        g.setColor(Color.WHITE);
        g.drawRect(x, y, 120, 30);
        g.drawString("1. Archer", x + 10, y + 20);

        // button Cannon
        g.drawRect(x, y + 40, 120, 30);
        g.drawString("2. Cannon", x + 10, y + 60);
    }

    private boolean isIn(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w &&
               my >= y && my < y + h;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isInside(int mx, int my) {
        return isIn(mx, my, x, y, 140, 100);
    }
}
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
    private int errorTimer = 0;
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
                buildTower(1,level);
            }
        }
    }

    private void buildTower(int type,Level level) {
        if (selectedSlot == null) return;
        if( towerManager.buildTower(selectedSlot, type) ) {
            level.removeSlot(selectedSlot);
            selectedSlot.setOccupied(true);
            close();
        }else{
            errorTimer = 60;
        }
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

        if (errorTimer > 0) {
            g.setColor(Color.RED);
            g.drawString("Not enough gold!", x, y - 10);
            errorTimer--;
        }   
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

package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import entity.Tower;
import system.TowerActionListener;
import utils.Constants;

public class TowerUI {
    ButtonBar buttonBar;
    private Tower selectedTower;
    TowerActionListener listener;
    public TowerUI(Tower t){
        initButton(t);
    }

    private void initButton(Tower t){
        buttonBar = new ButtonBar(0, 0, 88, 66);
        buttonBar.setOrientation(1, 5);

        MyButton upgrade = new MyButton("Uprade", 88,33);
        upgrade.setAction(()->{
            if (listener != null) {
                listener.onUpgrade(selectedTower);
            }
        });

        MyButton sell = new MyButton("Sell", 88,3);
        sell.setAction(()->{
             if (listener != null)
                listener.onSell(selectedTower);
        });
        buttonBar.addButton(upgrade);
        buttonBar.addButton(sell);

    }
     public void setListener(TowerActionListener l) {
        this.listener = l;
    }

    public void setSelectedTower(Tower t) {
        this.selectedTower = t;

        if (t != null) {
            int bx = t.getX() + 45;
            int by = t.getY();

            buttonBar.setPosition(bx, by);
        }
    }

    public void draw(Graphics2D g ){
        if (selectedTower == null) return;
        buttonBar.draw(g);
        drawSelectedInfo(g, selectedTower);
    }

    private void drawSelectedInfo(Graphics2D g2, Tower t) { //hien thong tin tru
        int x = t.getX() + Constants.Tiles.TILE_SIZE  + 4, y = t.getY() - 52;
        g2.setColor(new Color(0,0,0,150)); g2.fillRect(x,y,140,40);
        g2.setColor(Color.WHITE); g2.setFont(new Font("Arial",Font.PLAIN,12));
        g2.drawString("Lv:"+t.getDisplayLevel()+" Dmg:"+t.getDmg(), x+8, y+15);
        g2.drawString("Range:"+(int)t.getRange(), x+8, y+30);
    }

    public boolean mousePressed(int x, int y) {
        if (selectedTower == null) return false;
        boolean handled = false;

        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y)) {
                b.setMousePressed(true);
                handled = true;
            }
        }
        return handled;
    }

    public boolean mouseReleased(int x, int y) {
        if (selectedTower == null) return false;
        boolean handled = false;

        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y) && b.isMousePressed()) {
                b.execute();
                handled = true;
            }
        b.setMousePressed(false);
        }
        return handled;
    }
}

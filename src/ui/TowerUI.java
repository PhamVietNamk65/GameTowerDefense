package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import entity.Tower;
import utils.Constants;

public class TowerUI {
    int bx, by;
    public TowerUI(Tower t){
        initbutton(Tower t);
    }

    private void drawTowerButtons(Graphics2D g2, Tower t) { //hien nut bam upgrade va sell
        int bx = t.getX() + Constants.Tiles.TILE_SIZE + 4, by = t.getY() - 8;
        g2.setFont(new Font("Arial",Font.BOLD,14));
        if (t.canUpgrade() && !t.isUpgrading()) {
            g2.setColor(Color.YELLOW); g2.fillRect(bx,by,88,30); 
            g2.setColor(Color.BLACK);  g2.drawRect(bx,by,88,30);
            g2.drawString("Upgrade", bx+12, by+20);
        } else if (t.isUpgrading()) {
            g2.setColor(new Color(160,160,80)); g2.fillRect(bx,by,88,30);
            g2.setColor(Color.DARK_GRAY); g2.drawRect(bx,by,88,30);
            g2.drawString("Upgrading...", bx+4, by+20);
        }
        g2.setColor(Color.RED);   g2.fillRect(bx,by+40,88,30);
        g2.setColor(Color.BLACK); g2.drawRect(bx,by+40,88,30);
        g2.drawString("Sell", bx+28, by+60);
    }

    private void drawSelectedInfo(Graphics2D g2, Tower t) { //hien thong tin tru
        int x = t.getX() + Constants.Tiles.TILE_SIZE  + 4, y = t.getY() - 52;
        g2.setColor(new Color(0,0,0,150)); g2.fillRect(x,y,140,40);
        g2.setColor(Color.WHITE); g2.setFont(new Font("Arial",Font.PLAIN,12));
        g2.drawString("Lv:"+t.getDisplayLevel()+" Dmg:"+t.getDmg(), x+8, y+15);
        g2.drawString("Range:"+(int)t.getRange(), x+8, y+30);
    }

    public boolean handleButtonClick(int mx, int my) { //xu ly su kien click chuot;
        Tower t = getSelectedTower();
        if (t==null) return false;
        int bx = t.getX()+ Constants.Tiles.TILE_SIZE + 4, by = t.getY()-8;
        if (t.canUpgrade() && !t.isUpgrading())
            if (mx>=bx && mx<=bx+88 && my>=by && my<=by+30) { upgradeSelectedTower(); return true; }
        if (mx>=bx && mx<=bx+88 && my>=by+40 && my<=by+70) { sellSelectedTower(); return true; }
        return false;
    }
}

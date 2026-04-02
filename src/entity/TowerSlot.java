package entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import asset.TowerAsset;
import utils.Constants;

public class TowerSlot {
    private int x, y;
    private boolean occupied;
    private Rectangle bounds;

    public TowerSlot(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        bounds = new Rectangle(x * Constants.Tiles.TILE_SIZE, y * Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
    }

    public void render(Graphics g) {
        g.drawImage(TowerAsset.placeTower,x * Constants.Tiles.TILE_SIZE , y * Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE,null);
    }

    public boolean isClicked(int mouseX, int mouseY) {
        int px = x * Constants.Tiles.TILE_SIZE ;
        int py = y * Constants.Tiles.TILE_SIZE ;
        return mouseX >= px && mouseX < px + Constants.Tiles.TILE_SIZE &&
               mouseY >= py && mouseY < py + Constants.Tiles.TILE_SIZE ;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setOccupied(boolean b) {
        this.occupied = b;
    }

    public boolean isOccupied() {
       return occupied;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
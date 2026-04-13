package entity.trap;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import asset.WallAsset;
import utils.Constants;

public class Wall {
    private int x, y;

    private int hp, maxHp;
    private int level = 1;
    private int direction;

    private int buildProgress = 0;
    private boolean isBuilding = true;

    private int animTick, animIndex;

    public Wall(int x, int y, int direction) {
        this.x = x;
        this.y = y;

        this.maxHp = Constants.Walls.getStartHP(level);

        this.hp = maxHp;

        this.direction = direction;
    }

    public void update() {

        if (isBuilding) {
            buildProgress++;
            updateAnimation();
            if (buildProgress >= 60) {
                isBuilding = false;
                animIndex = 0;
            }
            return;
        }

        updateAnimation();
    }

    private void updateAnimation() {
        animTick++;
        if (animTick > 10) {
            animTick = 0;
            animIndex++;
        }
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public void render(Graphics g) {

        int tileSize = Constants.Tiles.TILE_SIZE;

        int drawX = x * tileSize;
        int drawY = y * tileSize;

        int drawWidth = tileSize;
        int drawHeight = tileSize;


        if (direction == Constants.Direction.LEFT) {
            drawX += tileSize;
            drawWidth = -tileSize;
        } 

        BufferedImage img;

        if (isBuilding) {
            BufferedImage[] frames = WallAsset.wallBuild.get(level).get(direction);
            img = frames[animIndex % frames.length];
        } 
        else {
            float percent = (float) hp / maxHp;

            BufferedImage[] frames = WallAsset.wallDestroyed.get(level).get(direction);

            if (percent > 0.8f) img = frames[0];
            else if (percent > 0.6f) img = frames[1];
            else if (percent > 0.4f) img = frames[2];
            else if (percent > 0.2f) img = frames[3];
            else img = frames[4];
        }

        g.drawImage(img, drawX, drawY, drawWidth, drawHeight, null);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
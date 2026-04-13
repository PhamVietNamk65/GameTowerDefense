package entity.trap;

import static utils.Constants.Tiles.TILE_SIZE;
import static utils.Constants.Spikes.*;
import static utils.Constants.Monsters.BEE;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import asset.TrapAsset;
import entity.monster.EnemyState;
import entity.monster.Monster;

public class Spikes {

    private int x, y;
    private int level;

    private Rectangle bounds;

    // animation
    private int aniIndex = 0;
    private int aniTick = 0;
    private int aniSpeed = 6;

    // usage
    private int maxUse;
    private int currentUses = 0;
    private boolean active = true;

    private boolean isAttack = false;

    public Spikes(int x, int y, int level) {
        this.x = x;
        this.y = y;
        this.level = level;

        maxUse = getMaxUse(level);
        bounds = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
    }

    public void update(List<Monster> monsters) {
        if (!active) return;

        boolean hasMonster = false;

        for (Monster m : monsters) {
            if (m.getState() == EnemyState.DYING) continue;
            if (m.getEnemyType() == BEE){
                continue;
            }
            if (bounds.intersects(m.getBounds())) {
                hasMonster = true;
                break;
            }
        }

        if (hasMonster) {
            isAttack = true;

            int prevFrame = aniIndex;
            updateAnimation();

            if (aniIndex == 2 && prevFrame != 2) {

                for (Monster m : monsters) {
                    if (m.getState() == EnemyState.DYING) continue;

                    if (bounds.intersects(m.getBounds())) {
                        m.hurt(getDame(level));
                    }
                }

                currentUses++;

                if (currentUses >= maxUse) {
                    active = false;
                }
            }

        } else {
            isAttack = false;
            resetAnimation();
        }
    }

    private void updateAnimation() {
        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            BufferedImage[] frames = TrapAsset.spikes.get(level);

            if (aniIndex >= frames.length) {
                aniIndex = 0;
            }
        }
    }

    private void resetAnimation() {
        aniIndex = 0;
        aniTick = 0;
    }

    public void render(Graphics g) {
        BufferedImage[] frames = TrapAsset.spikes.get(level);

        BufferedImage frame;

        if (isAttack) {
            frame = frames[Math.min(aniIndex, frames.length - 1)];
        } else {
            frame = frames[0]; // nằm dưới đất
        }

        g.drawImage(frame, x, y, TILE_SIZE, TILE_SIZE, null);
    }

    public boolean isActive() {
        return active;
    }

    public int getX() {
        return x / TILE_SIZE;
    }

    public int getY() {
        return y / TILE_SIZE;
    }
}
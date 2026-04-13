package entity.trap;

import static utils.Constants.Tiles.TILE_SIZE;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import asset.TrapAsset;
import entity.monster.EnemyState;
import entity.monster.Monster;

public class Bomb {

    public float x, y;

    private BombState state;

    private long placedTime;
    private long explodeTime;

    private boolean finished = false;

    private int aniIndex = 0;
    private int aniTick = 0;
    private int aniSpeed = 6;

    private int explosionAniIndex = 0;
    private int explosionAniTick = 0;
    private int explosionAniSpeed = 4;

    private long placedDuration = 250; 
    private long countdownDuration = 1000;

    private int damage = 50;
    private int explosionSize = TILE_SIZE * 2;

    public Bomb(float x, float y) {
        this.x = x;
        this.y = y;

        state = BombState.PLACED;
        placedTime = System.currentTimeMillis();
    }

    public void update(List<Monster> monsters) {
        long current = System.currentTimeMillis();

        switch (state) {

            case PLACED:
                updateAnimation();

                if (current - placedTime >= placedDuration) {
                    state = BombState.COUNTDOWN;
                    placedTime = current;
                    resetAnimation();
                }
                break;

            case COUNTDOWN:
                updateAnimation();

                if (current - placedTime >= countdownDuration) {
                    state = BombState.EXPLODED;
                    explodeTime = current;

                    resetAnimation();
                    resetExplosionAnimation();

                    dealDamage(monsters);
                }
                break;

            case EXPLODED:
                updateAnimation();
                updateExplosionAnimation();
                break;
        }
    }

    private void updateAnimation() {
        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            BufferedImage[] frames = getCurrentFrames();

            if (aniIndex >= frames.length) {

                if (state == BombState.EXPLODED) {
                    aniIndex = frames.length - 1;
                } else {
                    aniIndex = 0;
                }
            }
        }
    }

    private void resetAnimation() {
        aniIndex = 0;
        aniTick = 0;
    }

    private BufferedImage[] getCurrentFrames() {
        switch (state) {
            case PLACED:
                return TrapAsset.bombPlaced; 
            case COUNTDOWN:
                return TrapAsset.bombCountDown;
            case EXPLODED:
                return TrapAsset.bombExploded; 
        }
        return TrapAsset.bombPlaced;
    }
    private void updateExplosionAnimation() {
        explosionAniTick++;

        if (explosionAniTick >= explosionAniSpeed) {
            explosionAniTick = 0;
            explosionAniIndex++;

            if (explosionAniIndex >= TrapAsset.effect.length) {
                finished = true;
            }
        }
    }

    private void resetExplosionAnimation() {
        explosionAniIndex = 0;
        explosionAniTick = 0;
    }

    private void dealDamage(List<Monster> monsters) {

        Rectangle explosionArea = new Rectangle(
                (int) x - explosionSize / 2,
                (int) y - explosionSize / 2,
                explosionSize,
                explosionSize
        );

        for (Monster m : monsters) {

            if (m.getState() == EnemyState.DYING) continue;

            if (explosionArea.intersects(m.getBounds())) {
                m.hurt(damage);
            }
        }
    }

    public void render(Graphics g) {

        BufferedImage[] frames = getCurrentFrames();
        BufferedImage frame = frames[Math.min(aniIndex, frames.length - 1)];

        if (state == BombState.COUNTDOWN) {
            int offsetX = (int)(Math.sin(System.currentTimeMillis() * 0.02) * 2);
            g.drawImage(frame, (int)x + offsetX, (int)y, TILE_SIZE, TILE_SIZE, null);

        } else if (state == BombState.EXPLODED) {

            g.drawImage(frame, (int)x, (int)y, TILE_SIZE, TILE_SIZE, null);

            BufferedImage effectFrame =
                    TrapAsset.effect[Math.min(explosionAniIndex, TrapAsset.effect.length - 1)];

            int size = TILE_SIZE * 2;
            int offset = TILE_SIZE / 2;

            g.drawImage(effectFrame,
                    (int)x - offset,
                    (int)y - offset,
                    size,
                    size,
                    null);
        }
        else {

            g.drawImage(frame, (int)x, (int)y, TILE_SIZE, TILE_SIZE, null);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
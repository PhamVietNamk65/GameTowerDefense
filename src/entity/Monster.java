package entity;

import static utils.Constants.Direction.*;
import java.awt.Rectangle;
import java.util.Random;

public class Monster {
    //vi tri va hitbox cua monster
    protected float x, y;
    protected Rectangle bounds;
    //thong so monster
    protected int health;
    protected int maxHealth;
    protected int ID;
    protected int enemyType;
    //trang thai va huong di chuyen
    private EnemyState state = EnemyState.WALK;
    private int direction = DOWN;
    private int pathIndex = 0;
    //offset de tranh trung nhau khi spawn
    protected float xOffset, yOffset;
    //death animation
    protected int deathTick = 0;
    protected boolean deathDone = false;
    protected boolean reachedEnd = false;

    public Monster(float x, float y, int ID, int enemyType) {

        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;

        bounds = new Rectangle((int) x, (int) y, 32, 32);

        setStartHealth();
    }

    private void setStartHealth() {
        health = utils.Constants.Monsters.GetStartHealth(enemyType);
        maxHealth = health;
    }

    public void hurt(int dmg) {
        if (state == EnemyState.DEATH) return;

        health -= dmg;

        if (health <= 0 && state != EnemyState.DEATH) {
        setState(EnemyState.DEATH);
        }
    }

    public void setState(EnemyState newState) {
        if (state == EnemyState.DEATH) return;
            this.state = newState;

            // RESET ANIMATION (QUAN TRỌNG)
            deathTick = 0;
            deathDone = false;
        
    }

    public EnemyState getState() {
        return state;
    }

    public void move(float speed, int dir) {
        if (state == EnemyState.DEATH) return;

        direction = dir;

        switch (dir) {
            case LEFT -> x -= speed;
            case UP -> y -= speed;
            case RIGHT -> x += speed;
            case DOWN -> y += speed;
        }

        updateHitBox();
    }

    public void updateDirection(float dx, float dy) {
        if (state == EnemyState.DEATH) return;

        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? RIGHT : LEFT;
        } else {
            direction = (dy > 0) ? DOWN : UP;
        }
    }

    private void updateHitBox() {
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    public void tickDeath(int totalFrames, int aniSpeed) {
        if (state != EnemyState.DEATH) return;

        deathTick++;

        if (deathTick >= totalFrames * aniSpeed) {
            deathDone = true;
        }
    }

    public boolean isDeathDone() {
        return deathDone;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void nextPath() {
        pathIndex++;
    }

    public void reachEnd() {
        reachedEnd = true;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
        updateHitBox();
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }

    public int getEnemyType() { return enemyType; }
    public int getDirection() { return direction; }

    public float getHealthBarFloat() {
        return health / (float) maxHealth;
    }

    public void createOffset() {
        int maxOffset = 15;
        Random r = new Random();

        xOffset = r.nextInt(maxOffset * 2) - maxOffset;
        yOffset = r.nextInt(maxOffset * 2) - maxOffset;
    }

    public float getxOffset() { return xOffset; }
    public float getyOffset() { return yOffset; }
}
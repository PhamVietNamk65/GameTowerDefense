package entity;

import static helpz.Constants.Direction.*;
import java.awt.Rectangle;

public class Monster {
    protected float x, y;
    protected Rectangle bounds;
    protected int health;
    protected int maxHealth;
    protected int ID;
    protected int enemyType;
    protected int lastDir;
    protected boolean alive = true;

    private int pathIndex = 1;
    private EnemyState state = EnemyState.WALK;
    private int aniIndex = 0;

    // U = up, S = side, D = down
    private String animDir = "D";
    private int deathTick = 0;
    private boolean deathDone = false;
    private boolean reachedEnd = false;

    // true = đang nhìn sang phải, false = đang nhìn sang trái
    private boolean facingRight = false;

    public Monster(float x, float y, int ID, int enemyType) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;
        bounds = new Rectangle((int) x, (int) y, 32, 32);
        lastDir = -1;
        setStartHealth();
    }

    private void setStartHealth() {
        health = helpz.Constants.Monsters.GetStartHealth(enemyType);
        maxHealth = health;
    }

    public void hurt(int dmg) {
        this.health -= dmg;
        if (health <= 0 && alive) {
            alive = false;
            state = EnemyState.DEATH;
            deathTick = 0;
        }
    }

    // cập nhật hướng animation + hướng nhìn
    public void updateAnimDirection(float dx, float dy) {
        if (!alive) return;

        float adx = Math.abs(dx);
        float ady = Math.abs(dy);

        if (adx > ady) {
            animDir = "S";
            facingRight = dx > 0;
        } else if (dy < 0) {
            animDir = "U";
        } else {
            animDir = "D";
        }
    }

    public void tickDeath(int totalFrames, int aniSpeed) {
        if (state != EnemyState.DEATH) return;
        deathTick++;
        if (deathTick >= totalFrames * aniSpeed) {
            deathDone = true;
        }
    }

    public void reachEnd() {
        reachedEnd = true;
        alive = false;
    }

    public void move(float speed, int dir) {
        lastDir = dir;

        switch (dir) {
            case LEFT:
                this.x -= speed;
                facingRight = false;
                break;
            case UP:
                this.y -= speed;
                break;
            case RIGHT:
                this.x += speed;
                facingRight = true;
                break;
            case DOWN:
                this.y += speed;
                break;
        }

        updateHitBox();
    }

    private void updateHitBox() {
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    public EnemyState getState() {
        return state;
    }

    public void setState(EnemyState state) {
        this.state = state;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void nextPath() {
        pathIndex++;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
        updateHitBox();
    }

    public String getAnimDir() {
        return animDir;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isDeathDone() {
        return deathDone;
    }

    public int getDeathTick() {
        return deathTick;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    public float getHealthBarFloat() {
        return health / (float) maxHealth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHealth() {
        return health;
    }

    public int getID() {
        return ID;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getLastDir() {
        return lastDir;
    }

    public boolean IsAlive() {
        return alive;
    }
}
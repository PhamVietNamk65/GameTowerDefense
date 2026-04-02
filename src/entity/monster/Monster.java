package entity.monster;

<<<<<<< HEAD:src/entity/monster/Monster.java
import static utils.Constants.Direction.*;
import static utils.Constants.Monsters.GetReward;

=======
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java
import java.awt.Rectangle;
import java.util.Random;
import static utils.Constants.Direction.*;

public class Monster {
    protected float x, y;
    protected Rectangle bounds;
    protected int health;
    protected int maxHealth;
    protected int ID;
    protected int enemyType;
    private EnemyState state = EnemyState.WALK;
    private int direction = RIGHT;
    private int pathIndex = 0;
    protected float xOffset, yOffset;
    protected boolean reachedEnd = false;
<<<<<<< HEAD:src/entity/monster/Monster.java
    //
    public Monster(float x, float y, int ID, int enemyType) {
=======

    // FIX: mỗi quái tự quản lý animation index riêng
    private int animTick  = 0;
    private int animIndex = 0;
    private static final int ANI_SPEED = 8; // tốc độ animation walk (tăng = chậm hơn)
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java

    // FIX: death animation chạy 1 lần duy nhất, không loop
    private int deathAnimTick  = 0;
    private int deathAnimIndex = 0;
    private boolean deathDone  = false;
    private static final int DEATH_ANI_SPEED = 10; // tốc độ death animation

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

    // FIX: update walk animation tick (gọi từ EnemyManager mỗi frame)
    public void updateAnim() {
        if (state == EnemyState.DEATH) return;
        animTick++;
        if (animTick >= ANI_SPEED) {
            animTick = 0;
            animIndex++;
        }
    }

    // FIX: update death animation, chỉ chạy 1 lần đến frame cuối rồi dừng
    public void updateDeathAnim(int totalFrames) {
        if (state != EnemyState.DEATH || deathDone) return;
        deathAnimTick++;
        if (deathAnimTick >= DEATH_ANI_SPEED) {
            deathAnimTick = 0;
            if (deathAnimIndex < totalFrames - 1) {
                deathAnimIndex++;
            } else {
                deathDone = true; // đã chạy hết frame → đánh dấu xong
            }
        }
    }

    public int getAnimIndex(int totalFrames) {
        if (totalFrames <= 0) return 0;
        return animIndex % totalFrames;
    }

    public int getDeathAnimIndex() {
        return deathAnimIndex;
    }

    public void hurt(int dmg) {
<<<<<<< HEAD:src/entity/monster/Monster.java
        if (state == EnemyState.DYING) return;

        health -= dmg;

        if (health <= 0 && state != EnemyState.DYING) {
            setState(EnemyState.DYING);
            
=======
        if (state == EnemyState.DEATH) return;
        health -= dmg;
        if (health <= 0) {
            setState(EnemyState.DEATH);
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java
        }
    }

    public void setState(EnemyState newState) {
<<<<<<< HEAD:src/entity/monster/Monster.java
        if (state == EnemyState.DYING) return;
            this.state = newState;

            // RESET ANIMATION (QUAN TRỌNG)
            deathTick = 0;
            deathDone = false;
=======
        if (state == EnemyState.DEATH) return;
        this.state = newState;
        // Reset death animation khi bắt đầu chết
        deathAnimTick  = 0;
        deathAnimIndex = 0;
        deathDone      = false;
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java
    }

    public EnemyState getState() { return state; }

    public void move(float speed, int dir) {
<<<<<<< HEAD:src/entity/monster/Monster.java
        if (state == EnemyState.DYING) return;

=======
        if (state == EnemyState.DEATH) return;
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java
        direction = dir;
        switch (dir) {
            case LEFT  -> x -= speed;
            case UP    -> y -= speed;
            case RIGHT -> x += speed;
            case DOWN  -> y += speed;
        }
        updateHitBox();
    }

    public void updateDirection(float dx, float dy) {
<<<<<<< HEAD:src/entity/monster/Monster.java
        if (state == EnemyState.DYING) return;

=======
        if (state == EnemyState.DEATH) return;
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java
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

    // Giữ lại để tương thích với EnemyManager cũ nếu cần
    public void tickDeath(int totalFrames, int aniSpeed) {
<<<<<<< HEAD:src/entity/monster/Monster.java
        if (state != EnemyState.DYING) return;

        deathTick++;

        if (deathTick >= totalFrames * aniSpeed) {
            deathDone = true;
        }
=======
        updateDeathAnim(totalFrames);
>>>>>>> 6ef79cb00a50072b1f2aa9c3154e6643fc87d99d:src/entity/Monster.java
    }

    public boolean isDeathDone() { return deathDone; }

    public int getPathIndex()  { return pathIndex; }
    public void nextPath()     { pathIndex++; }
    public void reachEnd()     { reachedEnd = true; }
    public boolean hasReachedEnd() { return reachedEnd; }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
        updateHitBox();
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getBounds() { return bounds; }
    public int getEnemyType()  { return enemyType; }
    public int getDirection()  { return direction; }
    public float getHealthBarFloat() { return health / (float) maxHealth; }

    public void createOffset() {
        int maxOffset = 15;
        Random r = new Random();
        xOffset = r.nextInt(maxOffset * 2) - maxOffset;
        yOffset = r.nextInt(maxOffset * 2) - maxOffset;
    }

    public float getxOffset() { return xOffset; }
    public float getyOffset() { return yOffset; }

    public int getReward(){
        return GetReward(enemyType);
    }
}
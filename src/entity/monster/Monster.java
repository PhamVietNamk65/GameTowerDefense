package entity.monster;

import entity.trap.Wall;
import java.awt.Rectangle;
import java.util.Random;
import system.EnemyMovement;
import static utils.Constants.Direction.*;
import static utils.Constants.Monsters.*;

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
 
    private int animTick  = 0;
    private int animIndex = 0;
    private static final int ANI_SPEED = 8; 

    private int deathAnimTick  = 0;
    private int deathAnimIndex = 0;
    private boolean deathDone  = false;
    private static final int DEATH_ANI_SPEED = 10;

    private EnemyMovement movement;
    private StatusEffect statusEffect = new StatusEffect();

    private Wall targetWall;
    private int attackCooldown = 0;

    public Monster(float x, float y, int ID, int enemyType) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;
        bounds = new Rectangle((int) x, (int) y, 32, 32);
        setStartHealth();
    }

    private void setStartHealth() {
        health    = utils.Constants.Monsters.GetStartHealth(enemyType);
        maxHealth = health;
    }

    public void updateAnim() {
        if (state == EnemyState.DYING) return;
        // Nếu đang bị stun → không chạy walk animation
        if (statusEffect.isStunned()) return;
        animTick++;
        if (animTick >= ANI_SPEED) {
            animTick = 0;
            animIndex++;
        }
    }

    public void updateDeathAnim(int totalFrames) {
        if (state != EnemyState.DYING || deathDone) return;
        deathAnimTick++;
        if (deathAnimTick >= DEATH_ANI_SPEED) {
            deathAnimTick = 0;
            if (deathAnimIndex < totalFrames - 1) {
                deathAnimIndex++;
            } else {
                deathDone = true; 
            }
        }
    }

    // ── Update mỗi tick ───────────────────────────────────────────────────────

    public void update() {
        if (state == EnemyState.DYING) return;

        // Cập nhật status effect — trả về burn dmg tick này
        int burnDmg = statusEffect.update();
        if (burnDmg > 0) {
            hurt(burnDmg);
        }
    }

    // ── Di chuyển — dừng hoàn toàn khi bị stun ───────────────────────────────

    public void move(float speed, int dir) {
        if (state == EnemyState.DYING) return;

        // Stun → đứng yên
        if (statusEffect.isStunned()) return;

        // Slow → giảm tốc độ
        float actualSpeed = speed * statusEffect.getSpeedMultiplier();

        direction = dir;
        switch (dir) {
            case LEFT  -> x -= actualSpeed;
            case UP    -> y -= actualSpeed;
            case RIGHT -> x += actualSpeed;
            case DOWN  -> y += actualSpeed;
        }
        updateHitBox();
    }

    public void updateDirection(float dx, float dy) {
        if (state == EnemyState.DYING) return;
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? RIGHT : LEFT;
        } else {
            direction = (dy > 0) ? DOWN : UP;
        }
    }

    // ── Damage ────────────────────────────────────────────────────────────────

    public void hurt(int dmg) {
        if (state == EnemyState.DYING) return;
        health -= dmg;
        if (health <= 0) {
            setState(EnemyState.DYING);
        }
    }

    // ── Apply status effects (gọi từ Projectile khi trúng quái) ──────────────

    /** Flame Tower: cháy, mất máu mỗi BURN_TICK_RATE tick */
    public void applyBurn(int dmgPerTick, int durationTicks) {
        statusEffect.applyBurn(dmgPerTick, durationTicks);
    }

    /** Frost Tower: giảm tốc độ còn SLOW_FACTOR% */
    public void applySlow(int durationTicks) {
        statusEffect.applySlow(durationTicks);
    }

    /** Lightning Tower: đứng yên hoàn toàn */
    public void applyStun(int durationTicks) {
        statusEffect.applyStun(durationTicks);
    }

    // ── State ─────────────────────────────────────────────────────────────────

    public void setState(EnemyState newState) {
        if (state == EnemyState.DYING) return;
        this.state     = newState;
        deathAnimTick  = 0;
        deathAnimIndex = 0;
        deathDone      = false;
    }

    public EnemyState getState() { return state; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void updateHitBox() {
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    public void tickDeath(int totalFrames, int aniSpeed) {
        updateDeathAnim(totalFrames);
    }

    public int getAnimIndex(int totalFrames) {
        if (totalFrames <= 0) return 0;
        return animIndex % totalFrames;
    }

    public int getDeathAnimIndex() { return deathAnimIndex; }
    public boolean isDeathDone()   { return deathDone; }

    public int   getPathIndex()        { return pathIndex; }
    public void  nextPath()            { pathIndex++; }
    public void  reachEnd()            { reachedEnd = true; }
    public boolean hasReachedEnd()     { return reachedEnd; }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
        updateHitBox();
    }

    public float     getX()               { return x; }
    public float     getY()               { return y; }
    public Rectangle getBounds()          { return bounds; }
    public int       getEnemyType()       { return enemyType; }
    public int       getDirection()       { return direction; }
    public float     getHealthBarFloat()  { return health / (float) maxHealth; }

    public void createOffset() {
        int maxOffset = 15;
        Random r = new Random();
        xOffset = r.nextInt(maxOffset * 2) - maxOffset;
        yOffset = r.nextInt(maxOffset * 2) - maxOffset;
    }

    public float getxOffset() { return xOffset; }
    public float getyOffset() { return yOffset; }

    public int getReward() { return GetReward(enemyType); }

    public void setMovement(EnemyMovement movement) { this.movement = movement; }
    public EnemyMovement getMovement()              { return movement; }

    public StatusEffect getStatusEffect() { return statusEffect; }

    public void setTargetWall(Wall wall)  { this.targetWall = wall; }
    public Wall getTargetWall()           { return targetWall; }

    public void attackWall() {
        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }
        if (targetWall != null) {
            targetWall.takeDamage(getDame(enemyType));
        }
        attackCooldown = getAttackSpeed(enemyType);
    }

}
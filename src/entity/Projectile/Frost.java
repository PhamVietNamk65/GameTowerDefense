package entity.Projectile;

import entity.monster.EnemyState;
import entity.monster.Monster;
import java.util.ArrayList;

/**
 * Frost orb của Frost Tower.
 * Bay đến target → vỡ → applySlow() lên quái trong splashRadius.
 */
public class Frost {

    public float x, y;
    private float vx, vy, speed = 5.5f, angle;

    private final int   directDmg;
    private final int   slowDuration;
    private final int   splashRadius;
    private final Monster target;

    public enum State { FLYING, BURSTING, DEAD }
    private State state = State.FLYING;

    // Animation
    private int frame = 0, frameTick = 0;
    private static final int ANIM_SPEED   = 6;
    private int burstFrame = 0, burstTick = 0;
    private static final int BURST_FRAMES = 5;
    private static final int BURST_SPEED  = 5;

    private boolean effectApplied = false;

    // ─────────────────────────────────────────────────────────────────────────

    public Frost(float startX, float startY, Monster target,
                 int directDmg, int slowDuration, int splashRadius) {
        this.x = startX; this.y = startY;
        this.target = target;
        this.directDmg   = directDmg;
        this.slowDuration= slowDuration;
        this.splashRadius= splashRadius;

        float dx = (target.getX() + 16) - startX;
        float dy = (target.getY() + 16) - startY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        vx = dist > 0 ? dx / dist * speed : speed;
        vy = dist > 0 ? dy / dist * speed : 0;
        angle = (float) Math.atan2(dy, dx);
    }

    public boolean update(ArrayList<Monster> monsters, int screenW, int screenH) {
        switch (state) {
            case FLYING:   updateFlying(monsters, screenW, screenH); break;
            case BURSTING: updateBursting(monsters); break;
            case DEAD:     return false;
        }
        return state != State.DEAD;
    }

    private void updateFlying(ArrayList<Monster> monsters, int screenW, int screenH) {
        if (target == null || target.getState() == EnemyState.DYING) {
            burst(monsters); return;
        }

        float tx = target.getX() + 16, ty = target.getY() + 16;
        float dx = tx - x, dy = ty - y;

        float desired = (float) Math.atan2(dy, dx);
        float diff = desired - angle;
        while (diff >  Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;
        float turn = 0.14f;
        angle += Math.abs(diff) < turn ? diff : Math.signum(diff) * turn;
        vx = (float)(Math.cos(angle) * speed);
        vy = (float)(Math.sin(angle) * speed);

        if (dx * dx + dy * dy <= 16 * 16) { burst(monsters); return; }
        x += vx; y += vy;

        if (x < -80 || x > screenW + 80 || y < -80 || y > screenH + 80) {
            state = State.DEAD; return;
        }

        frameTick++;
        if (frameTick >= ANIM_SPEED) {
            frameTick = 0;
            frame = (frame + 1) % 6;
        }
    }

    private void burst(ArrayList<Monster> monsters) {
        state = State.BURSTING;
        if (!effectApplied) {
            effectApplied = true;
            float r2 = splashRadius * splashRadius;
            for (Monster m : monsters) {
                if (m.getState() == EnemyState.DYING) continue;
                float dx = (m.getX() + 16) - x;
                float dy = (m.getY() + 16) - y;
                if (dx * dx + dy * dy <= r2) {
                    m.hurt(directDmg);
                    m.getStatusEffect().applySlow(slowDuration);
                }
            }
        }
    }

    private void updateBursting(ArrayList<Monster> monsters) {
        burstTick++;
        if (burstTick >= BURST_SPEED) {
            burstTick = 0;
            burstFrame++;
            if (burstFrame >= BURST_FRAMES) state = State.DEAD;
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public State getState()        { return state;       }
    public boolean isBursting()    { return state == State.BURSTING; }
    public int   getFrame()        { return frame;       }
    public int   getBurstFrame()   { return burstFrame;  }
    public int   getSplashRadius() { return splashRadius;}
    public float getAngle()        { return angle;       }
}
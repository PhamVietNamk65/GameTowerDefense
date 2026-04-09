package entity.Projectile;

import entity.monster.EnemyState;
import entity.monster.Monster;
import java.util.ArrayList;

/**
 * Fireball của Flame Tower.
 * Bay đến target → nổ → applyBurn() lên tất cả quái trong splashRadius.
 */
public class Flame {

    public float x, y;
    private float vx, vy, speed = 5f, angle;

    private final int   directDmg;
    private final int   burnDmg;
    private final int   burnDuration;
    private final int   splashRadius;
    private final Monster target;

    public enum State { FLYING, EXPLODING, DEAD }
    private State state = State.FLYING;

    // Animation
    private int frame = 0, frameTick = 0;
    private static final int ANIM_SPEED = 5;
    private static final int EXPLOSION_FRAMES = 6;
    private int explodeFrame = 0, explodeTick = 0;
    private static final int EXPLODE_SPEED = 4;

    private boolean effectApplied = false;

    // ─────────────────────────────────────────────────────────────────────────

    public Flame(float startX, float startY, Monster target,
                 int directDmg, int burnDmg, int burnDuration, int splashRadius) {
        this.x = startX; this.y = startY;
        this.target = target;
        this.directDmg   = directDmg;
        this.burnDmg     = burnDmg;
        this.burnDuration= burnDuration;
        this.splashRadius= splashRadius;

        float dx = (target.getX() + 16) - startX;
        float dy = (target.getY() + 16) - startY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        vx = dist > 0 ? dx / dist * speed : speed;
        vy = dist > 0 ? dy / dist * speed : 0;
        angle = (float) Math.atan2(dy, dx);
    }

    /** @return false khi DEAD → xoá khỏi list */
    public boolean update(ArrayList<Monster> monsters, int screenW, int screenH) {
        switch (state) {
            case FLYING:    updateFlying(monsters, screenW, screenH); break;
            case EXPLODING: updateExploding(monsters); break;
            case DEAD:      return false;
        }
        return state != State.DEAD;
    }

    private void updateFlying(ArrayList<Monster> monsters, int screenW, int screenH) {
        if (target == null || target.getState() == EnemyState.DYING) {
            explode(monsters); return;
        }

        float tx = target.getX() + 16, ty = target.getY() + 16;
        float dx = tx - x, dy = ty - y;

        // Slight homing
        float desired = (float) Math.atan2(dy, dx);
        float diff = desired - angle;
        while (diff >  Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;
        float turn = 0.15f;
        angle += Math.abs(diff) < turn ? diff : Math.signum(diff) * turn;
        vx = (float)(Math.cos(angle) * speed);
        vy = (float)(Math.sin(angle) * speed);

        if (dx * dx + dy * dy <= 16 * 16) { explode(monsters); return; }
        x += vx; y += vy;

        if (x < -80 || x > screenW + 80 || y < -80 || y > screenH + 80) {
            state = State.DEAD; return;
        }

        // Animate
        frameTick++;
        if (frameTick >= ANIM_SPEED) {
            frameTick = 0;
            frame = (frame + 1) % 4;
        }
    }

    private void explode(ArrayList<Monster> monsters) {
        state = State.EXPLODING;
        if (!effectApplied) {
            effectApplied = true;
            float r2 = splashRadius * splashRadius;
            for (Monster m : monsters) {
                if (m.getState() == EnemyState.DYING) continue;
                float dx = (m.getX() + 16) - x;
                float dy = (m.getY() + 16) - y;
                if (dx * dx + dy * dy <= r2) {
                    m.hurt(directDmg);
                    m.getStatusEffect().applyBurn(burnDmg, burnDuration);
                }
            }
        }
    }

    private void updateExploding(ArrayList<Monster> monsters) {
        explodeTick++;
        if (explodeTick >= EXPLODE_SPEED) {
            explodeTick = 0;
            explodeFrame++;
            if (explodeFrame >= EXPLOSION_FRAMES) state = State.DEAD;
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public State getState()          { return state;        }
    public boolean isExploding()     { return state == State.EXPLODING; }
    public int   getFrame()          { return frame;        }
    public int   getExplodeFrame()   { return explodeFrame; }
    public int   getSplashRadius()   { return splashRadius; }
    public float getAngle()          { return angle;        }
}
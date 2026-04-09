package entity.Projectile;

import entity.monster.EnemyState;
import entity.monster.Monster;
import java.util.ArrayList;

/**
 * Đạn bom của Canon Tower.
 *
 * - Bay theo đường thẳng đến target.
 * - Khi tới target (hoặc mất target) → nổ, gây splash damage bán kính splashRadius.
 * - Có animation bay (bombFrame cycling) và animation nổ (explosionFrame).
 */
public class Bomb {

    // ── Vị trí & vận tốc ─────────────────────────────────────────────────────
    public float x, y;
    private float vx, vy;
    private float speed = 4.5f;

    // ── Damage & splash ───────────────────────────────────────────────────────
    private int   dmg;
    private int   splashRadius;

    // ── Target ────────────────────────────────────────────────────────────────
    private Monster target;

    // ── State ─────────────────────────────────────────────────────────────────
    public enum BombState { FLYING, EXPLODING, DEAD }
    private BombState state = BombState.FLYING;

    // ── Animation ─────────────────────────────────────────────────────────────
    /** Frame của bomb sprite đang bay */
    private int bombFrame  = 0;
    private int bombTick   = 0;
    private static final int BOMB_ANIM_SPEED = 6;   // ticks / frame

    /** Frame của explosion sprite */
    private int explosionFrame = 0;
    private int explosionTick  = 0;
    private static final int EXPLOSION_ANIM_SPEED = 5;

    /** Góc bay (dùng để xoay sprite nếu muốn) */
    public float angle = 0f;

    // ── Danh sách monster bị ảnh hưởng splash (chỉ tính 1 lần) ──────────────
    private boolean splashApplied = false;

    // ─────────────────────────────────────────────────────────────────────────

    public Bomb(float startX, float startY, Monster target, int dmg, int splashRadius) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.dmg    = dmg;
        this.splashRadius = splashRadius;

        float dx = (target.getX() + 16) - startX;
        float dy = (target.getY() + 16) - startY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        this.vx = dist > 0 ? dx / dist * speed : speed;
        this.vy = dist > 0 ? dy / dist * speed : 0;
        this.angle = (float) Math.atan2(dy, dx);
    }

    /**
     * Update mỗi tick.
     * @param monsters tất cả monster đang sống – dùng để tính splash.
     * @param screenW / screenH – giới hạn màn hình.
     * @return true nếu vẫn còn sống (FLYING hoặc EXPLODING).
     */
    public boolean update(ArrayList<Monster> monsters, int screenW, int screenH) {
        switch (state) {
            case FLYING:
                updateFlying(monsters, screenW, screenH);
                break;
            case EXPLODING:
                updateExploding(monsters);
                break;
            case DEAD:
                return false;
        }
        return state != BombState.DEAD;
    }

    // ── Flying update ─────────────────────────────────────────────────────────
    private void updateFlying(ArrayList<Monster> monsters, int screenW, int screenH) {
        // Nếu target chết → nổ tại vị trí hiện tại
        if (target == null || target.getState() == EnemyState.DYING) {
            explode(monsters);
            return;
        }

        // Điều hướng về target
        float tx = target.getX() + 16;
        float ty = target.getY() + 16;
        float dx = tx - x;
        float dy = ty - y;
        float dist2 = dx * dx + dy * dy;

        // Tính lại góc mỗi tick (theo đuổi nhẹ)
        float desiredAngle = (float) Math.atan2(dy, dx);
        float diff = desiredAngle - angle;
        while (diff >  Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;
        float turnSpeed = 0.12f;
        if (Math.abs(diff) < turnSpeed) angle = desiredAngle;
        else angle += Math.signum(diff) * turnSpeed;

        vx = (float)(Math.cos(angle) * speed);
        vy = (float)(Math.sin(angle) * speed);

        // Kiểm tra va chạm – dùng bán kính 14px
        if (dist2 <= 14 * 14) {
            explode(monsters);
            return;
        }

        x += vx;
        y += vy;

        // Ra ngoài màn hình
        if (x < -80 || x > screenW + 80 || y < -80 || y > screenH + 80) {
            state = BombState.DEAD;
            return;
        }

        // Animate bomb sprite
        bombTick++;
        if (bombTick >= BOMB_ANIM_SPEED) {
            bombTick = 0;
            int maxFrames = asset.CanonAsset.bombFrames != null ? asset.CanonAsset.bombFrames.length : 1;
            bombFrame = (bombFrame + 1) % Math.max(1, maxFrames);
        }
    }

    // ── Explode ───────────────────────────────────────────────────────────────
    private void explode(ArrayList<Monster> monsters) {
        state = BombState.EXPLODING;
        explosionFrame = 0;
        explosionTick  = 0;
        applySplash(monsters);
    }

    private void applySplash(ArrayList<Monster> monsters) {
        if (splashApplied) return;
        splashApplied = true;

        float r2 = splashRadius * splashRadius;
        for (Monster m : monsters) {
            if (m.getState() == EnemyState.DYING) continue;
            float dx = (m.getX() + 16) - x;
            float dy = (m.getY() + 16) - y;
            if (dx * dx + dy * dy <= r2) {
                // Giảm dần theo khoảng cách (50% ở rìa)
                float factor = 1f - 0.5f * (float)Math.sqrt(dx * dx + dy * dy) / splashRadius;
                int actualDmg = Math.max(1, (int)(dmg * factor));
                m.hurt(actualDmg);
            }
        }
    }

    // ── Exploding update ──────────────────────────────────────────────────────
    private void updateExploding(ArrayList<Monster> monsters) {
        explosionTick++;
        if (explosionTick >= EXPLOSION_ANIM_SPEED) {
            explosionTick = 0;
            int maxFrames = asset.CanonAsset.explosionFrames != null
                    ? asset.CanonAsset.explosionFrames.length : 1;
            explosionFrame++;
            if (explosionFrame >= maxFrames) {
                state = BombState.DEAD;
            }
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public BombState getState()        { return state; }
    public boolean   isAlive()         { return state != BombState.DEAD; }
    public boolean   isExploding()     { return state == BombState.EXPLODING; }
    public int       getBombFrame()    { return bombFrame; }
    public int       getExplosionFrame() { return explosionFrame; }
    public int       getSplashRadius() { return splashRadius; }
    public float     getAngle()        { return angle; }
}
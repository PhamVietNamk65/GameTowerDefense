package entity.Projectile;

import entity.monster.Monster;

/**
 * Bullet – đạn của SniperTower.
 *
 * Pha bay  : di chuyển về phía target, renderer vẽ Snipe1.png xoay theo hướng.
 * Pha nổ   : đứng yên tại điểm chạm, phát animation Snipe1→Snipe6, rồi đánh dấu done.
 */
public class Bullet {

    // ── vị trí ────────────────────────────────────────────────────────────────
    private float x, y;           // tâm đạn
    private float targetX, targetY;
    private Monster target;

    // ── bay ───────────────────────────────────────────────────────────────────
    private static final float SPEED = 12f;
    private int   dmg;
    private float angle;           // radian, dùng để xoay sprite khi vẽ
    private boolean hit = false;

    // ── nổ ────────────────────────────────────────────────────────────────────
    private int  hitFrame    = 0;
    private int  hitTick     = 0;
    private static final int HIT_SPEED       = 6;   // frames mỗi tick
    private static final int HIT_FRAME_TOTAL = 6;
    private boolean done = false;

    // ── kích thước vẽ (renderer dùng) ─────────────────────────────────────────
    /** Kích thước vẽ đạn đang bay (px) — đủ lớn để nhìn thấy */
    public static final int BULLET_DRAW_W = 20;
    public static final int BULLET_DRAW_H = 32;

    /** Kích thước vẽ explosion */
    public static final int HIT_DRAW_SIZE = 56;

    // ─────────────────────────────────────────────────────────────────────────
    public Bullet(float startX, float startY, Monster target, int dmg) {
        this.x      = startX;
        this.y      = startY;
        this.target = target;
        this.dmg    = dmg;
        refreshTarget();
    }

    // ── update ────────────────────────────────────────────────────────────────
    public void update() {
        if (done) return;

        if (!hit) {
            // Cập nhật vị trí target mỗi frame để tracking bee đang di chuyển
            if (target != null && !target.isDeathDone()) {
                targetX = target.getX() + 16;
                targetY = target.getY() + 16;
            }

            float dx   = targetX - x;
            float dy   = targetY - y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            angle      = (float) Math.atan2(dy, dx);

            if (dist <= SPEED) {
                // Chạm target
                x   = targetX;
                y   = targetY;
                hit = true;
                if (target != null) target.hurt(dmg);
            } else {
                x += (dx / dist) * SPEED;
                y += (dy / dist) * SPEED;
            }
        } else {
            // Chạy animation nổ
            hitTick++;
            if (hitTick >= HIT_SPEED) {
                hitTick = 0;
                hitFrame++;
                if (hitFrame >= HIT_FRAME_TOTAL) {
                    done = true;
                }
            }
        }
    }

    private void refreshTarget() {
        if (target != null) {
            targetX = target.getX() + 16;
            targetY = target.getY() + 16;
        }
        float dx = targetX - x;
        float dy = targetY - y;
        angle = (float) Math.atan2(dy, dx);
    }

    // ── getters ───────────────────────────────────────────────────────────────
    public float   getX()        { return x; }
    public float   getY()        { return y; }
    public float   getAngle()    { return angle; }
    public boolean isHit()       { return hit; }
    public boolean isDone()      { return done; }
    public int     getHitFrame() { return hitFrame; }
}
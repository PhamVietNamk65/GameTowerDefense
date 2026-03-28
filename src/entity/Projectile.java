package entity;

public class Projectile {
    private float x, y;
    private float angle;
    private float vx, vy;
    private boolean alive;
    private int dmg;
    private Monster target;

    private int animIndex, animTick;
    private final int animSpeed = 4;

    private static final float SPEED = 6f;
    private static final float HIT_RADIUS = 12f;
    private static final float TURN_SPEED = 0.18f;

    public Projectile(float x, float y, Monster target, int dmg) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.dmg = dmg;
        this.alive = true;
    }

    public boolean update(int screenW, int screenH) {
        updateMovement();
        updateAnimation();

        if (x < -64 || x > screenW + 64 || y < -64 || y > screenH + 64)
            alive = false;

        return alive;
    }

    private void updateMovement() {
        if (target != null && target.isAlive()) {
            float tx = target.getX() + 16;
            float ty = target.getY() + 16;

            float desiredAngle = (float) Math.atan2(ty - y, tx - x);

            float diff = desiredAngle - angle;
            while (diff > Math.PI) diff -= 2 * Math.PI;
            while (diff < -Math.PI) diff += 2 * Math.PI;

            if (Math.abs(diff) < TURN_SPEED) angle = desiredAngle;
            else angle += Math.signum(diff) * TURN_SPEED;

            vx = (float)(Math.cos(angle) * SPEED);
            vy = (float)(Math.sin(angle) * SPEED);

            float dx = x - tx;
            float dy = y - ty;
            if (dx * dx + dy * dy <= HIT_RADIUS * HIT_RADIUS) {
                target.hurt(dmg);
                alive = false;
            }
        }

        x += vx;
        y += vy;
    }

    private void updateAnimation() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
        }
    }

    // getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getAngle() { return angle; }
    public int getAnimIndex() { return animIndex; }
}
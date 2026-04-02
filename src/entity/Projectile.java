package entity;
import static utils.Constants.Projectile.*;
public class Projectile {
    public float x, y;
    public float vx, vy;
    public float angle;
    public boolean alive;
    public int dmg;
    public Monster target;

    public Projectile(float startX, float startY, Monster target, int dmg) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.dmg = dmg;

        float dx = (target.getX() + 16) - startX;
        float dy = (target.getY() + 16) - startY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        this.vx = dist > 0 ? (dx / dist) * SPEED : 0;
        this.vy = dist > 0 ? (dy / dist) * SPEED : 0;
        this.angle = (float) Math.atan2(dy, dx);
        this.alive = true;
    }

    public boolean update(int screenW, int screenH) {
        if (target == null || target.getState() == EnemyState.DEATH) {
            alive = false;
            return false;
        }

            float tx = target.getX() + 16;
            float ty = target.getY() + 16;
            float desiredAngle = (float) Math.atan2(ty - y, tx - x);

            float diff = desiredAngle - angle;
            while (diff >  Math.PI) diff -= 2 * Math.PI;
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
                return false;
            }

        x += vx;
        y += vy;

        if (x < -64 || x > screenW + 64 || y < -64 || y > screenH + 64)
            alive = false;

        return alive;
    }
}
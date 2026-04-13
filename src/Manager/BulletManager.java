package Manager;

import entity.Projectile.Bullet;
import entity.monster.Monster;
import entity.tower.SniperTower;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * BulletManager
 * Quản lý tất cả Bullet của SniperTower.
 */
public class BulletManager {

    private final ArrayList<Bullet> bullets = new ArrayList<>();

    public void spawnBullet(SniperTower tower, Monster target) {
        float sx = tower.getCenterX();
        float sy = tower.getCenterY();
        bullets.add(new Bullet(sx, sy, target, tower.getDmg()));
    }

    public void update() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (b.isDone()) it.remove();
        }
    }

    public ArrayList<Bullet> getBullets() { return bullets; }
}
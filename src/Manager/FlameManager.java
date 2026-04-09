package Manager;

import entity.Projectile.Flame;
import entity.monster.Monster;
import entity.tower.FlameTower;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Quản lý vòng đời các fireball của Flame Tower.
 */
public class FlameManager {

    private final ArrayList<Flame> flames = new ArrayList<>();

    public void spawnFlame(FlameTower tower, Monster target) {
        if (target == null) return;
        flames.add(new Flame(
                tower.getCenterX(), tower.getCenterY(),
                target,
                tower.getDmg(),
                tower.getBurnDmg(),
                tower.getBurnDuration(),
                tower.getSplashRadius()
        ));
    }

    public void update(ArrayList<Monster> monsters, int screenW, int screenH) {
        Iterator<Flame> it = flames.iterator();
        while (it.hasNext()) {
            if (!it.next().update(monsters, screenW, screenH)) it.remove();
        }
    }

    public ArrayList<Flame> getFlames() { return flames; }
}
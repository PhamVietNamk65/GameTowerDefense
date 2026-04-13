package Manager;

import entity.Projectile.Frost;
import entity.monster.Monster;
import entity.tower.FrostTower;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Quản lý vòng đời các frost orb của Frost Tower.
 */
public class FrostManager {

    private final ArrayList<Frost> frosts = new ArrayList<>();

    public void spawnFrost(FrostTower tower, Monster target) {
        if (target == null) return;
        frosts.add(new Frost(
                tower.getCenterX(), tower.getCenterY(),
                target,
                tower.getDmg(),
                tower.getSlowDuration(),
                tower.getSplashRadius()
        ));
    }

    public void update(ArrayList<Monster> monsters, int screenW, int screenH) {
        Iterator<Frost> it = frosts.iterator();
        while (it.hasNext()) {
            if (!it.next().update(monsters, screenW, screenH)) it.remove();
        }
    }

    public ArrayList<Frost> getFrosts() { return frosts; }
}
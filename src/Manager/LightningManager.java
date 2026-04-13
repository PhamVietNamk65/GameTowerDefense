package Manager;

import entity.Projectile.Lightning;
import entity.monster.Monster;
import entity.tower.LightningTower;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Quản lý các tia sét của Lightning Tower.
 * Lightning không bay — nó được tạo tức thì và tồn tại vài tick để renderer vẽ.
 */
public class LightningManager {

    private final ArrayList<Lightning> lightnings = new ArrayList<>();

    public void spawnLightning(LightningTower tower, Monster target,
                               ArrayList<Monster> allMonsters) {
        if (target == null) return;
        lightnings.add(new Lightning(
                tower.getCenterX(), tower.getCenterY(),
                target, allMonsters,
                tower.getDmg(),
                tower.getStunDuration(),
                tower.getChainTargets(),
                tower.getChainDmgFactor()
        ));
    }

    public void update() {
        Iterator<Lightning> it = lightnings.iterator();
        while (it.hasNext()) {
            if (!it.next().update()) it.remove();
        }
    }

    public ArrayList<Lightning> getLightnings() { return lightnings; }
}
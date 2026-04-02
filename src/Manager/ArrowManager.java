package Manager;

import entity.Arrow;
import entity.Monster;
import entity.Tower;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrowManager {

    private ArrayList<Arrow> arrows = new ArrayList<>();

    public void update(int screenW, int screenH) {
        Iterator<Arrow> it = arrows.iterator();
        while (it.hasNext()) {
            if (!it.next().update(screenW, screenH))
                it.remove();
        }
    }

    public void spawnArrow(Tower t, Monster target) {
        arrows.add(new Arrow(
                t.getArrowSpawnX(),
                t.getArrowSpawnY(),
                target,
                t.getDmg()
        ));
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }
}
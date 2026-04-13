package Manager;

import entity.trap.Bomb;
import entity.monster.Monster;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillBombManager {

    private List<Bomb> bombs = new ArrayList<>();


    public SkillBombManager() {

    }

    public void addBomb(float x, float y) {
        bombs.add(new Bomb(x, y));
    }

    public void update(List<Monster> monsters) {
        Iterator<Bomb> it = bombs.iterator();

        while (it.hasNext()) {
            Bomb b = it.next();
            b.update(monsters);

            if (b.isFinished()) {
                it.remove();
            }
        }
    }

    public void render(Graphics g) {
        for (Bomb b : bombs) {
            b.render(g);
        }
    }
}
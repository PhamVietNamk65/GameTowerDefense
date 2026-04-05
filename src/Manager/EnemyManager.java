package Manager;

import system.EnemyMovement;
import system.EnemySpawner;

import asset.MonsterAsset;
import entity.monster.EnemyState;
import entity.monster.Monster;
import levels.Level;
import levels.LevelState;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class EnemyManager {

    private ArrayList<Monster> monsters = new ArrayList<>();

    private EnemySpawner enemySpawner;
    private LevelState levelState;
    private Level level;

    private int aniSpeed = 20;

    public EnemyManager(Level level, LevelState levelState) {
        this.enemySpawner = new EnemySpawner();
        this.levelState = levelState;
        this.level = level;
    }

    public void update() {
        Iterator<Monster> it = monsters.iterator();

        while (it.hasNext()) {
            Monster m = it.next();

            // ===== REACH END =====
            if (m.hasReachedEnd()) {
                levelState.loseLife(1);
                it.remove();
                continue;
            }

            // ===== DYING =====
            if (m.getState() == EnemyState.DYING) {

                BufferedImage[] df = MonsterAsset.getFrames(
                    m.getEnemyType(),
                    EnemyState.DYING,
                    m.getDirection()
                );

                int totalFrames = (df != null) ? df.length : 1;

                m.tickDeath(totalFrames, aniSpeed);

                if (m.isDeathDone()) {
                    levelState.addGold(m.getReward());
                    it.remove();
                }

            } 
            // ===== ALIVE =====
            else {
                m.getMovement().move(m);
            }
        }
    }

    public void spawnMonster(int type, int pathIndex) {

        Point[] chosenPath = level.getPaths().get(pathIndex);

        Monster m = enemySpawner.spawn(type);

        if (m != null) {

            EnemyMovement movement = new EnemyMovement(chosenPath);
            m.setMovement(movement);

            m.createOffset();

            Point start = movement.getStartPoint();

            m.setPos(
                start.x + m.getxOffset(),
                start.y + m.getyOffset()
            );

            monsters.add(m);
        }
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
}

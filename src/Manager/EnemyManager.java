package Manager;

import entity.Monster;
import entity.EnemyState;
import system.EnemyMovement;
import system.EnemySpawner;
import States.PlayingState;
import asset.MonsterAsset;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class EnemyManager {

    private PlayingState playingState;
    private ArrayList<Monster> monsters = new ArrayList<>();

    private EnemyMovement enemyMovement;
    private EnemySpawner enemySpawner;
    private WaveManager waveManager;

    private int aniSpeed = 20;

    public EnemyManager(PlayingState playingState, Point[] path) {
        this.playingState = playingState;
        this.enemyMovement = new EnemyMovement(path);
        this.enemySpawner = new EnemySpawner(path);
    }

    public void update() {
        Iterator<Monster> it = monsters.iterator();

        while (it.hasNext()) {
            Monster m = it.next();

            if (m.hasReachedEnd()) {
                // playingState.loseLife(1);
                it.remove();
                continue;
            }

            if (m.getState() == EnemyState.DEATH) {

                BufferedImage[] df = MonsterAsset.getFrames(
                    m.getEnemyType(),
                    EnemyState.DEATH,
                    m.getDirection()
                );

                int totalFrames = (df != null) ? df.length : 1;

                m.tickDeath(totalFrames, aniSpeed);

                if (m.isDeathDone())
                    it.remove();

            } 
            // ===== ALIVE =====
            else {
                enemyMovement.move(m);
            }
        }
    }
    
    public void spawnMonster(int type) {
        Monster m = enemySpawner.spawn(type);

        if (m != null) {
            m.createOffset();
            m.setPos(
                m.getX() + m.getxOffset(),
                m.getY() + m.getyOffset()
            );
            monsters.add(m);
        }
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
}
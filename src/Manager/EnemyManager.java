package Manager;

import system.EnemyMovement;
import system.EnemySpawner;
import States.PlayingState;
import asset.MonsterAsset;
import entity.monster.EnemyState;
import entity.monster.Monster;
import levels.LevelState;

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
    private LevelState levelState;

    private int aniSpeed = 20;

    public EnemyManager(PlayingState playingState, Point[] path, LevelState levelState) {
        this.playingState = playingState;
        this.enemyMovement = new EnemyMovement(path);
        this.enemySpawner = new EnemySpawner(path);
        this.levelState = levelState;
    }

    public void update() {
        Iterator<Monster> it = monsters.iterator();

        while (it.hasNext()) {
            Monster m = it.next();

            if (m.hasReachedEnd()) {
                levelState.loseLife(1);
                it.remove();
                continue;
            }

            if (m.getState() == EnemyState.DYING) {

                BufferedImage[] df = MonsterAsset.getFrames(
                    m.getEnemyType(),
                    EnemyState.DYING,
                    m.getDirection()
                );

                int totalFrames = (df != null) ? df.length : 1;

                m.tickDeath(totalFrames, aniSpeed);

                if (m.isDeathDone()){
                    levelState.addGold(m.getReward());
                    it.remove();
                }

                    

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
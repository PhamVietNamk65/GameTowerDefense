package render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Manager.EnemyManager;
import asset.MonsterAsset;

import entity.Monster;
import utils.Constants;
public class EnemyRenderer {

    private EnemyManager enemyManager;
    private MonsterAsset monsterAsset;
    // animation đơn giản cho WALK
    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;

    public EnemyRenderer(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void draw(Graphics g) {
        updateAnimation();

        for (Monster m : enemyManager.getMonsters()) {
            if (m.IsAlive()) {
                drawEnemy(m, g);
                drawHealthBar(m, g);
            }  
        }
    }

    private void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
    }

    private void drawEnemy(Monster m, Graphics g) {
        if( m.getDirection() == Constants.Direction.RIGHT){
            BufferedImage[] frames = MonsterAsset.getFrames(
                m.getEnemyType(),
                m.getState(),
                Constants.Direction.LEFT
            );
            if (frames == null || frames.length == 0)
                return;

            int index = aniIndex % frames.length;

            g.drawImage(frames[index],
                (int) m.getX() + Constants.Monsters.ENEMY_SIZE,
                (int) m.getY(),
                -Constants.Monsters.ENEMY_SIZE, Constants.Monsters.ENEMY_SIZE,
                null);
            }
        else{
            BufferedImage[] frames = MonsterAsset.getFrames(
            m.getEnemyType(),
            m.getState(),
            m.getDirection()
        );

        if (frames == null || frames.length == 0)
            return;

        int index = aniIndex % frames.length;

        g.drawImage(frames[index],
            (int) m.getX(),
            (int) m.getY(),
            Constants.Monsters.ENEMY_SIZE, Constants.Monsters.ENEMY_SIZE,
            null);
        }
        
    }

    private void drawHealthBar(Monster m, Graphics g) {
        int currentWidth = getNewBarWidth(m);

        int barX = (int) m.getX() + (Constants.Monsters.ENEMY_SIZE - Constants.Monsters.HP_BAR_WIDTH) / 2;
        int barY = (int) m.getY() - Constants.Monsters.HP_BAR_Y_OFFSET;

        g.setColor(Color.black);
        g.drawRect(barX - 1, barY - 1, Constants.Monsters.HP_BAR_WIDTH + 1, Constants.Monsters.HP_BAR_HEIGHT + 1);

        g.setColor(Color.red);
        g.fillRect(barX, barY, Constants.Monsters.HP_BAR_WIDTH, Constants.Monsters.HP_BAR_HEIGHT);

        g.setColor(Color.green);
        g.fillRect(barX, barY, currentWidth, Constants.Monsters.HP_BAR_HEIGHT);
    }

    private int getNewBarWidth(Monster m) {
        return (int) (Constants.Monsters.HP_BAR_WIDTH * m.getHealthBarFloat());
    }
}

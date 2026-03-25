package Manager;

import entity.Bee;
import entity.EnemyState;
import entity.Monster;
import entity.Orc;
import entity.Slime;
import entity.Wolf;
import helpz.LoadSave;

import static utils.Constants.Direction.*;
import static utils.Constants.Monsters.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import States.PlayingState;


public class EnemyManager {

    private PlayingState playing;

    // Mỗi loại enemy có 1 mảng frame riêng
    private Map<Integer, Map<EnemyState, BufferedImage[]>> enemyAnimations;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private int HPbarWidth = 20;

    // animation đơn giản cho WALK
    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;
	private static final int ENEMY_SIZE = 32;
	private static final int HP_BAR_WIDTH = 24;
	private static final int HP_BAR_HEIGHT = 4;
	private static final int HP_BAR_Y_OFFSET = 8;

    public EnemyManager(PlayingState playing) {
        this.playing = playing;
        this.enemyAnimations = new HashMap<>();

        addMonster(ORC);
        addMonster(BEE);
        addMonster(SLIME);
        addMonster(WOLF);

        loadEnemyImgs();
    }

    private void loadEnemyImgs() {
    	loadEnemyAnimation(SLIME,
            "enemies/1/U_Walk.png",
            "enemies/1/S_Special.png",
            "enemies/1/S_Death.png");

    	loadEnemyAnimation(ORC,
            "enemies/2/U_Walk.png",
            "enemies/2/D_Attack.png",
            "enemies/2/D_Death.png");

    	loadEnemyAnimation(WOLF,
            "enemies/3/U_Walk.png",
            "enemies/3/U_Attack.png",
            "enemies/3/U_Death.png");

    	loadEnemyAnimation(BEE,
            "enemies/4/U_Walk.png",
            null,
            "enemies/4/U_Death.png");
	}

	private void loadEnemyAnimation(int type, String walkPath, String attackPath, String deathPath) {
    	Map<EnemyState, BufferedImage[]> stateMap = new HashMap<>();

    	stateMap.put(EnemyState.WALK, LoadSave.getSpriteFrames(walkPath, 48, 48));
    	stateMap.put(EnemyState.DEATH, LoadSave.getSpriteFrames(deathPath, 48, 48));

    	if (attackPath != null) {
        	stateMap.put(EnemyState.ATTACK, LoadSave.getSpriteFrames(attackPath, 48, 48));
    	} else {
        	stateMap.put(EnemyState.ATTACK, stateMap.get(EnemyState.WALK));
    	}

    	enemyAnimations.put(type, stateMap);
	}

    public void update() {

    	for (Monster m : monsters) {

        	if (m.IsAlive()) {
            	moveMonsterAlongPath(m);
        	}

    	}

    	updateAnimationTick();
	}

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
    }

    public void updateMonsterMove(Monster m) {
        if (m.getLastDir() == -1)
            setNewDirectionAndMove(m);

        int newX = (int) (m.getX() + getSpeedAndWidth(m.getLastDir(), m.getEnemyType()));
        int newY = (int) (m.getY() + getSpeedAndHeight(m.getLastDir(), m.getEnemyType()));

        // if (getTileType(newX, newY) == ROAD_TILE) {
        //     m.move(GetSpeed(m.getEnemyType()), m.getLastDir());
        // } else if (isAtEnd(m)) {
        //     System.out.println("Lives Lost!");
        // } else {
        //     setNewDirectionAndMove(m);
        // }
    }

    private void setNewDirectionAndMove(Monster m) {
        int dir = m.getLastDir();

        int xCord = (int) (m.getX() / 32);
        int yCord = (int) (m.getY() / 32);

        fixMonsterOffsetTile(m, dir, xCord, yCord);

        // if (dir == LEFT || dir == RIGHT) {
        //     int newY = (int) (m.getY() + getSpeedAndHeight(UP, m.getEnemyType()));
        //     if (getTileType((int) m.getX(), newY) == ROAD_TILE)
        //         m.move(GetSpeed(m.getEnemyType()), UP);
        //     else
        //         m.move(GetSpeed(m.getEnemyType()), DOWN);
        // } else {
        //     int newX = (int) (m.getX() + getSpeedAndWidth(RIGHT, m.getEnemyType()));
        //     if (getTileType(newX, (int) m.getY()) == ROAD_TILE)
        //         m.move(GetSpeed(m.getEnemyType()), RIGHT);
        //     else
        //         m.move(GetSpeed(m.getEnemyType()), LEFT);
        // }
    }

    private void fixMonsterOffsetTile(Monster e, int dir, int xCord, int yCord) {
        switch (dir) {
            case RIGHT:
                if (xCord < 19)
                    xCord++;
                break;
            case DOWN:
                if (yCord < 19)
                    yCord++;
                break;
        }

        e.setPos(xCord * 32, yCord * 32);
    }

    private float getSpeedAndHeight(int dir, int enemyType) {
        if (dir == UP)
            return -GetSpeed(enemyType);
        else if (dir == DOWN)
            return GetSpeed(enemyType) + 32;

        return 0;
    }

    private float getSpeedAndWidth(int dir, int enemyType) {
        if (dir == LEFT)
            return -GetSpeed(enemyType);
        else if (dir == RIGHT)
            return GetSpeed(enemyType) + 32;

        return 0;
    }

	private Point[] levelPath = {
        new Point(32, 100),
        new Point(200, 100),
        new Point(200, 200),
        new Point(400, 200),
        new Point(400, 350),
        new Point(700, 350)
	};

	private void moveMonsterAlongPath(Monster m) {

    	if (m.getPathIndex() >= levelPath.length){
			m.setState(EnemyState.ATTACK);
        	return;
		}

    	Point target = levelPath[m.getPathIndex()];

    	float speed = GetSpeed(m.getEnemyType());

    	float dx = target.x - m.getX();
    	float dy = target.y - m.getY();

    	float distance = (float)Math.sqrt(dx * dx + dy * dy);

    	if(distance < speed){
        	m.setPos(target.x, target.y);
        	m.nextPath();
        	return;
    	}

    	float moveX = (dx / distance) * speed;
    	float moveY = (dy / distance) * speed;

    	m.setPos((int)(m.getX() + moveX), (int)(m.getY() + moveY));
	}

    public void addMonster(int monsterType) {
        int x = levelPath[0].x;
    	int y = levelPath[0].y;


        switch (monsterType) {
            case ORC:
                monsters.add(new Orc(x*3, y*3, 0));
                break;
            case BEE:
                monsters.add(new Bee(x, y, 0));
                break;
            case SLIME:
                monsters.add(new Slime(x*2, y*2, 0));
                break;
            case WOLF:
                monsters.add(new Wolf(x*9, y*9, 0));
                break;
        }
    }

    public void draw(Graphics g) {
        for (Monster m : monsters) {
            if (m.IsAlive()) {
                drawEnemy(m, g);
                drawHealthBar(m, g);
            }
        }
    }

    private void drawHealthBar(Monster m, Graphics g) {
    int currentWidth = getNewBarWidth(m);

    int barX = (int) m.getX() + (ENEMY_SIZE - HP_BAR_WIDTH) / 2;
    int barY = (int) m.getY() - HP_BAR_Y_OFFSET;

    g.setColor(Color.black);
    g.drawRect(barX - 1, barY - 1, HP_BAR_WIDTH + 1, HP_BAR_HEIGHT + 1);

    g.setColor(Color.red);
    g.fillRect(barX, barY, HP_BAR_WIDTH, HP_BAR_HEIGHT);

    g.setColor(Color.green);
    g.fillRect(barX, barY, currentWidth, HP_BAR_HEIGHT);
}

private int getNewBarWidth(Monster m) {
    return (int) (HP_BAR_WIDTH * m.getHealthBarFloat());
}

private void drawEnemy(Monster m, Graphics g) {
    Map<EnemyState, BufferedImage[]> stateMap = enemyAnimations.get(m.getEnemyType());
    if (stateMap == null) return;

    EnemyState state = m.getState();
    BufferedImage[] frames = stateMap.get(state);

    if (frames == null || frames.length == 0) {
        frames = stateMap.get(EnemyState.WALK);
    }

    if (frames == null || frames.length == 0) return;

    int index = aniIndex % frames.length;

    g.drawImage(frames[index], (int) m.getX(), (int) m.getY(), ENEMY_SIZE, ENEMY_SIZE, null);
}


}

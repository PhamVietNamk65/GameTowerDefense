package Manager;

import entity.Bee;
import entity.Monster;
import entity.Orc;
import entity.Slime;
import entity.Wolf;
import system.EnemyMovement;
import system.EnemySpawner;


import static utils.Constants.Monsters.*;

import java.awt.Point;

import java.util.ArrayList;

import States.PlayingState;


public class EnemyManager {

    private PlayingState playingState;

    private ArrayList<Monster> monsters = new ArrayList<>();
	private EnemyMovement enemyMovement;
    private EnemySpawner enemySpawner;
    private WaveManager waveManager;
    public EnemyManager(PlayingState playingState,EnemyMovement enemyMovement,Point[] path) {
        this.playingState = playingState;
        this.enemyMovement = enemyMovement;
        enemySpawner = new EnemySpawner(path);
        waveManager = new WaveManager(this);
    }

    public void update() {
        waveManager.update();
        java.util.Iterator<Monster> it = monsters.iterator();

        while (it.hasNext()) {
            Monster m = it.next();

            if (m.hasReachedEnd()) {
                it.remove();
                continue;
            }

        	if (m.isAlive()) {
            	enemyMovement.move(m);
        	}

    	}

	}

    public void addMonster(int monsterType) {
        // Lấy điểm bắt đầu từ Path
        Point start = enemyMovement.getStartPoint();
        float startX = (float) start.x;
        float startY = (float) start.y;

        Monster m = null;

        //Khởi tạo đúng loại Monster
        switch (monsterType) {
            case ORC:   m = new Orc(startX, startY, 0);   break;
            case BEE:   m = new Bee(startX, startY, 0);   break;
            case SLIME: m = new Slime(startX, startY, 0); break;
            case WOLF:  m = new Wolf(startX, startY, 0);  break;
        }

        if (m != null) {
            m.createOffset(); 
        
            //Áp dụng offset vào vị trí xuất phát để chúng không đè lên nhau ngay từ đầu
            m.setPos(startX + m.getxOffset(), startY + m.getyOffset()); 

            monsters.add(m);
        }
    }

    public ArrayList<Monster> getMonsters(){
        return monsters;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }
}

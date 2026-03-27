package Manager;

import entity.Bee;
import entity.EnemyState;
import entity.Monster;
import entity.Orc;
import entity.Slime;
import entity.Wolf;
import helpz.LoadSave;
import system.EnemyMovement;
import system.EnemySpawner;

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

    private PlayingState playingState;

    private ArrayList<Monster> monsters = new ArrayList<>();
	private EnemyMovement enemyMovement;
    private EnemySpawner enemySpawner;

    public EnemyManager(PlayingState playingState,EnemyMovement enemyMovement,Point[] path) {
        this.playingState = playingState;
        this.enemyMovement = enemyMovement;
        enemySpawner = new EnemySpawner(path);
        addMonster(ORC);
        addMonster(BEE);
        addMonster(SLIME);
        addMonster(WOLF);

    }

    public void update() {

    	for (Monster m : monsters) {

        	if (m.IsAlive()) {
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
}

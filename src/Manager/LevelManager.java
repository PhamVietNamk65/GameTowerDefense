package Manager;

import java.awt.Graphics;
import java.util.ArrayList;

import asset.MapType;
import entity.monster.EnemyState;
import entity.monster.Monster;
import helpz.LoadLevelData;
import levels.Level;
import levels.LevelData;

public class LevelManager {

    private Level currentLevel;
    private LevelData currentLevelData;
    private int currentLevelId;

    public LevelManager(int levelId){
        loadLevel(levelId);
    }

    public void loadLevel(int id){
        this.currentLevelId = id;
        if( id > 3){
            currentLevel = new Level(id,MapType.SNOW);
        }
        else currentLevel = new Level(id, MapType.BASIC);
        currentLevelData = LoadLevelData.loadLevelData(id);
    }

    public void nextLevel(){
        loadLevel(currentLevelId + 1);
    }

    public void restartLevel(){
        loadLevel(currentLevelId);
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }

    public LevelData getCurrentLevelData() {
        return currentLevelData;
    }
    
    public void update(ArrayList<Monster> monsters){
        currentLevel.update(monsters);
    }

    public void render(Graphics g){
        currentLevel.render(g);
    }
}
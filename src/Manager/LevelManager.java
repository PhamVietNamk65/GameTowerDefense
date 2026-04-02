package Manager;

import java.awt.Graphics;

import helpz.LoadLevelData;
import levels.Level;
import levels.LevelData;
public class LevelManager {

    private Level currentLevel;
    private LevelData currentLevelData;
    public LevelManager(int levelId){
        loadLevel(levelId);
    }

    public void loadLevel(int id){
        currentLevel = new Level(id);
        currentLevelData = LoadLevelData.LoadLevelData(id);
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }

    public LevelData getCurrentLevelData() {
        return currentLevelData;
    }
    
    public void update(){
        currentLevel.update();
    }

    public void render(Graphics g){
        currentLevel.render(g);
    }

    
}
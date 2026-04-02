package Manager;

import java.awt.Graphics;

import levels.Level;

public class LevelManager {

    private Level currentLevel;

    public LevelManager(int levelId){
        loadLevel(levelId);
    }

    public void loadLevel(int id){
        currentLevel = new Level(id);
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }
    public void update(){
        currentLevel.update();
    }

    public void render(Graphics g){
        currentLevel.render(g);
    }
}
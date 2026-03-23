package levels;

import java.awt.Graphics;

public class LevelManager {

    private Level currentLevel;

    public LevelManager(int levelId){
        loadLevel(levelId);
    }

    public void loadLevel(int id){
        currentLevel = new Level(id);
    }

    public void update(){
        currentLevel.update();
    }

    public void render(Graphics g){
        currentLevel.render(g);
    }
}
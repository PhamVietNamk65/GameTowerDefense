package levels;

import java.awt.Graphics;

public class Level {

    private int[][] map;
    private int levelId;

    public Level(int levelId){
        this.levelId = levelId;
        loadLevel();
    }

    private void loadLevel(){
        String path = "assets/levels/level" + levelId + ".csv";
        map = LoadLevel.loadLevelCSV(path);
    }

    public void update(){}

    public void render(Graphics g){

        for(int row = 0; row < map.length; row++){
            for(int col = 0; col < map[row].length; col++){

                int tile = map[row][col]; 

            }
        }
    }
}
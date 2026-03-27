package levels;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;

import asset.MapAsset;
import system.PathFinder;

public class Level {
    private int levelid;
    private int[][] map;
    private Point[] path;
    
    public Level(int levelid){
        this.levelid = levelid;
        loadLevel();
    }
    private void loadLevel() {
        map = LoadLevel.loadLevel("res/Map/level" + levelid +".csv");
        path = PathFinder.buildPath(map);
    }
    public void update() {
       
    }
    public void render(Graphics g) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {

                int tile = map[y][x];

                if (tile == 0) {
                    g.drawImage(MapAsset.tiles[0], x * 32 * 2, y * 32 * 2, 32 * 2,32 * 2, null);
                } else if (tile == 1) {
                    g.drawImage(MapAsset.tiles[1], x * 32 * 2, y * 32 * 2,32 * 2, 32 * 2, null);
                }
            }
        }
    }
    
    public Point[] getPath() {
        return path;
    }
}

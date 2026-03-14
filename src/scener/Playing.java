package scener;

import java.awt.Graphics;
import java.util.Random;

import Manager.AssetManager;
import Manager.TileManager;
import helpz.LevelBuild;
import main.GamePanel;

public class Playing extends GameScene implements SceneMethods{
    private GamePanel gamePanel;
    private int[][] lvl;
    private TileManager tileManager;

    public Playing(GamePanel gamePanel, AssetManager assetManager) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        lvl = LevelBuild.getLevelData();
        tileManager = new TileManager();
    }

    @Override
    public void render(Graphics g) {

        for(int y = 0; y < lvl.length; y++){
            for(int x = 0; x < lvl[y].length; x++){
                int id = lvl[y][x];
                g.drawImage(tileManager.getSprite(id), x*gamePanel.tileSize, y*gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize, null);
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y) {
        
    }

    @Override
    public void mouseMoved(int x, int y) {
        
    }

    @Override
    public void mousePressed(int x, int y) {
        
    }

    @Override
    public void mouseReleased(int x, int y) {
     
    }

}

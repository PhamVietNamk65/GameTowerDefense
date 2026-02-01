package scener;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import main.GamePanel;

public class Level extends GameScene implements SceneMethods{
    private GamePanel gamePanel;
    private Random random;

    public Level(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.random = new Random();
    }

    @Override
    public void render(Graphics g) {
            for(int x = 0; x < gamePanel.maxScreenCol; x++ ){
            for(int y = 0; y < gamePanel.maxScreenRow; y++ ){
                g.setColor(getRndColor());
                g.fillRect(x*gamePanel.tileSize, y*gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
            }
        }
    }
    private Color getRndColor(){
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r,g,b);
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

package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
    
public class Render {
    private GamePanel gamePanel;
    private Random random;
    
    public Render( GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.random = new Random();
    }

    public void render(Graphics g){
        switch(GameStates.gameStates) {
            case MENU:
                for(int x = 0; x < gamePanel.maxScreenCol; x++ ){
            for(int y = 0; y < gamePanel.maxScreenRow; y++ ){
                g.setColor(getRndColor());
                g.fillRect(x*gamePanel.tileSize, y*gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
            }
        }
                break;
            case PLAYING:
                
                break;
            case SETTING:
                
                break;
            default:
                break;
        }
    }
    
    private Color getRndColor(){
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r,g,b);
    }
}

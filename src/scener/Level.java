package scener;

import java.awt.Color;
import java.awt.Graphics;
import main.GamePanel;
import main.GameStates;

public class Level extends GameScene implements SceneMethods{
    private GamePanel gamePanel;
 
    public Level(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

    }

    public void update(){
        
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    }

    @Override
    public void mouseClicked(int x, int y) {
        //if(level1Button.contains(x,y))
        GameStates.SetGameState(GameStates.PLAYING);

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

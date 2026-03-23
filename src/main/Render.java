package main;

import java.awt.Graphics;

import States.GameState;
import States.GameStateManager;

    
public class Render {
    private GamePanel gamePanel;
    GameStateManager gameStateManager;
    public Render( GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.gameStateManager = gamePanel.getGameStateManager();
    }

    public void render(Graphics g){
        gameStateManager.getCurrentState().render(g);
    }
    
}

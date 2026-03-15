package main;

import java.awt.Graphics;

import scener.GameScene;
    
public class Render {
    private GamePanel gamePanel;
    private GameScene gameScene;

    public Render( GamePanel gamePanel, GameScene gameScene){
        this.gamePanel = gamePanel;
        this.gameScene = gameScene;
    }

    public void render(Graphics g){
        switch(gameScene.getCurrentState()){ 
            case MENU:
                gamePanel.getMenu().render(g);
                break;
            case LEVEL:
                gamePanel.getLevel().render(g);
                break;
            case SETTING:
                gamePanel.getSetting().render(g);
                break;
            case PLAYING:
                gamePanel.getPlaying().render(g);
                break;
            default:
                break;
        }
    }
    
}

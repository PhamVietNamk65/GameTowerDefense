package main;

import java.awt.Graphics;

    
public class Render {
    private GamePanel gamePanel;

    public Render( GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void render(Graphics g){
        switch(GameStates.getGameStates()){ 
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

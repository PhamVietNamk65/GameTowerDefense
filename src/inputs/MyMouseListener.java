package inputs;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.GamePanel;
import main.GameStates;

public class MyMouseListener implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MyMouseListener(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameStates.gameStates) {
                case MENU:
                    gamePanel.getMenu().mouseMoved(e.getX(), e.getY());
                    break;
                case PLAYING:

                    break;
                case SETTING:

                    break;
                default:
                    break;
            }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            
            switch (GameStates.gameStates) {
                case MENU:
                    gamePanel.getMenu().mouseClicked(e.getX(), e.getY());
                    break;
                case PLAYING:

                    break;
                case SETTING:

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameStates.gameStates) {
                case MENU:
                    gamePanel.getMenu().mousePressed(e.getX(), e.getY());
                    break;
                case PLAYING:

                    break;
                case SETTING:

                    break;
                default:
                    break;
            }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameStates.gameStates) {
                case MENU:
                    gamePanel.getMenu().mouseReleased(e.getX(), e.getY());
                    break;
                case PLAYING:

                    break;
                case SETTING:

                    break;
                default:
                    break;
            }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}

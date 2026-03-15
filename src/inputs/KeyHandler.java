package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import main.GameStates;

public class KeyHandler implements KeyListener {

    private GamePanel game;

    public KeyHandler(GamePanel game){
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        switch(e.getKeyCode()){

            case KeyEvent.VK_W:
                game.getGameScene().setCurrentState(GameStates.LEVEL);
                break;

            case KeyEvent.VK_ESCAPE:
                game.getGameScene().setCurrentState(GameStates.MENU);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
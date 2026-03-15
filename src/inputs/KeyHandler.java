package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;
import main.GameStates;

import static main.GameStates.*;

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
                GameStates.setGameStates(PLAYING);
                break;

            case KeyEvent.VK_ESCAPE:
                GameStates.setGameStates(MENU);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
package inputs;

import static main.GameStates.PLAYING;
import static main.GameStates.gameStates;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    @Override
    public void keyTyped(KeyEvent e) {
     
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getExtendedKeyCode() == KeyEvent.VK_W){
            gameStates.SetGameState(PLAYING);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
}

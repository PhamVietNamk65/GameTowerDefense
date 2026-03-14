//Quan ly man hinh hien thi ( man hinh menu hay playing hay setting )
package scener;

import main.GamePanel;
import main.GameStates;


public class GameScene {
    private GamePanel gamePanel; 
    private GameStates currentState;
    public GameScene(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        currentState = GameStates.MENU;
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }

    public GameStates getCurrentState(){
        return currentState;
    }

    public void setCurrentState(GameStates currentState){
        this.currentState = currentState;
    }
}

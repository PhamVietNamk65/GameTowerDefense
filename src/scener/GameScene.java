//Quan ly man hinh hien thi ( man hinh menu hay playing hay setting )
package scener;

import main.GamePanel;

public class GameScene {
    private GamePanel gamePanel; 


    public GameScene(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }
}

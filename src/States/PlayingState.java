package States;
import java.awt.Graphics;

import Manager.EnemyManager;
import Manager.TowerManager;
import entity.Tower;
import levels.LevelManager;
import main.GamePanel;

public class PlayingState implements GameState {

    private int level;
    private GamePanel gamePanel;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    public PlayingState(GamePanel gamePanel,int level){
        this.level = level;
        this.gamePanel = gamePanel;

        levelManager = new LevelManager(level);
        towerManager = new TowerManager();
        enemyManager = new EnemyManager(this);
    }
    @Override
    public void update() {
        levelManager.update();
        towerManager.update();
        enemyManager.update();
    }
    @Override
    public void render(Graphics g) {
        levelManager.render(g);
    }
    @Override
    public void mousePressed(int x, int y) {
        
    }
    @Override
    public void mouseReleased(int x, int y) {
        
    }
    @Override
    public void mouseMoved(int x, int y) {
       
    }

}
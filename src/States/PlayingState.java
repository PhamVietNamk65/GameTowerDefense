package States;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import Manager.EnemyManager;
import Manager.LevelManager;
import Manager.TowerManager;
import entity.Tower;
import main.GamePanel;
import render.EnemyRenderer;
import render.TowerRenderer;
import system.EnemyMovement;
import system.EnemySpawner;
import system.TowerActionListener;
import system.TowerUpdater;
import ui.TowerUI;

public class PlayingState implements GameState {

    private int level;
    private GamePanel gamePanel;

    private LevelManager levelManager;

    private TowerManager towerManager;
    private TowerUpdater towerUpdater;
   
    private TowerRenderer towerRenderer;
    private Tower selectedTower;

    private TowerUI towerUI;

    private EnemyManager enemyManager;
    private EnemyMovement enemyMovement;
    private EnemyRenderer enemyRenderer;
    public PlayingState(GamePanel gamePanel,int level){
        this.level = level;
        this.gamePanel = gamePanel;

        levelManager = new LevelManager(level);
        towerManager = new TowerManager();
        towerUpdater = new TowerUpdater();
        towerRenderer = new TowerRenderer(towerManager);
       
        towerUI = new TowerUI(selectedTower);
        towerUI.setListener(new TowerActionListener() {

            @Override
            public void onUpgrade(Tower t) {
                if( t == null ) return;
                else t.upgrade();
            }

            @Override
            public void onSell(Tower t) {
                if( t == null ) return;
                else { 
                    towerManager.removeTower(t);
                    selectedTower = null;
                    towerUI.setSelectedTower(null);
                }
            }
            
        });
        Point[] path = levelManager.getCurrentLevel().getPath();
        enemyMovement = new EnemyMovement(path);
        enemyManager = new EnemyManager(this,enemyMovement,path);
        enemyRenderer = new EnemyRenderer(enemyManager);
    }

    @Override
    public void update() {
        levelManager.update();
        towerUpdater.update(towerManager.getTowers());
        enemyManager.update();
    }
    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        levelManager.render(g2);
        towerRenderer.draw(g2);
        towerUI.draw(g2);

        enemyRenderer.draw(g2);
    }

    @Override
    public void mousePressed(int x, int y) {
        if( towerUI.mousePressed(x, y) ) return;
        
    }
    @Override
    public void mouseReleased(int x, int y) {
        if (towerUI.mouseReleased(x, y)) return;
        Tower t = towerManager.getTowerAt(x, y);

        if (t != null) {
            selectedTower = t;
            towerUI.setSelectedTower(t);
        } else {
            selectedTower = null;
            towerUI.setSelectedTower(null);
        }

    }

    @Override
    public void mouseMoved(int x, int y) {
       
    }

}
package scener;

import Manager.EnemyManager;
import Manager.TowerManager;
import helpz.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import main.GamePanel;

public class Playing extends GameScene implements SceneMethods {

    private GamePanel gamePanel;
    private EnemyManager enemyManager;
    private TowerManager towerManager;

    private final int TILE_SIZE = 32;

    private final int PLACE_BTN_X = 20;
    private final int PLACE_BTN_Y = 40;
    private final int PLACE_BTN_W = 120;
    private final int PLACE_BTN_H = 30;

    public Playing(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        enemyManager = new EnemyManager(this);
        towerManager = new TowerManager();
    }

    public void update() {
        enemyManager.update();
        towerManager.update();
    }

    @Override
    public void render(Graphics g) {
        enemyManager.draw(g);
        towerManager.draw(g);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 14));

        if (towerManager.isPlacingTower()) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.CYAN);
        }

        g.fillRect(PLACE_BTN_X, PLACE_BTN_Y, PLACE_BTN_W, PLACE_BTN_H);
        g.setColor(Color.BLACK);
        g.drawRect(PLACE_BTN_X, PLACE_BTN_Y, PLACE_BTN_W, PLACE_BTN_H);
        g.drawString("Place Archer", PLACE_BTN_X + 18, PLACE_BTN_Y + 20);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (isInPlaceButton(x, y)) {
            towerManager.startPlacingTower(Constants.Towers.ARCHER);
            return;
        }

        if (towerManager.handleButtonClick(x, y)) {
            return;
        }

        if (towerManager.isPlacingTower()) {
            int tileX = x / TILE_SIZE;
            int tileY = y / TILE_SIZE;

            if (towerManager.canPlaceTower(tileX, tileY)) {
                towerManager.placeTower(tileX, tileY);
            }
            return;
        }

        towerManager.selectTowerAt(x, y);
    }

    private boolean isInPlaceButton(int x, int y) {
        return x >= PLACE_BTN_X && x <= PLACE_BTN_X + PLACE_BTN_W
                && y >= PLACE_BTN_Y && y <= PLACE_BTN_Y + PLACE_BTN_H;
    }

    @Override
    public void mouseMoved(int x, int y) {
    }

    @Override
    public void mousePressed(int x, int y) {
    }

    @Override
    public void mouseReleased(int x, int y) {
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public TowerManager getTowerManager() {
        return towerManager;
    }
}
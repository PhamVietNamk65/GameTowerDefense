package scener;

import Manager.EnemyManager;
import Manager.TowerManager;
import Manager.WaveManager;
import helpz.Constants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import main.GamePanel;

public class Playing extends GameScene implements SceneMethods {

    private GamePanel gamePanel;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private WaveManager waveManager;

    private int lives = 20;
    private boolean gameOver = false;

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
        waveManager = new WaveManager(enemyManager);
    }

    public void update() {
        if (gameOver) return;

        waveManager.update();
        enemyManager.update();
        towerManager.update(enemyManager.getMonsters());
    }

    public void loseLife(int amount) {
        lives -= amount;

        if (lives <= 0) {
            lives = 0;
            gameOver = true;
            System.out.println("GAME OVER!");
        }
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public void render(Graphics g) {
        enemyManager.drawPath(g);
        enemyManager.draw(g);
        towerManager.draw(g);
        drawUI(g);

        if (gameOver) {
            drawGameOver(g);
        }
    }

    private void drawUI(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(new Font("Arial", Font.BOLD, 14));

        // Nút đặt tower - giảm viền đen, bo góc nhẹ hơn
        g2.setColor(towerManager.isPlacingTower()
                ? new Color(80, 220, 120)
                : new Color(40, 220, 235));
        g2.fillRoundRect(PLACE_BTN_X, PLACE_BTN_Y, PLACE_BTN_W, PLACE_BTN_H, 10, 10);

        g2.setColor(new Color(20, 20, 20, 120));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(PLACE_BTN_X, PLACE_BTN_Y, PLACE_BTN_W, PLACE_BTN_H, 10, 10);

        g2.setColor(Color.BLACK);
        g2.drawString("Place Archer", PLACE_BTN_X + 18, PLACE_BTN_Y + 20);

        // Lives panel - nền xám trong hơn, không đen đặc
        g2.setColor(new Color(45, 45, 45, 120));
        g2.fillRoundRect(PLACE_BTN_X, 80, 130, 25, 8, 8);
        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(PLACE_BTN_X, 80, 130, 25, 8, 8);

        g2.setColor(lives > 5 ? new Color(0, 255, 0) : new Color(255, 80, 80));
        g2.drawString("Lives: " + lives, PLACE_BTN_X + 8, 97);

        // Wave panel - cũng giảm nền đen
        g2.setColor(new Color(45, 45, 45, 120));
        g2.fillRoundRect(PLACE_BTN_X, 110, 160, 25, 8, 8);
        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(PLACE_BTN_X, 110, 160, 25, 8, 8);

        g2.setColor(Color.WHITE);

        if (waveManager.isWavesDone()) {
            g2.drawString("All waves done!", PLACE_BTN_X + 8, 127);
        } else if (waveManager.isWaitingNext()) {
            int secs = waveManager.getSecondsUntilNext(60);
            g2.drawString("Next wave in: " + secs + "s", PLACE_BTN_X + 8, 127);
        } else {
            g2.drawString("Wave: " + waveManager.getCurrentWave()
                    + "/" + waveManager.getTotalWaves(), PLACE_BTN_X + 8, 127);
        }
    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String msg = "GAME OVER";
        int w = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (gamePanel.getWidth() - w) / 2, gamePanel.getHeight() / 2);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String sub = "All lives lost!";
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (gamePanel.getWidth() - sw) / 2, gamePanel.getHeight() / 2 + 40);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (gameOver) return;

        if (isInPlaceButton(x, y)) {
            towerManager.startPlacingTower(Constants.Towers.ARCHER);
            return;
        }

        if (towerManager.handleButtonClick(x, y)) return;

        if (towerManager.isPlacingTower()) {
            int tileX = x / TILE_SIZE;
            int tileY = y / TILE_SIZE;

            // chặn đặt tower lên đường
            if (isTileOnPath(tileX, tileY)) {
                System.out.println("Không thể đặt tower lên đường đi!");
                return;
            }

            if (towerManager.canPlaceTower(tileX, tileY)) {
                towerManager.placeTower(tileX, tileY);
            }
            return;
        }

        towerManager.selectTowerAt(x, y);
    }

    private boolean isTileOnPath(int tileX, int tileY) {
        Point[] path = enemyManager.getLevelPath();
        if (path == null || path.length < 2) return false;

        int tileLeft = tileX * TILE_SIZE;
        int tileTop = tileY * TILE_SIZE;
        int tileRight = tileLeft + TILE_SIZE;
        int tileBottom = tileTop + TILE_SIZE;

        int pathHalfWidth = 11; // gần bằng nửa độ dày đường đang vẽ

        for (int i = 0; i < path.length - 1; i++) {
            Point p1 = path[i];
            Point p2 = path[i + 1];

            int minX = Math.min(p1.x, p2.x) - pathHalfWidth;
            int maxX = Math.max(p1.x, p2.x) + pathHalfWidth;
            int minY = Math.min(p1.y, p2.y) - pathHalfWidth;
            int maxY = Math.max(p1.y, p2.y) + pathHalfWidth;

            boolean intersects =
                    tileRight > minX &&
                    tileLeft < maxX &&
                    tileBottom > minY &&
                    tileTop < maxY;

            if (intersects) {
                return true;
            }
        }

        return false;
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

    public WaveManager getWaveManager() {
        return waveManager;
    }
}
package States;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import Manager.ArrowManager;
import Manager.EnemyManager;
import Manager.LevelManager;
import Manager.TowerManager;
import entity.Tower;
import entity.TowerSlot;
import main.GamePanel;
import render.ArrowRenderer;
import render.EnemyRenderer;
import render.TowerRenderer;
import system.EnemyMovement;
import system.EnemySpawner;
import system.TowerActionListener;
import system.TowerUpdater;
import ui.TowerSlotUI;
import ui.TowerUI;

public class PlayingState implements GameState {

    private int level;
    private GamePanel gamePanel;

    private LevelManager levelManager;

    private TowerManager towerManager;
    private TowerUpdater towerUpdater;
    private TowerRenderer towerRenderer;
    private Tower selectedTower;
    private ArrowManager arrowManager ;
    private ArrowRenderer arrowRenderer;
    private TowerSlotUI slotUI;
    private TowerUI towerUI;

    private EnemyManager enemyManager;
    private EnemyMovement enemyMovement;
    private EnemyRenderer enemyRenderer;

    private int mouseX, mouseY;
    public PlayingState(GamePanel gamePanel,int level){
        this.level = level;
        this.gamePanel = gamePanel;

        levelManager = new LevelManager(level);
        towerManager = new TowerManager();
        towerUpdater = new TowerUpdater();
        towerRenderer = new TowerRenderer(towerManager);
        arrowManager = new ArrowManager();
        arrowRenderer = new ArrowRenderer();

        towerUI = new TowerUI(selectedTower);
        slotUI = new TowerSlotUI(towerManager);

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
                    levelManager.getCurrentLevel().addBackSlot(t.getX() / 64, t.getY() / 64);
                    TowerSlot slot = levelManager.getCurrentLevel().getSlotAt(t.getX(), t.getY());
                    slot.setOccupied(false); // <--- QUAN TRỌNG: Mở khóa ô đất để xây lại
                    

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
        towerManager.update(enemyManager.getMonsters());
        towerManager.getArrowManager().update(gamePanel.screenWidth, gamePanel.screenHeight);
    }
    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        levelManager.render(g2);
        towerRenderer.draw(g2);
        enemyRenderer.draw(g2);
        arrowRenderer.render(g2,towerManager.getArrowManager().getArrows());

        towerUI.draw(g2);
        slotUI.render(g2);
    }

    @Override
    public void mousePressed(int x, int y) {
        // ===== Nếu đang mở TowerUI (upgrade/sell) =====
        if (towerUI.mousePressed(x, y)) {
            return;
        }
        // ===== Nếu đang mở TowerSlotUI =====
        if (slotUI.isVisible()) {
            if (slotUI.isInside(x, y)) {
                slotUI.update(x, y, true,levelManager.getCurrentLevel());
                return;
            } else {
                slotUI.close();
            }
        }

        // ===== Click vào Tower =====
        Tower t = towerManager.getTowerAt(x, y);
        if (t != null) {
            selectedTower = t;
            towerUI.setSelectedTower(t);
            return;
        }

        // ===== Click vào TowerSlot =====
        TowerSlot slot = levelManager.getCurrentLevel().getSlotAt(x, y);

        if (slot != null && !slot.isOccupied()) {
            slotUI.open(slot);
            return;
        }

        // =====  Click ra ngoài → bỏ chọn tower =====
        selectedTower = null;
        towerUI.setSelectedTower(null);
    }

    @Override
    public void mouseReleased(int x, int y) {
        // ===== 1. Nếu đang mở TowerSlotUI → không xử lý gì thêm =====
        if (slotUI.isVisible()) {
            return;
        }

        // ===== 2. Xử lý UI của Tower (upgrade / sell) =====
        if (towerUI.mouseReleased(x, y)) {
            return;
        }

        // ===== 3. Click chọn Tower =====
        Tower t = towerManager.getTowerAt(x, y);

        if (t != null) {
            selectedTower = t;
            towerUI.setSelectedTower(t);
        } else {
            // ===== 4. Click ra ngoài → bỏ chọn =====
            selectedTower = null;
            towerUI.setSelectedTower(null);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        mouseX = x;
        mouseY = y;
    }
}
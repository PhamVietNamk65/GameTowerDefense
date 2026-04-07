package states;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.runtime.SwitchBootstraps;

import Manager.ArrowManager;
import Manager.EnemyManager;
import Manager.LevelManager;
import Manager.ProgressManager;
import Manager.TowerManager;
import Manager.WaveManager;
import entity.TowerSlot;
import entity.tower.Tower;
import levels.LevelState;
import listeners.TowerActionListener;
import main.GamePanel;
import render.ArrowRenderer;
import render.EnemyRenderer;
import render.TowerRenderer;
import system.EnemyMovement;
import system.TowerUpdater;
import ui.GameUI;
import ui.MenuLost;
import ui.MenuPause;
import ui.MenuWin;
import ui.TowerSlotUI;
import ui.TowerUI;
import utils.Constants;

public class PlayingState implements GameState {

    private int winDelay = 120;

    public int level;
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

    private WaveManager waveManager;

    private LevelState levelState;

    private GameUI gameUI;
    private int mouseX, mouseY;

    private MenuPause menuPause = new MenuPause();
    private MenuWin menuWin = new MenuWin();
    private MenuLost menuLost = new MenuLost();
    
    private PlayingStatus currentStatus = PlayingStatus.PLAYING;

    private ProgressManager progressManager;

    public PlayingState(GamePanel gamePanel,int level){

        this.level = level;
        this.gamePanel = gamePanel;

        levelManager = new LevelManager(level);
        levelState = new LevelState(levelManager.getCurrentLevelData());

        arrowManager = new ArrowManager();

        towerManager = new TowerManager(levelState,arrowManager);
        towerUpdater = new TowerUpdater();
        towerRenderer = new TowerRenderer(towerManager);
        arrowRenderer = new ArrowRenderer();

        towerUI = new TowerUI(selectedTower, levelState);
        slotUI = new TowerSlotUI(towerManager);
        progressManager = gamePanel.getProgressManager();

        towerUI.setListener(new TowerActionListener() {

            @Override
            public void onUpgrade(Tower t) {
                if( t == null ) return;
                else{
                    t.upgrade();
                    levelState.spendGold(t.getCost()); 
                }
            }

            @Override
            public void onSell(Tower t) {
                if( t == null ) return;
                else { 
                    levelManager.getCurrentLevel().addBackSlot(t.getX() / 64, t.getY() / 64);
                    TowerSlot slot = levelManager.getCurrentLevel().getSlotAt(t.getX(), t.getY());
                    slot.setOccupied(false); // <--- QUAN TRỌNG: Mở khóa ô đất để xây lại
                    

                    towerManager.removeTower(t);
                    levelState.addGold(selectedTower.getSellValue());

                    selectedTower = null;
                    towerUI.setSelectedTower(null);
                }
            }
            
        });

        menuWin.setOnNextLevel(() -> {
            gamePanel.getGameStateManager().setState(new PlayingState(gamePanel, level + 1));
        });

        menuWin.setOnReplay(() -> {
            gamePanel.getGameStateManager().setState(new PlayingState(gamePanel, level));
        });

        menuWin.setOnExit(() -> {
            gamePanel.getGameStateManager().setState(new LevelSelectState(gamePanel));
        });

        menuPause.setOnResume(() -> {
            currentStatus = PlayingStatus.PLAYING;
        });

        menuPause.setOnRestart(() -> {
            gamePanel.getGameStateManager().setState(new PlayingState(gamePanel, level));
        });

        menuPause.setOnExit(() -> {
            gamePanel.getGameStateManager().setState(new LevelSelectState(gamePanel));
        });

        menuLost.setOnReplay(() -> {
            gamePanel.getGameStateManager().setState(new PlayingState(gamePanel, level));
        });

        menuLost.setOnExit(() -> {
            gamePanel.getGameStateManager().setState(new LevelSelectState(gamePanel));
        });
        enemyManager = new EnemyManager(levelManager.getCurrentLevel(),levelState);
        enemyRenderer = new EnemyRenderer(enemyManager);

        waveManager = new WaveManager(enemyManager,levelManager);
        gameUI = new GameUI(levelState, waveManager);

        gameUI.setGameListener(()->{
            currentStatus = PlayingStatus.PAUSE;
        });
    
    }

    @Override
    public void update() {
        if( currentStatus == PlayingStatus.PLAYING){
            levelManager.update();
            towerUpdater.update(towerManager.getTowers());
            enemyManager.update();
            waveManager.update();
            towerUI.update();
            towerManager.update(enemyManager.getMonsters());
            towerManager.getArrowManager().update(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

            if(waveManager.isWavesDone() && enemyManager.getMonsters().isEmpty()){
                winDelay--;
                if (winDelay <= 0) {
                    currentStatus = PlayingStatus.WIN;
                }
            }

            if(levelState.getLives() <= 0){
                currentStatus = PlayingStatus.LOST;
            }
        }
        
        if(currentStatus == PlayingStatus.WIN){
            menuWin.update();
            progressManager.unlockNextLevel(level);
        }

    }

    @Override
    public void render(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        levelManager.render(g2);
        enemyRenderer.draw(g2);
        towerRenderer.draw(g2);
        arrowRenderer.render(g2,towerManager.getArrowManager().getArrows());

        slotUI.render(g2);
        towerUI.draw(g2);

        switch (currentStatus) {
            case PLAYING:
                gameUI.render(g);
                break;
            case PAUSE:
                menuPause.render(g);
                break;
            case WIN:
                menuWin.render(g);
                break;
            case LOST:
                menuLost.render(g);
                break;
            default:
                break;
        }

        
    }

    @Override
    public void mousePressed(int x, int y) {
        if (currentStatus == PlayingStatus.PAUSE) {
            menuPause.mousePressed(x, y);
            return;
        }
        else if (currentStatus == PlayingStatus.WIN) {
            menuWin.mousePressed(x, y);
            return;
        }
        else if (currentStatus == PlayingStatus.LOST) {
            menuLost.mousePressed(x, y);
            return;
        }

        gameUI.mousePressed(x, y);

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
        if (currentStatus == PlayingStatus.PAUSE) {
            menuPause.mouseReleased(x, y);
        return;
        }else if (currentStatus == PlayingStatus.WIN) {
            menuWin.mouseReleased(x, y);
            return;
        }else   if( currentStatus == PlayingStatus.LOST) {
            menuLost.mouseReleased(x, y);
            return;
        }

        gameUI.mouseReleased(x, y);

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
        gameUI.mouseMoved(x, y);
        if (currentStatus == PlayingStatus.PAUSE) {
            menuPause.mouseMoved(x, y);
        return;
        }else if (currentStatus == PlayingStatus.WIN) {
            menuWin.mouseMoved(x, y);
            return;
        }else   if( currentStatus == PlayingStatus.LOST) {
            menuLost.mouseMoved(x, y);
            return;
        }
    }
}
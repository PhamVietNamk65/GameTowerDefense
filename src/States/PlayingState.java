package states;
import Manager.ArrowManager;
import Manager.BombManager;
import Manager.EnemyManager;
import Manager.LevelManager;
import Manager.ProgressManager;
import Manager.TowerManager;
import Manager.WaveManager;
import asset.TrapAsset;
import entity.TowerSlot;
import entity.monster.Monster;
import entity.tower.Tower;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import levels.LevelState;
import listeners.GameListener;
import listeners.TowerActionListener;
import main.GamePanel;
import render.ArrowRenderer;
import render.BombRenderer;
import render.BulletRenderer;
import render.CanonRenderer;
import render.EnemyRenderer;
import render.SniperRenderer;
import render.TowerRenderer;
import render.WirzardRenderer;
import system.EnemyMovement;
import system.TowerUpdater;
import ui.GameUI;
import ui.MenuLost;
import ui.MenuPause;
import ui.MenuWin;
import ui.TowerSlotUI;
import ui.TowerUI;
import utils.Constants;
import static utils.Constants.Tiles.*;

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
    private CanonRenderer canonRenderer;   
    private BombRenderer    bombRender;
    private WirzardRenderer wirzardRenderer;
    private SniperRenderer  sniperRenderer;   // ← NEW
    private BulletRenderer  bulletRenderer;   // ← NEW

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

    private boolean isPlacingWall = false;
    private boolean isPlacingBomb = false;
    private boolean isPlacingSpikes = false;

    private BombManager bombManager;
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
        canonRenderer = new CanonRenderer();   
        bombRender    = new BombRenderer();
        wirzardRenderer = new WirzardRenderer();
        sniperRenderer  = new SniperRenderer();    // ← NEW
        bulletRenderer  = new BulletRenderer();    // ← NEW

        towerUI = new TowerUI(selectedTower, levelState);
        slotUI = new TowerSlotUI(towerManager);
        progressManager = gamePanel.getProgressManager();
        bombManager = new BombManager();

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
                    slot.setOccupied(false);
                    

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
        gameUI = new GameUI(levelState, waveManager,levelManager);

        gameUI.setGameListener(new GameListener(){
                @Override
                public void onPause() {
                    currentStatus = PlayingStatus.PAUSE;
                }

                @Override
                public void onBuildWall() {
                    isPlacingWall = true;
                }
                @Override
                public void placeBomb(){
                    isPlacingBomb = true;
                }

                @Override
                public void onBuildSpikes() {
                    isPlacingSpikes = true;
                }
                });
                
    
    }

    @Override
    public void update() {
        if( currentStatus == PlayingStatus.PLAYING){
            towerUpdater.update(towerManager.getTowers());
            enemyManager.update();
            levelManager.update(enemyManager.getMonsters());
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
        bombManager.update(enemyManager.getMonsters());
        gameUI.update();
    }

    @Override
    public void render(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        levelManager.render(g2);
        enemyRenderer.draw(g2);
        towerRenderer.draw(g2);
        arrowRenderer.render(g2,towerManager.getArrowManager().getArrows());
        canonRenderer.draw(g2,towerManager.getTowers(),towerManager.getSelectedTower());      
        bombRender.render(g2,towerManager.getBombManager().getBombs());
        wirzardRenderer.drawTowers(g2, towerManager.getTowers(), towerManager.getSelectedTower());
        wirzardRenderer.drawProjectiles(g2, towerManager.getFlameManager(), 
        towerManager.getFrostManager(), towerManager.getLightningManager());
        wirzardRenderer.drawStatusEffects(g2, enemyManager.getMonsters());

        // ── Sniper + Bullet (NEW) ───────────────────────────────────────────
        sniperRenderer.draw(g2, towerManager.getTowers(), towerManager.getSelectedTower());
        bulletRenderer.render(g2, towerManager.getBulletManager().getBullets());

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
        drawBuildWall(g);
        drawBuilBomb(g);
        drawBuilSpikes(g);
        bombManager.render(g);
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
        if (isPlacingWall) {

            int tileX = mouseX / TILE_SIZE;
            int tileY = mouseY / TILE_SIZE;

            if (levelManager.getCurrentLevel().canBuildWall(tileX, tileY)) { 
                levelManager.getCurrentLevel().buildWall(tileX, tileY);
                isPlacingWall = false;
            } else {
                
            }
        }
        if (isPlacingBomb) {

            int tileX = mouseX / TILE_SIZE;
            int tileY = mouseY / TILE_SIZE;

            int drawX = tileX * TILE_SIZE;
            int drawY = tileY * TILE_SIZE;

            if (levelManager.getCurrentLevel().canBuildWall(tileX, tileY)) { 
                bombManager.addBomb(drawX,drawY);
                isPlacingBomb = false;
            } else {
                
            }
        }
        if (isPlacingSpikes) {

            int tileX = mouseX / TILE_SIZE;
            int tileY = mouseY / TILE_SIZE;

            if (levelManager.getCurrentLevel().canBuildSpikes(tileX, tileY)) { 
                levelManager.getCurrentLevel().buildSpikes(tileX, tileY);
                isPlacingSpikes = false;
            } else {
                
            }
        }
        gameUI.mousePressed(x, y);

        if (towerUI.mousePressed(x, y)) {
            return;
        }

        if (slotUI.isVisible()) {
            if (slotUI.isInside(x, y)) {
                slotUI.update(x, y, true,levelManager.getCurrentLevel());
                return;
            } else {
                slotUI.close();
            }
        }

        Tower t = towerManager.getTowerAt(x, y);
        if (t != null) {
            selectedTower = t;
            towerUI.setSelectedTower(t);
            return;
        }

        TowerSlot slot = levelManager.getCurrentLevel().getSlotAt(x, y);

        if (slot != null && !slot.isOccupied()) {
            slotUI.open(slot);
            return;
        }

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


        if (slotUI.isVisible()) {
            return;
        }

        if (towerUI.mouseReleased(x, y)) {
            return;
        }

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

    private void drawBuildWall(Graphics g){
        if (isPlacingWall) {

            int tileX = mouseX / Constants.Tiles.TILE_SIZE;
            int tileY = mouseY / Constants.Tiles.TILE_SIZE;

            int drawX = tileX * TILE_SIZE;
            int drawY = tileY * TILE_SIZE;

            boolean canBuild = levelManager.getCurrentLevel().canBuildWall(tileX, tileY);

            Graphics2D g2 = (Graphics2D) g;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            if (canBuild) {
                g.drawImage(TrapAsset.wallBuild.get(levelManager.getCurrentLevel().getLevelWall()).get(1)[1], drawX, drawY, TILE_SIZE, TILE_SIZE, null);
            } else {
                g.drawImage(TrapAsset.wallBuild.get(levelManager.getCurrentLevel().getLevelWall()).get(1)[1], drawX, drawY, TILE_SIZE, TILE_SIZE,null);
                g.setColor(new Color(255, 0, 0, 100));
                g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    private void drawBuilBomb(Graphics g){
        if (isPlacingBomb) {

            int tileX = mouseX / Constants.Tiles.TILE_SIZE;
            int tileY = mouseY / Constants.Tiles.TILE_SIZE;

            int drawX = tileX * TILE_SIZE;
            int drawY = tileY * TILE_SIZE;

            boolean canBuild = levelManager.getCurrentLevel().canBuildWall(tileX, tileY);

            Graphics2D g2 = (Graphics2D) g;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            if (canBuild) {
                g.drawImage(TrapAsset.bombPlaced[3], drawX, drawY, TILE_SIZE, TILE_SIZE, null);
            } else {
                g.drawImage(TrapAsset.bombPlaced[3], drawX, drawY, TILE_SIZE, TILE_SIZE,null);
                g.setColor(new Color(255, 0, 0, 100));
                g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    private void drawBuilSpikes(Graphics g) {
        if (isPlacingSpikes) {

            int tileX = mouseX / Constants.Tiles.TILE_SIZE;
            int tileY = mouseY / Constants.Tiles.TILE_SIZE;

            int drawX = tileX * TILE_SIZE;
            int drawY = tileY * TILE_SIZE;

            boolean canBuild = levelManager.getCurrentLevel().canBuildSpikes(tileX, tileY);

            Graphics2D g2 = (Graphics2D) g;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            if (canBuild) {
                g.drawImage(TrapAsset.spikes.get(levelManager.getCurrentLevel().getLevelSpikes())[2], drawX, drawY, TILE_SIZE, TILE_SIZE, null);
            } else {
                g.drawImage(TrapAsset.spikes.get(levelManager.getCurrentLevel().getLevelSpikes())[2], drawX, drawY, TILE_SIZE, TILE_SIZE,null);
                g.setColor(new Color(255, 0, 0, 100));
                g.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }


}
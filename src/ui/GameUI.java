package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Manager.LevelManager;
import Manager.WaveManager;
import asset.TrapAsset;
import asset.UIAsset;
import levels.LevelState;
import listeners.GameListener;
import listeners.PauseListener;
import utils.Constants;

public class GameUI {
    private LevelState levelState;
    private WaveManager waveManager;
    private LevelManager levelManager;

    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;
    private MyButton buttonPause;
    
    private ButtonBar skillBar;
    private MyButton buttonupgade;
    private GameListener gameListener;
    public GameUI(LevelState levelState, WaveManager waveManager, LevelManager levelManager) {
        this.levelState = levelState;
        this.waveManager = waveManager;
        this.levelManager = levelManager;
        initbutton();
    }
    
    private void initbutton(){
        buttonPause = new MyButton(UIAsset.PauseIcon[0], UIAsset.PauseIcon[1], UIAsset.PauseIcon[2], Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        buttonPause.setButton(19 * Constants.Tiles.TILE_SIZE - 32, 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        buttonPause.setAction(()->{
            if (gameListener != null) {
                gameListener.onPause();
            }
        });
        skillBar = new ButtonBar(15 * Constants.Tiles.TILE_SIZE - 32, 1 * Constants.Tiles.TILE_SIZE - 32, 4 * Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        skillBar.setOrientation(1, 10);

        MyButton buttonBuildWall = new MyButton(TrapAsset.wallBuild.get(levelManager.getCurrentLevel().getLevelWall()).get(2)[2], TrapAsset.wallBuild.get(levelManager.getCurrentLevel().getLevelWall()).get(2)[2],TrapAsset.wallBuild.get(levelManager.getCurrentLevel().getLevelWall()).get(2)[2], 64, 64);
        buttonBuildWall.setButton(18 * Constants.Tiles.TILE_SIZE - 32, 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        buttonBuildWall.setAction(()->{
            if (gameListener != null) {
                gameListener.onBuildWall();
            }
        });

        MyButton buttonBuildBomd = new MyButton(TrapAsset.bombPlaced[3], TrapAsset.bombPlaced[3],TrapAsset.bombPlaced[3], 64, 64);
        buttonBuildBomd.setButton(19 * Constants.Tiles.TILE_SIZE - 32, 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
            buttonBuildBomd.setAction(()->{
            if (gameListener != null) {
                gameListener.placeBomb();
            }
        });

        MyButton buttonBuildSpikes = new MyButton(TrapAsset.spikes.get(levelManager.getCurrentLevel().getLevelSpikes())[3],TrapAsset.spikes.get(levelManager.getCurrentLevel().getLevelSpikes())[3],TrapAsset.spikes.get(levelManager.getCurrentLevel().getLevelSpikes())[3], 64, 64);
        buttonBuildSpikes.setButton(17 * Constants.Tiles.TILE_SIZE - 32, 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
            buttonBuildSpikes.setAction(()->{
            if (gameListener != null) {
                gameListener.onBuildSpikes();
            }
        });
        buttonupgade = new MyButton("Upgade: ", 50, 50);
        buttonupgade.setButton(13 * Constants.Tiles.TILE_SIZE - 30, Constants.Tiles.TILE_SIZE - 10, 100, 50);
        buttonupgade.setAction(()->{
            if (gameListener != null && levelState.getGold() >= Constants.Walls.getCostUpgrade(levelManager.getCurrentLevel().getLevelWall()) && levelManager.getCurrentLevel().getLevelWall() < 4 ){
                gameListener.onUpgradeSkill();
            }
        });
        buttonBuildWall.setType(MyButton.ButtonType.SKILL);
        buttonBuildWall.setCooldown(30000);

        buttonBuildBomd.setType(MyButton.ButtonType.SKILL);
        buttonBuildBomd.setCooldown(15000);

        buttonBuildSpikes.setType(MyButton.ButtonType.SKILL);
        buttonBuildSpikes.setCooldown(20000);
        
        skillBar.addButton(buttonBuildWall);
        skillBar.addButton(buttonBuildBomd);
        skillBar.addButton(buttonBuildSpikes);
    }

    public void update(){
        for (MyButton b : skillBar.buttons) {
            b.update();
        }
    }
    public void render(Graphics g){
        g.setColor(new Color(0,0,0,150));
        g.fillRect(20, 20, 140, 50);
        g.fillRect(170, 20,100,50);
        g.fillRect(20, 80, 250, 50);

        g.setColor(new Color(255,255,255,150));
        g.fillRect(17 * Constants.Tiles.TILE_SIZE - 12 , 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        g.fillRect(16 * Constants.Tiles.TILE_SIZE - 23 , 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        g.fillRect(15 * Constants.Tiles.TILE_SIZE - 32 , 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);

        g.setColor(Color.BLACK);
        g.drawRect(17 * Constants.Tiles.TILE_SIZE - 12 , 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        g.drawRect(16 * Constants.Tiles.TILE_SIZE - 23 , 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        g.drawRect(15 * Constants.Tiles.TILE_SIZE - 32 , 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);

        drawIcon(g);


        // text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("" + levelState.getGold(), 80, 55);
        g.drawString("" + levelState.getLives(), 220, 55);
        g.drawString("Wave: " + waveManager.getCurrentWave() + "/" + levelState.getMaxWaves(), 30, 115);


        drawButtons(g);
    }

    private void drawIcon(Graphics g){
        BufferedImage[] coinFrames = loadCoinFrames(UIAsset.coin);
        updateAnimation();
        if (coinFrames == null || coinFrames.length == 0) return;

        int index = aniIndex % coinFrames.length;

        int size = 32; 

        g.drawImage(coinFrames[index], 40, 30 , 32, 30, null);
        g.drawImage(UIAsset.heart, 180, 30,30,30, null);
    }

    public BufferedImage[] loadCoinFrames(BufferedImage atlas) {
        int count = 6; // Số lượng frame trong ảnh của bạn
        int width = atlas.getWidth() / count;
        int height = atlas.getHeight();
        BufferedImage[] frames = new BufferedImage[count];

        for (int i = 0; i < count; i++) {
            frames[i] = atlas.getSubimage(i * width, 0, width, height);
        }
        return frames;
    }

    private void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
    }

    private void drawButtons(Graphics g){
        buttonPause.draw(g);
        skillBar.draw(g);
        buttonupgade.draw(g);
    }
    
    public void mousePressed(int x, int y) {
        for(MyButton b : skillBar.buttons){
            if(b.getBounds().contains(x, y))
                b.setMousePressed(true);
        }
        if(buttonPause.getBounds().contains(x, y)){
            buttonPause.setMousePressed(true);
        }
        if(buttonupgade.getBounds().contains(x, y)){
            buttonupgade.setMousePressed(true);
        }
    }

    public void mouseReleased(int x, int y) {
        for(MyButton b : skillBar.buttons){
            if( b.getBounds().contains(x,y) && b.isMousePressed() ){
                b.setMousePressed(false);
                b.execute();
            } else {
                b.setMousePressed(false);
            }
        }

        if(buttonPause.getBounds().contains(x, y) && buttonPause.isMousePressed()){
            buttonPause.setMousePressed(false);
            buttonPause.execute();
        } else {
            buttonPause.setMousePressed(false);
        }

        if(buttonupgade.getBounds().contains(x, y) && buttonupgade.isMousePressed()){
            buttonupgade.setMousePressed(false);
            buttonupgade.execute(); 
        } else {
            buttonupgade.setMousePressed(false);
        }
    }

    public void refreshButtonIcons() {
        int wallLvl = levelManager.getCurrentLevel().getLevelWall();
        int spikeLvl = levelManager.getCurrentLevel().getLevelSpikes();


        BufferedImage newWallIcon = TrapAsset.wallBuild.get(wallLvl).get(2)[2];
        skillBar.buttons.get(0).setIcons(newWallIcon, newWallIcon, newWallIcon);


        BufferedImage newSpikeIcon = TrapAsset.spikes.get(spikeLvl)[3];
        skillBar.buttons.get(2).setIcons(newSpikeIcon, newSpikeIcon, newSpikeIcon);
    }

    public void mouseMoved(int x, int y) {
        for( MyButton b : skillBar.buttons){
            if( b.getBounds().contains(x, y)){
                b.setMouseOver(true);
            }
            else 
                b.setMouseOver(false);
        }
        if( buttonPause.getBounds().contains(x, y)){
            buttonPause.setMouseOver(true);
        }
        else buttonPause.setMouseOver(false);
        if( buttonupgade.getBounds().contains(x, y)){
            buttonupgade.setMouseOver(true);
        }
        else buttonupgade.setMouseOver(false);
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }
}

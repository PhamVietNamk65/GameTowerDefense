package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Manager.WaveManager;
import asset.UIAsset;
import levels.LevelState;
import listeners.GameListener;
import listeners.PauseListener;
import utils.Constants;

public class GameUI {
    private LevelState levelState;
    private WaveManager waveManager;

    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;
    private MyButton buttonPause;
    private MyButton buttonBuild;

    private GameListener gameListener;
    public GameUI(LevelState levelState, WaveManager waveManager) {
        this.levelState = levelState;
        this.waveManager = waveManager;

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
        buttonBuild = new MyButton("Build wall", 64, 64);
        buttonBuild.setButton(18 * Constants.Tiles.TILE_SIZE - 32, 1 * Constants.Tiles.TILE_SIZE - 32, Constants.Tiles.TILE_SIZE, Constants.Tiles.TILE_SIZE);
        buttonBuild.setAction(()->{
            if (gameListener != null) {
                gameListener.onBuild();
            }
        });
    }

    public void render(Graphics g){
        g.setColor(new Color(0,0,0,150));
        g.fillRect(20, 20, 140, 50);
        g.fillRect(170, 20,100,50);
        g.fillRect(20, 80, 250, 50);
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

        // Tính toán index hiện tại
        int index = aniIndex % coinFrames.length;

        // Kích thước hiển thị mong muốn (ví dụ 32x32)
        int size = 32; 

        // Vẽ đồng xu
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
        buttonBuild.draw(g);
    }
    
    public void mousePressed(int x, int y) {
        if (buttonPause.getBounds().contains(x, y)) {
            buttonPause.setMousePressed(true);
        }
        if (buttonBuild.getBounds().contains(x, y)) {
            buttonBuild.setMousePressed(true);
        }
    }

    public void mouseReleased(int x, int y) {
        if (buttonPause.getBounds().contains(x, y) && buttonPause.isMousePressed()) {
            buttonPause.execute();
        } else {
            buttonPause.setMousePressed(false);
        }
        if (buttonBuild.getBounds().contains(x, y) && buttonBuild.isMousePressed()) {
            buttonBuild.execute();
        } else {
            buttonBuild.setMousePressed(false);
        }
    }

    public void mouseMoved(int x, int y) {
        if (buttonPause.getBounds().contains(x,y)) {
            buttonPause.setMouseOver(true);
        }else{
            buttonPause.setMouseOver(false);
        }
        if (buttonBuild.getBounds().contains(x,y)) {
            buttonBuild.setMouseOver(true);
        }else{
            buttonBuild.setMouseOver(false);
        }
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }
}

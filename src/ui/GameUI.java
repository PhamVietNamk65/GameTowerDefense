package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Manager.WaveManager;
import asset.UIAsset;
import levels.LevelState;

public class GameUI {
    private LevelState levelState;
    private WaveManager waveManager;

    private int aniTick;
    private int aniIndex;
    private final int aniSpeed = 20;

    public GameUI(LevelState levelState, WaveManager waveManager) {
        this.levelState = levelState;
        this.waveManager = waveManager;
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
}

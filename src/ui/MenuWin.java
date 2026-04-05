package ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import asset.UIAsset;
import system.Particle;
import utils.Constants;

public class MenuWin {
    private ButtonBar buttonBar;
    private MyButton mainMenuButton, restartButton, nextLevelButton;

    private Runnable onNextLevel;
    private Runnable onReplay;
    private Runnable onExit;
    
    private List<Particle> particles = new ArrayList<>();
    private Random rand = new Random();

    private BufferedImage victoryImage = UIAsset.victory;
    public  MenuWin() {
        initButton();
    }

    private void initButton() {
        buttonBar = new ButtonBar(
            (int)(Constants.SCREEN_WIDTH * 0.3) + 30, 
            (int)(Constants.SCREEN_HEIGHT * 0.4) + 50,
            (int)(Constants.SCREEN_WIDTH * 0.4) ,
            (int)(Constants.SCREEN_HEIGHT* 0.2));
        buttonBar.setOrientation(0, 30);
        mainMenuButton = new MyButton(
            UIAsset.quit[0],
            UIAsset.quit[1],
            UIAsset.quit[2],
            (int)(Constants.SCREEN_WIDTH * 0.1), 
            (int)(Constants.SCREEN_HEIGHT * 0.15));
        

        restartButton = new MyButton(
            UIAsset.restart[0],
            UIAsset.restart[1],
            UIAsset.restart[2],
            (int)(Constants.SCREEN_WIDTH * 0.1), 
            (int)(Constants.SCREEN_HEIGHT * 0.15));
        
        nextLevelButton = new MyButton(
            UIAsset.nextLevel[0],
            UIAsset.nextLevel[1],
            UIAsset.nextLevel[2],
            (int)(Constants.SCREEN_WIDTH * 0.1), 
            (int)(Constants.SCREEN_HEIGHT * 0.15));

        buttonBar.addButton(mainMenuButton);
        buttonBar.addButton(restartButton);
        buttonBar.addButton(nextLevelButton);
    }

    public void setOnNextLevel(Runnable r) {
        this.onNextLevel = r;
        if(nextLevelButton != null) {
            nextLevelButton.setAction(onNextLevel);
        }
    }

    public void setOnReplay(Runnable r) {
        this.onReplay = r;
        if (restartButton != null) {
            restartButton.setAction(onReplay);
            
        }
    }

    public void setOnExit(Runnable r) {
        this.onExit = r;
        if (mainMenuButton != null) {
            mainMenuButton.setAction(r);
        }
    }

    public void update() {

    if (particles.size() < 100) { // Giới hạn số lượng hạt để tránh lag
        float px = 340 + rand.nextInt(580);
        float py = 100 + rand.nextInt(100);
        float vx = (rand.nextFloat() - 0.5f) * 4f; // Bay ngang nhẹ
        float vy = (rand.nextFloat() - 1.0f) * 3f; // Bắn lên trên một chút
        
        particles.add(new Particle(px, py, vx, vy, 1.0f, 255, 215, 0, 5f));
    }

        particles.removeIf(p -> !p.update());
    }
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
    
        g2d.setColor(new java.awt.Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, utils.Constants.SCREEN_WIDTH, utils.Constants.SCREEN_HEIGHT);

        int centerX = 340 + (580 / 2);
        int centerY = 100 + (180 / 2);
        float radius = 400f;

        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 225, 100, 200), new Color(0, 0, 0, 0)};

        RadialGradientPaint gp = new RadialGradientPaint(centerX, centerY, radius, dist, colors);

        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(gp);
    
   
        g2d.fillOval(centerX - (int)radius, centerY - (int)radius, (int)radius * 2, (int)radius * 2);
    
    
        g2d.setPaint(oldPaint);
            g.fillRect(0, 0, utils.Constants.SCREEN_WIDTH, utils.Constants.SCREEN_HEIGHT);
            g.drawImage(victoryImage,340 , 100, 580,180, null);
            buttonBar.drawButtons(g);
        }

    public void mousePressed(int x, int y) {
        for(MyButton b : buttonBar.buttons)
        if (b.getBounds().contains(x, y)) {
            b.setMousePressed(true);
        }
    }

    public void mouseReleased(int x, int y) {
        for (MyButton b : buttonBar.buttons) {
        if (b.getBounds().contains(x, y) && b.isMousePressed()) {
            b.setMousePressed(false);
            b.execute();
            }
            b.setMousePressed(false);
        }
    }

    public void mouseMoved(int x, int y) {
        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y)) {
                b.setMouseOver(true);
            } else {
                b.setMouseOver(false);
            }
        }
      
    }
}
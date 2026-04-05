package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import asset.UIAsset;
import utils.Constants;

public class MenuPause {
    private MyButton mainMenuButton, restartButton, cancelButton;
    private ButtonBar buttonBar;

    private Runnable onResume;
    private Runnable onRestart;
    private Runnable onExit;

    public MenuPause() {
        
        initbutton();
    }

    private void initbutton(){
        buttonBar = new ButtonBar(
            (int)(Constants.SCREEN_WIDTH * 0.3), 
            (int)(Constants.SCREEN_HEIGHT * 0.4),
            (int)(Constants.SCREEN_WIDTH * 0.4),
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
        
        cancelButton = new MyButton(
            UIAsset.cancel[0],
            UIAsset.cancel[1],
            UIAsset.cancel[2],
            (int)(Constants.SCREEN_WIDTH * 0.1), 
            (int)(Constants.SCREEN_HEIGHT * 0.15));

        buttonBar.addButton(mainMenuButton);
        buttonBar.addButton(restartButton);
        buttonBar.addButton(cancelButton);
    }

    // ===== SET ACTION =====
    public void setOnResume(Runnable r) {
        this.onResume = r;
        if(cancelButton != null) cancelButton.setAction(r);
    }

    public void setOnRestart(Runnable r) {
        this.onRestart = r;
        if(restartButton != null) restartButton.setAction(r);
    }

    public void setOnExit(Runnable r) {
        this.onExit = r;
        if(mainMenuButton != null) mainMenuButton.setAction(r);
    }

    // ===== RENDER =====
    public void render(Graphics g) {
        g.setColor(new Color(0,0,0, 170));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        buttonBar.draw(g);
    }

  

    // ===== INPUT =====
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
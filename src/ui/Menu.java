package ui;

import java.awt.Color;
import java.awt.Graphics;

import States.LevelSelectState;
import States.SettingState;
import asset.UIAsset;
import main.GamePanel;

public class Menu {

    private GamePanel gamePanel;
    private ButtonBar buttonBar;

    public Menu(GamePanel gamePanel){ 
        this.gamePanel = gamePanel;

        initButtons();
    }

    private void initButtons() {
        buttonBar = new ButtonBar(
            (int) (gamePanel.screenWidth * 0.3), 
            (int) (gamePanel.screenHeight * 0.45),
            (int) (gamePanel.screenWidth * 0.4),
            (int) (gamePanel.screenHeight * 0.5));
        buttonBar.setOrientation(1, 10); // vertical, gap 20px
        MyButton button1 = new MyButton(
            UIAsset.menuButtonsNormol[0],
            UIAsset.menuButtonsOver[0],
            UIAsset.menuButtonsPressed[0],
            (int)(gamePanel.screenWidth * 0.4),
            (int)(gamePanel.screenHeight * 0.5) / 3 - 10);
        button1.setAction(()->{
            gamePanel.getGameStateManager().setState(new LevelSelectState(gamePanel));
        });
        MyButton button2 = new MyButton(
            UIAsset.menuButtonsNormol[1],
            UIAsset.menuButtonsOver[1],
            UIAsset.menuButtonsPressed[1],
            (int)(gamePanel.screenWidth * 0.4),
            (int)(gamePanel.screenHeight * 0.5) / 3 - 10) ;
        button2.setAction(()->{
            gamePanel.getGameStateManager().setState(new SettingState(gamePanel));
        });
        MyButton button3 = new MyButton(
            UIAsset.menuButtonsNormol[2],
            UIAsset.menuButtonsOver[2],
            UIAsset.menuButtonsPressed[2],
            (int)(gamePanel.screenWidth * 0.4),
            (int)(gamePanel.screenHeight * 0.5) / 3 - 10);
        
        buttonBar.addButton(button1);
        buttonBar.addButton(button2);
        buttonBar.addButton(button3);
        buttonBar.visible = true;

    }
    public void render(Graphics g) {
        // 1. Vẽ ảnh nền trước
        drawBackground(g);
        drawLayout(g);
        drawLogo(g);
        drawButtons(g);
    }
    private void drawBackground(Graphics g){
        g.drawImage(UIAsset.backGround,0, 0, gamePanel.screenWidth, gamePanel.screenHeight, null);
        g.drawImage(UIAsset.logoTH, 1180 , 669, 80,80,null) ;
    }

    private void drawLayout(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,gamePanel.screenWidth,gamePanel.screenHeight);   
    }

    private void drawLogo(Graphics g){
        if (UIAsset.logo != null) {
        g.drawImage(UIAsset.logo, 
            (int) (gamePanel.screenWidth * 0.25),
            (int) (gamePanel.screenHeight * 0.05),
            (int) (gamePanel.screenWidth * 0.5),
            (int) (gamePanel.screenHeight * 0.38),
             null);
        }
    }

    private void drawButtons(Graphics g) {
        buttonBar.draw(g);
    }

    public void update() {
        
    }

    public void mousePressed(int x, int y) {
    for (MyButton b : buttonBar.buttons) {
        if (b.getBounds().contains(x, y)) {
            b.setMousePressed(true);
            }
        }
    }

    public void mouseReleased(int x, int y) {
        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y) && b.isMousePressed()) {
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

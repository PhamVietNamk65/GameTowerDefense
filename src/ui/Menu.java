package ui;
import static States.GameState.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;

import States.GameStateManager;
import States.LevelSelectState;
import States.SettingState;
import main.GamePanel;
import utils.AssetManager;

public class Menu {

    private AssetManager assetManager;
    private GamePanel gamePanel;
    private ButtonBar buttonBar;

    public Menu(GamePanel gamePanel){ 
        this.gamePanel = gamePanel;
        this.assetManager = AssetManager.getInstance();

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
            assetManager.menuButtonsNormol[0],
            assetManager.menuButtonsOver[0],
            assetManager.menuButtonsPressed[0]);
        button1.setAction(()->{
            gamePanel.getGameStateManager().setState(new LevelSelectState(gamePanel));
        });
        MyButton button2 = new MyButton(
            assetManager.menuButtonsNormol[1],
            assetManager.menuButtonsOver[1],
            assetManager.menuButtonsPressed[1]);
        button2.setAction(()->{
            gamePanel.getGameStateManager().setState(new SettingState(gamePanel));
        });
        MyButton button3 = new MyButton(assetManager.menuButtonsNormol[2],assetManager.menuButtonsOver[2],assetManager.menuButtonsPressed[2]);
        
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
        g.drawImage(assetManager.backGround,0, 0, gamePanel.screenWidth, gamePanel.screenHeight, null); 
    }

    private void drawLayout(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,gamePanel.screenWidth,gamePanel.screenHeight);   
    }

    private void drawLogo(Graphics g){
        if (assetManager.logo != null) {
        g.drawImage(assetManager.logo, 
            (int) (gamePanel.screenWidth * 0.25),
            (int) (gamePanel.screenHeight * 0.05),
            (int) (gamePanel.screenWidth * 0.5),
            (int) (gamePanel.screenHeight * 0.38),
             null);
        }
    }

    private void drawButtons(Graphics g) {
        buttonBar.drawButtons(g);
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

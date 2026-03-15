package scener;
import java.awt.Color;
import java.awt.Graphics;

import Manager.AssetManager;
import Manager.UIManager;
import main.GamePanel;
import ui.MyButton;

import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

    private GamePanel gamePanel;
    AssetManager assetManager;
    private UIManager uiManager;

    public Menu(GamePanel gamePanel, AssetManager assetManager) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.assetManager = assetManager;
        uiManager = new UIManager(gamePanel, assetManager);
    }

    @Override
    public void render(Graphics g) {
        // 1. Vẽ ảnh nền trước
        drawBackground(g);
        drawLogo(g);
        uiManager.draw(g, this);
    }

    private void drawLogo(Graphics g){
        if (assetManager.logo != null) {
        int logoWidth = 550;
        int logoHeight = 250;
        int logoX = (gamePanel.screenWidth - logoWidth) / 2;
        int logoY = 5;

        g.drawImage(assetManager.logo, logoX, logoY, logoWidth, logoHeight, null);

        }
    }

    private void drawBackground(Graphics g){
        g.drawImage(assetManager.backGround,0, 0, gamePanel.screenWidth, gamePanel.screenHeight, null); 
        g.setColor(new Color(0, 0, 0, 110)); // Màu đen với độ trong suốt (Alpha)
        g.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if( uiManager.quitMenuBar.visible ){
            if( uiManager.quitMenuBar.buttons.get(0).getBounds().contains(x, y))
                System.exit(0);
            else if( uiManager.quitMenuBar.buttons.get(1).getBounds().contains(x, y)){
                uiManager.quitMenuBar.visible = false;
                uiManager.mainMenuBar.visible = true;
            }
            return;
        }
            
        if( uiManager.mainMenuBar.visible )
            if( uiManager.mainMenuBar.buttons.get(0).getBounds().contains(x,y) )
                setCurrentState(PLAYING);
            else if( uiManager.mainMenuBar.buttons.get(1).getBounds().contains(x,y))
                setCurrentState(SETTING);
            else if( uiManager.mainMenuBar.buttons.get(2).getBounds().contains(x,y)){
                uiManager.quitMenuBar.visible = true;
                uiManager.mainMenuBar.visible = false;
            }
            
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (uiManager.mainMenuBar.visible) {
        for (MyButton button : uiManager.mainMenuBar.buttons) {
            button.setMouseOver(button.getBounds().contains(x, y));
        }
    }

    if (uiManager.quitMenuBar.visible) {
        for (MyButton button : uiManager.quitMenuBar.buttons) {
            button.setMouseOver(button.getBounds().contains(x, y));
        }
    }
    }

    @Override
    public void mousePressed(int x, int y) {
        if ( !uiManager.quitMenuBar.getBounds().contains(x,y)){
                uiManager.quitMenuBar.visible = false;
                uiManager.mainMenuBar.visible = true;
                return;
            }    
        if (uiManager.mainMenuBar.visible) {
        for (MyButton button : uiManager.mainMenuBar.buttons) {
            if (button.getBounds().contains(x, y)) {
                button.setMousePressed(true);
            }
        }
    }

    if (uiManager.quitMenuBar.visible) {
        for (MyButton button : uiManager.quitMenuBar.buttons) {
            if (button.getBounds().contains(x, y)) {
                button.setMousePressed(true);
            }
        }
    }
    }

    @Override
    public void mouseReleased(int x, int y) {
        if (uiManager.mainMenuBar.visible) {
            for (MyButton button : uiManager.mainMenuBar.buttons) {
                button.resetBooleans();
                }
        }

        if (uiManager.quitMenuBar.visible) {
            for (MyButton button : uiManager.quitMenuBar.buttons) {
                button.resetBooleans();
            }
        }
    }

  
}

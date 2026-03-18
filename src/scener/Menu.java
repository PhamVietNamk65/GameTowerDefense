package scener;
import static main.GameStates.*;

import java.awt.Color;
import java.awt.Graphics;

import Manager.AssetManager;
import Manager.UIManager;
import main.GamePanel;
import main.GameStates;
import ui.MyButton;

public class Menu extends GameScene implements SceneMethods{

    private GamePanel gamePanel;
    AssetManager assetManager;
    private UIManager uiManager;

    public Menu(GamePanel gamePanel, AssetManager assetManager) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.assetManager = assetManager;
        if(GameStates.getGameStates() == GameStates.MENU && assetManager.backGround == null){
            assetManager.loadMenuAssets();
        }
        uiManager = new UIManager(gamePanel, assetManager);
    }

    @Override
    public void render(Graphics g) {
        // 1. Vẽ ảnh nền trước
        drawBackground(g);
        drawLayout(g);
        drawLogo(g);
        uiManager.draw(g, this);
    }

    private void drawLayout(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,gamePanel.screenWidth,gamePanel.screenHeight);   
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
        uiManager.mouseClicked(x, y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        uiManager.mouseMoved(x, y);
    }

    @Override
    public void mousePressed(int x, int y) {
        uiManager.mousePressed(x, y);
    }


    @Override
    public void mouseReleased(int x, int y) {
        uiManager.mouseReleased(x, y);
    }

    @Override
    public void update() {

    }

  
}

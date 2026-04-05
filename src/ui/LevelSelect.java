package ui;

import java.awt.Graphics;
import java.util.List;

import Manager.ProgressManager;
import asset.UIAsset;
import entity.monster.Monster;
import entity.tower.Tower;
import levels.LevelNode;
import main.GamePanel;
import states.MenuState;
import states.PlayingState;
import utils.Constants;

public class LevelSelect {
    private GamePanel gamePanel;
    private ButtonBar buttonBar;
    private MyButton homeButton;

    private ProgressManager progressManager;
    public LevelSelect(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        progressManager = gamePanel.getProgressManager();

        initbutton();
    }

    private void initbutton() {
        buttonBar = new ButtonBar(
           (int) (Constants.SCREEN_WIDTH * 0.315) , 
           (int) (Constants.SCREEN_HEIGHT * 0.25) ,
           (int) (Constants.SCREEN_WIDTH * 0.5) - 20 ,
           (int) (Constants.SCREEN_HEIGHT * 0.2) );
        buttonBar.setOrientation(0, 5);
        int btnW = (int)(Constants.SCREEN_WIDTH * 0.12);
        int btnH = (int)(Constants.SCREEN_WIDTH * 0.12);
        for( int i = 1; i < 10; i++){
            int level = i ;
            if(progressManager.isLevelUnlocked(level)){
                MyButton lv = new MyButton(
                    UIAsset.levelIcons.get(level)[0], 
                    UIAsset.levelIcons.get(level)[1], 
                    UIAsset.levelIcons.get(level)[2], 
                    btnW, btnH 
                );
                lv.setAction(() -> {
                gamePanel.getGameStateManager().setState( new PlayingState(gamePanel, level));
                });
                buttonBar.addButton(lv);
            } else {
                MyButton lv = new MyButton(
                    UIAsset.levelLock[0], 
                    UIAsset.levelLock[1], 
                    UIAsset.levelLock[2], 
                    btnW, btnH 
                );
                
                buttonBar.addButton(lv);
            }
        }
        homeButton = new MyButton(
            UIAsset.quit[0],
            UIAsset.quit[1],
            UIAsset.quit[2],
            Constants.SCREEN_WIDTH, 
            Constants.SCREEN_HEIGHT);
        homeButton.setButton(
            19 * Constants.Tiles.TILE_SIZE - 32, 
            11 * Constants.Tiles.TILE_SIZE - 32, 
            Constants.Tiles.TILE_SIZE + 10, 
            Constants.Tiles.TILE_SIZE + 10);
    }

    public static void update() {
        
    }

    public void render(Graphics g) {
        drawBackground(g);
        drawButtons(g);
    }

    private void drawButtons(Graphics g){
        buttonBar.drawButtons(g);
        homeButton.draw(g);
    }
    public void mousePressed(int x, int y) {
        for (MyButton b : buttonBar.buttons) {
        if (b.getBounds().contains(x, y)) {
            b.setMousePressed(true);
            }
        }
        homeButton.setMousePressed(homeButton.getBounds().contains(x, y));
    }

    public void mouseReleased(int x, int y) {
        for (MyButton b : buttonBar.buttons) {
        if (b.getBounds().contains(x, y) && b.isMousePressed()) {
            b.setMousePressed(false);
            b.execute();
            }
            b.setMousePressed(false);
        }
        homeButton.setMousePressed(false);
        if (homeButton.getBounds().contains(x, y)) {
            gamePanel.getGameStateManager().setState(new MenuState(gamePanel));
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
        homeButton.setMouseOver(homeButton.getBounds().contains(x, y));
    }
    
    public void drawBackground(Graphics g){
        g.drawImage(UIAsset.backGround_levelSelect, 0, 0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT,null);
    }

}

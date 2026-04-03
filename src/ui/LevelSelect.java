package ui;

import java.awt.Graphics;

import States.MenuState;
import States.PlayingState;
import asset.UIAsset;
import main.GamePanel;
import utils.Constants;

public class LevelSelect {
    private UIAsset uiAsset;
    private GamePanel gamePanel;
    private ButtonBar buttonBar1, buttonBar2;
    public LevelSelect(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        uiAsset = UIAsset.getInstance();

        initbutton();
    }

    private void initbutton() {
        buttonBar1 = new ButtonBar(
           (int) (Constants.SCREEN_WIDTH * 0.1), 
           (int) (Constants.SCREEN_HEIGHT * 0.2),
           (int) (Constants.SCREEN_WIDTH * 0.8),
           (int) (Constants.SCREEN_HEIGHT * 0.3) );
        buttonBar1.setOrientation(1, 20);
        
        buttonBar2 = new ButtonBar(
           (int) (Constants.SCREEN_WIDTH * 0.1), 
           (int) (Constants.SCREEN_HEIGHT * 0.5) + 20,
           (int) (Constants.SCREEN_WIDTH * 0.8),
           (int) (Constants.SCREEN_HEIGHT * 0.3) );
        buttonBar2.setOrientation(1, 20);

        // Level 1
        MyButton lv1 = new MyButton(UIAsset.levelSelect[1],null,null, (int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3));
        lv1.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 1)
            );
        });

        // Level 2
        MyButton lv2 = new MyButton("LEVEL 2", (int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3));
        lv2.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 2)
            );
        });

        MyButton lv3 = new MyButton("LEVEL 3", (int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3));
        lv3.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 3)
            );
        });

        MyButton lv4 = new MyButton("LEVEL 4", (int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3));
        lv4.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 4)
            );
        });
        MyButton lv5 = new MyButton("LEVEL 5", (int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3));
        lv5.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 5)
            );
        });
        MyButton lv6 = new MyButton("LEVEL 6", (int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3));
        lv6.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 6)
            );
        });


        buttonBar1.addButton(lv1);
        buttonBar1.addButton(lv2);
        buttonBar1.addButton(lv3);
        buttonBar2.addButton(lv4);
        buttonBar2.addButton(lv5);
        buttonBar2.addButton(lv6);

    
    }

    public static void update() {
        
    }

    public void render(Graphics g) {
        drawBackground(g);
        drawlevel(g);
        drawButtons(g);
    }

    private void drawButtons(Graphics g){
        buttonBar1.drawButtons(g);
        buttonBar2.drawButtons(g);
    }
    public void mousePressed(int x, int y) {
        for (MyButton b : buttonBar1.buttons) {
        if (b.getBounds().contains(x, y)) {
            b.setMousePressed(true);
            }
        }
        for (MyButton b : buttonBar2.buttons) {
        if (b.getBounds().contains(x, y)) {
            b.setMousePressed(true);
            }
        }
    }

    public void mouseReleased(int x, int y) {
        for (MyButton b : buttonBar1.buttons) {
        if (b.getBounds().contains(x, y) && b.isMousePressed()) {
            b.execute();
            }
            b.setMousePressed(false);
        }
        for (MyButton b : buttonBar2.buttons) {
        if (b.getBounds().contains(x, y) && b.isMousePressed()) {
            b.execute();
            }
            b.setMousePressed(false);
        }

    }

    public void mouseMoved(int x, int y) {
        for (MyButton b : buttonBar1.buttons) {
            if (b.getBounds().contains(x, y)) {
                b.setMouseOver(true);
            } else {
                b.setMouseOver(false);
            }
        }
        for (MyButton b : buttonBar2.buttons) {
            if (b.getBounds().contains(x, y)) {
                b.setMouseOver(true);
            } else {
                b.setMouseOver(false);
            }
        }      
    }
    
    public void drawBackground(Graphics g){
        g.drawImage(UIAsset.backGround_levelSelect, 0, 0, Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT,null);
    }

    public void drawlevel(Graphics g){
        g.drawImage(UIAsset.levelSelect[1],(int) (Constants.SCREEN_WIDTH * 0.1), (int) (Constants.SCREEN_HEIGHT * 0.2),(int)(Constants.SCREEN_WIDTH * 0.25), (int)(Constants.SCREEN_HEIGHT * 0.3),null );
    }
}

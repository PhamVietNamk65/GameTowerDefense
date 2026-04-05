package ui;

import java.awt.Color;
import java.awt.Graphics;

import asset.UIAsset;
import main.GamePanel;
import states.LevelSelectState;
import states.SettingState;
import utils.Constants;

public class Menu {

    private GamePanel gamePanel;
    private ButtonBar buttonBar;
    private ButtonBar quitButtonBar;
    boolean showQuit = false;

    public Menu(GamePanel gamePanel){ 
        this.gamePanel = gamePanel;

        initButtons();
    }

    private void initButtons() {
        buttonBar = new ButtonBar(
            (int) (Constants.SCREEN_WIDTH  * 0.3), 
            (int) (Constants.SCREEN_HEIGHT * 0.45),
            (int) (Constants.SCREEN_WIDTH  * 0.4),
            (int) (Constants.SCREEN_HEIGHT * 0.5));
        buttonBar.setOrientation(1, 10); // vertical, gap 20px
        MyButton button1 = new MyButton(
            UIAsset.menuButtonsNormol[0],
            UIAsset.menuButtonsOver[0],
            UIAsset.menuButtonsPressed[0],
            (int)(Constants.SCREEN_WIDTH  * 0.4),
            (int)(Constants.SCREEN_HEIGHT * 0.5) / 3 - 10);
        button1.setAction(()->{
            gamePanel.getGameStateManager().setState(new LevelSelectState(gamePanel));
        });
        MyButton button2 = new MyButton(
            UIAsset.menuButtonsNormol[1],
            UIAsset.menuButtonsOver[1],
            UIAsset.menuButtonsPressed[1],
            (int)(Constants.SCREEN_WIDTH  * 0.4),
            (int)(Constants.SCREEN_HEIGHT * 0.5) / 3 - 10) ;
        button2.setAction(()->{
            gamePanel.getGameStateManager().setState(new SettingState(gamePanel));
        });
        MyButton button3 = new MyButton(
            UIAsset.menuButtonsNormol[2],
            UIAsset.menuButtonsOver[2],
            UIAsset.menuButtonsPressed[2],
            (int)(Constants.SCREEN_WIDTH  * 0.4),
            (int)(Constants.SCREEN_HEIGHT * 0.5) / 3 - 10);
        button3.setAction(()->{
            showQuit = true;
        });
        buttonBar.addButton(button1);
        buttonBar.addButton(button2);
        buttonBar.addButton(button3);
        buttonBar.visible = true;

        quitButtonBar = new ButtonBar(
            (int)(Constants.SCREEN_WIDTH * 0.3), 
            (int)(Constants.SCREEN_HEIGHT * 0.47),
            (int)(Constants.SCREEN_WIDTH * 0.4),
            (int)(Constants.SCREEN_HEIGHT* 0.2));
        quitButtonBar.setOrientation(1, 55);
        MyButton yesButton = new MyButton(
            UIAsset.yesButton[0],
            UIAsset.yesButton[1],
            UIAsset.yesButton[2],
            (int)(Constants.SCREEN_WIDTH * 0.17),
            (int)(Constants.SCREEN_HEIGHT * 0.25));
        yesButton.setAction(()->{
            System.exit(0);
        });
        MyButton noButton = new MyButton(
            UIAsset.noButton[0],
            UIAsset.noButton[1],
            UIAsset.noButton[2],
            (int)(Constants.SCREEN_WIDTH * 0.17),
            (int)(Constants.SCREEN_HEIGHT * 0.25));
        noButton.setAction(()->{
            showQuit = false;
        });
        quitButtonBar.addButton(yesButton);
        quitButtonBar.addButton(noButton);
    }
    public void render(Graphics g) {
        // 1. Vẽ ảnh nền trước
        drawBackground(g);
        drawLayout(g);
        drawLogo(g);
        drawButtons(g);
        if(showQuit) {
            drawQuitMenu(g);
        }
    }
    private void drawQuitMenu(Graphics g) {
        g.setColor(new Color(0,0,0,170));
        g.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        g.drawImage(
            UIAsset.quitFrame,
            (int)(Constants.SCREEN_WIDTH * 0.26),
            (int)(Constants.SCREEN_HEIGHT * 0.36),
            590, 
            325,
            null);
        quitButtonBar.draw(g);
    }

    private void drawBackground(Graphics g){
        g.drawImage(UIAsset.backGround,0, 0, Constants.SCREEN_WIDTH , Constants.SCREEN_HEIGHT, null);
        g.drawImage(UIAsset.logoTH, 1180 , 669, 80,80,null) ;
    }

    private void drawLayout(Graphics g) {
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0,0,Constants.SCREEN_WIDTH ,Constants.SCREEN_HEIGHT);   
    }

    private void drawLogo(Graphics g){
        if (UIAsset.logo != null) {
        g.drawImage(UIAsset.logo, 
            (int) (Constants.SCREEN_WIDTH  * 0.25),
            (int) (Constants.SCREEN_HEIGHT * 0.05),
            (int) (Constants.SCREEN_WIDTH  * 0.5),
            (int) (Constants.SCREEN_HEIGHT * 0.38),
             null);
        }
    }

    private void drawButtons(Graphics g) {
        buttonBar.draw(g);
    }

    public void update() {
        
    }

    public void mousePressed(int x, int y) {
        if (showQuit) {
            for (MyButton b : quitButtonBar.buttons) {
                if (b.getBounds().contains(x, y)) {
                    b.setMousePressed(true);
                }
            }
            return;
        }
        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y)) {
                b.setMousePressed(true);
            }
        }
    }

    public void mouseReleased(int x, int y) {
        if (showQuit) {
            for (MyButton b : quitButtonBar.buttons) {
                if (b.getBounds().contains(x, y) && b.isMousePressed()) {
                    b.execute();
                }
                b.setMousePressed(false);
            }
            return;
        }
        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y) && b.isMousePressed()) {
                b.execute();
            }
            b.setMousePressed(false);
        }
    }

    public void mouseMoved(int x, int y) {
        if (showQuit) {
            for (MyButton b : quitButtonBar.buttons) {
                if (b.getBounds().contains(x, y)) {
                    b.setMouseOver(true);
                } else {
                    b.setMouseOver(false);
                }
            }   
            return;
        }
        for (MyButton b : buttonBar.buttons) {
            if (b.getBounds().contains(x, y)) {
                b.setMouseOver(true);
            } else {
                b.setMouseOver(false);
            }
        }   
    }

  
}

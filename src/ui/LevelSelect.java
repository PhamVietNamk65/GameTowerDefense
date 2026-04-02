package ui;

import java.awt.Graphics;

import States.MenuState;
import States.PlayingState;
import asset.UIAsset;
import main.GamePanel;

public class LevelSelect {
    private UIAsset uiAsset;
    private GamePanel gamePanel;
    private ButtonBar buttonBar;
    public LevelSelect(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        uiAsset = UIAsset.getInstance();

        initbutton();
    }

    private void initbutton() {
        buttonBar = new ButtonBar(150, 100, 980,568 );
        buttonBar.setOrientation(1, 10);

        // Level 1
        MyButton lv1 = new MyButton("LEVEL 1", 200, 60);
        lv1.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 1)
            );
        });

        // Level 2
        MyButton lv2 = new MyButton("LEVEL 2", 200, 60);
        lv2.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new PlayingState(gamePanel, 2)
            );
        });

        //  MyButton lv3 = new MyButton("LEVEL 3", 200, 60);
        // lv1.setAction(() -> {
        //     gamePanel.getGameStateManager().setState(
        //         new PlayingState(gamePanel, 3)
        //     );
        // });

        //  MyButton lv4 = new MyButton("LEVEL 4", 200, 60);
        // lv1.setAction(() -> {
        //     gamePanel.getGameStateManager().setState(
        //         new PlayingState(gamePanel, 4)
        //     );
        // });
        //  MyButton lv5 = new MyButton("LEVEL 5", 200, 60);
        // lv1.setAction(() -> {
        //     gamePanel.getGameStateManager().setState(
        //         new PlayingState(gamePanel, 5)
        //     );
        // });
        //  MyButton lv6 = new MyButton("LEVEL 6", 200, 60);
        // lv1.setAction(() -> {
        //     gamePanel.getGameStateManager().setState(
        //         new PlayingState(gamePanel, 6)
        //     );
        // });
        // Back
        MyButton back = new MyButton("BACK", 200, 60);
        back.setAction(() -> {
            gamePanel.getGameStateManager().setState(
                new MenuState(gamePanel)
            );
        });

        buttonBar.addButton(lv1);
        buttonBar.addButton(lv2);
        // buttonBar.addButton(lv3);
        // buttonBar.addButton(lv4);
        // buttonBar.addButton(lv5);
        // buttonBar.addButton(lv6);

        buttonBar.addButton(back);
    
    }

    public static void update() {
        
    }

    public void render(Graphics g) {
        drawButtons(g);
    }

    private void drawButtons(Graphics g){
        buttonBar.drawButtons(g);
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

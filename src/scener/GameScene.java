package scener;
import java.awt.Graphics;

import main.GamePanel;

public abstract class GameScene {

    protected GamePanel game;

    public GameScene(GamePanel game){
        this.game = game;
    }

    public abstract void update();
    public abstract void render(Graphics g);

}
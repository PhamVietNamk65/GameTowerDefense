package scener;

import java.awt.Graphics;

import Manager.AssetManager;
import main.GamePanel;

public class Setting extends GameScene implements SceneMethods{

    GamePanel gamePanel;

    public Setting(GamePanel gamePanel, AssetManager assetManager) {
        super(gamePanel);
        this.gamePanel = gamePanel;
    }

    @Override
    public void render(Graphics g) {
    }

    @Override
    public void mouseClicked(int x, int y) {
    }

    @Override
    public void mouseMoved(int x, int y) {
    }

    @Override
    public void mousePressed(int x, int y) {
    }

    @Override
    public void mouseReleased(int x, int y) {
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}

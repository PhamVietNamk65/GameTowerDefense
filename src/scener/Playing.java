package scener;

import java.awt.Color;
import java.awt.Graphics;

import main.GamePanel;

public class Playing extends GameScene implements SceneMethods{

    public Playing(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(30, 30, 100, 100);
    }
    
}

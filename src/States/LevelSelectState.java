package States;

import java.awt.Graphics;

import main.GamePanel;
import ui.LevelSelect;
import ui.Menu;

public class LevelSelectState implements GameState{

    private LevelSelect levelSelect;
    public LevelSelectState(GamePanel gamePanel){
        this.levelSelect = new LevelSelect(gamePanel);
    }

    @Override
    public void update() {
        LevelSelect.update();
    }

    @Override
    public void render(Graphics g) {
        levelSelect.render(g);
    }

    @Override
    public void mousePressed(int x, int y) {
        levelSelect.mousePressed(x,y);
    }
    @Override
    public void mouseReleased(int x, int y) {
       levelSelect.mouseReleased(x,y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        levelSelect.mouseMoved(x, y);
    }
    
}

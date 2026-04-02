package States;

import java.awt.Graphics;

import main.GamePanel;
import ui.Menu;

public class MenuState implements GameState{
    private Menu menu;
    public MenuState(GamePanel gamePanel){
        this.menu = new Menu(gamePanel);
    }
    @Override
    public void update() {
        menu.update();
    }

    @Override
    public void render(Graphics g) {
        menu.render(g);
    }

    @Override
    public void mousePressed(int x, int y) {
        menu.mousePressed(x, y);
    }

    @Override
    public void mouseReleased(int x, int y) {
        menu.mouseReleased(x, y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        menu.mouseMoved(x, y);
    }
    
}

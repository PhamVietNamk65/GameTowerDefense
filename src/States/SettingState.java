package states;

import java.awt.Graphics;
import main.GamePanel;
import ui.Setting;

public class SettingState implements GameState {

    private final Setting setting;

    public SettingState(GamePanel gamePanel) {
        this.setting = new Setting(gamePanel);
    }

    @Override
    public void update() {
        setting.update();
    }

    @Override
    public void render(Graphics g) {
        setting.render(g);
    }

    @Override
    public void mousePressed(int x, int y) {
        setting.mousePressed(x, y);
    }

    @Override
    public void mouseReleased(int x, int y) {
        setting.mouseReleased(x, y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        setting.mouseMoved(x, y);
    }
}
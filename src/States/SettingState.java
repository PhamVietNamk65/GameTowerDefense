package States;

import java.awt.Graphics;

import main.GamePanel;
import ui.Setting;

public class SettingState implements GameState{

    private Setting setting;
    public SettingState(GamePanel gamePanel){
        this.setting = new Setting(gamePanel);
    }
    @Override
    public void update() {
        setting.update();
    }

    @Override
    public void render(Graphics g) {
        setting.render();
    }

    @Override
    public void mousePressed(int x, int y) {
       
    }

    @Override
    public void mouseReleased(int x, int y) {
        
    }

    @Override
    public void mouseMoved(int x, int y) {
       
    }
    
}

package ui;

import main.GamePanel;
import utils.AssetManager;

public class Setting {
    private AssetManager assetManager;
    GamePanel gamePanel;
    public Setting(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        assetManager = AssetManager.getInstance();
    }
    
    public static void update() {
        
    }

    public static void render() {
        
    }
    
}

package ui;

import asset.UIAsset;
import main.GamePanel;

public class Setting {
    private UIAsset uiAsset;
    GamePanel gamePanel;
    public Setting(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        uiAsset = UIAsset.getInstance();
    }
    
    public static void update() {
        
    }

    public static void render() {
        
    }
    
}

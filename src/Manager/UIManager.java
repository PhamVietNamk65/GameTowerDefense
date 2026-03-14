package Manager;

import scener.GameScene;
import ui.ButtonBar;
import ui.MyButton;

import java.awt.Color;
import java.awt.Graphics;

import main.GamePanel;

public class UIManager{
    public ButtonBar mainMenuBar , quitMenuBar, settingsMenuBar; 
    private AssetManager assetManager;
    private GamePanel gamePanel;
    public UIManager(GamePanel gamePanel, AssetManager assetManager){
        this.gamePanel = gamePanel;
        this.assetManager = assetManager;

        mainMenuBar = new ButtonBar(280, 250 , 400, 200);
        mainMenuBar.setOrientation(1, 20); // vertical, gap 20px
        mainMenuBar.addButton(new MyButton("LEVEL", 200, 50));
        mainMenuBar.addButton(new MyButton("SETTING", 200, 50));
        mainMenuBar.addButton(new MyButton("QUIT", 200, 50));
        mainMenuBar.visible = true;

        quitMenuBar = new ButtonBar(295, 320, 420, 100);
        quitMenuBar.setOrientation(0, 20); // horizontal, gap 20px
        quitMenuBar.addButton(new MyButton("YES", 200, 50)); 
        quitMenuBar.addButton(new MyButton("NO", 200, 50)); 
        quitMenuBar.visible = false;

    }
    public void update() {
        
    }

    public void draw(Graphics g, GameScene currentScene) {
        switch (currentScene.getCurrentState()) {
            case MENU:
                drawMenu(g);    // Vẽ giao diện menu
                break;
            case PLAYING:
                // Vẽ giao diện chơi game
                break;
        
            default:
                break;
        }
    }
    private void drawMenu(Graphics g) {
        mainMenuBar.draw(g);
        if( quitMenuBar.visible ){
            drawOverlay(g);
            quitMenuBar.draw(g);
        }
    }

    private void drawOverlay(Graphics g) {
        // 1. Vẽ lớp phủ mờ toàn màn hình
        g.setColor(new Color(0, 0, 0, 180)); 
        g.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // 2. Vẽ khung cho bảng Quit Menu
        int menuX = 280;
        int menuY = 214;
        int menuWidth = 450;
        int menuHeight = 200; // Điều chỉnh chiều cao cho phù hợp

        g.setColor(new Color(50, 50, 50)); // Màu nền của bảng (xám đậm)
        g.fillRect(menuX, menuY, menuWidth, menuHeight);
    
        g.setColor(Color.WHITE);
        g.drawRect(menuX, menuY, menuWidth, menuHeight); // Vẽ viền trắng cho bảng

        // 3. Vẽ chữ "QUIT" làm tiêu đề
        g.setFont(g.getFont().deriveFont(30f)); // Chỉnh cỡ chữ to lên
        String text = "DO YOU WANT TO QUIT?";
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textX = menuX + (menuWidth - textWidth) / 2; // Căn giữa chữ
        int textY = menuY + 60; // Vị trí chữ ở phía trên bảng
    
        g.drawString(text, textX, textY);
    }
}
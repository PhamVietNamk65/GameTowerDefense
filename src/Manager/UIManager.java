package Manager;

import scener.GameScene;
import scener.SceneMethods;
import ui.ButtonBar;
import ui.MyButton;

import java.awt.Color;
import java.awt.Graphics;

import main.GamePanel;
import main.GameStates;

public class UIManager implements SceneMethods{
    public ButtonBar mainMenuBar , quitMenuBar, settingsMenuBar; 
    private AssetManager assetManager;
    private GamePanel gamePanel;
    public UIManager(GamePanel gamePanel, AssetManager assetManager){
        this.gamePanel = gamePanel;
        this.assetManager = assetManager;

        mainMenuBar = new ButtonBar(290, 250 , 370, 230);
        mainMenuBar.setOrientation(1, 10); // vertical, gap 20px
        mainMenuBar.addButton(new MyButton(assetManager.menuButtonsNormol[0]));
        mainMenuBar.addButton(new MyButton(assetManager.menuButtonsNormol[1]));
        mainMenuBar.addButton(new MyButton(assetManager.menuButtonsNormol[2]));
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
        switch (GameStates.getGameStates()) {
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
        // vẽ lớp phủ màu đen với độ trong suốt
        g.setColor(new Color(0, 0, 0, 180)); 
        g.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // Vẽ bảng Quit
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

    @Override
    public void mouseClicked(int x, int y) {
        if (quitMenuBar.visible) {
            if (quitMenuBar.buttons.get(0).getBounds().contains(x, y)) {
                System.exit(0); // Nút YES
            } else if (quitMenuBar.buttons.get(1).getBounds().contains(x, y)) {
                quitMenuBar.visible = false; // Nút NO
                mainMenuBar.visible = true;
        }
        return; // Dừng xử lý khi bảng Quit đang hiện
    }

    if (mainMenuBar.visible) {
        if (mainMenuBar.buttons.get(0).getBounds().contains(x, y)) {
            GameStates.setGameStates(GameStates.PLAYING);
        } else if (mainMenuBar.buttons.get(1).getBounds().contains(x, y)) {
            // GameStates.setGameStates(SETTING);
        } else if (mainMenuBar.buttons.get(2).getBounds().contains(x, y)) {
            quitMenuBar.visible = true;
            mainMenuBar.visible = false;
        }
    }
    }

    @Override
    public void mouseMoved(int x, int y) {
        // Reset trạng thái hover cho tất cả
        for (MyButton b : mainMenuBar.buttons) b.setMouseOver(false);
        for (MyButton b : quitMenuBar.buttons) b.setMouseOver(false);

        // Cập nhật hover cho menu đang hiển thị
        if (quitMenuBar.visible) {
            for (MyButton b : quitMenuBar.buttons) {
                if (b.getBounds().contains(x, y)) b.setMouseOver(true);
            }
        } else if (mainMenuBar.visible) {
            for (MyButton b : mainMenuBar.buttons) {
                if (b.getBounds().contains(x, y)) b.setMouseOver(true);
            }
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (quitMenuBar.visible) {
            for (MyButton b : quitMenuBar.buttons) {
                if (b.getBounds().contains(x, y)) b.setMousePressed(true);
            }
        } else if (mainMenuBar.visible) {
            for (MyButton b : mainMenuBar.buttons) {
                if (b.getBounds().contains(x, y)) b.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        for (MyButton b : mainMenuBar.buttons) b.resetBooleans();
        for (MyButton b : quitMenuBar.buttons) b.resetBooleans();
    }
    @Override
    public void render(Graphics g) {
    }
}
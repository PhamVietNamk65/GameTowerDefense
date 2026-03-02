package scener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import ui.ButtonBar;
import ui.MyButton;

import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

    private GamePanel gamePanel;

    private MyButton[] buttons = new MyButton[3];

    private BufferedImage logo, background;

    private ButtonBar quitMenu;
    public Menu(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        
            try {
                logo = ImageIO.read(new File("res/logo.png"));
                background = ImageIO.read(new File("res/background.png"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        quitMenu = new ButtonBar(280, 164 , 400, 200);

        initButton();
    }

    public void initButton(){
        String[] labels = {"LEVEL", "SETTING", "QUIT"};
        int w = 200;
        int h = 45;
        int gap = 20;

        // Tọa độ X căn giữa
        int x = (gamePanel.screenWidth - w) / 2;
        
        // Tính Y bắt đầu để cả khối menu nằm giữa màn hình
        int totalHeight = (labels.length * h) + ((labels.length - 1) * gap);
        int startY = (gamePanel.screenHeight - totalHeight) / 2 + 80;

        for (int i = 0; i < labels.length; i++) {
            int y = startY + (i * (h + gap));

            // Khởi tạo nút với tọa độ đã tính
            buttons[i] = new MyButton(labels[i], x, y, w, h);
        }
    }
    @Override
    public void render(Graphics g) {
        // 1. Vẽ ảnh nền trước
        drawBackground(g);
        drawLogo(g);
        drawButons(g);
        if( quitMenu.visible ){
            drawOverlay(g);
            drawQuitMenu(g);
        }
    }


    private void drawButons(Graphics g){
        for (MyButton b : buttons) {
            b.draw(g);
        }
    }

    private void drawQuitMenu(Graphics g){
        quitMenu.draw(g);
    }
    private void drawLogo(Graphics g){
        if (logo != null) {
        int logoWidth = 550;
        int logoHeight = 250;
        int logoX = (gamePanel.screenWidth - logoWidth) / 2;
        int logoY = 5;

        g.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);

        }
    }

    private void drawOverlay(Graphics g) {
        // Màu đen với độ trong suốt (Alpha). 
        // Giá trị Alpha từ 0 (trong suốt) đến 255 (đậm đặc). 150 là mức mờ vừa phải.
        g.setColor(new Color(0, 0, 0, 200)); 
    
        // Vẽ hình chữ nhật phủ toàn bộ màn hình
        g.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    }

    private void drawBackground(Graphics g){
        g.drawImage(background,0, 0, gamePanel.screenWidth, gamePanel.screenHeight, null);
        g.setColor(new Color(0, 0, 0, 110)); // Màu đen với độ trong suốt (Alpha)
        g.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    }

    @Override
    public void mouseClicked(int x, int y) {

    // Kiểm tra nút LEVEL 
    if(buttons[0].getBounds().contains(x, y)){
        SetGameState(LEVEL);
    }
    // Kiểm tra nút SETTING 
    else if(buttons[1].getBounds().contains(x, y)){
        SetGameState(SETTING); // Nếu bạn có state này
    }
    // Kiểm tra nút QUIT 
    else if(buttons[2].getBounds().contains(x, y)){
        quitMenu.visible = true; // hien menu thoat game
    }
    }

    @Override
    public void mouseMoved(int x, int y) {
        for (MyButton b : buttons) {
        b.setMouseOver(false); // Tắt highlight tất cả các nút
    }

    for (MyButton b : buttons) {
        if (b.getBounds().contains(x, y)) {
            b.setMouseOver(true); // Chỉ bật highlight nút đang trỏ vào
            break; 
        }
    }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (quitMenu.visible) {
        if (!quitMenu.getBounds().contains(x, y)) {
            quitMenu.visible = false; // Thả chuột ngoài bảng thì tắt
            }
        }
        for (MyButton b : buttons) {
        if (b.getBounds().contains(x, y)) {
            b.setMousePressed(true);
            break;
            }
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        resetButtons();
    }

    private void resetButtons() {
        for (MyButton b : buttons) {
            b.resetBooleans();
        }
    }
    
    
}

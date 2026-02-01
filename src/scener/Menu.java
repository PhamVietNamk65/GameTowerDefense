package scener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import ui.MyButton;

import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

    private GamePanel gamePanel;

    private MyButton[] buttons = new MyButton[3];

    private BufferedImage logo;

    public Menu(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        try {
            logo = ImageIO.read(new File("res/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (logo != null) {
        int logoWidth = 500;
        int logoHeight = 250;

        int logoX = (gamePanel.screenWidth - logoWidth) / 2; // Căn giữa ngang
        int logoY = 5; // Cách đỉnh màn hình 60px

        g.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);
        }
        drawButons(g);
    }
    

    private void drawButons(Graphics g){
        for (MyButton b : buttons) {
            b.draw(g);
        }
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
        System.exit(0); // Thoát game chuyên nghiệp
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

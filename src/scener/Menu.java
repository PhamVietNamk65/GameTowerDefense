package scener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger.Level;

import javax.imageio.ImageIO;

import main.GamePanel;
import ui.ButtonBar;
import ui.MyButton;

import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

    private GamePanel gamePanel;

    private BufferedImage logo, background;

    private ButtonBar quitMenu, settingsMenu, mainMenu;
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
        
        mainMenu = new ButtonBar(280, 250 , 400, 200);
        mainMenu.setOrientation(1, 20); // vertical, gap 20px
        mainMenu.addButton(new MyButton("LEVEL", 200, 50));
        mainMenu.addButton(new MyButton("SETTING", 200, 50));
        mainMenu.addButton(new MyButton("QUIT", 200, 50));
        mainMenu.visible = true;

        quitMenu = new ButtonBar(280, 214, 420, 100);
        quitMenu.setOrientation(0, 20); // horizontal, gap 20px
        quitMenu.addButton(new MyButton("YES", 200, 50)); 
        quitMenu.addButton(new MyButton("NO", 200, 50)); 
        quitMenu.visible = false;
    }

    @Override
    public void render(Graphics g) {
        // 1. Vẽ ảnh nền trước
        drawBackground(g);
        drawLogo(g);
        mainMenu.draw(g);
        if( quitMenu.visible ){
            drawOverlay(g);
            quitMenu.draw(g);
        }
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
        if( quitMenu.visible ){
            if( quitMenu.buttons.get(0).getBounds().contains(x, y))
                System.exit(0);
            else if( quitMenu.buttons.get(1).getBounds().contains(x, y)){
                quitMenu.visible = false;
                mainMenu.visible = true;
                return;
            }
            else if ( !quitMenu.getBounds().contains(x,y)){
                quitMenu.visible = false;
                mainMenu.visible = true;
                System.out.println("Click: " + x + " " + y);
                System.out.println("Quit bounds: " + quitMenu.getBounds()); 
            }    
            return;
        }
            
        if( mainMenu.visible )
            if( mainMenu.buttons.get(0).getBounds().contains(x,y) )
                gameStates = LEVEL;
            else if( mainMenu.buttons.get(1).getBounds().contains(x,y))
                gameStates = SETTING;
            else if( mainMenu.buttons.get(2).getBounds().contains(x,y)){
                quitMenu.visible = true;
                mainMenu.visible = false;
            }
            
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (mainMenu.visible) {
        for (MyButton button : mainMenu.buttons) {
            button.setMouseOver(button.getBounds().contains(x, y));
        }
    }

    if (quitMenu.visible) {
        for (MyButton button : quitMenu.buttons) {
            button.setMouseOver(button.getBounds().contains(x, y));
        }
    }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (mainMenu.visible) {
        for (MyButton button : mainMenu.buttons) {
            if (button.getBounds().contains(x, y)) {
                button.setMousePressed(true);
            }
        }
    }

    if (quitMenu.visible) {
        for (MyButton button : quitMenu.buttons) {
            if (button.getBounds().contains(x, y)) {
                button.setMousePressed(true);
            }
        }
    }
    }

    @Override
    public void mouseReleased(int x, int y) {
        if (mainMenu.visible) {
            for (MyButton button : mainMenu.buttons) {
                button.resetBooleans();
                }
        }

        if (quitMenu.visible) {
            for (MyButton button : quitMenu.buttons) {
                button.resetBooleans();
            }
        }
    }

  
}

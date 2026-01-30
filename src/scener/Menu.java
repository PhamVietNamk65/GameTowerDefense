package scener;
import java.awt.Graphics;
import main.GamePanel;
import ui.MyButton;

import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods{

    private GamePanel gamePanel;

    private MyButton bPlaying, bSetting, bQuit ;

    public Menu(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        initButton();
    }

    public void initButton(){
        bPlaying = new MyButton(100, 100, 100, 30, "Play");
    }
    @Override
    public void render(Graphics g) {
        drawButons(g);
    }
    
    private void drawButons(Graphics g){
        bPlaying.draw(g);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if(bPlaying.getBounds().contains(x, y)){
            SetGameState(PLAYING);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        bPlaying.setMouseOver(false);
        if(bPlaying.getBounds().contains(x, y)){
            bPlaying.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        bPlaying.setMousePressed(false);
        if(bPlaying.getBounds().contains(x, y)){
            bPlaying.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        resetButtons();
    }

    private void resetButtons() {
       bPlaying.resetBooleans();
    }
    
    
}

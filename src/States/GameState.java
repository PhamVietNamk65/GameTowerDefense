package states;
//trang thai cua game
import java.awt.Graphics;
public interface GameState {
    
    void update();
    void render(Graphics g);

    void mousePressed(int x, int y);
    void mouseReleased(int x, int y);
    void mouseMoved(int x, int y);
    
}

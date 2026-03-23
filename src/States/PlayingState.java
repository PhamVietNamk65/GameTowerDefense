package States;
import java.awt.Graphics;

import levels.LevelManager;
import main.GamePanel;

public class PlayingState implements GameState {

    private int level;
    private GamePanel gamePanel;
    private LevelManager levelManager;
    public PlayingState(GamePanel gamePanel,int level){
        this.level = level;
        this.gamePanel = gamePanel;

        levelManager = new LevelManager(level);
    
    }
    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }
    @Override
    public void mousePressed(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }
    @Override
    public void mouseReleased(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }
    @Override
    public void mouseMoved(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }

}
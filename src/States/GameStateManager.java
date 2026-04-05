package states;

import java.awt.Graphics;

public class GameStateManager {
    private GameState currentState;

    public void setState(GameState state){
        this.currentState = state;
    }

    public void update(){
        if(currentState != null)
            currentState.update();
    }

    public void render(Graphics g){
        if(currentState != null)
            currentState.render(g);
    }

    //xử lý input
    public void mousePressed(int x, int y){
        if(currentState != null)
            currentState.mousePressed(x, y);
    }

    public void mouseReleased(int x, int y){
        if(currentState != null)
            currentState.mouseReleased(x, y);
    }

    public void mouseMoved(int x, int y){
        if(currentState != null)
            currentState.mouseMoved(x, y);
    }
    
    public GameState getCurrentState(){
        return currentState;
    }
}
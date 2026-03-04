package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ButtonBar {
    
    private int x, y, width, height; // kich thuoc va vi tri
    private Rectangle bounds; // khung va cham cho 1 doi tuong (hitbox)
    public boolean visible; // trang thai hien thi
    public List<MyButton>  buttons; // danh sach cac nut trong button bar 
    private int orientation; // huong cua button bar (horizontal or vertical)
    private int gap; // khoang cach giua cac nut
    
    public ButtonBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttons = new ArrayList<>();
        initBounds();
    }
    
    public void setOrientation(int orientation, int gap) {
        this.orientation = orientation;
        this.gap = gap;
    }

    public void draw(Graphics g){
        drawButtons(g);
    }

    private void drawButtons( Graphics g){
        if(orientation == 0){ // horizontal
            int buttonWidth = (width - (buttons.size() - 1) * gap) / buttons.size();
            for(int i = 0; i < buttons.size(); i++){
                MyButton button = buttons.get(i);
                button.setButton(x + i * (buttonWidth + gap), y, buttonWidth, height);
                button.draw(g);
            }
        } else { // vertical
            int buttonHeight = (height - (buttons.size() - 1) * gap) / buttons.size();
            for(int i = 0; i < buttons.size(); i++){
                MyButton button = buttons.get(i);
                button.setButton(x, y + i * (buttonHeight + gap), width, buttonHeight);
                button.draw(g);
            }
        }
    }

    private void initBounds(){
        this.bounds = new Rectangle(x,y,width,height);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void addButton(MyButton button){
        buttons.add(button);
    }
    
}

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
    private List<MyButton>  buttons;
    public ButtonBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttons = new ArrayList<>();
        this.visible = false;
        initBounds();
    }
    
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0));
        g.fillRect(x, y, width, height);
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

package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.FontMetrics;

public class MyButton {

    private int x,y,width,height;
    private String text;
    private Rectangle bounds; // khung va cham cho 1 doi tuong (hitbox)
    private boolean mouseOver, mousePressed, mouseReleased; // kiem tra xem chuot co chi vao khong 

    public MyButton(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;

        initBounds();
    }
    
    private void initBounds(){
        this.bounds = new Rectangle(x, y, width, height);
    }
    public void draw(Graphics g){

        //body 
        drawBody(g);

        //Border    // vien
        drawBorder(g);

        //TEXT
        drawText(g);
    }
    private void drawBody(Graphics g){
        if( mouseOver )
            g.setColor(Color.GRAY);
        else 
            g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    private void drawBorder(Graphics g){
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        if( mousePressed ){
            g.drawRect(x + 1, y + 1, width - 2, height - 2);
            g.drawRect(x + 2, y + 2, width - 2, height - 2);
        }
    }

    private void drawText(Graphics g){
        FontMetrics fm = g.getFontMetrics(); // lay thong tin cua text
        
        int stringWidth = fm.stringWidth(text); //chieu dai cua text
        int stringHeight = fm.getHeight(); //chieu cao cua text
        int ascent = fm.getAscent(); // khoang cach tu duong co so den dinh cua text

        // Công thức căn giữa chuẩn
        int textX = x + (width - stringWidth) / 2;
        int textY = y + ((height - stringHeight) / 2) + ascent;
        
        g.drawString(text, textX, textY);
    }

    public void setMouseOver(boolean mouseOver){    // ham de gan gia tri cho mouseOver
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void resetBooleans() {
        this.mouseOver = false;
        this.mousePressed = false;
    }
}
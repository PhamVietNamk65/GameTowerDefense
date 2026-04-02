package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.FontMetrics;
import java.awt.Font;

public class MyButton {

    private int x,y,width,height;
    private String text;
    private Rectangle bounds; // khung va cham cho 1 doi tuong (hitbox)
    private boolean mouseOver, mousePressed, mouseReleased; // kiem tra xem chuot co chi vao khong 

    private BufferedImage normalImage, pressedImage, overImage;

    private Runnable action;

    boolean type ;
    public MyButton(String text , int width, int height) {
        this.width = width;
        this.height = height;
        this.text = text;
        this.type = false;
        initBounds();
    }

    public MyButton(BufferedImage normal,BufferedImage over,BufferedImage pressed,int width, int height ) {
        this.normalImage = normal;
        this.pressedImage = pressed;
        this.overImage = over;
        this.width = width;
        this.height = height;
        this.type = true;
        initBounds();
    }

    private void initBounds(){
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void setAction(Runnable action){
        this.action = action;
    }

    public void draw(Graphics g){
        if(type){
            drawImageButton(g);
        }
        else{
            //body 
            drawBody(g);

            //Border    // vien
            drawBorder(g);

            //TEXT
            drawText(g);
        }
        
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
            g.drawRect(x + 2, y + 2, width - 2, height - 4);
        }
    }

    private void drawText(Graphics g){
        Font myFont = new Font("Arial", Font.PLAIN, 22); 
        g.setFont(myFont); // Áp dụng Font này để vẽ
        g.setColor(Color.BLACK); // Đừng quên set màu chữ
        FontMetrics fm = g.getFontMetrics(); // lay thong tin cua text
        
        int stringWidth = fm.stringWidth(text); //chieu dai cua text
        int stringHeight = fm.getHeight(); //chieu cao cua text
        int ascent = fm.getAscent(); // khoang cach tu duong co so den dinh cua text

        // Công thức căn giữa chuẩn
        int textX = x + (width - stringWidth) / 2;
        int textY = y + ((height - stringHeight) / 2) + ascent;
        
        g.drawString(text, textX, textY);
    }

    public void drawImageButton(Graphics g){
         if(mousePressed){
            g.drawImage(pressedImage, x, y,width,height, null);
        }
        else if(mouseOver){
            g.drawImage(overImage, x, y, width, height, null);
        }
        else g.drawImage(normalImage, x, y, width, height, null);
    }

    public void execute(){
        if( action != null ) action.run();
    }
    
    public void setMouseOver(boolean mouseOver){    // ham de gan gia tri cho mouseOver
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }
    
    public Rectangle getBounds(){
        return bounds;
    }

    public void resetBooleans() {
        this.mouseOver = false;
        this.mousePressed = false;
    }

    public void setButton(int x2, int y, int width2, int buttonHeight) {
        this.x = x2;
        this.y = y;
        this.width = width2;
        this.height = buttonHeight;
        initBounds();
    }
}
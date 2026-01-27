package main;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16; // 16x16 title 
    final int scale = 3;
    public final int tileSize = originalTileSize * scale ; // 48x48 title
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    private Thread gameThread;

    // FPS
    int FPS = 60;

    private Random random;
    
    public GamePanel(){
        this.setPreferredSize(new DimensionUIResource(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(getFocusTraversalKeysEnabled());
        this.setFocusable(true);
        
        random = new Random();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for(int x = 0; x < 16; x++ ){
            for(int y = 0; y < 12; y++ ){
                g.setColor(getRndColor());
                g.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
            }
        }
        
    }    

    private Color getRndColor(){
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r,g,b);
    }

    public void startGameThread(){      //Goi vong lap
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){   //cap nhat Frame
        getRndColor();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime(); // tg ve truoc day
        long currenTime;    // tg hien tai
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null){
            currenTime = System.nanoTime();
            delta += (currenTime - lastTime)/drawInterval; 
            timer += (currenTime - lastTime);
            lastTime = currenTime;
            if( delta >= 1 ){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

}

package main;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import inputs.MyMouseListener;

import java.awt.Color;
import java.awt.Graphics;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16; // 16x16 title 
    final int scale = 3;
    public final int tileSize = originalTileSize * scale ; // 48x48 title
    final int maxScreenCol = 20;
    final int maxScreenRow = 11;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    private Thread gameThread;

    // FPS
    int FPS = 60;


    
    private MyMouseListener myMouseListener = new MyMouseListener();
    private Render render;

    public GamePanel(){
        this.setPreferredSize(new DimensionUIResource(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(getFocusTraversalKeysEnabled());
        this.setFocusable(true);

        render = new Render(this);
        
    }

    public void initInputs(){
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);

        requestFocus();
    }

    public void startGameThread(){      //Goi vong lap
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){   //cap nhat Frame
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
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        render.render(g);
    } 
}

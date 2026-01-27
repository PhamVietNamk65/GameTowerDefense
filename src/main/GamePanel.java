package main;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class GamePanel extends JPanel{
    final int originalTileSize = 16; // 16x16 title 
    final int scale = 3;
    public final int tileSize = originalTileSize * scale ; // 48x48 title
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Thread thread;
            
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

        for(int x = 0; x < 25; x++ ){
            for(int y = 0; y < 25; y++ ){
                g.setColor(getRndColor());
                g.fillRect(x*32, y*32, 32, 32);
            }
        }
        
    }    
    private Color getRndColor(){
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r,g,b);
    }

}

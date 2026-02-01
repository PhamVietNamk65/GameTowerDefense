package main;

import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import inputs.KeyHandler;
import inputs.MyMouseListener;
import scener.Level;
import scener.Menu;
import scener.Playing;
import scener.Setting;

import java.awt.Color;
import java.awt.Graphics;


public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16; // 16x16 title 
    final int scale = 3;     
    public final int tileSize = originalTileSize * scale ; // 48x48 title
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 11;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    private Thread gameThread;

    // FPS
    private int FPS = 60;

    private Menu menu;
    private Playing playing;
    private Setting setting;
    private Level level;

    private MyMouseListener myMouseListener;
    private KeyHandler keyH = new KeyHandler();
    
    private Render render;

    public GamePanel(){
        this.setPreferredSize(new DimensionUIResource(screenWidth, screenHeight));
        this.setBackground(Color.white);
        setDoubleBuffered(true);
        this.setFocusable(true);

        initClasses();
        initInputs();
    }
    // nham nhan tin hieu chon che do cua trang ( trang menu hay trang playing ... )
    private void initClasses() {
        render = new Render(this);
        menu = new Menu(this);
        playing = new Playing(this);
        setting = new Setting(this);
        level = new Level(this);

        myMouseListener = new MyMouseListener(this);
    }

    // ham nhan tin hieu lenh tu chuot
    public void initInputs(){

        addKeyListener(keyH);

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

        double drawInterval = 1000000000/FPS; // thoi gian ve 1 khung hinh 
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
        super.paintComponent(g); // xoa trang de ve truoc day
        render.render(g);
    } 

    // lấy giá trị và gán giá trị
    public Render getRender(){
        return render;
    }
    //trang menu game
    public Menu getMenu() {
        return menu;
    }
    //trang man choi 
    public Playing getPlaying() {
        return playing;
    }
    // trang cai dat 
    public Setting getSetting() {
        return setting;
    }
    public Level getLevel(){
        return level;
    }
}

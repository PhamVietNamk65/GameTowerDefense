package main;

import inputs.KeyHandler;
import inputs.MyMouseListener;
import states.GameStateManager;
import states.MenuState;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.ProgressBarUI;

import Manager.ProgressManager;
import asset.AssetLoad;
import inputs.KeyHandler;
import inputs.MyMouseListener;
import ui.LevelSelect;
import ui.Menu;
import utils.Constants;

import java.awt.Color;
import java.awt.Graphics;


public class GamePanel extends JPanel implements Runnable{

    private Thread gameThread;

    // FPS
    private int FPS = 60;

    private AssetLoad assetLoad;
    // private PlayingState playing;
    // private Setting setting;

    private MyMouseListener myMouseListener;
    private KeyHandler keyH;
    
    private Render render;
    private GameStateManager gameStateManager;

    private ProgressManager progressManager;
    public GamePanel(){
        this.setPreferredSize(new DimensionUIResource(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.setBackground(Color.white);
        setDoubleBuffered(true); // tang hieu suat ve
        this.setFocusable(true); // de JPanel co the nhan duoc su kien tu ban phim

        
        keyH = new KeyHandler(this);
        progressManager = new ProgressManager(Constants.TOTAL_LEVELS);

        initClasses(); 
        initInputs();   
    }


    // khoi tao cac lop can thiet
    private void initClasses() {
        assetLoad = new AssetLoad(); // khoi tao asset manager
        assetLoad.loadAllAssets(); // load tat ca asset
        
        gameStateManager = new GameStateManager(); // khoi tao game state manager
        gameStateManager.setState(new MenuState(this)); // dat trang hien tai la menu

        render = new Render(this); // khoi tao lop render de ve theo trang hien tai

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

    public void update(){ //cap nhat Frame
          gameStateManager.update();
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
            currenTime = System.nanoTime(); // lay tg hien tai
            delta += (currenTime - lastTime)/drawInterval; // tinh toan so khung hinh can ve
            timer += (currenTime - lastTime); // dem tg
            lastTime = currenTime; // cap nhat tg ve truoc day
            if( delta >= 1 ){
                update(); // cap nhat khung hinh
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
        render.render(g); // goi ham render de ve theo trang hien tai
    } 

    public void render(Graphics g){
        gameStateManager.render(g);
    }

    public GameStateManager getGameStateManager(){
        return gameStateManager;
    }
    
    public ProgressManager getProgressManager() {
        return progressManager;
    }
}
package main;

import javax.swing.JFrame;
public class Window extends JFrame{

    public Window( GamePanel gamePanel){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("2D game");
        add(gamePanel);
        pack();

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }   
   
}

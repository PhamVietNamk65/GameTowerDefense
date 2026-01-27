package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame windown = new JFrame();
        windown.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windown.setTitle("2D game");

        GamePanel gamePanel = new GamePanel();
        windown.add(gamePanel);
        windown.pack();

        windown.setLocationRelativeTo(null);
        windown.setVisible(true);
        
    }
        
}

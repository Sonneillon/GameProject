package src;

import src.core.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Street Brawler");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            GamePanel gamePanel = new GamePanel();
            Input input = new Input();
            
            frame.add(gamePanel);
            frame.addKeyListener(input);
            gamePanel.addKeyListener(input);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            gamePanel.requestFocusInWindow();
            
            new Game(gamePanel, input).start();
        });
    }
}
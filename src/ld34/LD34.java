package ld34;

import java.awt.Dimension;
import javax.swing.JFrame;

public class LD34 {
    public static void main(String[] args) {
        Game g = new Game(800, 600);
        
        JFrame frame = new JFrame();
        frame.setTitle("LD34 - Story Into Jungle");
        frame.add(g);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        g.start();
    }
}

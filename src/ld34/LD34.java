package ld34;

import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class LD34 {
    public static void main(String[] args) {
        Game g = new Game(800, 600);
        
        JFrame frame = new JFrame();
        frame.setTitle("Story Into Jungle - v1.0");
        frame.add(g);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        URL iconUrl = LD34.class.getResource("/storyintojungle.png");
        ImageIcon icon = new ImageIcon(iconUrl);
        frame.setIconImage(icon.getImage());
        
        g.start();
    }
}

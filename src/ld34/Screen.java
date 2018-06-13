package ld34;

import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Screen extends JFrame {
    
    public Screen(){
        Game g = new Game(800, 600);
        
        this.setTitle("Story Into Jungle - v2.0");
        this.add(g);
        this.getContentPane().setPreferredSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        URL iconUrl = getClass().getResource("/storyintojungle.png");
        ImageIcon icon = new ImageIcon(iconUrl);
        this.setIconImage(icon.getImage());
        
        g.start();
    }
    
}

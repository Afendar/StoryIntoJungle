package ld34;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Screen extends JFrame {
    
    public Screen(){
        Game g = new Game(800, 600);
        
        this.init(g);
        
        g.start();
    }
    
    private void init(Game g){
        this.setTitle("Story Into Jungle - v" + Defines.VERSION);
        this.add(g);
        this.getContentPane().setPreferredSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(getClass().getResource("/cursor.png")).getImage(), new Point(0,0), "cursorSIJ"));
        URL iconUrl = getClass().getResource("/storyintojungle.png");
        ImageIcon icon = new ImageIcon(iconUrl);
        this.setIconImage(icon.getImage());
    }
    
}

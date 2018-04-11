package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Screen class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Screen extends JFrame 
{
    /**
     * 
     */
    public Screen()
    {
        Game g = new Game();
        
        init(g);
        
        g.start();
    }
    
    /**
     * 
     * @param g 
     */
    private void init(Game g)
    {
        setTitle("Story Into Jungle - v" + Defines.VERSION);
        add(g);
        getContentPane().setPreferredSize(new Dimension(Defines.SCREEN_WIDTH, Defines.SCREEN_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new ImageIcon(getClass().getResource("/cursor.png")).getImage(), new Point(0,0), "cursorSIJ"));
        URL iconUrl = getClass().getResource("/storyintojungle.png");
        ImageIcon icon = new ImageIcon(iconUrl);
        setIconImage(icon.getImage());
    }
}

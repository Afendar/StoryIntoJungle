package core;

import audio.Sound;
import ld34.profile.Settings;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public final class CustomTextField extends JComponent {
    private boolean isEditing;
    private String value, name;
    private int x, y, w, h;
    private final long threshold = 150;
    private long lastPress;
    private BufferedImage background;
    
    public CustomTextField(String name, String value, int x, int y, int w, int h){
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.name = name;
        this.isEditing = false;
        this.value = value;
        this.lastPress = System.currentTimeMillis();
        
        this.setFont(new Font("Arial", Font.PLAIN, 12));
        try{
            URL url = this.getClass().getResource("/gui2.png");
            this.background = ImageIO.read(url);
            this.background = this.background.getSubimage(1, 501, 287, 46);
        }
        catch(IOException e){
            e.getMessage();
        }
    }
    
    public CustomTextField(String name, String value, int w, int h){
        this(name, value, 0, 0, w, h);
    }
    
    @Override
    public void setFont(Font font){
        super.setFont(font);
    }
    
    public boolean isEditing(){
        return this.isEditing;
    }
    
    private void configure(KeyEvent e){
        if(e.getKeyCode() > 47 && e.getKeyCode() < 91){
            long now = System.currentTimeMillis();
            FontMetrics metrics = this.getFontMetrics(this.getFont());
            int charW = metrics.stringWidth(this.value);
            
            if(now - lastPress > threshold && charW < this.w){
                this.value += e.getKeyChar();
                Settings.getInstance().setConfigValue("Name", this.value);
                lastPress = now;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            
            long now = System.currentTimeMillis();
            
            
            if(now - lastPress > threshold){
                
                this.value += e.getKeyChar();
                lastPress = now;
                if(this.value.length() > 1)
                    this.value = this.value.substring(0, this.value.length() - 2);
                else{
                    this.value = "";
                }
                Settings.getInstance().setConfigValue("Name", this.value);
            }
        }
    }
    
    public String getValue(){
        return this.value;
    }
    
    private void editing(){
        this.requestFocus(true);
        this.isEditing = true;
        if(this.value.equals("Enter name"))
            this.value = "";
    }
    
    public void render(Graphics g){
        g.drawImage(this.background, this.x, this.y, null);
        if(this.value.equals("Enter name")){
            g.setColor(Color.DARK_GRAY);
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.setFont(this.getFont());
        FontMetrics metrics = this.getFontMetrics(this.getFont());
        g.drawString(this.value, x + 20, y + 20 + metrics.getAscent()/2);
        if(this.isEditing){
            int stringWidth = metrics.stringWidth(this.value);
            g.drawLine(this.x + 23 + stringWidth, this.y + 13, this.x + 23 + stringWidth, this.y + this.h - 13);
        }
    }
    
    public void processClick(int x, int y){
        if(x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h){
            editing();
            new Thread(Sound.select::play).start();
        }
        else{
            if(this.isEditing){
                    new Thread(Sound.select::play).start();
            }
            this.isEditing = false;
            if(this.value.equals("")){
                this.value = "Enter name";
            }
            Settings.getInstance().setConfigValue("Enter name", this.value);
        }
    }
    
    public void processKey(KeyEvent e){
        if(this.isEditing){
            if(this.value.equals("Enter name"))this.value = "";
            configure(e);
        }
    }
}

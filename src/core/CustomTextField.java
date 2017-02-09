package core;

import ld34.profile.Settings;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;

public final class CustomTextField extends JComponent {
    private boolean isEditing;
    private String value, name;
    private int x, y, w, h;
    private final long threshold = 150;
    private long lastPress;
    
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
        if(this.value.equals(""))
            this.value = "Enter name";
    }
    
    public void render(Graphics g){
        g.setColor(new Color(200,200,200));
        g.fillRect(this.x - 15, this.y - this.h - 7, this.w + 30, this.h + 14);
        g.setColor(Color.BLACK);
        g.setFont(this.getFont());
        g.drawString(this.value, x, y);
    }
    
    public void processClick(int x, int y){
        if(x > this.x && x < this.x + this.w && y < this.y && y > this.y - this.h){
            editing();
        }
        else{
            this.isEditing = false;
            if(this.value.equals("Enter name")){
                this.value = "";
            }
            Settings.getInstance().setConfigValue("Name", this.value);
        }
    }
    
    public void processKey(KeyEvent e){
        if(this.isEditing){
            if(this.value.equals("Enter name"))this.value = "";
            configure(e);
        }
    }
}

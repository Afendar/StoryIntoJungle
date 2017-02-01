package ld34;

import ld34.profile.Settings;
import audio.Sound;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComponent;

public final class OptionButton extends JComponent{
    
    private boolean isEditing;
    private String value;
    private int x, y, w, h;
    private String text, name, pressKey;
    public Locale langs[] = {new Locale("en","EN"), new Locale("fr", "FR")};
    public ResourceBundle bundle;
    
    public OptionButton(String value, String name, int x, int y){
        super();
        this.text = value;
        this.name = name;
        this.value = value;
        this.x = x;
        this.y = y;
        this.setFont(new Font("Arial", Font.PLAIN, 12));
        
        FontMetrics metrics = this.getFontMetrics(this.getFont());
        this.h = metrics.getHeight();
        this.w = metrics.stringWidth(this.text);
        
        this.initLocales();
    }
    
    public void initLocales(){
        this.bundle = ResourceBundle.getBundle("lang.options", this.langs[Integer.parseInt(Settings.getInstance().getConfigValue("Lang"))]);
        this.pressKey = this.bundle.getString("pressKey");
    }
    
    public OptionButton(String value, String name){
        this(value, name, 0, 0);
    }
    
    public void setText(String text){
        this.text = text;
        FontMetrics metrics = this.getFontMetrics(this.getFont());
        this.w = metrics.stringWidth(this.text);
    }
    
    @Override
    public void setFont(Font font){
        super.setFont(font);
        FontMetrics metrics = this.getFontMetrics(this.getFont());
        this.w = metrics.stringWidth(this.text);
    }
    
    public boolean isEditing(){
        return this.isEditing;
    }
    
    private void configure(KeyEvent e){
        this.isEditing = false;
        this.value = Integer.toString(e.getKeyCode());
        Settings.getInstance().setConfigValue(this.name, this.value);
        this.setText(KeyEvent.getKeyText(e.getKeyCode()));
    }
    
    public Object getValue(){
        return this.value;
    }
    
    private void editing(){
        this.requestFocus(true);
        this.isEditing = true;
        this.setText(this.pressKey);
    }
    
    public void render(Graphics g){
        g.setColor(new Color(200,200,200));
        g.fillRect(this.x - 15, this.y - this.h - 7, this.w + 30, this.h + 14);
        g.setColor(Color.BLACK);
        g.setFont(this.getFont());
        g.drawString(text, x, y);
    }
    
    public void processClick(int x, int y){
        if(x > this.x - 15 && x < this.x + this.w + 15 && y < this.y + 7 && y > this.y - this.h - 7){
            new Thread(Sound.select::play).start();
            editing();
        }
    }
    
    public void processKey(KeyEvent e){
        if(this.isEditing){
            configure(e);
        }
    }
}

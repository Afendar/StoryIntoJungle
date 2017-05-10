package core;

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
        
        this.w = 212;
        this.h = 88;
        
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
    }
    
    @Override
    public void setFont(Font font){
        super.setFont(font);
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
        //g.setColor(new Color(200,200,200,230));
        //g.fillRect(this.x, this.y, this.w, this.h);
        g.setColor(Color.BLACK);
        g.setFont(this.getFont());
        FontMetrics metrics = g.getFontMetrics(this.getFont());
        int stringWidth = metrics.stringWidth(this.text);
        
        if(metrics.stringWidth(this.text) >= this.w - 40){
            String[] textTab = new String[2];
            for(int i=this.text.length() - 1;i > 0;i--){
                if(this.text.charAt(i) == ' '){
                    textTab[0] = this.text.substring(0, i);
                    textTab[1] = this.text.substring(i);
                    break;
                }
            }
            
            for(int i=0;i<textTab.length;i++){
                stringWidth = metrics.stringWidth(textTab[i]);
                g.drawString(textTab[i], this.x + this.w/2 - stringWidth/2, this.y + metrics.getAscent()/2 + ((i+1) * this.h/4) - 2);
            }
        }
        else{
            g.drawString(this.text, x + this.w/2 - stringWidth/2, y + metrics.getAscent()/2 + this.h/2 - 12);
        }
    }
    
    public void processClick(int x, int y){
        if(x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h){
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

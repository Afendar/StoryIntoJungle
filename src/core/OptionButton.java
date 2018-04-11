package core;

import ld34.profile.Settings;
import audio.Sound;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import javax.swing.JComponent;

/**
 * OptionButton component. ( Used to set controls into Game )
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public final class OptionButton extends JComponent
{
    private boolean isEditing;
    private String value;
    private int x, y, w, h;
    private String text, name;
    public ResourceBundle bundle;
    
    /**
     * Main OptionButton constructor
     * @param value Value of button
     * @param name Name of button
     * @param x Position x axis
     * @param y Position y axis
     */
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
    }
    
    /**
     * Override of OptionButton's main constructor
     * @param value Value of button
     * @param name Name of button
     */
    public OptionButton(String value, String name){
        this(value, name, 0, 0);
    }
    
    /**
     * Setter of text into button.
     * @param text Text appear in button component
     */
    public void setText(String text){
        this.text = text;
    }
    
    @Override
    public void setFont(Font font){
        super.setFont(font);
    }
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getX(){
        return this.x;
    }
    
    @Override
    public int getY(){
        return this.y;
    }
    
    @Override
    public void setSize(int w, int h){
        this.w = w;
        this.h = h;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isEditing(){
        return this.isEditing;
    }
    
    /**
     * 
     * @param e 
     */
    private void configure(KeyEvent e){
        this.isEditing = false;
        this.value = Integer.toString(e.getKeyCode());
        Settings.getInstance().setConfigValue(this.name, this.value);
        this.setText(KeyEvent.getKeyText(e.getKeyCode()));
    }
    
    /**
     * 
     * @return 
     */
    public Object getValue(){
        return this.value;
    }
    
    /**
     * 
     */
    private void editing(){
        requestFocus(true);
        isEditing = true;
        setText(I18nManager.getInstance().trans("pressKey"));
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g){
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
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void processClick(int x, int y){
        if(x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h){
            new Thread(Sound.select::play).start();
            editing();
        }
    }
    
    /**
     * 
     * @param e 
     */
    public void processKey(KeyEvent e){
        if(this.isEditing){
            configure(e);
        }
    }
}

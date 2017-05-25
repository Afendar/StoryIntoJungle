package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import ld34.profile.Settings;

/**
 * CustomDialog class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class CustomDialog {
    
    private List<JComponent> components;
    private String title, messageText;
    private int messageType;
    private JRootPane rootPane;
    private String[] options;
    private int optionIndex, selectedItem, value;
    private Font font;
    private Game game;
    private BufferedImage spritesheet, background, bgBtn;
    
    public Locale langs[] = {new Locale("en","EN"), new Locale("fr", "FR")};
    
    /**
     * 
     */
    public CustomDialog(){
        components = new ArrayList<>();
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 16.0f);
            url = this.getClass().getResource("/gui.png");
            this.spritesheet = ImageIO.read(url);
            this.background = this.spritesheet.getSubimage(214, 0, 300, 200);
            this.bgBtn = this.spritesheet.getSubimage(0, 70, 107, 40);
        }
        catch(IOException | FontFormatException e){
            System.out.println(e.getMessage());
        }
        
        this.setTitle("Custom dialog");
        this.setMessageType(JOptionPane.PLAIN_MESSAGE);
        this.setRootPane(null);
        
        ResourceBundle bundle = ResourceBundle.getBundle("lang.lang", this.langs[Integer.parseInt(Settings.getInstance().getConfigValue("Lang"))]);
        String option1 = bundle.getString("yes");
        String option2 = bundle.getString("no");
        
        this.setOptions(new String[] {option1, option2});
        this.setOptionSelected(0);
        this.setMessageText("");
        this.setGame(null);
        this.selectedItem = 0;
        this.value = 0;
    }
    
    /**
     * 
     * @param title 
     */
    public void setTitle(String title){
        this.title = title;
    }
    
    /**
     * 
     * @param messageType 
     */
    public void setMessageType(int messageType){
        this.messageType = messageType;
    }
    
    /**
     * 
     * @param component 
     */
    public void addComponent(JComponent component){
        this.components.add(component);
    }
    
    /**
     * 
     * @param messageText 
     */
    public void setMessageText(String messageText){
        this.messageText = messageText;
    }
    
    /**
     * 
     * @param game 
     */
    public void setGame(Game game){
        this.game = game;
    }
    
    /**
     * 
     * @param rootPane 
     */
    public void setRootPane(JRootPane rootPane){
        this.rootPane = rootPane;
    }
    
    /**
     * 
     * @param options 
     */
    public void setOptions(String[] options){
        this.options = options;
    }
    
    /**
     * 
     * @param optionIndex 
     */
    public void setOptionSelected(int optionIndex){
        this.optionIndex = optionIndex;
    }
    
    /**
     * 
     * @return 
     */
    public int show(){
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        Object optionSelection = null;
        
        if(this.options.length != 0){
            optionSelection = this.options[this.optionIndex];
        }
        
        int selection = JOptionPane.showOptionDialog(
                this.rootPane, 
                this.components.toArray(), 
                this.title, 
                optionType, 
                this.messageType, 
                null, 
                this.options, 
                optionSelection);
        
        return selection;
    }
    
    /**
     * 
     */
    public void update(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        this.processHover(mouseX, mouseY);
        this.processClick();
    }
    
    /**
     * 
     * @param mouseX
     * @param mouseY 
     */
    private void processHover(int mouseX, int mouseY){
        if(mouseX > 275 && mouseX < 275 + 107 && mouseY > 345 && mouseY < 345 + 70){
            this.selectedItem = 1;
        }
        else if(mouseX > 420 && mouseX < 420 + 107 && mouseY > 345 && mouseY < 345 + 70){
            this.selectedItem = 2;
        }
        else{
            this.selectedItem = 0;
        }
    }
    
    /**
     * 
     */
    private void processClick(){
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1)
        {
            switch(this.selectedItem){
                case 1:
                    this.value = 1;
                    break;
                case 2:
                    this.value = 2;
                    break;
            }
        }
    }
    
    /**
     * 
     * @param g 
     */
    public void render(Graphics g){
        g.drawImage(this.background, 250, 200, null);
        
        g.setColor(Color.BLACK);
        g.setFont(this.font);
        g.drawString(this.messageText, 280, 250);
        
        g.drawImage(this.bgBtn, 275, 345, null);
        if(this.selectedItem == 1){
            g.setColor(new Color(128, 0, 19));
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.drawString(this.options[0], 315, 370);
        
        g.drawImage(this.bgBtn, 420, 345, null);
        if(this.selectedItem == 2){
            g.setColor(new Color(128, 0, 19));
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.drawString(this.options[1], 465, 370);   
    }
    
    /**
     * 
     * @return 
     */
    public int getValue(){
        return this.value;
    }
}

package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

public class CustomDialog {
    private List<JComponent> components;
    
    private String title, messageText;
    private int messageType;
    private JRootPane rootPane;
    private String[] options;
    private int optionIndex;
    private Font font;
    private Game game;
    
    public CustomDialog(){
        components = new ArrayList<>();
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 16.0f);
        }
        catch(IOException | FontFormatException e){
            System.out.println(e.getMessage());
        }
        
        this.setTitle("Custom dialog");
        this.setMessageType(JOptionPane.PLAIN_MESSAGE);
        this.setRootPane(null);
        this.setOptions(new String[] {"Oui", "Non"});
        this.setOptionSelected(0);
        this.setMessageText("");
        this.setGame(null);
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setMessageType(int messageType){
        this.messageType = messageType;
    }
    
    public void addComponent(JComponent component){
        this.components.add(component);
    }
    
    public void setMessageText(String messageText){
        this.messageText = messageText;
    }
    
    public void setGame(Game game){
        this.game = game;
    }
    
    public void setRootPane(JRootPane rootPane){
        this.rootPane = rootPane;
    }
    
    public void setOptions(String[] options){
        this.options = options;
    }
    
    public void setOptionSelected(int optionIndex){
        this.optionIndex = optionIndex;
    }
    
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
    
    public void update(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        this.processHover(mouseX, mouseY);
        this.processClick(mouseX, mouseY);
    }
    
    private void processHover(int mouseX, int mouseY){
        
    }
    
    private void processClick(int mouseX, int mouseY){
        
    }
    
    public void render(Graphics g){
        g.setColor(new Color(223, 211, 153));
        g.fillRect(250, 200, 300, 200);
        g.setColor(Color.BLACK);
        g.setFont(this.font);
        g.drawString(this.messageText, 270, 230);
        
        g.setColor(Color.GREEN);
        g.drawRect(275, 345, 120, 35);
        g.setColor(Color.BLACK);
        g.drawString("Yes", 280, 390);
        
        g.setColor(Color.RED);
        g.drawRect(405, 345, 120, 35);
        g.setColor(Color.BLACK);
        g.drawString("No", 415, 390);
        
    }
}

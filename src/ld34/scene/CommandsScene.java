package ld34.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import ld34.profile.Settings;
import ld34.Game;
import ld34.OptionButton;

public class CommandsScene extends Scene {

    public String title, btnBack, controlJump, controlWalk;
    public Font font, fontL, fontU;
    public int[][] btnCoords;
    public int selectedItem;
    public ArrayList<OptionButton> optionButtons = new ArrayList<>();
    
    public CommandsScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            this.fontU = this.font;
            this.fontU = this.fontU.deriveFont(fontAttributes);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        OptionButton btn1 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))), 
                "Jump", 
                150, 
                200
        );
        btn1.setFont(this.font);
        this.optionButtons.add(btn1);
        OptionButton btn2 = new OptionButton(
                KeyEvent.getKeyText(Integer.parseInt(Settings.getInstance().getConfigValue("Walk"))), 
                "Walk", 
                150, 
                250
        );
        btn2.setFont(this.font);
        this.optionButtons.add(btn2);
        
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.commands", this.game.langs[localeIndex]);
        
        this.btnBack = this.bundle.getString("backToMain");
        this.title = this.bundle.getString("title");
        this.controlJump = this.bundle.getString("ctrlJump");
        this.controlWalk = this.bundle.getString("ctrlWalk");
    }
    
    @Override
    public Scene update(double dt) {
        processHover();
        
        if(this.game.listener.e != null){
            this.processKey(this.game.listener.e);
        }
        return processClick();
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background, 0, 0, null);
        
        FontMetrics metrics = g.getFontMetrics(this.fontL);
        g.setFont(this.fontL);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 60);
        
        g.setFont(this.font);
        metrics = g.getFontMetrics(this.font);
        g.drawString(this.controlJump, 50, 200);
        
        g.drawString(this.controlWalk, 50, 250);
        
        int backWidth = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        if(this.selectedItem == 1){
            g2d.rotate(-0.1, (3*this.w/4)+25, 475);
            g.setColor(this.darkGreen);
            g.drawString(this.btnBack, (3*this.w/4) + 25 - backWidth/2, 495);
            g2d.rotate(0.1, (3*this.w/4)+25, 475);
        }
        else{
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, (3*this.w/4) + 25 - backWidth/2, 495);
        }
        
        g.drawImage(this.foreground2, 0, 0, null);
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).render(g);
        }
    }
    
    public void processHover(){
        
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 214 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 70 ){
            this.selectedItem = 1;
        }
        else{
            this.selectedItem = 0;
        }
    }
    
    public void processKey(KeyEvent e){
        for(int i=0;i<this.optionButtons.size();i++){
            if(this.optionButtons.get(i).isEditing()){
                this.optionButtons.get(i).processKey(e);
            }
        }
    }
    
    public Scene processClick(){
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            
            this.processButtonsClick();
            
            switch(this.selectedItem){
                case 1:
                    Settings.getInstance().saveConfig();
                    currentScene = new OptionsScene(this.w, this.h, this.game);
                    break;
                default:
                    currentScene = this;
                    break;
            }
        }
        
        return currentScene;
    }
    
    public void processButtonsClick(){
        for(int i=0;i<this.optionButtons.size();i++){
            if(this.optionButtons.get(i).isEditing())
                return;
        }
        
        for(int i=0;i<this.optionButtons.size();i++){
            this.optionButtons.get(i).processClick(this.game.listener.mouseX, this.game.listener.mouseY);
        }
    }
}

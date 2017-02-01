package ld34.scene;

import audio.Sound;
import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Camera;
import ld34.profile.Settings;
import ld34.Game;
import ld34.profile.Save;
import level.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SavesScene extends Scene {

    private String btnBack, emptyTxt, easy, medium, hard, hardcore, title;
    private Font font, fontL, fontS;
    private BufferedImage gui, bgSave, levelIcon, removeIcon, cageIcon, loadIcon, dollardIcon;
    private int selectedItem, selectedSave, selectRemove, selectLoad;
    private int[][] btnCoords;
    private JSONObject jsonSaves;
    private Color kaki;
    
    public SavesScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 16.0f);
            this.fontL = this.font.deriveFont(Font.PLAIN, 36.0f);
            
            url = this.getClass().getResource("/gui.png");
            this.gui = ImageIO.read(url);
            this.bgSave = this.gui.getSubimage(0, 282, 500, 118);
            this.removeIcon = this.gui.getSubimage(152, 110, 24, 20);
            this.cageIcon = this.gui.getSubimage(150, 130, 33, 32);
            this.levelIcon = this.gui.getSubimage(183, 135, 32, 26);
            this.loadIcon = this.gui.getSubimage(153, 163, 22, 24);
            this.dollardIcon = this.gui.getSubimage(154, 188, 20, 24);
        }
        catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        int [][]coords = {
            {(3*this.w/4) - 80, 455}
        };
        
        this.btnCoords = coords;
        
        this.bundle = ResourceBundle.getBundle("lang.saves", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("loadGame");
        this.btnBack = this.bundle.getString("backToMain");
        this.emptyTxt = this.bundle.getString("empty");
        this.easy = this.bundle.getString("easy");
        this.medium = this.bundle.getString("medium");
        this.hard = this.bundle.getString("hard");
        this.hardcore = this.bundle.getString("hardcore");
        
        this.selectedItem = 0;
        this.selectedSave = 0;
        this.selectRemove = 0;
        this.selectLoad = 0;
        
        this.jsonSaves = Save.getInstance().getSaves();
        this.kaki = new Color(176, 173, 137);
    }
    
    @Override
    public Scene update(double dt) {
        this.processHover();
        
        return this.processClick();
    }

    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
         if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 214 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 70){
            //if btn Back
            if(this.selectedItem != 1)
                new Thread(Sound.hover::play).start();
            this.selectedItem = 1;
        }
         else{
            this.selectedItem = 0;
        }
         
        if(mouseX > 150 && mouseX < 650 && mouseY > 80 && mouseY < 198 ){
            this.selectedSave = 1;
        }
        else if(mouseX > 150 && mouseX < 650 && mouseY > 205 && mouseY < 323){
            this.selectedSave = 2;
        }
        else if(mouseX > 150 && mouseX < 650 && mouseY > 330 && mouseY < 448){
            this.selectedSave = 3;
        }
        else{
            this.selectedSave = 0;
        }
         
         this.selectRemove = 0;
         this.selectLoad = 0;
         for(int i=0;i<this.jsonSaves.size();i++){
             //cancel btns
             if(mouseX > this.w - 195 && mouseX < this.w - 171 && mouseY > ((i * 125) + 100) && mouseY < ((i * 125) + 120)){
                this.selectRemove = i + 1;
             }
             if(mouseX > this.w - 193 && mouseX < this.w - 171 && mouseY > (i * 125) + 130 && mouseY < (i * 125) + 154){
                 this.selectLoad = i + 1;
             }
         }
         
    }
    
    public Scene processClick(){
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItem){
                case 1:
                    Settings.getInstance().saveConfig();
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                default:
                    currentScene = this;
                    break;
            }
            
            if(this.selectRemove != 0){
                Save.getInstance().removeSave(this.selectRemove - 1);
            }
            
            if(this.selectLoad != 0){
                JSONObject save = Save.getInstance().getSave(this.selectLoad - 1);
                if(save != null){
                    JSONObject jsonLevel = (JSONObject)save.get("level");
                    JSONObject jsonPlayer = (JSONObject)save.get("player");
                    Level level = new Level(Integer.parseInt((String)jsonLevel.get("number")));
                    JSONArray coords = (JSONArray)jsonPlayer.get("coords");
                    Camera cam = new Camera(Math.toIntExact((Long)coords.get(0)), Math.toIntExact((Long)coords.get(1)), this.w, this.h, level);
                    Player player = new Player(Math.toIntExact((Long)coords.get(0)), Math.toIntExact((Long)coords.get(1)), level, this.game.listener, cam, Integer.parseInt((String)jsonLevel.get("difficulty")));
                    player.score = Integer.parseInt((String)jsonPlayer.get("score"));
                    currentScene = new GameScene(this.w, this.h, this.game, level, player);
                }
            }
        }
        
        return currentScene;
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
        
        metrics = g.getFontMetrics(this.font);
        g.setFont(this.font);
        
        for(int i=0;i<this.jsonSaves.size();i++){
            if(this.selectedSave == i + 1){
                g.drawImage(this.gui.getSubimage(0, 400, 500, 118), 150, (i * 125) + 80, null);
            }
            else{
                g.drawImage(this.bgSave, 150, (i * 125) + 80, null);
            }
            JSONObject save = (JSONObject) this.jsonSaves.get("Slot" + i);
            if(save.isEmpty()){
                int emptyTxtWidth = metrics.stringWidth(this.emptyTxt);
                g.drawString(this.emptyTxt, (this.w - emptyTxtWidth)/2, (i * 125) + 80 + ((118 + metrics.getAscent())/2));
            }
            else{
                String slotNumber = "#" + (i + 1);
                g.drawString(slotNumber, 170, ( i * 125) + 115);
                
                g.setColor(this.kaki);
                g.fillRoundRect(208, (i * 125) + 104 , 70, 70, 5, 5);
                g.fillRoundRect(290, (i * 125) + 104, 150, 32, 5, 5);
                g.fillRoundRect(290, (i * 125) + 142, 70, 32, 5, 5);
                g.fillRoundRect(365, (i * 125) + 142, 90, 32, 5, 5);
                g.fillRoundRect(445, (i * 125) + 104, 150, 32, 5, 5);
                g.fillRoundRect(460, (i * 125) + 142, 90, 32, 5, 5);
                g.fillRoundRect(555, (i * 125) + 142, 40, 32, 5, 5);
                g.setColor(Color.BLACK);
                
                if(this.selectRemove == i + 1){
                    g.drawImage(this.gui.getSubimage(176, 110, 24, 20), this.w - 195, (i * 125) + 100, null);
                }
                else{
                    g.drawImage(this.removeIcon, this.w - 195, (i * 125) + 100, null);
                }
                
                if(this.selectLoad == i + 1){
                    g.drawImage(this.gui.getSubimage(177, 163, 22, 24), this.w - 193, (i * 125) + 130, null);
                }
                else{
                    g.drawImage(this.loadIcon, this.w - 193, (i * 125) + 130, null);
                }
                
                JSONObject player = (JSONObject) save.get("player");
                String name = (String) player.get("name");
                int sex = Integer.parseInt((String) player.get("sex"));
                int spicies = Integer.parseInt((String) player.get("spicies"));
                g.setFont(this.fontS);
                metrics = g.getFontMetrics(this.fontS);
                
                g.drawString(name, 298, (i * 125) + 109 + metrics.getAscent());
                
                int x = 0;
                int y = 160;
                if(sex == 0){
                    x = 50;
                }
                if(spicies == 1){
                    y = 208;
                }
                g.drawImage(this.gui.getSubimage(x, y, 50, 48), 219, (i * 125) + 115, null);
                
                g.drawImage(this.dollardIcon, 450, (i * 125) + 108 ,null);
                String score =  (String) player.get("score");
                int scoreWidth = metrics.stringWidth(score);
                g.drawString(score, this.w - 215 - scoreWidth, (i * 125) + 109 + metrics.getAscent());
                
                g.drawImage(this.levelIcon, 295, (i * 125) + 144, null);
                JSONObject jsonLevel = (JSONObject) save.get("level");
                String levelNumber = (String) jsonLevel.get("number");
                int levelNumberW = metrics.stringWidth(levelNumber);
                g.drawString(levelNumber, 333, (i * 125) + 146 + metrics.getAscent());
                
                g.drawImage(this.cageIcon, 367, (i * 125) + 141, null);
                String cageNumbers = (String) jsonLevel.get("freeCages") + "/30";
                g.drawString(cageNumbers, 403, (i * 125) + 146 + metrics.getAscent());
                
                String complete = (String) jsonLevel.get("complete") + "%";
                int completeW = metrics.stringWidth(complete);
                g.drawString(complete, this.w - 210 - completeW, (i * 125) + 146 + metrics.getAscent());
                
                int difficulty = Integer.parseInt((String) jsonLevel.get("difficulty"));
                switch(difficulty){
                    case 0:
                        g.drawString(this.easy, 470, (i * 125) + 146 + metrics.getAscent());
                        break;
                    case 2:
                        g.drawString(this.medium, 470, (i * 125) + 146 + metrics.getAscent());
                        break;
                    case 4:
                        g.drawString(this.hard, 470, (i * 125) + 146 + metrics.getAscent());
                        break;
                    case 5:
                        g.drawString(this.hardcore, 470, (i * 125) + 146 + metrics.getAscent());
                        break;
                }
                g.setFont(this.font);
                metrics = g.getFontMetrics(this.font);
            }
        }
        
        //back Btn
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
    }
    
}

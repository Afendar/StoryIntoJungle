package ld34.scene;

import audio.Sound;
import core.Defines;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.profile.Settings;
import core.Game;
import ld34.profile.Save;
import particles.Leaf;

/**
 * MenuScene class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class MenuScene extends Scene {
    
    public BufferedImage background2, logo, settingsIcon, highScoresIcon, creditsIcon;
    public Font font, fontL, fontS;
    public String title, btnNewGame, btnOptions, btnBestScores, btnCredits, btnQuit, btnLoadGame;
    public int[][] btnCoords;
    public int selectedItem;
    
    private String[] btnLabels;
    private ArrayList<Leaf> leavesList = new ArrayList<>(5);
    private boolean displayLoad;
    
    /**
     * 
     * @param w
     * @param h
     * @param game 
     */
    public MenuScene(int w, int h, Game game){
        
        super(w, h, game);
        
        try{
            this.settingsIcon = this.spritesheetGui2.getSubimage(150, 68, 25, 24);
            this.highScoresIcon = this.spritesheetGui2.getSubimage(175, 69, 35, 23);
            this.creditsIcon = this.spritesheetGui2.getSubimage(154, 92, 16, 24);
            
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontS = this.font.deriveFont(18.0f);
            this.font = this.font.deriveFont(24.0f);
            this.fontL = this.font.deriveFont(52.0f);
            
            url = runtimeClass.getResource("/background.png");
            this.background2 = ImageIO.read(url);
           
            this.logo = this.spritesheetGui2.getSubimage(310, 347, 590, 200);

        }catch(FontFormatException|IOException e){
            e.getMessage();
        }

        this.bundle = ResourceBundle.getBundle("lang.lang", this.game.langs[Integer.parseInt(Settings.getInstance().getConfigValue("Lang"))]);
        
        this.displayLoad = Save.getInstance().hasSave();
        
        //new game
        if(this.displayLoad){
            int [][]coords = {
                {this.w/2 - 117 - (15*30), 211},
                {this.w/2 - 117 - (17*30), 311},
                {this.w/2 - 117 - (19*30), 411},
                {16, 518},
                {107, 518},
                {703, 518}
            };
            this.btnCoords = coords;
            String[] labels = {
                bundle.getString("newGame"),
                bundle.getString("loadGame"),
                bundle.getString("quit")
            };
            this.btnLabels = labels;
        }
        else{
            int [][]coords = {
                {this.w/2 - 107 - 15*30, 235},
                {this.w/2 - 107 - 19*30, 355},
                {16, 518},
                {107, 518},
                {703, 518}
            };
            this.btnCoords = coords;
            String[] labels = {
                bundle.getString("newGame"),
                bundle.getString("quit")
            };
            this.btnLabels = labels;
        }
        
        this.selectedItem = 0;
        
        for(int i = 0;i<5;i++){
            this.leavesList.add(new Leaf(5, 0, 0, this.w, this.h));
        }
    }

    @Override
    public Scene update(double dt) {
        
        processHover();

        for(int i=0;i<btnCoords.length-3;i++){
            if(this.btnCoords[i][0] < this.w/2 - 117){
                this.btnCoords[i][0] += 30;
            }
        }
        
        for(int i=0; i< this.leavesList.size(); i++){
            Leaf leaf = this.leavesList.get(i);
            if(!leaf.isGenStartX())
            {
                leaf.genRandStartX();
            }
            leaf.update(dt);
        }
        
        return processClick();
    }

    @Override
    public void render(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background2, 0, 0, null);

        for(int i=0; i < this.leavesList.size(); i++){
            Leaf leaf = this.leavesList.get(i);
            leaf.render(g2d);
        }
        
        g.drawImage(this.logo, 99, 20, null);
        
        g.drawImage(this.foreground, 0, 0, null);
        
        //draw btn
        FontMetrics metrics = g.getFontMetrics(this.font);
            for(int i=0; i< this.btnCoords.length;i++){
                if(i < this.btnCoords.length - 3){
                    g.setFont(this.font);
                    if(this.selectedItem == i + 1){
                        this.bgBtn = this.spritesheetGui2.getSubimage(0, 133, 234, 99);
                    }
                    else{
                        this.bgBtn = this.spritesheetGui2.getSubimage(0, 232, 234, 99);
                    }
                    if(i % 2 == 0){
                        g.drawImage(this.bgBtn, this.btnCoords[i][0], this.btnCoords[i][1], null);
                    }
                    else{
                        g.drawImage(this.horizontalflip(this.bgBtn), this.btnCoords[i][0], this.btnCoords[i][1], null);
                    }
                    int labelWidth = metrics.stringWidth(this.btnLabels[i]);
                    g.setColor(Scene.DARKGREY);
                    g.drawString(this.btnLabels[i], this.btnCoords[i][0] + 117 - labelWidth/2, this.btnCoords[i][1] + metrics.getAscent() + 28);
                }
                else{
                    if(this.selectedItem == i + 1){
                        this.bgBtnSmall = this.spritesheetGui2.getSubimage(0, 69, 76, 63);
                    }
                    else{
                        this.bgBtnSmall = this.spritesheetGui2.getSubimage(76, 69, 76, 63);
                    }
                    g.drawImage(this.bgBtnSmall, this.btnCoords[i][0], this.btnCoords[i][1], null);
                }
            }
        
        g.drawImage(this.settingsIcon, 41, 535, null);
        g.drawImage(this.highScoresIcon, 127, 537, null);
        g.drawImage(this.creditsIcon, 733, 535, null);
    }
    
    /**
     * 
     */
    public void processHover(){
        
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        int oldSelected = this.selectedItem;
        this.selectedItem = 0;
        for(int i=0;i<this.btnCoords.length;i++){
            if(i < this.btnCoords.length - 3){
                if(mouseX > this.btnCoords[i][0] && mouseX < this.btnCoords[i][0] + 234 &&
                        mouseY > this.btnCoords[i][1] && mouseY < this.btnCoords[i][1] + 99){
                    this.selectedItem = i + 1;
                }
            }
            else{
                if(mouseX > this.btnCoords[i][0] && mouseX < this.btnCoords[i][0] + 76 &&
                mouseY > this.btnCoords[i][1] && mouseY < this.btnCoords[i][1] + 63){
                    this.selectedItem = i + 1;
                }
            }
        }
        
        if(this.selectedItem != 0 && this.selectedItem != oldSelected){
            new Thread(Sound.hover::play).start();
        }
    }
    
    /**
     * 
     * @return 
     */
    public Scene processClick(){
        
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            if(this.displayLoad){
                switch(this.selectedItem){
                    case 1:
                        new Thread(Sound.select::play).start();
                        currentScene = new GameScene(this.w, this.h, this.game, Defines.START_LEVEL, 0);
                        break;
                    case 2:
                        new Thread(Sound.select::play).start();
                        currentScene = new SavesScene(this.w, this.h, this.game);
                        break;
                    case 3:
                        new Thread(Sound.select::play).start();
                        System.exit(0);
                        break;
                    case 4:
                        new Thread(Sound.select::play).start();
                        currentScene = new OptionsScene(this.w, this.h, this.game);
                        break;
                    case 5:
                        new Thread(Sound.select::play).start();
                        currentScene = new HighScoresScene(this.w, this.h, this.game);
                        break;
                    case 6:
                        new Thread(Sound.select::play).start();
                        currentScene = new CreditsScene(this.w, this.h, this.game);
                        break;
                    default:
                        currentScene = this;
                }
            }
            else{
                switch(this.selectedItem){
                    case 1:
                        new Thread(Sound.select::play).start();
                        currentScene =new GameScene(this.w, this.h, this.game);
                        break;
                    case 2:
                        new Thread(Sound.select::play).start();
                        System.exit(0);
                        break;
                    case 3:
                        new Thread(Sound.select::play).start();
                        currentScene = new OptionsScene(this.w, this.h, this.game);
                        break;
                    case 4:
                        new Thread(Sound.select::play).start();
                        currentScene = new HighScoresScene(this.w, this.h, this.game);
                        break;
                    case 5:
                        new Thread(Sound.select::play).start();
                        currentScene = new CreditsScene(this.w, this.h, this.game);
                        break;
                    default:
                        currentScene = this;
                }
            }
        }
        
        return currentScene;
    }
    
    /**
     * 
     * @param img
     * @return 
     */
    private BufferedImage horizontalflip(BufferedImage img) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();  
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);  
        g.dispose();
        return dimg;  
    }
}
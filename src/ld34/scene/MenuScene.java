package ld34.scene;

import audio.Sound;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.profile.Settings;
import ld34.Game;
import ld34.profile.Save;
import particles.Leaf;

public class MenuScene extends Scene {
    
    public BufferedImage background2;
    public Font font, fontL, fontS;
    public String title, btnNewGame, btnOptions, btnBestScores, btnCredits, btnQuit, btnLoadGame;
    public int[][] btnCoords;
    private String[] btnLabels;
    public int selectedItem;
    private ArrayList<Leaf> leavesList = new ArrayList<>(5);
    private boolean displayLoad;
    
    public MenuScene(int w, int h, Game game){
        
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontS = this.font.deriveFont(18.0f);
            this.font = this.font.deriveFont(22.0f);
            this.fontL = this.font.deriveFont(52.0f);
            
            url = runtimeClass.getResource("/background3.png");
            this.background2 = ImageIO.read(url);

        }catch(FontFormatException|IOException e){
            e.getMessage();
        }

        this.bundle = ResourceBundle.getBundle("lang.menu", this.game.langs[Integer.parseInt(Settings.getInstance().getConfigValue("Lang"))]);
        
        this.displayLoad = Save.getInstance().hasSave();
        
        //new game
        if(this.displayLoad){
            int [][]coords = {
                {this.w/2 - 107, 155},
                {this.w/2 - 107, 235},
                {this.w/2 - 107, 315},
                {this.w/2 - 107, 395},
                {(this.w/2) - 130, 480},
                {(this.w/2) + 20, 480}
            };
            this.btnCoords = coords;
            String[] labels = {
                bundle.getString("newGame"),
                bundle.getString("loadGame"),
                bundle.getString("settings"),
                bundle.getString("bestScores"),
                bundle.getString("about"),
                bundle.getString("quit")
            };
            this.btnLabels = labels;
        }
        else{
            int [][]coords = {
                {this.w/2 - 107, 195},
                {this.w/2 - 107, 275},
                {this.w/2 - 107, 355},
                {(this.w/2) - 130, 450},
                {(this.w/2) + 20, 450}
            };
            this.btnCoords = coords;
            String[] labels = {
                bundle.getString("newGame"),
                bundle.getString("settings"),
                bundle.getString("bestScores"),
                bundle.getString("about"),
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
        
        //draw btn
        FontMetrics metrics = g.getFontMetrics(this.font);
        for(int i=0; i< this.btnCoords.length;i++){
            if(i < this.btnCoords.length - 2){
                g.setFont(this.font);
                g.drawImage(this.bgBtn, this.btnCoords[i][0], this.btnCoords[i][1], null);
                int labelWidth = metrics.stringWidth(this.btnLabels[i]);
                
                if(this.selectedItem == i + 1){
                    if(this.selectedItem % 2 == 0){
                        g2d.rotate(0.07, this.w /2, this.btnCoords[i][1] + metrics.getAscent() + 18 + (metrics.getAscent()/2));
                    }
                    else{
                        g2d.rotate(-0.1, this.w / 2, this.btnCoords[i][1] + metrics.getAscent() + 18 + (metrics.getAscent()/2));
                    }
                    g.setColor(this.darkGreen);
                    g.drawString(this.btnLabels[i], this.w/2 - labelWidth/2, this.btnCoords[i][1] + metrics.getAscent() + 18);
                    if(this.selectedItem % 2 == 0){
                        g2d.rotate(-0.07, this.w / 2, this.btnCoords[i][1] + metrics.getAscent() + 18 + (metrics.getAscent()/2));
                    }
                    else{
                        g2d.rotate(0.1, this.w / 2, this.btnCoords[i][1] + metrics.getAscent() + 18 + (metrics.getAscent()/2));
                    }
                }
                else{
                    g.setColor(Color.BLACK);
                    g.drawString(this.btnLabels[i], this.w/2 - labelWidth/2, this.btnCoords[i][1] + metrics.getAscent() + 18);
                }
            }
            else{
                metrics = g.getFontMetrics(this.fontS);
                g.setFont(this.fontS);
                g.drawImage(this.bgBtnSmall, this.btnCoords[i][0], this.btnCoords[i][1], null);
                int labelWidth = metrics.stringWidth(this.btnLabels[i]);
                if(this.selectedItem == i + 1){
                    if(this.selectedItem % 2 == 0){
                        g2d.rotate(0.11, this.btnCoords[i][0] + 53.5, this.btnCoords[i][1] + metrics.getAscent() + 5 + (metrics.getAscent() / 2));
                    }
                    else{
                        g2d.rotate(-0.13, this.btnCoords[i][0] + 53.5, this.btnCoords[i][1] + metrics.getAscent() + 5 + (metrics.getAscent() / 2));
                    }
                    g.setColor(this.darkGreen);
                    g.drawString(this.btnLabels[i], this.btnCoords[i][0] + (107/2 - labelWidth/2), this.btnCoords[i][1] + metrics.getAscent() + 5);
                    if(this.selectedItem % 2 == 0){
                        g2d.rotate(-0.11, this.btnCoords[i][0] + 53.5, this.btnCoords[i][1] + metrics.getAscent() + 5 + (metrics.getAscent() / 2));
                    }
                    else{
                        g2d.rotate(0.13, this.btnCoords[i][0] + 53.5, this.btnCoords[i][1] + metrics.getAscent() + 5 + (metrics.getAscent() / 2));
                    }
                }
                else{
                    g.setColor(Color.BLACK);
                    g.drawString(this.btnLabels[i], this.btnCoords[i][0] + (107/2 - labelWidth/2), this.btnCoords[i][1] + metrics.getAscent() + 5);
                }
            }
        }
        g.drawImage(this.foreground, 0, 0, null);
    }
    
    public void processHover(){
        
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        int oldSelected = this.selectedItem;
        this.selectedItem = 0;
        for(int i=0;i<this.btnCoords.length;i++){
            if(i < this.btnCoords.length - 2){
                if(mouseX > this.btnCoords[i][0] && mouseX < this.btnCoords[i][0] + 214 &&
                        mouseY > this.btnCoords[i][1] && mouseY < this.btnCoords[i][1] + 70){
                    this.selectedItem = i + 1;
                }
            }
            else{
                if(mouseX > this.btnCoords[i][0] && mouseX < this.btnCoords[i][0] + 107 &&
                mouseY > this.btnCoords[i][1] && mouseY < this.btnCoords[i][1] + 40){
                    this.selectedItem = i + 1;
                }
            }
        }
        
        if(this.selectedItem != 0 && this.selectedItem != oldSelected){
            new Thread(Sound.hover::play2).start();
        }
    }
    
    public Scene processClick(){
        
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            if(this.displayLoad){
                switch(this.selectedItem){
                    case 1:
                        new Thread(Sound.select::play2).start();
                        currentScene = new GameScene(this.w, this.h, this.game);
                        break;
                    case 2:
                        new Thread(Sound.select::play2).start();
                        currentScene = new SavesScene(this.w, this.h, this.game);
                        break;
                    case 3:
                        new Thread(Sound.select::play2).start();
                        currentScene = new OptionsScene(this.w, this.h, this.game);
                        break;
                    case 4:
                        new Thread(Sound.select::play2).start();
                        currentScene = new BestScoresScene(this.w, this.h, this.game);
                        break;
                    case 5:
                        new Thread(Sound.select::play2).start();
                        currentScene = new CreditsScene(this.w, this.h, this.game);
                        break;
                    case 6:
                        new Thread(Sound.select::play2).start();
                        System.exit(0);
                        break;
                    default:
                        currentScene = this;
                }
            }
            else{
                switch(this.selectedItem){
                    case 1:
                        new Thread(Sound.select::play2).start();
                        currentScene =new GameScene(this.w, this.h, this.game);
                        break;
                    case 2:
                        new Thread(Sound.select::play2).start();
                        currentScene = new OptionsScene(this.w, this.h, this.game);
                        break;
                    case 3:
                        new Thread(Sound.select::play2).start();
                        currentScene = new BestScoresScene(this.w, this.h, this.game);
                        break;
                    case 4:
                        new Thread(Sound.select::play2).start();
                        currentScene = new CreditsScene(this.w, this.h, this.game);
                        break;
                    case 5:
                        new Thread(Sound.select::play2).start();
                        System.exit(0);
                        break;
                    default:
                        currentScene = this;
                }
            }
        }
        
        return currentScene;
    }
}
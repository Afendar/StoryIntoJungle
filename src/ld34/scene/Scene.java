package ld34.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.Game;

public abstract class Scene{
    
    public BufferedImage spritesheetGui, bgBtn, bgBtnSmall, background, foreground, foreground2, foreground3,
            bgBtnSmallRed;
    
    public int w, h;
    public Game game;
    public ResourceBundle bundle;
    public Color darkGreen;
    public Class runtimeClass;
    
    public Scene(int w, int h, Game game){
        
        this.w = w;
        this.h = h;
        this.game = game;
        this.runtimeClass = this.getClass();
        this.darkGreen = new Color(128, 0, 19);
        
        try{
            URL url = runtimeClass.getResource("/gui.png");
            this.spritesheetGui = ImageIO.read(url);
            url = runtimeClass.getResource("/background.png");
            this.background = ImageIO.read(url);
            url = runtimeClass.getResource("/foreground1.png");
            this.foreground = ImageIO.read(url);
            url = runtimeClass.getResource("/foreground2.png");
            this.foreground2 = ImageIO.read(url);
            url = runtimeClass.getResource("/foreground3.png");
            this.foreground3 = ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
        }
        
        this.bgBtn = this.spritesheetGui.getSubimage(0, 0, 214, 70);
        this.bgBtnSmall = this.spritesheetGui.getSubimage(0, 71, 107, 39);
        this.bgBtnSmallRed = this.spritesheetGui.getSubimage(107, 71, 107, 39);
    }
    
    public abstract Scene update(double dt);
    
    public abstract void render(Graphics g);
}

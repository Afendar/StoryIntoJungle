package ld34.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ResourceBundle;
import ld34.Game;

public abstract class Scene{
    
    public int w, h;
    public Game game;
    public ResourceBundle bundle;
    public Color darkGreen;
    
    public Scene(int w, int h, Game game){
        
        this.w = w;
        this.h = h;
        this.game = game;
        
        this.darkGreen = new Color(128, 0, 19);
    }
    
    
    public abstract Scene update();
    
    public abstract void render(Graphics g);
}

package ld34.scene;

import java.awt.Graphics;
import ld34.Game;

public abstract class Scene{
    
    public int w, h;
    public Game game;
    
    public Scene(int w, int h, Game game){
        this.w = w;
        this.h = h;
        this.game = game;
    }
    
    
    public abstract Scene update();
    public abstract void render(Graphics g);
}

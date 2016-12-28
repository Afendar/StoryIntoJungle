package ld34.scene;

import java.awt.Graphics;
import ld34.Game;
import ld34.profile.SaveManager;

public class SavesScene extends Scene {

    protected SaveManager saveManager;
    
    public SavesScene(int w, int h, Game game){
        super(w, h, game);
        this.saveManager = new SaveManager();
    }
    
    @Override
    public Scene update(double dt) {
        return this;
    }

    @Override
    public void render(Graphics g) {
        
    }
    
}

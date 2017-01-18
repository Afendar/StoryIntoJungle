package profiler;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import ld34.Defines;
import ld34.Game;
import ld34.scene.GameScene;

public class Profiler {

    private String[] labels;
    private String[] datas;
    private boolean visible;
    private Game game;
    private Font fontD;
    
    public Profiler(Game game)
    {
        this.labels = new String[]{"fps", "memory", "x", "y", "jump"};
        this.datas = new String[]{"0", "0", "0", "0", "true"};
        this.game = game;
        this.visible = false;
        
        try{
            URL url = this.getClass().getResource("/fonts/arial.ttf");
            this.fontD = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.fontD = this.fontD.deriveFont(Font.PLAIN, 18.0f);
        }
        catch(FontFormatException|IOException e){
            e.getMessage();
        }
    }
    
    public void update(String frames, String memory){
        
        this.datas[0] = frames;
        this.datas[1] = memory;
        
        if(this.game.gs instanceof GameScene){
            GameScene gs = (GameScene)this.game.gs;
            this.datas[2] = Float.toString(gs.player.getPosX());
            this.datas[3] = Float.toString(gs.player.getPosY());
            this.datas[4] = Boolean.toString(gs.player.isJumping());
        }
    }
    
    public void render(Graphics g){
        FontMetrics fm = g.getFontMetrics(this.fontD);
        String text = "FPS : ";
        text += this.datas[0];
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setFont(this.fontD);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 30);
        
        text = "Memory : " + this.datas[1] + "Mo";
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 57 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, 30, 60);
        
        if(this.game.gs instanceof GameScene){
            text = "X : " + this.datas[2];
            rect = fm.getStringBounds(text, g);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 84 - fm.getAscent(), (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
            g.setColor(Color.WHITE);
            g.drawString(text, 30, 90);
        }
        
        text = "Java : " + System.getProperty("java.version") + "  x" + System.getProperty("sun.arch.data.model") + " bit";
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(this.game.w - (int)rect.getWidth() - 40, 30 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, this.game.w - (int)rect.getWidth() - 30, 30);
                
        text = "Story Into Jungle : v" + Defines.VERSION;
        rect = fm.getStringBounds(text, g);
        g.setColor(new Color(0,0,0,150));
        g.fillRect(this.game.w - (int)rect.getWidth() - 40, 60 - fm.getAscent() - 3, (int)rect.getWidth() + 40, (int)rect.getHeight() + 6);
        g.setColor(Color.WHITE);
        g.drawString(text, this.game.w - (int)rect.getWidth() - 30, 60);
    }
    
    public boolean isVisible(){
        return this.visible;
    }
    
    public void toggleVisible(){
        this.visible = !this.visible;
    }
}

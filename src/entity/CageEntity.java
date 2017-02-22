package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import core.Defines;
import java.awt.Color;
import java.awt.Rectangle;
import level.Level;

public class CageEntity extends Entity {

    protected double dt;
    protected Level level;
    private int brokenStep, offset;
    private boolean isBreak, renderHurt, renderBreak;
    public BufferedImage tileset, topLeftSprite, topRightSprite, bottomLeftSprite, bottomRightSprite;
    private int timerender;
    
    public CageEntity(Level level, int posX, int posY){
        super(posX, posY);
        
        this.level = level;
        this.brokenStep = this.offset = 0;
        this.isBreak = this.renderHurt = this.renderBreak = false;
        this.timerender = 0;
        
        try{
            URL url = this.getClass().getResource("/tileset2.png");
            this.tileset = ImageIO.read(url);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        this.topLeftSprite = this.tileset.getSubimage(0 * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomLeftSprite = this.tileset.getSubimage(0 * Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.topRightSprite = this.tileset.getSubimage(Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomRightSprite = this.tileset.getSubimage(Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
    }
    
    public void hurt(){
        if(this.brokenStep < 4 && !this.renderHurt){
            this.brokenStep++;
            this.renderHurt = true;
            this.topLeftSprite = this.tileset.getSubimage(2 * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            this.bottomLeftSprite = this.tileset.getSubimage(2 * Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            this.topRightSprite = this.tileset.getSubimage(3 * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            this.bottomRightSprite = this.tileset.getSubimage(3 * Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
        
        if(this.brokenStep == 4 && !this.isBreak){
            this.isBreak = true;
            this.renderBreak = true;
            
            this.level.freeCage();
        }
    }
    
    @Override
    public void update(double dt) {
        if(this.renderHurt && !this.isBreak){
            if(this.timerender > 10){
                this.renderHurt = false;
                this.timerender = 0;
                this.topLeftSprite = this.tileset.getSubimage(0 * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
                this.bottomLeftSprite = this.tileset.getSubimage(0 * Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
                this.topRightSprite = this.tileset.getSubimage(Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
                this.bottomRightSprite = this.tileset.getSubimage(Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            }
            else{
                this.timerender += dt;
            }
        }
        
        if(this.isBreak){
            if(this.renderBreak && this.offset < 4){
                if(this.timerender > 3){
                    this.timerender = 0;
                    this.offset += 2;
                }
                else{
                    this.timerender += dt;
                }
                
            }
            else{
                this.timerender = 0;
                this.renderBreak = false;
            }
            this.topLeftSprite = this.tileset.getSubimage((4 + this.offset) * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            this.bottomLeftSprite = this.tileset.getSubimage((4 + this.offset) * Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            this.topRightSprite = this.tileset.getSubimage((5 + this.offset) * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
            this.bottomRightSprite = this.tileset.getSubimage((5 + this.offset) * Defines.TILE_SIZE, 7 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        }
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle((int)posX, (int)posY - Defines.TILE_SIZE + 25, 2 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE);
    }
    
    @Override
    public void render(Graphics g, Boolean debug) {
        g.drawImage(this.topLeftSprite, (int)posX, (int)(posY - Defines.TILE_SIZE + 25), null);
        g.drawImage(this.topRightSprite, (int)(posX + Defines.TILE_SIZE), (int)(posY - Defines.TILE_SIZE + 25), null);
        
        if(debug){
            this.renderHitbox(g);
        }
    }
    
    public void renderTop(Graphics g){
        g.drawImage(this.bottomLeftSprite, (int)posX, (int)(posY + 25), null);
        g.drawImage(this.bottomRightSprite, (int)(posX + Defines.TILE_SIZE), (int)(posY + 25), null);
    }
    
    @Override
    public void renderHitbox(Graphics g){
        Rectangle rect = this.getBounds();
        g.setColor(Color.MAGENTA);
        g.drawRect((int)rect.x, (int)rect.y, (int)rect.getWidth(), (int)rect.getHeight());
    }
    
    public boolean isBreak(){
        return this.isBreak;
    }
}

package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import core.Defines;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import level.Level;

/**
 * CageEntity class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class CageEntity extends Entity {

    
    public BufferedImage tileset, topLeftSprite, topRightSprite, bottomLeftSprite, bottomRightSprite, pandas;
    
    protected double dt;
    protected Level level;
    
    private float alpha;
    private int brokenStep, offset, offsetPandas;
    private boolean isBreak, renderHurt, renderBreak;
    private int timerender;
    
    /**
     * 
     * @param level
     * @param posX
     * @param posY 
     */
    public CageEntity(Level level, int posX, int posY){
        super(posX, posY);
        
        this.level = level;
        this.brokenStep = this.offset = this.offsetPandas = 0;
        this.isBreak = this.renderHurt = this.renderBreak = false;
        this.timerender = 0;
        this.alpha = 1.0f;
        
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
        this.pandas = this.tileset.getSubimage(4 *Defines.TILE_SIZE, 285, 2 * Defines.TILE_SIZE, 80);
    }
    
    /**
     * 
     */
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
            if(this.renderBreak && this.alpha > 0){
                if(this.timerender > 3 && this.offset < 4){
                    
                    this.timerender = 0;
                    
                    if(this.offset < 4){
                        this.offset += 2;
                    }
                }
                else if(this.timerender > 6){
                    
                    this.timerender = 0;
                    
                    if(this.offset == 4 && this.offsetPandas < 10){
                        this.pandas = this.tileset.getSubimage((this.offsetPandas + 4) * Defines.TILE_SIZE, 285, 2 * Defines.TILE_SIZE, 80);
                        this.offsetPandas += 2;
                    }
                    else{
                        if(this.offsetPandas == 10){
                            if(this.alpha > 0){
                                this.alpha -= 0.09;
                            }

                            if(this.alpha < 0){
                                this.alpha = 0;
                            }
                        }
                        this.pandas = this.tileset.getSubimage(4 *Defines.TILE_SIZE, 285, 2 * Defines.TILE_SIZE, 80);
                    }
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
        if(this.isBreak){
            if(this.offsetPandas == 10){
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
                g.drawImage(this.pandas, (int)posX, (int)(posY - 7), null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
            else{
                g.drawImage(this.pandas, (int)posX, (int)(posY - 7), null);
            }
        }
        
        g.drawImage(this.topLeftSprite, (int)posX, (int)(posY - Defines.TILE_SIZE + 25), null);
        g.drawImage(this.topRightSprite, (int)(posX + Defines.TILE_SIZE), (int)(posY - Defines.TILE_SIZE + 25), null);
        
        if(debug){
            this.renderHitbox(g);
        }
    }
    
    /**
     * 
     * @param g 
     */
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
    
    /**
     * 
     * @return 
     */
    public boolean isBreak(){
        return this.isBreak;
    }
    
    /**
     * 
     * @param broken 
     */
    public void setBroken(boolean broken){
        this.isBreak = broken;
        if(broken){
            this.offsetPandas = 10;
            this.alpha = 0;
            this.offset = 4;
        }
    }
}

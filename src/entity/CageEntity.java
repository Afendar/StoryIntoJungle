package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import ld34.Defines;
import level.Level;

public class CageEntity extends Entity {

    protected double dt;
    protected Level level;
    private int brokenStep;
    private boolean isBreak, renderHurt;
    public BufferedImage tileset, topLeftSprite, topRightSprite, bottomLeftSprite, bottomRightSprite;
    
    public CageEntity(Level level, int posX, int posY){
        super(posX, posY);
        
        this.level = level;
        this.brokenStep = 0;
        this.isBreak = this.renderHurt = false;
        
        try{
            URL url = this.getClass().getResource("/tileset2.png");
            this.tileset = ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
        }
        
        this.topLeftSprite = this.tileset.getSubimage(0 * Defines.TILE_SIZE, 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomLeftSprite = this.tileset.getSubimage(0 * Defines.TILE_SIZE, (int)((6 + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.topRightSprite = this.tileset.getSubimage((int)((0 + 1) * Defines.TILE_SIZE), 6 * Defines.TILE_SIZE, Defines.TILE_SIZE, Defines.TILE_SIZE);
        this.bottomRightSprite = this.tileset.getSubimage((int)((0 + 1) * Defines.TILE_SIZE), (int)((6 + 1) * Defines.TILE_SIZE), Defines.TILE_SIZE, Defines.TILE_SIZE);
    }
    
    public void hurt(){
        if(this.brokenStep < 3){
            this.brokenStep++;
            this.renderHurt = true;
        }
        
        if(this.brokenStep == 3)
            this.isBreak = true;
    }
    
    @Override
    public void update(double dt) {
        if(this.renderHurt){
            
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawRect((int)posX, (int)posY - Defines.TILE_SIZE + 25, 2 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE);
        g.drawImage(this.topLeftSprite, (int)posX, (int)(posY - Defines.TILE_SIZE + 25), null);
        g.drawImage(this.topRightSprite, (int)(posX + Defines.TILE_SIZE), (int)(posY - Defines.TILE_SIZE + 25), null);
    }
    
    public void renderTop(Graphics g){
        g.drawImage(this.bottomLeftSprite, (int)posX, (int)(posY + 25), null);
        g.drawImage(this.bottomRightSprite, (int)(posX + Defines.TILE_SIZE), (int)(posY + 25), null);
    }
}

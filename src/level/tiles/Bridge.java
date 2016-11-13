package level.tiles;

import java.awt.Graphics;
import java.awt.Graphics2D;
import ld34.Defines;

public class Bridge extends Tile {
    
    int timeanim = 15;
    float rotation;
    
    public Bridge(int imgX, int imgY){
        super(imgX, imgY, 3);
    }

    @Override
    public boolean canPass() {
        return false;
    }

    @Override
    public void update(double dt) {
    }
    
    public void render(Graphics g, int x, int y, boolean onBridge){
        Graphics2D g2d = (Graphics2D) g;
        if(onBridge){
            if(this.timeanim == 0){
                if(this.rotation > 0){
                    this.rotation = (-0.07f);
                }
                else{
                    this.rotation = 0.07f;
                }
                this.timeanim = 15;
            }

            if(this.timeanim > 0){
                this.timeanim--;
            }
            g2d.rotate(-this.rotation, (x * Defines.TILE_SIZE) + 16, (y * Defines.TILE_SIZE) + 16);
        }
        g.drawImage(this.tile, x * Defines.TILE_SIZE, y * Defines.TILE_SIZE, null);
        if(onBridge){
            g2d.rotate(this.rotation, (x * Defines.TILE_SIZE) + 16, (y * Defines.TILE_SIZE) + 16);
        }
    }
    
}

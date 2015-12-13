package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ld34.Defines;
import level.tiles.TileAtlas;

public class Level {
    
    public int w, h;
    public int nbTilesW, nbTilesH;
    public int[][] map;
    
    public static final int WHITE = 16777215; //RGB(0, 0, 0) EMPTY
    public static final int GREEN = 32768; //RGB(0, 128, 0) BAMBOO
    public static final int BLACK = 0; //RGB(255, 255, 255) FLOOR
    public static final int BROWN = 9127187; //RGB(139, 69, 19) BRIDGE
    public static final int RED = 16711680; //RGB(255, 0, 0) APPLE
    public static final int ORANGE = 16753920;//RGB(255, 165, 0) LEAVES
    public static final int BLUE = 255;//RGB(0, 0, 255) PIOUS
    public static final int PURPLE = 10354846;//RGB(158, 0, 158) LEVELUP
    
    public Level(int nbLevel){
        this.loadLevel(nbLevel);
    }
    
    public void update(){
        
    }
    
    public void render(Graphics g, int startX, int startY){
        
        for(int i=0;i<this.nbTilesW;i++){
            for(int j=0;j<this.nbTilesH;j++){
                switch(this.map[i][j]){
                    case 1:
                        TileAtlas.floor.render(g, i, j);
                        break;
                    case 2:
                        TileAtlas.bamboo.render(g, i, j);
                        break;
                    case 3:
                        TileAtlas.bridge.render(g, i, j);
                        break;
                    case 4:
                        TileAtlas.apple.render(g, i, j);
                        break;
                    case 5:
                        TileAtlas.leaves.render(g, i, j);
                        break;
                    case 6:
                        TileAtlas.pious.render(g, i, j);
                        break;
                    case 7:
                        TileAtlas.levelup.render(g, i, j);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    public int getTile(int x, int y){
        return this.map[x][y];
    }
    
    public void removeTile(int x, int y){
        this.map[x][y] = 0;
    }
    
    public void loadLevel(int nbLevel){
        try{
            BufferedImage lvlImg = ImageIO.read(new File("levels/lvl"+nbLevel+".png"));
            
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int width = lvlImg.getWidth();
            int height = lvlImg.getHeight();
            
            this.w = width * Defines.TILE_SIZE;
            this.h = height * Defines.TILE_SIZE;
            this.nbTilesH = height;
            this.nbTilesW = width;
            
            this.map = new int[width][height];
            
            boolean hasAlpha = true;
            
            if (hasAlpha) {
                final int pixelLength = 4;
                for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                   int argb = 0;
                   argb += 0; // alpha
                   argb += ((int) pixels[pixel + 1] & 0xff); // blue
                   argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                   argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                   //System.out.println(argb);
                   switch(argb){
                        case WHITE:
                            map[col][row] = TileAtlas.empty.ID;
                            break;
                        case GREEN:
                            map[col][row] = TileAtlas.bamboo.ID;
                            break;
                        case BLACK:
                            map[col][row] = TileAtlas.floor.ID;
                            break;
                        case BROWN:
                            map[col][row] = TileAtlas.bridge.ID;
                            break;
                        case RED:
                            map[col][row] = TileAtlas.apple.ID;
                            break;
                        case ORANGE:
                            map[col][row] = TileAtlas.leaves.ID;
                            break;
                        case BLUE:
                            map[col][row] = TileAtlas.pious.ID;
                            break;
                        case PURPLE:
                            map[col][row] = TileAtlas.levelup.ID;
                            break;
                        default:
                            break;
                   }

                   col++;
                   if (col == width) {
                      col = 0;
                      row++;
                    }
                }
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
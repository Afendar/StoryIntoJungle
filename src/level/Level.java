package level;

import entity.Braconeers;
import entity.CageEntity;
import entity.Player;
import entity.SandEntity;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import core.Defines;
import java.awt.Rectangle;
import level.tiles.TileAtlas;
import level.tiles.Tree;
import particles.Leaf;
import particles.Particle;
import profiler.Profiler;

/**
 * Level class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Level {
    
    public int w, h;
    public int nbTilesW, nbTilesH;
    protected int nbTilesInScreenX, nbTilesInScreenY;
    public int[][] map;
    public int[][] topMap;
    public int[][] data;
    public int[][] eventsPos;
    public boolean[] viewedEvent;
    public Player player;
    public int nbLevel;
    public int nbCages;
    public List<List<CageEntity>> cagesMap = new ArrayList<>();
    public List<Particle> particles = new ArrayList<>();
    public List<SandEntity> sandEntity = new ArrayList<>();
    public List<CageEntity> cageEntity = new ArrayList<>();
    public List<Braconeers> braconeers = new ArrayList<>();
    public boolean[] unlockedLevels;
    
    private int complete;
    private int freeCages;
    private int startPosY;
    
    public static final int WHITE = 16777215; //RGB(0, 0, 0) EMPTY
    public static final int GREEN = 32768; //RGB(0, 128, 0) BAMBOO
    public static final int BLACK = 0; //RGB(255, 255, 255) FLOOR
    public static final int BROWN = 9127187; //RGB(139, 69, 19) BRIDGE
    public static final int RED = 16711680; //RGB(255, 0, 0) APPLE
    public static final int ORANGE = 16753920;//RGB(255, 165, 0) LEAVES
    public static final int BLUE = 255;//RGB(0, 0, 255) PIOUS
    public static final int PURPLE = 10354846;//RGB(158, 0, 158) LEVELUP
    public static final int PINK = 16711808;//RGB(255, 0, 128) CHECKPOINTS
    public static final int SAND = 15721648;//RGB(239, 228, 176) SAND
    public static final int CAGE = 8355711;//RGB(127, 127, 127) CAGE
    public static final int BRACONEER = 32960;//RGB(0, 128, 192) BRACONEERS
    public static final int LIME = 11920925;//RGB(181, 230, 29) PLANTS
    
    /**
     * 
     * @param nbLevel 
     */
    public Level(int nbLevel){
        System.out.println("Loading level : " + nbLevel);
        this.nbLevel = nbLevel;
        this.nbCages = 0;
        this.complete = 0;
        this.freeCages = 0;
        
        this.unlockedLevels = new boolean[Defines.LEVEL_MAX];
        
        for(int i=0;i<Defines.LEVEL_MAX;i++){
            this.cagesMap.add(new ArrayList<>());
            this.unlockedLevels[i] = false;
        }
        
        if(nbLevel == 1){
            // Add events x pos
            int[][] eventsPos = {
                {192, 896},
                {896, 896},
                {1984, 768},
                {3136, 704},
                {4160, 896},
                {5120, 640},
                {6272, 640},
                {6976, 768},
                {8064, 640}
            };
            this.viewedEvent = new boolean[eventsPos.length];
            for(int i=0;i<this.viewedEvent.length;i++){
                this.viewedEvent[i] = false;
            }
            this.eventsPos = eventsPos;
        }
        
        this.cageEntity = new ArrayList<>();
        
        this.startPosY = this.loadLevel(nbLevel);
    }
    
    /**
     * 
     * @param dt
     * @param startX
     * @param startY 
     */
    public void update(double dt, int startX, int startY){
        int endX = (startX + this.nbTilesInScreenX + 2 <= this.nbTilesW)? startX + this.nbTilesInScreenX + 2 : this.nbTilesW;
        int endY = (startY + this.nbTilesInScreenY + 2 <= this.nbTilesH)? startY + this.nbTilesInScreenY + 2 : this.nbTilesH;
        
        for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(this.map[i][j]){
                    case 1:
                        TileAtlas.floor.update(this, i, j, dt);
                        break;
                    case 2:
                        TileAtlas.bamboo.update(this, i, j, dt);
                        break;
                    case 3:
                        TileAtlas.bridge.update(this, i, j, dt);
                        break;
                    case 4:
                        TileAtlas.apple.update(this, i, j, dt);
                        break;
                    case 5:
                        TileAtlas.leaves.update(this, i, j, dt);
                        break;
                    case 6:
                        TileAtlas.pious.update(this, i, j, dt);
                        break;
                    case 7:
                        TileAtlas.levelup.update(this, i, j, dt);
                        break;
                    case 8:
                        TileAtlas.checkpoint.update(this, i, j, dt);
                        break;
                    case 9:
                        TileAtlas.sand.update(this, i, j, dt);
                        break;
                    case 10:
                        TileAtlas.cage.update(this, i, j, dt);
                        break;
                    default:
                        break;
                }
            }
        }
        
        for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(this.topMap[i][j]){
                    case 12:
                        if(this.particles.size() < 4){
                            for(int k = 0; k < 4;k++){
                                this.particles.add(new Leaf(5,
                                        (i * Defines.TILE_SIZE) - Defines.TILE_SIZE + 32,
                                        (j * Defines.TILE_SIZE) - (Defines.TILE_SIZE + 32),
                                        4*Defines.TILE_SIZE,
                                        3*Defines.TILE_SIZE - 32
                                        ));
                            }
                        }
                        break;
                }
            }
        }
        
        for(int i=0; i<this.particles.size();i++){
            Particle p = this.particles.get(i);
            p.update(dt);
            if(p.isDead()){
                this.particles.remove(i);
            }
        }
        
        for(int i=0;i<this.sandEntity.size();i++){
            SandEntity se = this.sandEntity.get(i);
            se.update(dt);
        }
        
        for(int i=0;i<this.cageEntity.size();i++){
            CageEntity ce = this.cageEntity.get(i);
            ce.update(dt);
        }
        
        for(int i=0;i<this.braconeers.size();i++){
            Braconeers b = this.braconeers.get(i);
            b.update(dt);
            
            if(b.isDead()){
                this.braconeers.remove(i);
            }
        }
    }
    
    /**
     * 
     * @param screenWidth 
     */
    public void setNbTilesInScreenX(int screenWidth){
        this.nbTilesInScreenX = (int)(screenWidth / Defines.TILE_SIZE);
    }
    
    /**
     * 
     * @param screenHeight 
     */
    public void setNbTilesInScreenY(int screenHeight){
        this.nbTilesInScreenY = (int)(screenHeight / Defines.TILE_SIZE);
    }
    
    /**
     * 
     * @return 
     */
    public int getNbTilesInScreenX(){
        return this.nbTilesInScreenX;
    }
    
    /**
     * 
     * @return 
     */
    public int getNbTilesInScreenY(){
        return this.nbTilesInScreenY;
    }
    
    /**
     * 
     * @param g
     * @param startX
     * @param startY 
     */
    public void renderFirstLayer(Graphics g, int startX, int startY){
        
        Boolean debug = Profiler.getInstance().isVisible();
        int endX = (startX + this.nbTilesInScreenX + 2 <= this.nbTilesW)? startX + this.nbTilesInScreenX + 2 : this.nbTilesW;
        int endY = (startY + this.nbTilesInScreenY + 2 <= this.nbTilesH)? startY + this.nbTilesInScreenY + 2 : this.nbTilesH;
        
        for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(this.map[i][j]){
                    case 1:
                        boolean left = false;
                        boolean right = false;
                        if(i > 0){
                            if(this.map[i-1][j] == 1){
                                left = true;
                            }
                        }
                        if(i < this.nbTilesW - 1){
                            if(this.map[i+1][j] == 1){
                                right = true;
                            }
                        }
                        TileAtlas.floor.render(g, i, j, left, right);
                        break;
                    case 2:
                        TileAtlas.bamboo.render(g, i, j);
                        break;
                    case 3:
                        int playerX = (int)this.player.getPosX() / Defines.TILE_SIZE;
                        int playerY = (int)this.player.getPosY() / Defines.TILE_SIZE;
                        boolean onBridge = false;
                        if(playerX == i && playerY == j - 2){onBridge = true;}
                        TileAtlas.bridge.render(g, i, j, onBridge);
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
                    case 8:
                        TileAtlas.checkpoint.render(g, i, j);
                        break;
                    case 9:
                        if(this.data[i][j] != 2){
                            TileAtlas.sand.render(g, i, j);
                        }
                        break;
                    case 10:
                        if(this.map[i-1][j] != 11){
                            CageEntity ce = this.getCageEntity(i, j);
                            if(ce != null)
                                ce.render(g, debug);
                        }
                        break;
                    case 14:
                        TileAtlas.plant.render(g, i, j);
                        break;
                    default:
                        break;
                }
            }
        }
        
        for(int i=0;i<this.braconeers.size();i++){
            Braconeers b = this.braconeers.get(i);
            b.render(g, debug);
        }
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public CageEntity getCageEntity(int x, int y){
        for(int i=0;i< this.cageEntity.size();i++){
            CageEntity ce = this.cageEntity.get(i);
            Rectangle bounds = ce.getBounds();
            if(x * Defines.TILE_SIZE >= bounds.x && x * Defines.TILE_SIZE <= bounds.x + bounds.width && 
                    y * Defines.TILE_SIZE >= bounds.y && y * Defines.TILE_SIZE <= bounds.y + bounds.height){
                return ce;
            }
        }
        return null;
    }
    
    /**
     * 
     * @param g
     * @param startX
     * @param startY 
     */
    public void renderSecondLayer(Graphics g, int startX, int startY){
        
        startX = startX - 3;
        if(startX < 0)
            startX = 0;
        
        startY = startY - 2;
        if(startY < 0)
            startY = 0;
        
        int endX = (startX + this.nbTilesInScreenX + 6 <= this.nbTilesW)? startX + this.nbTilesInScreenX + 6 : this.nbTilesW;
        int endY = (startY + this.nbTilesInScreenY + 4 <= this.nbTilesH)? startY + this.nbTilesInScreenY + 4 : this.nbTilesH;
        
        for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(this.topMap[i][j]){
                    case 10:
                        CageEntity ce = this.getCageEntity(i, j);
                        if(ce != null)
                            ce.renderTop(g);
                        break;
                    case 11:
                        TileAtlas.bush.render(g, i, j);
                        break;
                    case 12:
                        for(int k=0;k<this.particles.size();k++){
                            Particle p = this.particles.get(k);
                            p.render(g);
                            
                        }
                        TileAtlas.tallTree.render(g, i, j, Tree.TALL);
                        break;
                    case 13:
                        TileAtlas.smallTree.render(g, i, j, Tree.SMALL);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public int getTile(int x, int y){
        return this.map[x][y];
    }
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void removeTile(int x, int y){
        this.map[x][y] = 0;
    }
    
    /**
     * 
     * @param data 
     */
    public void setData(int[][] data){
        for(int i=0;i< data.length;i++){
            for(int j=0;j<data[i].length;j++){
                if(this.map[i][j] == 10 && data[i][j] == 2){
                    this.nbCages--;
                }
                if(this.map[i][j] == 9 && data[i][j] == 2){
                    data[i][j] = 0;
                }
            }
        }
        this.data = data;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param val 
     */
    public void setData(int x, int y, int val){
        this.data[x][y] = val;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public int getData(int x, int y){
        return this.data[x][y];
    }
    
    /**
     * 
     * @param nbLevel 
     */
    public int loadLevel(int nbLevel){
        int startPos = this.loadFirstLayer(nbLevel);
        this.loadTopLayer(nbLevel);
        return startPos;
    }
    
    /**
     * 
     * @param nbLevel 
     */
    public int loadFirstLayer(int nbLevel){
        int startPos = 20;
        try{
            URL url = this.getClass().getResource("/lvl" + ((Defines.DEV)? "-dev" : "") + nbLevel + ".png");
            BufferedImage lvlImg = ImageIO.read(url);
            
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int width = lvlImg.getWidth();
            int height = lvlImg.getHeight();
            this.w = width * Defines.TILE_SIZE;
            this.h = height * Defines.TILE_SIZE;
            this.nbTilesH = height;
            this.nbTilesW = width;
            
            this.map = new int[width][height];
            this.data = new int[width][height];
            this.topMap = new int[width][height];
            
            for(int i=0;i<this.data.length;i++){
                for(int j=0;j<this.data[i].length;j++){
                    this.data[i][j] = 0;
                }
            }
            
            boolean hasAlpha = true;
            
            if (hasAlpha) {
                
                ArrayList cageLevelMapping = new ArrayList<>();
                
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
                            if(col == 0){
                                startPos = ( row - 1 ) * Defines.TILE_SIZE;
                            }
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
                        case PINK:
                            map[col][row] = TileAtlas.checkpoint.ID;
                            break;
                        case SAND:
                            map[col][row] = TileAtlas.sand.ID;
                            this.sandEntity.add(new SandEntity(this, col, row));
                            break;
                        case CAGE:
                            this.nbCages++;
                            CageEntity ce = new CageEntity(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE);
                            this.cageEntity.add(ce);
                            cageLevelMapping.add(ce);
                            map[col][row] = TileAtlas.cage.ID;
                            map[col+1][row] = TileAtlas.cage.ID;
                            col++;
                            pixel += pixelLength;
                            break;
                        case BRACONEER:
                            this.braconeers.add(new Braconeers(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE));
                            break;
                        case LIME:
                            map[col][row] = TileAtlas.plant.ID;
                            break;
                        default:
                            break;
                   }

                   col++;
                   if (col >= width) {
                      col = 0;
                      row++;
                    }
                }
                this.cagesMap.set(nbLevel - 1, cageLevelMapping);
            }
            
        }catch(IOException e){
            e.getMessage();
        }
        return startPos;
    }
    
    /**
     * 
     * @param nbLevel 
     */
    public void loadTopLayer(int nbLevel){
        try{
            URL url = this.getClass().getResource("/lvl"+ ((Defines.DEV)? "-dev" : "") + nbLevel +"-top.png");
            BufferedImage lvlImg = ImageIO.read(url);
            
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int width = lvlImg.getWidth();
            int height = lvlImg.getHeight();
            
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
                            this.topMap[col][row] = TileAtlas.empty.ID;
                            break;
                        case GREEN:
                            this.topMap[col][row] = TileAtlas.bush.ID;
                            break;
                        case BROWN:
                            this.topMap[col][row] = TileAtlas.tallTree.ID;
                            break;
                        case ORANGE:
                            this.topMap[col][row] = TileAtlas.smallTree.ID;
                            break;
                        case CAGE:
                            this.topMap[col][row] = TileAtlas.cage.ID;
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
            e.getMessage();
        }
    }
    
    /**
     * 
     * @param p 
     */
    public void addParticle(Particle p){
        if(!(p instanceof Particle)){
            return;
        }
        
        this.particles.add(p);
    }
    
    /**
     * 
     * @param p 
     */
    public void addPlayer(Player p){
        this.player = p;
        this.player.setPosY(this.startPosY);
    }
    
    /**
     * 
     * @param percentage 
     */
    public void setComplete(int percentage){
        this.complete = percentage;
    }
    
    /**
     * 
     * @return 
     */
    public int getComplete(){
        return this.complete;
    }
    
    /**
     * 
     */
    public void freeCage(){
        this.freeCages++;
        this.nbCages--;
    }
    
    /**
     * 
     * @return 
     */
    public int getFreeCages(){
        return this.freeCages;
    }
    
    /**
     * 
     * @param cagesInLevel 
     */
    public void setCagesInLevel(List<CageEntity> cagesInLevel){
        for(int i=0;i<cagesInLevel.size();i++){
            CageEntity ce = cagesInLevel.get(i);
            if(ce.isBreak()){
                this.nbCages--;
            }
        }
        this.cageEntity = cagesInLevel;
    }
    
    /**
     * 
     * @param index 
     */
    public void setUnlocked(int index){
        if(index > Defines.LEVEL_MAX - 1 || index < 1){
            return;
        }
        
        this.unlockedLevels[index-1] = true;
    }

    /**
     * 
     * @param index 
     */
    public void setLocked(int index){
        if(index > Defines.LEVEL_MAX - 1 || index < 0){
            return;
        }
        
        this.unlockedLevels[index] = false;
    }
    
    /**
     * 
     * @param unlockedLevels 
     */
    public void setUnlockedLevels(boolean[] unlockedLevels){
        this.unlockedLevels = unlockedLevels;
    }
    
    /**
     * 
     * @param x0
     * @param y0
     * @param w
     * @param h
     * @param isStuck
     * @return 
     */
    public List<Braconeers> getBraconeersEntities(int x0, int y0, int w, int h, boolean isStuck){
        List<Braconeers> result = new ArrayList<>();
        
        for(int i = 0; i < this.braconeers.size(); i++){
            Braconeers b = this.braconeers.get(i);
            if(b.getBounds().intersects(x0, y0, w, h)){
                result.add(b);
            }
        }
        
        return result;
    }
}
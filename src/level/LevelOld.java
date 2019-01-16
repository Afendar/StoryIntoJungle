package level;

import core.Context;
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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import level.tiles.TileAtlas;
import level.tiles.Tree;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import particles.Leaf;
import particles.Particle;
import profiler.Profiler;
import season.Season.SeasonState;
import weather.Weather.WeatherState;

/**
 * Level class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class LevelOld
{    
    public int m_w, m_h, m_nbTilesW, m_nbTilesH, m_nbLevel, m_nbCages;
    public int[][] m_map, m_topMap, m_data;
    public boolean[] m_viewedEvent, m_unlockedLevels;
    public Player m_player;
    public List<List<CageEntity>> m_cagesMap = new ArrayList<>();
    public List<Particle> m_particles = new ArrayList<>();
    public List<SandEntity> m_sandEntity = new ArrayList<>();
    public List<CageEntity> m_cageEntity = new ArrayList<>();
    public List<Braconeers> m_braconeers = new ArrayList<>();
    
    protected int m_nbTilesInScreenX, m_nbTilesInScreenY;
    
    private int m_complete, m_freeCages, m_startPosY;
    private int[][] m_eventsPos;
    private String m_time, m_name;
    private WeatherState m_weatherState;
    private SeasonState m_seasonState;
    private Context m_context;
    
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
    
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;
    public static final int DIFFICULTY_HARDCORE = 3;
    
    /*
    //WATER MANAGEMENT TODO
    private final Color water = new Color(100, 255, 235, 170);
    private int num_springs = 150;
    private int y_offset = 1050;
    private Spring[] springs = new Spring[num_springs];
    private final float spread = 0.25f;
    private final float gravity = 0.3f;
    private int start_pos = 6 * 64;
    private int end_pos = 4 * 64;
    */
    
    public LevelOld(int nbLevel)
    {
        this(nbLevel, "");
    }
    
    /**
     * 
     * @param nbLevel 
     * @param name 
     */
    public LevelOld(int nbLevel, String name)
    {
        m_nbLevel = nbLevel;
        m_name = name;
        m_nbCages = 0;
        m_complete = 0;
        m_freeCages = 0;
        m_unlockedLevels = new boolean[Defines.LEVEL_MAX];
        m_cageEntity = new ArrayList<>();
        
        for(int i=0;i<Defines.LEVEL_MAX;i++){
            m_cagesMap.add(new ArrayList<>());
            m_unlockedLevels[i] = false;
        }
        
        m_startPosY = loadLevel(nbLevel);
        
        /*
        //water test integration
        for(int n = 0; n < springs.length; n++)
        {
            float t = (float) n / (float) springs.length;
            springs[n] = new Spring(t * end_pos + start_pos, y_offset - 10);
        }*/
    }
    
    /**
     * 
     * @param eventsPos 
     */
    public void setEventsPos(int[][] eventsPos){
        m_eventsPos = eventsPos;
        m_viewedEvent = new boolean[m_eventsPos.length];
        
        for(int i=0;i<m_viewedEvent.length;i++){
            m_viewedEvent[i] = false;
        }
    }
    
    /**
     * 
     * @return 
     */
    public int[][] getEventsPos(){
        return m_eventsPos;
    }
    
    /**
     * 
     * @param dt
     * @param startX
     * @param startY 
     */
    public void update(double dt, int startX, int startY){
        int endX = (startX + m_nbTilesInScreenX + 2 <= m_nbTilesW)? startX + m_nbTilesInScreenX + 2 : m_nbTilesW;
        int endY = (startY + m_nbTilesInScreenY + 2 <= m_nbTilesH)? startY + m_nbTilesInScreenY + 2 : m_nbTilesH;
        
        /*for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(m_map[i][j]){
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
        }*/
        
        for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(m_topMap[i][j]){
                    case 12:
                        if(m_particles.size() < 4){
                            for(int k = 0; k < 4;k++){
                                m_particles.add(new Leaf(5,
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
        
        for(int i=0; i<m_particles.size();i++){
            Particle p = m_particles.get(i);
            p.update(dt);
            if(p.isDead()){
                m_particles.remove(i);
            }
        }
        
        for(int i=0;i<m_sandEntity.size();i++){
            SandEntity se = m_sandEntity.get(i);
            se.update(dt);
        }
        
        for(int i=0;i<m_cageEntity.size();i++){
            CageEntity ce = m_cageEntity.get(i);
            ce.update(dt);
        }
        
        int playerX = (int)m_player.getPosX();
        int playerY = (int)m_player.getPosY();
        
        for(int i=0;i<m_braconeers.size();i++){
            Braconeers b = m_braconeers.get(i);
            
            if(playerX >= b.getPosX() - Defines.DEFAULT_SCREEN_WIDTH/2 && playerX <= b.getPosX() + b.getBounds().width + Defines.DEFAULT_SCREEN_WIDTH/2 &&
                    playerY >= b.getPosY() - Defines.DEFAULT_SCREEN_HEIGHT/2 && playerY <= b.getPosY() + Defines.DEFAULT_SCREEN_HEIGHT/2)
            {
                if((m_nbLevel == 1 && m_viewedEvent[7]) || m_nbLevel > 1 )
                {
                    b.update(dt);
                }
            }   
            
            if(b.isDead()){
                m_braconeers.remove(i);
            }
        }
        
        /*
        for(int i = 0; i < springs.length; i++) 
            springs[i].update();

        float[] leftDeltas = new float[springs.length];
        float[] rightDeltas = new float[springs.length];

        for(int j = 0; j < 15; j++)
        {
            for(int i = 0; i < springs.length; i++)
            {
                if(i > 0) 
                {
                    leftDeltas[i] = spread * (springs[i].posy - springs[i - 1].posy);
                    springs[i - 1].speed += leftDeltas[i];
                }

                if(i < springs.length - 1)
                {
                    rightDeltas[i] = spread * (springs[i].posy - springs[i + 1].posy);
                    springs[i + 1].speed += rightDeltas[i];
                }			
            }

            for(int i = 0; i < springs.length; i++)
            {
                if(i > 0)
                    springs[i - 1].posy += leftDeltas[i];
                if(i < springs.length - 1)
                    springs[i + 1].posy += rightDeltas[i];
            }
        }

        for(int i = 0; i < particles.size(); i++) 
        {
            Particle particle = particles.get(i);
            if(particle instanceof Water)
            {
                Water p = (Water)particle;
                p.vely += gravity;

                p.x += p.velx;
                p.y += p.vely;

                p.orientation = (float) Math.atan2(p.vely, p.velx);

                if(p.x < 0 || p.x > Screen.RES_1X_WIDTH || p.y > y_offset)
                    particles.remove(i);
            }
        }*/
    }
    
    /**
     * 
     * @param screenWidth 
     */
    public void setNbTilesInScreenX(int screenWidth){
        m_nbTilesInScreenX = (int)(screenWidth / Defines.TILE_SIZE);
    }
    
    /**
     * 
     * @param screenHeight 
     */
    public void setNbTilesInScreenY(int screenHeight){
        m_nbTilesInScreenY = (int)(screenHeight / Defines.TILE_SIZE);
    }
    
    /**
     * 
     * @return 
     */
    public int getNbTilesInScreenX(){
        return m_nbTilesInScreenX;
    }
    
    /**
     * 
     * @return 
     */
    public int getNbTilesInScreenY(){
        return m_nbTilesInScreenY;
    }
    
    /**
     * 
     * @param g
     * @param startX
     * @param startY 
     */
    public void renderFirstLayer(Graphics g, int startX, int startY){
        
        Boolean debug = Profiler.getInstance().isVisible();
        int endX = (startX + m_nbTilesInScreenX + 2 <= m_nbTilesW)? startX + m_nbTilesInScreenX + 2 : m_nbTilesW;
        int endY = (startY + m_nbTilesInScreenY + 2 <= m_nbTilesH)? startY + m_nbTilesInScreenY + 2 : m_nbTilesH;
        
        //g.setColor(water);

        Graphics2D g2 = (Graphics2D)g;

        /*
        for(int i = 0; i < springs.length - 1; i++)
        {
            int[] xPoints = new int[] {(int)springs[i].posx, (int)springs[i + 1].posx,
                                                               (int)springs[i+1].posx, (int)springs[i].posx};
            int[] yPoints = new int[] {(int)springs[i].posy, (int)springs[i + 1].posy,
                                                                this.h, this.h};
            GradientPaint gp = new GradientPaint(this.y_offset, this.h, new Color(0, 0, 90, 170),
                            0, 0, water);
            g2.setPaint(gp);
            g2.fillPolygon(xPoints, yPoints, 4);
        }*/
        
        for(int i = startX;i<endX;i++){
            for(int j = startY;j<endY;j++){
                switch(m_map[i][j]){
                    case 1:
                        boolean left = false;
                        boolean right = false;
                        if(i > 0){
                            if(m_map[i-1][j] == 1){
                                left = true;
                            }
                        }
                        if(i < m_nbTilesW - 1){
                            if(m_map[i+1][j] == 1){
                                right = true;
                            }
                        }
                        TileAtlas.floor.render(g, i, j, left, right);
                        break;
                    case 2:
                        TileAtlas.bamboo.render(g, i, j);
                        break;
                    case 3:
                        int playerX = (int)m_player.getPosX() / Defines.TILE_SIZE;
                        int playerY = (int)m_player.getPosY() / Defines.TILE_SIZE;
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
                        if(m_data[i][j] != 2){
                            TileAtlas.sand.render(g, i, j);
                        }
                        break;
                    case 10:
                        if(m_map[i-1][j] != 11){
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
        
        for(int i=0;i<m_braconeers.size();i++){
            Braconeers b = m_braconeers.get(i);
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
        for(int i=0;i< m_cageEntity.size();i++){
            CageEntity ce = m_cageEntity.get(i);
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
        
        int endX = (startX + m_nbTilesInScreenX + 6 <= m_nbTilesW) ? startX + m_nbTilesInScreenX + 6 : m_nbTilesW;
        int endY = (startY + m_nbTilesInScreenY + 4 <= m_nbTilesH) ? startY + m_nbTilesInScreenY + 4 : m_nbTilesH;
        
        for(int i = startX;i<endX;i++)
        {
            for(int j = startY;j<endY;j++)
            {
                switch(m_topMap[i][j]){
                    case 10:
                        CageEntity ce = this.getCageEntity(i, j);
                        if(ce != null)
                            ce.renderTop(g);
                        break;
                    case 11:
                        TileAtlas.bush.render(g, i, j);
                        break;
                    case 12:
                        for(int k=0;k<m_particles.size();k++){
                            Particle p = m_particles.get(k);
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
        return m_map[x][y];
    }
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void removeTile(int x, int y){
        m_map[x][y] = 0;
    }
    
    /**
     * 
     * @param data 
     */
    public void setData(int[][] data)
    {
        for(int i=0;i< data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                if(m_map[i][j] == 10 && data[i][j] == 2)
                {
                    m_nbCages--;
                }
                if(m_map[i][j] == 9 && data[i][j] == 2)
                {
                    data[i][j] = 0;
                }
            }
        }
        m_data = data;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param val 
     */
    public void setData(int x, int y, int val)
    {
        m_data[x][y] = val;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public int getData(int x, int y)
    {
        return m_data[x][y];
    }
    
    /**
     * 
     * @param layer
     * @param filePath
     * @return 
     */
    public boolean loadLayer(int layer, String filePath){
        try{
            URL url = getClass().getResource(filePath);
            BufferedImage lvlImg = ImageIO.read(url);
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int width = lvlImg.getWidth();
            final int pixelLength = 4;
            ArrayList cageLevelMapping = new ArrayList<>();
            
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
               int argb = 0;
               argb += 0; // alpha
               argb += ((int) pixels[pixel + 1] & 0xff); // blue
               argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
               argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
               /*switch(argb){
                    case WHITE:
                        m_map[col][row] = TileAtlas.empty.ID;
                        break;
                    case GREEN:
                        m_map[col][row] = TileAtlas.bamboo.ID;
                        break;
                    case BLACK:
                        m_map[col][row] = TileAtlas.floor.ID;
                        break;
                    case BROWN:
                        m_map[col][row] = TileAtlas.bridge.ID;
                        break;
                    case RED:
                        m_map[col][row] = TileAtlas.apple.ID;
                        break;
                    case ORANGE:
                        m_map[col][row] = TileAtlas.leaves.ID;
                        break;
                    case BLUE:
                        m_map[col][row] = TileAtlas.pious.ID;
                        break;
                    case PURPLE:
                        m_map[col][row] = TileAtlas.levelup.ID;
                        break;
                    case PINK:
                        m_map[col][row] = TileAtlas.checkpoint.ID;
                        break;
                    case SAND:
                        m_map[col][row] = TileAtlas.sand.ID;
                        m_sandEntity.add(new SandEntity(this, col, row, m_context));
                        break;
                    case CAGE:
                        m_nbCages++;
                        CageEntity ce = new CageEntity(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE, m_context);
                        m_cageEntity.add(ce);
                        cageLevelMapping.add(ce);
                        m_map[col][row] = TileAtlas.cage.ID;
                        m_map[col+1][row] = TileAtlas.cage.ID;
                        col++;
                        pixel += pixelLength;
                        break;
                    case BRACONEER:
                        //m_braconeers.add(new Braconeers(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE, m_context));
                        break;
                    case LIME:
                        m_map[col][row] = TileAtlas.plant.ID;
                        break;
                    default:
                        break;
               }*/

               col++;
               if (col >= width) {
                  col = 0;
                  row++;
                }
            }
            m_cagesMap.set(m_nbLevel - 1, cageLevelMapping);
        }catch(IOException e){
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @param nbLevel 
     * @return  
     */
    public int loadLevel(int nbLevel)
    {
        int startPos = this.loadFirstLayer(nbLevel);
        this.loadTopLayer(nbLevel);
        return startPos;
    }
    
    /**
     * 
     * @param nbLevel 
     * @return  
     */
    public int loadFirstLayer(int nbLevel)
    {
        int startPos = 20;
        try
        {
            URL url = this.getClass().getResource("/lvl" + ((Defines.DEV)? "-dev" : "") + nbLevel + ".png");
            BufferedImage lvlImg = ImageIO.read(url);
            
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int width = lvlImg.getWidth();
            int height = lvlImg.getHeight();
            m_w = width * Defines.TILE_SIZE;
            m_h = height * Defines.TILE_SIZE;
            m_nbTilesH = height;
            m_nbTilesW = width;
            
            m_map = new int[width][height];
            m_data = new int[width][height];
            m_topMap = new int[width][height];
            
            for (int[] m_data1 : m_data)
            {
                for (int j = 0; j < m_data1.length; j++)
                {
                    m_data1[j] = 0;
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
                   /*switch(argb){
                        case WHITE:
                            m_map[col][row] = TileAtlas.empty.ID;
                            break;
                        case GREEN:
                            m_map[col][row] = TileAtlas.bamboo.ID;
                            break;
                        case BLACK:
                            m_map[col][row] = TileAtlas.floor.ID;
                            if(col == 0){
                                startPos = ( row - 1 ) * Defines.TILE_SIZE;
                            }
                            break;
                        case BROWN:
                            m_map[col][row] = TileAtlas.bridge.ID;
                            break;
                        case RED:
                            m_map[col][row] = TileAtlas.apple.ID;
                            break;
                        case ORANGE:
                            m_map[col][row] = TileAtlas.leaves.ID;
                            break;
                        case BLUE:
                            m_map[col][row] = TileAtlas.pious.ID;
                            break;
                        case PURPLE:
                            m_map[col][row] = TileAtlas.levelup.ID;
                            break;
                        case PINK:
                            m_map[col][row] = TileAtlas.checkpoint.ID;
                            break;
                        case SAND:
                            m_map[col][row] = TileAtlas.sand.ID;
                            m_sandEntity.add(new SandEntity(this, col, row, m_context));
                            break;
                        case CAGE:
                            m_nbCages++;
                            CageEntity ce = new CageEntity(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE, m_context);
                            m_cageEntity.add(ce);
                            cageLevelMapping.add(ce);
                            m_map[col][row] = TileAtlas.cage.ID;
                            m_map[col+1][row] = TileAtlas.cage.ID;
                            col++;
                            pixel += pixelLength;
                            break;
                        case BRACONEER:
                            //m_braconeers.add(new Braconeers(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE, m_context));
                            break;
                        case LIME:
                            m_map[col][row] = TileAtlas.plant.ID;
                            break;
                        default:
                            break;
                   }*/

                   col++;
                   if (col >= width) {
                      col = 0;
                      row++;
                    }
                }
                m_cagesMap.set(nbLevel - 1, cageLevelMapping);
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
                            m_topMap[col][row] = TileAtlas.empty.ID;
                            break;
                        case GREEN:
                            m_topMap[col][row] = TileAtlas.bush.ID;
                            break;
                        case BROWN:
                            m_topMap[col][row] = TileAtlas.tallTree.ID;
                            break;
                        case ORANGE:
                            m_topMap[col][row] = TileAtlas.smallTree.ID;
                            break;
                        case CAGE:
                            m_topMap[col][row] = TileAtlas.cage.ID;
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
        
        m_particles.add(p);
    }
    
    /**
     * 
     * @param p 
     */
    public void addPlayer(Player p){
        m_player = p;
        m_player.setPosY(m_startPosY);
    }
    
    /**
     * 
     * @param percentage 
     */
    public void setComplete(int percentage){
        m_complete = percentage;
    }
    
    /**
     * 
     * @return 
     */
    public int getComplete(){
        return m_complete;
    }
    
    /**
     * 
     */
    public void freeCage(){
        m_freeCages++;
        m_nbCages--;
    }
    
    /**
     * 
     * @return 
     */
    public int getFreeCages(){
        return m_freeCages;
    }
    
    /**
     * 
     * @param cagesInLevel 
     */
    public void setCagesInLevel(List<CageEntity> cagesInLevel){
        for(int i=0;i<cagesInLevel.size();i++){
            CageEntity ce = cagesInLevel.get(i);
            if(ce.isBreak()){
                m_nbCages--;
            }
        }
        m_cageEntity = cagesInLevel;
    }
    
    /**
     * 
     * @param index 
     */
    public void setUnlocked(int index){
        if(index > Defines.LEVEL_MAX - 1 || index < 1){
            return;
        }
        
        m_unlockedLevels[index-1] = true;
    }

    /**
     * 
     * @param index 
     */
    public void setLocked(int index){
        if(index > Defines.LEVEL_MAX - 1 || index < 0){
            return;
        }
        
        m_unlockedLevels[index] = false;
    }
    
    /**
     * 
     * @param unlockedLevels 
     */
    public void setUnlockedLevels(boolean[] unlockedLevels){
        m_unlockedLevels = unlockedLevels;
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
        
        for(int i = 0; i < m_braconeers.size(); i++){
            Braconeers b = m_braconeers.get(i);
            if(b.getBounds().intersects(x0, y0, w, h)){
                result.add(b);
            }
        }
        
        return result;
    }
    
    /*
    public void splash(float x, float speed)
    {
        float bestDistanceSoFar = end_pos;

        if(x > springs[springs.length - 1].posx || x < springs[0].posx)
        {
            return;
        }
        
        int index = springs.length / 2;

        for(int i = 0; i < springs.length; i++)
        {
            float distance = Math.abs(springs[i].posx - x);

            if(distance < bestDistanceSoFar)
            {
                bestDistanceSoFar = distance;
                index = i;
            }
        }

        springs[index].speed = speed*20;

        for(int i = 0; i < 20; i++)
        {
            float velx = (float) (Math.random()*speed - speed / 2);
            float vely = (float)(-Math.random()*speed);
            particles.add(new Water(springs[index].posx, springs[index].posy,
                            velx, vely, (float)Math.atan2(vely, velx)));
        }
    }*/
    
    /**
     * Return json data for saves.
     * @return JSONObject
     */
    public JSONObject toSave()
    {       
        JSONObject data = new JSONObject();
        data.put("number", m_nbLevel);
        data.put("time", "00:00");
        data.put("complete", m_complete);
        data.put("nbFreeCages", m_freeCages);
        
        JSONArray cages = new JSONArray();
        m_cagesMap.stream().map((list) ->
        {
            JSONArray levelCages = new JSONArray();
            for(int i = 0 ; i < list.size() ; i++)
            {
                JSONObject cageInfo = new JSONObject();
                CageEntity ce = (CageEntity)list.get(i);
                JSONArray pos = new JSONArray();
                pos.add(ce.getPosX());
                pos.add(ce.getPosY());
                cageInfo.put("pos", pos);
                cageInfo.put("free", ce.isBreak());
                levelCages.add(cageInfo);
            }           
            return levelCages;
        }).forEachOrdered((levelCages) ->
        {
            cages.add(levelCages);
        });
        data.put("cages", cages);
        
        return data;
    }
}
package level;

import core.Context;
import core.Defines;
import entity.Braconeers;
import entity.CageEntity;
import entity.Player;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import static level.LevelOld.BLACK;
import static level.LevelOld.BLUE;
import static level.LevelOld.BRACONEER;
import static level.LevelOld.BROWN;
import static level.LevelOld.CAGE;
import static level.LevelOld.GREEN;
import static level.LevelOld.LIME;
import static level.LevelOld.ORANGE;
import static level.LevelOld.PINK;
import static level.LevelOld.PURPLE;
import static level.LevelOld.RED;
import static level.LevelOld.SAND;
import static level.LevelOld.WHITE;
import level.tiles.TileAtlas;
import level.tiles.Tree;
import season.Season.SeasonState;
import weather.Weather.WeatherState;

public class Level
{
    private int m_number, m_width, m_height, m_nbTilesInScreenX, m_nbTilesInScreenY, m_nbTilesW, m_nbTilesH,
            m_freeCages, m_nbCages;
    private int[][] m_eventsPos, m_data;
    private boolean[] m_viewedEvent;
    private String m_name;
    private SeasonState m_season;
    private WeatherState m_weather;
    private Map<String, int[][]> m_layers;
    private Player m_player;
    private Context m_context;
    private List<CageEntity> m_cageEntity = new ArrayList<>();
    private List<BufferedImage> m_background = new ArrayList<>();
    
    /**
     * 
     */
    public Level(){
        m_number = 0;
        m_eventsPos = null;
        m_viewedEvent = null;
        m_name = "";
        m_season = SeasonState.SPRING;
        m_weather = WeatherState.SUN;
        m_nbTilesW = m_nbTilesH = 0;
        m_data = null;
        m_nbTilesInScreenX = m_nbTilesInScreenY = 0;
        m_layers = new HashMap<>();
        m_freeCages = m_nbCages = 0;
    }
    
    /**
     * Return level number
     * @return
     */
    public int getNumber(){
        return m_number;
    }
    
    /**
     * Set level number
     * @param number
     */
    public void setNumber(int number){
        m_number = number;
    }
    
    public Context getContext(){
        return m_context;
    }
    
    public void setContext(Context context){
        m_context = context;
    }
    
    /**
     * @return 
     */
    public String getName(){
        return m_name;
    }
    
    /**
     * @param name 
     */
    public void setName(String name){
        m_name = name;
    }
    
    /**
     * @return 
     */
    public int[][] getEventsPos(){
        return m_eventsPos;
    }
    
    /**
     * @param eventsPos 
     */
    public void setEventsPos(int[][] eventsPos){
        m_eventsPos = eventsPos;
        m_viewedEvent = new boolean[eventsPos.length];
        
        for(int i=0;i<m_viewedEvent.length;i++){
            m_viewedEvent[i] = false;
        }
    }
    
    /**
     * @return 
     */
    public SeasonState getSeason(){
        return m_season;
    }
    
    /**
     * @param season 
     */
    public void setSeason(SeasonState season){
        m_season = season;
    }
    
    /**
     * @return 
     */
    public WeatherState getWeather(){
        return m_weather;
    }
    
    /**
     * @param weather 
     */
    public void setWeather(WeatherState weather){
        m_weather = weather;
    }
    
    /**
     * 
     * @param img 
     */
    public void addBackground(BufferedImage img){
        m_background.add(img);
    }
    
    public List<BufferedImage> getBackgrounds(){
        return m_background;
    }
    
    /**
     * @return 
     */
    public int getWidth(){
        return m_width;
    }
    
    /**
     * @return 
     */
    public int getHeight(){
        return m_height;
    }
    
    /**
     * @param screenWidth 
     */
    public void setNbTilesInScreenX(int screenWidth){
        m_nbTilesInScreenX = (int)(screenWidth / Defines.TILE_SIZE);
    }
    
    /**
     * @param screenHeight 
     */
    public void setNbTilesInScreenY(int screenHeight){
        m_nbTilesInScreenY = (int)(screenHeight / Defines.TILE_SIZE);
    }
    
    /**
     * @return 
     */
    public int getNbTilesInScreenX(){
        return m_nbTilesInScreenX;
    }
    
    /**
     * @return 
     */
    public int getNbTilesInScreenY(){
        return m_nbTilesInScreenY;
    }
    
    /**
     * @return 
     */
    public int getNbTilesW(){
        return m_nbTilesW;
    }
    
    /**
     * @return 
     */
    public int getNbTilesH(){
        return m_nbTilesH;
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
     * @param name
     * @param filename
     * @return 
     */
    public boolean loadLayer(String name, String filename){
        try{
            URL url = this.getClass().getResource(filename);
            BufferedImage lvlImg = ImageIO.read(url);
            
            if(lvlImg == null){
                return false;
            }
            
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int width = lvlImg.getWidth();
            int height = lvlImg.getHeight();
            int startPos = 20;
            m_width = width * Defines.TILE_SIZE;
            m_height = height * Defines.TILE_SIZE;
            m_nbTilesH = height;
            m_nbTilesW = width;
            
            int[][] map = new int[width][height];
            m_data = new int[width][height];
            
            ArrayList cageLevelMapping = new ArrayList<>();    
            int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += 0; // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
               //System.out.println(argb);
                if(name.equals("layer-1")){
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
                            //m_sandEntity.add(new SandEntity(this, col, row, m_context));
                            break;
                        case CAGE:
                            m_nbCages++;
                            CageEntity ce = new CageEntity(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE, m_context);
                            m_cageEntity.add(ce);
                            cageLevelMapping.add(ce);
                            map[col][row] = TileAtlas.cage.ID;
                            map[col+1][row] = TileAtlas.cage.ID;
                            col++;
                            pixel += pixelLength;
                            break;
                        case BRACONEER:
                            //m_braconeers.add(new Braconeers(this, col * Defines.TILE_SIZE, row * Defines.TILE_SIZE, m_context));
                            break;
                        case LIME:
                            map[col][row] = TileAtlas.plant.ID;
                            break;
                        default:
                            break;
                    }
                }
                else
                {
                    switch(argb){
                        case WHITE:
                            map[col][row] = TileAtlas.empty.ID;
                            break;
                        case GREEN:
                            map[col][row] = TileAtlas.bush.ID;
                            break;
                        case BROWN:
                            map[col][row] = TileAtlas.tallTree.ID;
                            break;
                        case ORANGE:
                            map[col][row] = TileAtlas.smallTree.ID;
                            break;
                        case CAGE:
                            map[col][row] = TileAtlas.cage.ID;
                            break;
                        default:
                            break;
                   }
                }

                col++;
                if (col >= width) {
                   col = 0;
                   row++;
                }
            }
            
            m_layers.put(name, map);
        }
        catch(IOException e){
            return false;
        }
        
        return true;
    }
    
    public void freeCage(){
        m_freeCages++;
        m_nbCages--;
    }
    
    public void update(double dt, int startX, int startY){
        int endX = (startX + m_nbTilesInScreenX + 2 <= m_nbTilesW)? startX + m_nbTilesInScreenX + 2 : m_nbTilesW;
        int endY = (startY + m_nbTilesInScreenY + 2 <= m_nbTilesH)? startY + m_nbTilesInScreenY + 2 : m_nbTilesH;
        
        for(int i=0;i<m_cageEntity.size();i++){
            CageEntity ce = m_cageEntity.get(i);
            
            if(ce.getPosX() < startX && ce.getPosX() > endX && ce.getPosY() < startY && ce.getPosY() > endY){
                continue;
            }
            
            ce.update(dt);
        }
    }
    
    public void render(Graphics g, int startX, int startY){
        int endX = (startX + m_nbTilesInScreenX + 2 <= m_nbTilesW)? startX + m_nbTilesInScreenX + 2 : m_nbTilesW;
        int endY = (startY + m_nbTilesInScreenY + 2 <= m_nbTilesH)? startY + m_nbTilesInScreenY + 2 : m_nbTilesH;
        Graphics2D g2 = (Graphics2D)g;
        for(Map.Entry<String, int[][]> layer : m_layers.entrySet()){
            int[][] map = layer.getValue();
            for(int i = startX;i<endX;i++){
                for(int j = startY;j<endY;j++){
                    if(layer.getKey().equals("layer-1")){
                        switch(map[i][j]){
                            case 1:
                                boolean left = false;
                                boolean right = false;
                                if(i > 0){
                                    if(map[i-1][j] == 1){
                                        left = true;
                                    }
                                }
                                if(i < m_nbTilesW - 1){
                                    if(map[i+1][j] == 1){
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
                                /*if(m_data[i][j] != 2){
                                    TileAtlas.sand.render(g, i, j);
                                }*/
                                break;
                            case 10:
                                if(map[i-1][j] != 11){
                                    CageEntity ce = this.getCageEntity(i, j);
                                    if(ce != null)
                                        ce.render(g, false);
                                }
                                break;
                            case 14:
                                TileAtlas.plant.render(g, i, j);
                                break;
                            default:
                                break;
                        }
                    }else{
                        switch(map[i][j]){
                            case 10:
                                CageEntity ce = this.getCageEntity(i, j);
                                if(ce != null)
                                    ce.renderTop(g);
                                break;
                            case 11:
                                TileAtlas.bush.render(g, i, j);
                                break;
                            case 13:
                                TileAtlas.smallTree.render(g, i, j, Tree.SMALL);
                                break;
                            default:
                                break;
                        }
                        
                        if(i > 2 && map[i - 3][j] == 12){
                            /*for(int k=0;k<m_particles.size();k++){
                                    Particle p = m_particles.get(k);
                                    p.render(g);

                                }*/
                            TileAtlas.tallTree.render(g, i - 3, j, Tree.TALL);
                        }
                        else if(i < m_nbTilesW && map[i + 1][j] == 12){
                            /*for(int k=0;k<m_particles.size();k++){
                                    Particle p = m_particles.get(k);
                                    p.render(g);

                                }*/
                            TileAtlas.tallTree.render(g, i + 1, j, Tree.TALL);
                        }
                    }
                }
            }
        }
    }
    
    public void addPlayer(Player player){
        m_player = player;
    }
    
    public Player getPlayer(){
        return m_player;
    }
    
    public int getData(int x, int y){
        return m_data[x][y];
    }
    
    public void setData(int x, int y, int val){
        m_data[x][y] = val;
    }
    
    public void removeTile(int x, int y){
        m_layers.get("layer-1")[x][y] = TileAtlas.empty.ID;
    }
    
    public int getTile(int x, int y){
        return m_layers.get("layer-1")[x][y];
    }
    
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
    
    public List<Braconeers> getBraconeersEntities(int x0, int y0, int w, int h, boolean isStuck){
        //TODO
        return new ArrayList<>();
    }
    
    public boolean isViewedEvent(int eventId){
        if(eventId > m_viewedEvent.length){
            return true;
        }
        return m_viewedEvent[eventId];
    }
}

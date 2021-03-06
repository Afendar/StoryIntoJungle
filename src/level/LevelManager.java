package level;

import core.Context;
import core.ResourceManager;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import ld34.profile.Settings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import season.Season.SeasonState;
import weather.Weather.WeatherState;

/**
 * 
 */
public class LevelManager
{
    private static final String LEVELS_FOLDER = "levels/"; 
    private static final String LEVEL_SETTINGS_FILENAME = "settings.json";
    private final Context m_context;
    
    protected JSONParser m_parser;
    
    /**
     * 
     * @param context 
     */
    public LevelManager(Context context){
        m_parser = new JSONParser();
        m_context = context;
    }
    
    /**
     * 
     * @param nbLevel
     * @return 
     */
    public Level loadLevel(int nbLevel){
        File f = new File(LevelManager.LEVELS_FOLDER + "level_" + nbLevel);
        if(!f.exists() || !f.isDirectory()) {
            throw new RuntimeException("Level folder level_" + nbLevel + " not found.");
        }        
        
        f = new File(LevelManager.LEVELS_FOLDER + "level_" + nbLevel + "/" + LevelManager.LEVEL_SETTINGS_FILENAME);
        if(!f.exists() || !f.isFile()){
            throw new RuntimeException("File " + LevelManager.LEVEL_SETTINGS_FILENAME + " for level " + nbLevel + " not found.");
        }
        
        JSONObject o = readSettingFile(LevelManager.LEVELS_FOLDER + "level_" + nbLevel + "/" + LevelManager.LEVEL_SETTINGS_FILENAME);
        
        if(o == null){
            throw new RuntimeException("Cannot read level properties");
        }
        
        Level lvl = new Level();
        lvl.setNumber(nbLevel);
        lvl.setName((String)o.get("name"));
        lvl.setContext(m_context);
        lvl.setNbTilesInScreenX(m_context.m_screen.getWidth());
        lvl.setNbTilesInScreenY(m_context.m_screen.getHeight());
        JSONArray jsonEvents = (JSONArray)o.get("events");
        if(jsonEvents != null){
            int[][] eventsPos = new int[jsonEvents.size()][2];
            int i = 0;
            for(Object evt : jsonEvents){
                if(evt instanceof JSONArray){
                    JSONArray e = (JSONArray)evt;
                    Long x = (Long)e.get(0);
                    Long y = (Long)e.get(1);
                    eventsPos[i][0] = x.intValue();
                    eventsPos[i][1] = y.intValue();
                    i++;
                }
            }
            lvl.setEventsPos(eventsPos);
        }
        
        try{
            lvl.setSeason(SeasonState.valueOf((String)o.get("season")));
        }
        catch(IllegalArgumentException e){
            lvl.setSeason(SeasonState.SPRING);
        }
        
        try{
            lvl.setWeather(WeatherState.valueOf((String)o.get("weather")));
        }
        catch(IllegalArgumentException e){
            lvl.setWeather(WeatherState.SUN);
        }
        
        JSONArray backgrounds = (JSONArray)o.get("background");
        if(backgrounds != null){
            backgrounds.stream().filter((bg) -> (bg instanceof String)).forEachOrdered((bg) ->
            {
                lvl.addBackground(m_context.m_resourceManager.getBackground((String)bg));
            });
        }
        
        String tilesetName = (String)o.get("tileset");
        if(tilesetName.equals("")){
            lvl.setTileset(m_context.m_resourceManager.getSpritesheets("tileset2"));
        }
        else{
            lvl.setTileset(m_context.m_resourceManager.getSpritesheets(tilesetName));
        }
        
        JSONObject map = (JSONObject)o.get("map");
        for(Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
            String layerName = (String) iterator.next();
            if(!lvl.loadLayer(layerName, "/level_" + nbLevel + "/" + map.get(layerName))){
                System.out.println("Error when loading: " + layerName);
            }
        }
        
        return lvl;
    }
    
    /**
     * 
     * @param filepath
     * @return 
     */
    private JSONObject readSettingFile(String filepath){
        try{
            return (JSONObject) m_parser.parse(new FileReader(filepath));
        }
        catch (IOException | ParseException e){
            e.getMessage();
        }
        return null;
    }
    
    /**
     * @param args 
     */
    public static void main(String[] args){
        Context c = new Context();
        Settings.init("Afendar");
        c.m_resourceManager = ResourceManager.getInstance();
        LevelManager lm = new LevelManager(c);
        lm.loadLevel(2);
    }
}

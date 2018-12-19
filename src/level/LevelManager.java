package level;

import core.ResourceManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import ld34.profile.Settings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import season.Season.SeasonState;
import weather.Weather.WeatherState;

public class LevelManager
{
    private static String LEVELS_FOLDER = "levels/"; 
    private static String LEVEL_SETTINGS_FILENAME = "settings.json";
    private ResourceManager m_resourceManager;
    
    protected JSONParser m_parser;
    
    public LevelManager(ResourceManager resourceManager){
        m_parser = new JSONParser();
        m_resourceManager = resourceManager;
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
        
        BufferedImage bg = m_resourceManager.getBackground((String)o.get("background"));
        lvl.setBackground(bg);
        
        return lvl;
    }
    
    private JSONObject readSettingFile(String filepath){
        try
        {
            return (JSONObject) m_parser.parse(new FileReader(filepath));
        }
        catch (IOException | ParseException e)
        {
            e.getMessage();
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        Settings.init("Afendar");
        ResourceManager rm = ResourceManager.getInstance();
        LevelManager lm = new LevelManager(rm);
        Level l = lm.loadLevel(1);
        System.out.println("done");
    }
}

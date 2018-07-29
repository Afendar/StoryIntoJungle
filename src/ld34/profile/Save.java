package ld34.profile;

import entity.Player;
import level.Level;
import org.json.simple.JSONObject;

/**
 * Save class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Save extends Profile
{
    protected static Save INSTANCE;
    
    /**
     * 
     */
    private Save(String profileName)
    {
        super(profileName);
    }
    
    public static void init(String profileName)
    {
        if(INSTANCE != null)
        {
            return;
        }
        
        INSTANCE = new Save(profileName);
    }
    
    public static Save getInstance()
    {
        if(INSTANCE == null)
        {
            throw new RuntimeException("Save INSTANCE may not be initialized");
        }
        
        return INSTANCE;
    }
    
    /**
     * 
     * @param slotNumber 
     */
    public void removeSave(int slotNumber)
    {
        
    }
    
    /**
     * 
     * @return 
     */
    public boolean hasSave()
    {
        return false;
    }
    
    /**
     * 
     * @param slotId
     * @param level
     * @param player 
     */
    public void saveGame(int slotId, Level level, Player player)
    {
        
    }
    
    /**
     * 
     * @param slotNumber
     * @return 
     */
    public JSONObject getSave(int slotNumber)
    {
        return new JSONObject();
    }
    
    public JSONObject getSaves()
    {
        return new JSONObject();
    }
}

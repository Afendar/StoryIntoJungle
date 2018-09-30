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
    
    /**
     * 
     * @param profileName 
     */
    public static void init(String profileName)
    {
        if(INSTANCE != null)
        {
            return;
        }
        
        INSTANCE = new Save(profileName);
    }
    
    /**
     * 
     * @return 
     */
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
        JSONObject saves = (JSONObject)m_data.get("saves");
        for(int i = 1 ; i <= saves.size() ; i++)
        {
            JSONObject save = (JSONObject)saves.get("slot" + i);
            if(!save.isEmpty())
            {
                return true;
            }
        }
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
        JSONObject saves = (JSONObject)m_data.get("saves");
        JSONObject slot = (JSONObject)saves.get("slot" + slotId);
        slot.put("level", level.toSave());
        slot.put("player", player.toSave());
        saves.put("slot" + slotId, slot);
        m_data.put("saves", saves);
        save();
    }
    
    /**
     * 
     * @param slotNumber
     * @return 
     */
    public JSONObject getSave(int slotNumber)
    {
        JSONObject saves = (JSONObject)m_data.get("saves");
        return (JSONObject)saves.get("slot" + slotNumber);
    }
    
    /**
     * 
     * @return 
     */
    public JSONObject getSaves()
    {
        return (JSONObject)m_data.get("saves");
    }
}

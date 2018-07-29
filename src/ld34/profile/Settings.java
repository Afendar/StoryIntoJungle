package ld34.profile;

import org.json.simple.JSONObject;

/**
 * Settings class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class Settings extends Profile
{
    protected static Settings INSTANCE;

    /**
     *
     */
    private Settings(String profileName)
    {
        super(profileName);
    }

    public static void init(String profileName)
    {
        if(INSTANCE != null)
        {
            return;
        }
        
        INSTANCE = new Settings(profileName);
    }
    
    public static Settings getInstance()
    {
        if(INSTANCE == null)
        {
            throw new RuntimeException("Settings INSTANCE may not be initialized");
        }
        
        return INSTANCE;
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public void setConfigValue(String key, String value)
    {
        
    }

    /**
     *
     * @param key
     * @return
     */
    public String getConfigValue(String key)
    {
        JSONObject o = (JSONObject)(m_data.get("settings"));
        return o.get(key).toString();
    }
}
package ld34.profile;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 * Settings class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Settings extends Profile {
    
    private JSONObject jsonSettings;
    private final Map<String, String> defaultValues;
    
    private static final Settings INSTANCE = new Settings();
    
    /**
     * 
     */
    private Settings(){
        super();
        
        this.defaultValues = new HashMap<>();
        this.defaultValues.put("Lang", "0");
        this.defaultValues.put("Difficulty", "0");
        this.defaultValues.put("Jump", Integer.toString(KeyEvent.VK_SPACE));
        this.defaultValues.put("Walk", Integer.toString(KeyEvent.VK_CONTROL));
        this.defaultValues.put("Sex", "0");
        this.defaultValues.put("Spicies", "0");
        this.defaultValues.put("Sound", "100");
        this.defaultValues.put("Name", "");
        
        this.loadConfigs();
    }
    
    /**
     * 
     * @return 
     */
    public static Settings getInstance(){
        return INSTANCE;
    }
    
    /**
     * 
     */
    private void loadConfigs(){
        this.load();
        this.jsonSettings = (JSONObject) this.profile.get("Settings");
        if(this.jsonSettings.isEmpty()){
            this.defaultValues.entrySet().stream().forEach((entry) -> {
                this.jsonSettings.put(entry.getKey(), entry.getValue());
            });
        }
    }
    
    /**
     * 
     * @param key
     * @param value 
     */
    public void setConfigValue(String key, String value){
        this.jsonSettings.replace(key, value);
    }
    
    /**
     * 
     * @param key
     * @return 
     */
    public String getConfigValue(String key){
        return this.jsonSettings.get(key).toString();
    }
    
    /**
     * 
     */
    public void saveConfig(){
        this.profile.replace("Settings", this.jsonSettings);
        this.save();
    }
}

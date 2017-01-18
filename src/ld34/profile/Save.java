package ld34.profile;

import ld34.Game;
import org.json.simple.JSONObject;

public class Save extends Profile {
    
    private JSONObject jsonSaves;
    
    private static final Save INSTANCE = new Save();
    
    private Save(){
        super();
        this.loadSaves();
    }
    
    public static Save getInstance(){
        return INSTANCE;
    }
    
    private void loadSaves(){
        this.load();
        this.jsonSaves = (JSONObject) this.profile.get("Saves");
        if(this.jsonSaves.isEmpty()){
            for(int i=0;i<3;i++){
                this.jsonSaves.put("Slot" + i, new JSONObject());
            }
        }
    }
    
    public void removeSave(int slotNumber){
        this.jsonSaves.replace("Slot" + slotNumber, new JSONObject());
        this.save();
    }
    
    public boolean hasSave(){
        for(int i = 0;i<this.jsonSaves.size();i++){
            JSONObject save = (JSONObject)this.jsonSaves.get("Slot" + i);
            if(!save.isEmpty()){
                return true;
            }
        }
        return false;
    }
    
    public void saveGame(String slot, Game game){
        this.jsonSaves.replace(slot, new JSONObject());
    }
    
    public void saveSaves(){
        this.profile.remove("Saves", this.jsonSaves);
        this.save();
    }
    
    public JSONObject getSave(int slotNumber){
        return (JSONObject) this.jsonSaves.get("Slot" + slotNumber);
    }
    
    public JSONObject getSaves(){
        return this.jsonSaves;
    }
}

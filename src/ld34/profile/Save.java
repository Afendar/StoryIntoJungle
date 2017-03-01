package ld34.profile;

import core.Defines;
import entity.CageEntity;
import entity.Player;
import java.util.List;
import level.Level;
import org.json.simple.JSONArray;

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
    
    public void saveGame(int slotId, Level level, Player player){
        JSONObject data = new JSONObject();
        
        JSONObject levelData = new JSONObject();
        levelData.put("difficulty", Integer.toString(player.difficulty));
        levelData.put("number", Integer.toString(level.nbLevel));
        levelData.put("time", "00:00");
        levelData.put("complete", Integer.toString(level.getComplete()));
        levelData.put("freeCages", Integer.toString(level.getFreeCages()));
        
        JSONObject cages = new JSONObject();
        for(int i = 0; i < level.cagesMap.size(); i++){
            List<CageEntity> cagesInLevel = level.cagesMap.get(i);
            JSONArray cagesDatasInLevel = new JSONArray();
            for(int j = 0;j < cagesInLevel.size();j++){
                CageEntity ce = cagesInLevel.get(j);
                JSONArray cageDatas = new JSONArray();
                cageDatas.add(ce.getPosX());
                cageDatas.add(ce.getPosY());
                cageDatas.add(ce.isBreak());
                cagesDatasInLevel.add(cageDatas);
            }
            cages.put(i + 1, cagesDatasInLevel);
        }
        levelData.put("cages", cages);
        
        data.put("level", levelData);
        
        JSONObject playerData = new JSONObject();
        playerData.put("spicies", Settings.getInstance().getConfigValue("Spicies"));
        playerData.put("score", Integer.toString(player.score));
        playerData.put("sex", Settings.getInstance().getConfigValue("Sex"));
        playerData.put("name", player.getName() != null ? player.getName() : "");
        JSONArray coords = new JSONArray();
        coords.add((int)player.getPosX());
        coords.add((int)player.getPosY());
        playerData.put("coords", coords);
        data.put("player", playerData);
        
        this.jsonSaves.replace("Slot" + slotId, data);
        this.saveSaves();
    }
    
    public void saveSaves(){
        this.profile.replace("Saves", this.jsonSaves);
        this.save();
    }
    
    public JSONObject getSave(int slotNumber){
        return (JSONObject) this.jsonSaves.get("Slot" + slotNumber);
    }
    
    public JSONObject getSaves(){
        return this.jsonSaves;
    }
}

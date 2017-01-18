package ld34.profile;

import org.json.simple.JSONArray;

public class BestScores extends Profile {
    
    private JSONArray bestScores;
    
    private static final BestScores INSTANCE = new BestScores();
    
    private BestScores(){
        super();
        this.loadBestScores();
    }
    
    public static BestScores getInstance(){
        return INSTANCE;
    }
    
    public JSONArray getBestScores(){
        return this.bestScores;
    }
    
    private void loadBestScores(){
        this.load();
        this.bestScores = (JSONArray)this.profile.get("BestScores");
    }
    
    public void insertScore(String name, int score){
        JSONArray jsonScores = this.bestScores;
        int insertion = -1;
        for(int i = 0; i<this.bestScores.size();i++){
            JSONArray arr = (JSONArray) this.bestScores.get(i);
            if(Integer.parseInt((String)arr.get(1)) < score){
                insertion = i;
                break;
            }
        }
        
        if(insertion != -1){
            JSONArray jsonScore = new JSONArray();
            jsonScore.add(name);
            jsonScore.add(Integer.toString(score));
            jsonScores.add(insertion, jsonScore);
            jsonScores.remove(5);
            this.bestScores = jsonScores;
            this.saveBestScores();
        }
    }
    
    public void saveBestScores(){
        this.profile.replace("BestScores", this.bestScores);
    }
}

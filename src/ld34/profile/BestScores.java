package ld34.profile;

import org.json.simple.JSONArray;

/**
 * BestScores class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class BestScores
{
    private JSONArray bestScores;
    
    private static BestScores INSTANCE;
    
    /**
     * 
     */
    private BestScores()
    {
        super();
        this.loadBestScores();
    }
    
    /**
     * 
     * @return 
     */
    public static BestScores getInstance(){
        return INSTANCE;
    }
    
    /**
     * 
     * @return 
     */
    public JSONArray getBestScores()
    {
        return new JSONArray();
    }
    
    /**
     * 
     */
    private void loadBestScores()
    {
        
    }
    
    /**
     * 
     * @param name
     * @param score 
     */
    public void insertScore(String name, int score)
    {
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
    
    /**
     * 
     */
    public void saveBestScores()
    {
        
    }
}
package ld34.profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * BestScores class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class BestScores
{
    private JSONArray m_bestScores;
    
    private static BestScores INSTANCE;
    
    private final String FOLDER = "saves";
    
    private final String m_filePath;
    
    private final JSONParser m_parser;
    
    /**
     * 
     */
    private BestScores()
    {
        m_filePath = FOLDER + File.separator + "best_scores.dat";
        m_parser = new JSONParser();
        
        File folder = new File(FOLDER);
        if(!folder.exists() && !folder.isDirectory())
        {
            folder.mkdir();
        }
        
        this.loadBestScores();
    }
    
    /**
     * 
     * @return 
     */
    public static BestScores getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new BestScores();
        }
        
        return INSTANCE;
    }
    
    /**
     * 
     * @return 
     */
    public JSONArray getBestScores()
    {
        return m_bestScores;
    }
    
    /**
     * 
     */
    private void loadBestScores()
    {
        File f = new File(m_filePath);
        if(f.exists() && !f.isDirectory())
        {
            try
            {
                JSONObject data = (JSONObject) m_parser.parse(new FileReader(m_filePath));
                m_bestScores = (JSONArray)data.get("scores");
            }
            catch(IOException|ParseException e)
            {
                e.getMessage();
            }
        }
        else
        {
            m_bestScores = new JSONArray();
            saveBestScores();
        }
    }
    
    /**
     * 
     * @param name
     * @param score 
     */
    public void insertScore(String name, int score)
    {
        JSONArray jsonScores = m_bestScores;
        int insertion = -1;
        
        for(int i = 0; i<m_bestScores.size();i++)
        {
            JSONArray arr = (JSONArray)m_bestScores.get(i);
            if(Integer.parseInt((String)arr.get(1)) < score)
            {
                insertion = i;
                break;
            }
        }
        
        if(insertion != -1)
        {
            JSONArray jsonScore = new JSONArray();
            jsonScore.add(name);
            jsonScore.add(Integer.toString(score));
            jsonScores.add(insertion, jsonScore);
            jsonScores.remove(5);
            m_bestScores = jsonScores;
            saveBestScores();
        }
    }
    
    /**
     * 
     */
    public void saveBestScores()
    {
        try
        {
            PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(m_filePath)
                )
            );
            pw.println(m_bestScores.toString());
            pw.flush();
            pw.close();
        }
        catch(IOException e)
        {
            e.getMessage();
        }
    }
}
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
 * Profile class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class Profile
{    
    /** */
    protected JSONObject profile;
    
    /** */
    protected JSONParser parser;
    
    /** */
    protected String FOLDER = "saves";
    
    /** */
    protected String FILENAME = FOLDER + File.separatorChar + "data.json";
    
    /**
     * 
     */
    public Profile()
    {
        this.profile = new JSONObject();
        this.profile.put("Settings", new JSONObject());
        this.profile.put("Saves", new JSONObject());
        this.profile.put("BestScores", new JSONArray());

        this.parser = new JSONParser();
        
        File folder = new File(FOLDER);
        if(!folder.exists() && !folder.isDirectory())
        {
            folder.mkdir();
        }
        load();
    }
    
    /**
     * 
     */
    protected void save()
    {
        try
        {
            PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(FILENAME)
                )
            );
            pw.println(this.profile.toString());
            pw.flush();
            pw.close();
        }
        catch(IOException e)
        {
            e.getMessage();
        }
    }
    
    /**
     * 
     */
    protected void load()
    {
        File f = new File(FILENAME);
        if(f.exists() && !f.isDirectory())
        {
            try
            {
                this.profile = (JSONObject) this.parser.parse(new FileReader(FILENAME));
            }
            catch(IOException|ParseException e){
                e.getMessage();
            }
        }
        else
        {
            this.save();
        }
    }
}
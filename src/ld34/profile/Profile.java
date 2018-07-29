package ld34.profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    protected JSONParser m_parser;
    
    protected String FOLDER = "saves";
    
    protected String m_filePath;
    
    protected JSONObject m_data;
    
    protected Profile(String profileName)
    {
        m_parser = new JSONParser();
        m_filePath = FOLDER + File.separator + profileName + ".json";
        
        File folder = new File(FOLDER);
        if(!folder.exists() && !folder.isDirectory())
        {
            folder.mkdir();
        }
        
        load();
    }
    
    public void save()
    {
        try
        {
            PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(m_filePath)
                )
            );
            pw.println(m_data.toString());
            pw.flush();
            pw.close();
        }
        catch(IOException e)
        {
            e.getMessage();
        }
    }
    
    public void load()
    {
        File f = new File(m_filePath);
        if(f.exists() && !f.isDirectory())
        {
            try
            {
                m_data = (JSONObject) m_parser.parse(new FileReader(m_filePath));
            }
            catch(IOException|ParseException e)
            {
                e.getMessage();
            }
        }
        else
        {
            m_data = setDefaultData();
            save();
        }
    }
    
    protected JSONObject setDefaultData()
    {
        File f = new File("bin" + File.separator + "default.json");
        JSONObject data = null;
        
        if(!f.exists())
        {
            throw new RuntimeException("default.json not exist");
        }
        
        try
        {
           data = (JSONObject) m_parser.parse(new FileReader(f));
        }
        catch(IOException|ParseException e)
        {
            e.getMessage();
        }
        return data;
    }
}
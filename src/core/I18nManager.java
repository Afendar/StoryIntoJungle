package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class I18nManager
{
    private static I18nManager _INSTANCE = null;
    
    public static I18nManager getInstance()
    {
        if(_INSTANCE == null)
        {
            _INSTANCE = new I18nManager();
        }
        return _INSTANCE;
    }
    
    public enum Language
    {
        FRENCH("fr_FR"),
        ENGLISH("en_EN");
        
        private final String m_lcid;
        
        private Language(String lcid)
        {
            m_lcid = lcid;
        }
        
        @Override
        public String toString()
        {
            return m_lcid;
        }
    }
    
    private Language m_lang;
    private Properties m_translations;
    
    private I18nManager()
    {
        m_lang = Language.ENGLISH;
        m_translations = new Properties();
        loadTranslations();
    }
    
    public void setLanguage(Language language)
    {
        m_lang = language;
        
        loadTranslations();
    }
    
    public Language getLanguage()
    {
        return m_lang;
    }
    
    private void loadTranslations()
    {
        try
        {
            FileInputStream file = new FileInputStream(new File(getClass().getResource("/lang_" + m_lang + ".properties").getFile()));
            m_translations.load(file);
        }
        catch (FileNotFoundException ex)
        {
            //Logger.getLogger(I18nManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            //Logger.getLogger(I18nManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String trans(String key)
    {   
        return m_translations.getProperty(key);
    }
}

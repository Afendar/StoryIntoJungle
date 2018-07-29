package core;

import java.util.logging.Logger;

/**
 * 
 */
public class Context
{
    public InputsListeners m_inputsListener;
    public I18nManager m_I18nManager;
    public ResourceManager m_resourceManager;
    public Screen m_screen;
    public Logger m_logger;
    
    public String m_profileName;

    /**
     * 
     */
    public void save()
    {
        //TODO save context into settings file.
    }
}

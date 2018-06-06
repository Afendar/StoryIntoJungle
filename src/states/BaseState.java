package states;

import core.StateManager;
import java.awt.Graphics2D;

/**
 * Abstract base state
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class BaseState
{
    /** StateManager m_stateManager */
    protected StateManager m_stateManager;
    
    /** boolean m_transparent */
    protected boolean m_transparent;
    
    /** boolean m_transcendent */
    protected boolean m_transcendent;
    
    /**
     * 
     * @param stateManager 
     */
    public BaseState(StateManager stateManager)
    {
        m_stateManager = stateManager;
        m_transcendent = false;
        m_transparent = false;
    }
    
    /**
     * 
     */
    public abstract void onCreate();
    
    /**
     * 
     */
    public abstract void onDestroy();
    
    /**
     * 
     */
    public abstract void activate();
    
    /**
     * 
     */
    public abstract void desactivate();
    
    /**
     * 
     */
    public abstract void reloadLocales();
    
    /**
     * 
     * @param dt 
     */
    public abstract void update(double dt);
    
    /**
     * 
     * @param g 
     */
    public abstract void render(Graphics2D g);
    
    /**
     * 
     * @param b 
     */
    public void setTransparent(boolean b)
    {
        m_transparent = b;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isTransparent()
    {
        return m_transparent;
    }
    
    /**
     * 
     * @param b 
     */
    public void setTranscendent(boolean b)
    {
        m_transcendent = b;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isTranscendent()
    {
        return m_transcendent;
    }
    
    /**
     * 
     * @return 
     */
    public StateManager getStateManager()
    {
        return m_stateManager;
    }
}

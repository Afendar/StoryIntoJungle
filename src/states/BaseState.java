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
    protected StateManager m_stateManager;
    protected boolean m_transparent;
    protected boolean m_transcendent;
    
    public BaseState(StateManager stateManager)
    {
        m_stateManager = stateManager;
        m_transcendent = false;
        m_transparent = false;
    }
    
    public abstract void onCreate();
    public abstract void onDestroy();
    
    public abstract void activate();
    public abstract void desactivate();
    
    public abstract void reloadLocales();
    
    public abstract void update(double dt);
    public abstract void render(Graphics2D g);
    
    public void setTransparent(boolean b)
    {
        m_transparent = b;
    }
    
    public boolean isTransparent()
    {
        return m_transparent;
    }
    
    public void setTranscendent(boolean b)
    {
        m_transcendent = b;
    }
    
    public boolean isTranscendent()
    {
        return m_transcendent;
    }
    
    public StateManager getStateManager()
    {
        return m_stateManager;
    }
}

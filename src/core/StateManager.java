package core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import states.BaseState;
import states.IntroState;
import states.PausedState;

/**
 * StateManager class for managing states ( menu, pause, in game... ).
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class StateManager
{
    private Map<StateType, BaseState> m_statesFactory;
    
    private ArrayList<BaseState> m_states;
    
    public StateManager()
    {
        m_statesFactory = new HashMap<>();
        m_states = new ArrayList<>();
        
        registerState(StateType.INTRO);
        registerState(StateType.PAUSED);
    }
    
    public void update(double dt)
    {
        if(m_states.isEmpty())
        {
            return;
        }
        
        if(m_states.get(m_states.size() - 1).isTranscendent() && m_states.size() > 1)
        {
            int i;
            for(i = m_states.size() - 1 ; i >= 0 ; --i)
            {
                if(!m_states.get(i).isTranscendent())
                {
                    break;
                }
            }
            for( ; i < m_states.size() ; i++)
            {
                m_states.get(i).update(dt);
            }
        }
        else
        {
            m_states.get(m_states.size() - 1).update(dt);
        }
    }
    
    public void render(Graphics2D g)
    {
        if(m_states.isEmpty())
        {
            return;
        }
        
        if(m_states.get(m_states.size() - 1).isTransparent() && m_states.size() > 1)
        {
            int i;
            for(i = m_states.size() - 1 ; i >= 0 ; --i)
            {
                if(!m_states.get(i).isTransparent())
                {
                    break;
                }
            }
            for( ; i < m_states.size() ; i++)
            {
                m_states.get(i).render(g);
            }
        }
        else
        {
            m_states.get(m_states.size() - 1).render(g);
        }
    }
    
    public void switchTo(StateType type)
    {
        if(!m_states.isEmpty())
        {
            m_states.get(m_states.size() - 1).desactivate();
        }
        createState(type);
        m_states.get(m_states.size() - 1).activate();
    }
    
    public void remove(StateType type)
    {
        
    }
    
    private void createState(StateType type)
    {
        BaseState s = m_statesFactory.get(type);
        if(s == null)
        {
            return;
        }
        
        m_states.add(s);
        s.onCreate();
    }
    
    private void removeState(StateType type)
    {
        
    }
    
    private void registerState(StateType type)
    {
        switch(type)
        {
            case INTRO:
                m_statesFactory.put(StateType.INTRO, new IntroState(this));
                break;
            case PAUSED:
                m_statesFactory.put(StateType.PAUSED, new PausedState(this));
                break;
        }
    }
}

package core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import states.BaseState;
import states.CreditsState;
import states.EndState;
import states.GameState;
import states.HighScoresState;
import states.IntroState;
import states.MainMenuState;
import states.MapState;
import states.PausedState;
import states.SavesState;
import states.SettingsState;

/**
 * StateManager class for managing states ( menu, pause, in game... ).
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class StateManager
{
    private Map<StateType, BaseState> m_statesFactory;
    private Context m_context;
    private ArrayList<BaseState> m_states;
    
    public StateManager(Context context)
    {
        m_statesFactory = new HashMap<>();
        m_states = new ArrayList<>();
        m_context = context;
        
        registerState(StateType.INTRO);
        registerState(StateType.MAIN_MENU);
        registerState(StateType.CREDITS);
        registerState(StateType.HIGHT_SCORES);
        registerState(StateType.SAVES);
        registerState(StateType.END);
        registerState(StateType.SETTINGS);
        registerState(StateType.GAME);
        registerState(StateType.MAP);
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
    
    public BaseState getState(StateType type)
    {
        if(m_states.isEmpty())
        {
           return null; 
        }
        
        BaseState search = m_statesFactory.get(type);
        
        for (BaseState state : m_states) {
            if(state == search)
            {
                return state;
            }
        }
        return null;
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
            case MAIN_MENU:
                m_statesFactory.put(StateType.MAIN_MENU, new MainMenuState(this));
                break;
            case CREDITS:
                m_statesFactory.put(StateType.CREDITS, new CreditsState(this));
                break;
            case HIGHT_SCORES:
                m_statesFactory.put(StateType.HIGHT_SCORES, new HighScoresState(this));
                break;
            case SAVES:
                m_statesFactory.put(StateType.SAVES, new SavesState(this));
                break;
            case END:
                m_statesFactory.put(StateType.END, new EndState(this));
                break;
            case SETTINGS:
                m_statesFactory.put(StateType.SETTINGS, new SettingsState(this));
                break;
            case GAME:
                m_statesFactory.put(StateType.GAME, new GameState(this));
                break;
            case MAP:
                m_statesFactory.put(StateType.MAP, new MapState(this));
                break;
            case PAUSED:
                m_statesFactory.put(StateType.PAUSED, new PausedState(this));
                break;
        }
    }
    
    public Context getContext()
    {
        return m_context;
    }
}

package core;

import core.I18nManager.Language;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import states.BaseState;
import states.CreditsState;
import states.EndState;
import states.GameState;
import states.HighScoresState;
import states.IntroState;
import states.MainMenuState;
import states.MapState;
import states.PausedSavesState;
import states.PausedSettingsState;
import states.PausedState;
import states.SavesState;
import states.SettingsState;
import states.TestState;
import states.TutorialState;

/**
 * StateManager class for managing states ( menu, pause, in game... ).
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class StateManager
{
    private final Map<StateType, BaseState> m_statesFactory;
    private final Context m_context;
    private final Map<StateType, BaseState> m_states;
    private Language m_currentLocal;
    
    public StateManager(Context context)
    {
        m_statesFactory = new HashMap<>();
        m_states = new LinkedHashMap<>();
        m_context = context;
        
        m_currentLocal = m_context.m_I18nManager.getLanguage();
        
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
        registerState(StateType.PAUSED_SAVES);
        registerState(StateType.PAUSED_SETTINGS);
        registerState(StateType.TUTORIAL);
        registerState(StateType.TEST);
    }
    
    public void update(double dt)
    {
        if(m_states.isEmpty())
        {
            return;
        }
        
        if(m_context.m_I18nManager.getLanguage() != m_currentLocal)
        {
            m_currentLocal = m_context.m_I18nManager.getLanguage();
            m_states.entrySet().stream().forEach((entry) -> {
                entry.getValue().reloadLocales();
            });
        }
        
        BaseState[] states = m_states.values().toArray(new BaseState[0]);
        
        if(states[states.length - 1].isTranscendent() && states.length > 1)
        {
            int i;
            for(i = states.length - 1 ; i >= 0 ; --i)
            {
                if(!states[i].isTranscendent())
                {
                    break;
                }
            }
            for( ; i < states.length ; i++)
            {
                states[i].update(dt);
            }
        }
        else
        {
            states[m_states.size() - 1].update(dt);
        }
    }
    
    public void render(Graphics2D g)
    {
        if(m_states.isEmpty())
        {
            return;
        }
        
        BaseState[] states = m_states.values().toArray(new BaseState[0]);
        
        if(states[states.length - 1].isTransparent() && states.length > 1)
        {
            int i;
            for(i = states.length - 1 ; i >= 0 ; --i)
            {
                if(!states[i].isTransparent())
                {
                    break;
                }
            }
            for( ; i < states.length ; i++)
            {
                states[i].render(g);
            }
        }
        else
        {
            ((BaseState)m_states.values().toArray()[m_states.size() - 1]).render(g);
        }
    }
    
    public void switchTo(StateType type)
    {
        if(!m_states.isEmpty())
        {
            ((BaseState)m_states.values().toArray()[m_states.size() - 1]).desactivate();
        }
        
        if(m_states.containsKey(type))
        {
            BaseState states = (BaseState)m_states.values().toArray()[m_states.size() - 1];
            states.desactivate();
            
            BaseState tmpState = m_states.get(type);
            m_states.remove(type);
            m_states.put(type, tmpState);
            tmpState.activate();
            return;
        }
        
        createState(type);
        ((BaseState)m_states.values().toArray()[m_states.size() - 1]).activate();
    }
    
    public BaseState getState(StateType type)
    {
        if(m_states.isEmpty())
        {
           return null; 
        }
        
        BaseState search = m_statesFactory.get(type);
        
        if(m_states.containsKey(type))
        {
            return m_states.get(type);
        }
        return null;
    }
    
    public void remove(StateType type)
    {
        removeState(type);
    }
    
    private void createState(StateType type)
    {       
        BaseState s = m_statesFactory.get(type);
        if(s == null)
        {
            return;
        }
        
        m_states.putIfAbsent(type, s);
        s.onCreate();
    }
    
    private void removeState(StateType type)
    {
        m_states.get(type).onDestroy();
        m_states.remove(type);
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
            case PAUSED_SAVES:
                m_statesFactory.put(StateType.PAUSED_SAVES, new PausedSavesState(this));
                break;
            case PAUSED_SETTINGS:
                m_statesFactory.put(StateType.PAUSED_SETTINGS, new PausedSettingsState(this));
                break;
            case TUTORIAL:
                m_statesFactory.put(StateType.TUTORIAL, new TutorialState(this));
                break;
            case TEST:
                m_statesFactory.put(StateType.TEST, new TestState(this));
                break;
        }
    }
    
    public Context getContext()
    {
        return m_context;
    }
}

package core;

import java.awt.Graphics;
import java.util.ArrayList;

import states.BaseState;

/**
 * StateManager class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class StateManager 
{
    public static int INTRO = 0;
    public static int MAIN_MENU = 1;
    public static int SETTINGS = 2;
    public static int LOAD = 3;
    public static int SAVE = 4;
    public static int CREDITS = 5;
    public static int GAME = 6;
    public static int PAUSE = 7;
    
    private ArrayList<BaseState> m_states;
    
    public StateManager()
    {
        this.m_states = new ArrayList<BaseState>();
    }
    
    public void update()
    {
        //TODO implement update all states into LIFO
    }
    
    public void render(Graphics g)
    {
        //TODO implements rendering all states into LIFO
    }
    
    public void pushState()
    {
        //TODO push state into LIFO
    }
    
    public void popState()
    {
        //TODO pop state into LIFO
    }
}

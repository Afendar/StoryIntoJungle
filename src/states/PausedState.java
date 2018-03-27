package states;

import core.StateManager;
import java.awt.Color;
import java.awt.Graphics2D;

public class PausedState extends BaseState
{
    public PausedState(StateManager stateManager)
    {
        super(stateManager);
        
        setTransparent(true);
    }
    
    @Override
    public void onCreate()
    {
        System.out.println("onCreate paused");
    }

    @Override
    public void onDestroy()
    {
        System.out.println("onDestroy paused");
    }

    @Override
    public void activate()
    {
        System.out.println("activate paused");
    }

    @Override
    public void desactivate()
    {
        System.out.println("desactivate paused");
    }

    @Override
    public void update(double dt)
    {
        
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setColor(Color.green);
        g.fillRect(150, 150, 100, 100);
    }
}

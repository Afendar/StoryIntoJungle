package states;

import core.StateManager;
import java.awt.Color;
import java.awt.Graphics2D;

public class IntroState extends BaseState
{
    public IntroState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        System.out.println("onCreate intro");
    }

    @Override
    public void onDestroy() 
    {
        System.out.println("onDestroy intro");
    }

    @Override
    public void activate()
    {
        System.out.println("activate intro");
    }

    @Override
    public void desactivate() 
    {
        System.out.println("desactivate intro");
    }

    @Override
    public void update(double dt)
    {
        
    }

    @Override
    public void render(Graphics2D g)
    {
        g.setColor(Color.red);
        g.fillRect(100, 50, 100, 150);
    }
}

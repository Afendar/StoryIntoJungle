package weather;

import core.Screen;
import core.WeatherManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import particles.Snow;

public class SnowWeather extends Weather
{
    public ArrayList<Snow> m_snowFlakes = new ArrayList<Snow>(10);
    
    public SnowWeather(WeatherManager weatherManager){
        super(weatherManager);
        
        Screen s = weatherManager.getContext().m_screen;
        
        for(int i = 0 ; i < 10 ; i++){
            m_snowFlakes.add(new Snow(5, 0, 0, s.getContentPane().getWidth(), s.getContentPane().getHeight()));
        }
    }

    @Override
    public void update(double dt)
    {
        m_snowFlakes.stream().map((snow) ->
        {
            if(!snow.isGenStartX())
            {
                snow.genRandStartX();
            }
            return snow;
        }).forEachOrdered((snow) ->
        {
            snow.update(dt);
        });
    }

    @Override
    public void render(Graphics2D g)
    {
        m_snowFlakes.forEach((snow) ->
        {
            snow.render(g);
        });
        
        Screen s = m_weatherManager.getContext().m_screen;
        g.setColor(new Color(210, 210, 210, 50));
        g.fillRect(0, 0, s.getWidth(), s.getHeight());
    }
}

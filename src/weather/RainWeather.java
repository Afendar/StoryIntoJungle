package weather;

import core.Screen;
import core.WeatherManager;
import java.awt.Color;
import java.awt.Graphics2D;

public class RainWeather extends Weather
{
    public RainWeather(WeatherManager weatherManager){
        super(weatherManager);
    }

    @Override
    public void update(double dt)
    {
        
    }

    @Override
    public void render(Graphics2D g)
    {
        Screen s = m_weatherManager.getContext().m_screen;
        g.setColor(new Color(63, 72, 204, 200));
        g.fillRect(0, 0, s.getWidth(), s.getHeight());
    }
}

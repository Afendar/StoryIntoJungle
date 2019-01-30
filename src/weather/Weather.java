package weather;

import core.WeatherManager;
import java.awt.Graphics2D;

public abstract class Weather
{
    public enum WeatherState {
        RAIN,
        SUN,
        SNOW
    };
    
    protected WeatherManager m_weatherManager;
    
    public Weather(WeatherManager weatherManager){
        m_weatherManager = weatherManager;
    }
    
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
}

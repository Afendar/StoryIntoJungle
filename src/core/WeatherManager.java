package core;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import weather.RainWeather;
import weather.SnowWeather;
import weather.Weather;
import weather.Weather.WeatherState;

public class WeatherManager
{
    private final Map<WeatherState, Weather> m_weathersFactory;
    private Context m_context;
    private Weather m_currentWeather;
    
    public WeatherManager(Context context)
    {
        m_context = context;
        m_weathersFactory = new HashMap<>();
        
        registerWeather(WeatherState.SUN);
        registerWeather(WeatherState.RAIN);
        registerWeather(WeatherState.SNOW);
        
        m_currentWeather = m_weathersFactory.get(WeatherState.SUN);
    }
    
    public void setWeather(WeatherState type){
        m_currentWeather = m_weathersFactory.get(type);
    }
    
    public void update(double dt){
        m_currentWeather.update(dt);
    }
    
    public void render(Graphics2D g){
        m_currentWeather.render(g);
    }
    
    private void registerWeather(WeatherState type){
        switch(type){
            case RAIN:
                m_weathersFactory.put(WeatherState.RAIN, new RainWeather(this));
                break;
            case SNOW:
                m_weathersFactory.put(WeatherState.SNOW, new SnowWeather(this));
                break;
        }
    }
    
    public Context getContext(){
        return m_context;
    }
}

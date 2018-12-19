package level;

import java.awt.image.BufferedImage;
import season.Season.SeasonState;
import weather.Weather.WeatherState;

public class Level
{
    private int m_number;
    private int[][] m_eventsPos;
    private String m_name;
    private SeasonState m_season;
    private WeatherState m_weather;
    private BufferedImage m_background;
    
    public Level(){
        m_number = 0;
        m_eventsPos = null;
        m_name = "";
        m_season = SeasonState.SPRING;
        m_weather = WeatherState.SUN;
        m_background = null;
    }
    
    public int getNumber(){
        return m_number;
    }
    
    public void setNumber(int number){
        m_number = number;
    }
    
    public String getName(){
        return m_name;
    }
    
    public void setName(String name){
        m_name = name;
    }
    
    public int[][] getEventsPos(){
        return m_eventsPos;
    }
    
    public void setEventsPos(int[][] eventsPos){
        m_eventsPos = eventsPos;
    }
    
    public SeasonState getSeason(){
        return m_season;
    }
    
    public void setSeason(SeasonState season){
        m_season = season;
    }
    
    public WeatherState getWeather(){
        return m_weather;
    }
    
    public void setWeather(WeatherState weather){
        m_weather = weather;
    }
    
    public BufferedImage getBackground(){
        return m_background;
    }
    
    public void setBackground(BufferedImage background){
        m_background = background;
    }
}

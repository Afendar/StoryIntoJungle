package core;

public class Easing
{
    /**
     * 
     * @param time
     * @param begin
     * @param stepChange
     * @param duration
     * @return 
     */
    public static int linearEase(double time, double begin, double stepChange, double duration)
    {
        return (int)(stepChange * (time / duration) + begin);
    }
    
    /**
     * 
     * @param time
     * @param begin
     * @param stepChange
     * @param duration
     * @return 
     */
    public static int cubicEaseIn (double time, double begin, double stepChange, double duration)
    {
        return (int)(stepChange * (time /= duration) * time * time + begin);
    }
    
    /**
     * 
     * @param time
     * @param begin
     * @param stepChange
     * @param duration
     * @return 
     */
    public static int bounceEaseOut(double time, double begin, double stepChange, double duration)
    {
        if((time /= duration) < (1 / 2.75))
        {            
            return (int)((stepChange * 7.5625 * time * time) + begin);
        }
        else if(time < (2 / 2.75))
        {
            return (int)(stepChange * (7.5625 * (time -= (1.5 / 2.75)) * time + 0.75) + begin);
        }
        else if(time < (2.5 / 2.75))
        {
            return (int)(stepChange * (7.5625 * (time -= (2.25 / 2.75)) * time + 0.9375) + begin);
        }
        else
        {
            return (int)(stepChange * (7.5625 * (time -= (2.625 / 2.75)) * time + 0.984375) + begin);
        }
    }
}

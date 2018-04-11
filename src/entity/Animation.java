package entity;

import java.awt.image.BufferedImage;

public class Animation
{
    private BufferedImage[] m_frames;
    private int m_currentFrame;
    
    private long m_startTime;
    private long m_delay;
    
    private boolean m_playedOnce;
    
    public Animation()
    {
        m_playedOnce = false;
    }
    
    public void setFrames(BufferedImage[] frames)
    {
        m_frames = frames;
        m_currentFrame = 0;
        m_startTime = System.nanoTime();
        m_playedOnce = false;
    }
    
    public void setDelay(long delay)
    {
        m_delay = delay;
    }
    
    public void setFrame(int index)
    {
        m_currentFrame = index;
    }
    
    public void update()
    {
        if(m_delay == -1)
        {
            return;
        }
        
        long elapsed = (System.nanoTime() - m_startTime) / 1000000;
        if(elapsed > m_delay)
        {
            m_currentFrame++;
            m_startTime = System.nanoTime();
        }
        if(m_currentFrame == m_frames.length)
        {
            m_currentFrame = 0;
            m_playedOnce = true;
        }
    }
    
    public int getFrame()
    {
        return m_currentFrame;
    }
    
    public BufferedImage getImage()
    {
        return m_frames[m_currentFrame];
    }
    
    public boolean hasPlayedOnce()
    {
        return m_playedOnce;
    }
}

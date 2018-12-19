package entity;

import java.awt.image.BufferedImage;

public class Animation
{
    private BufferedImage[] m_frames;
    private int m_currentFrame;
    private int m_startFrame;
    private int m_endFrame;
    private int m_frameRow;
    
    private long m_startTime;
    private long m_delay;
    
    private String m_name;
    
    private boolean m_playedOnce;
    private boolean m_playing;
    
    public Animation()
    {
        m_playedOnce = false;
        m_playing = false;
    }
    
    public void setFrames(BufferedImage[] frames)
    {
        m_frames = frames;
        m_currentFrame = 0;
        m_startTime = System.nanoTime();
        m_playedOnce = false;
    }
    
    public void setStartFrame(int startFrame)
    {
        m_startFrame = startFrame;
    }
    
    public int getStartFrame()
    {
        return m_startFrame;
    }
    
    public void setEndFrame(int endFrame)
    {
        m_endFrame = endFrame;
    }
    
    public int getEndFrame()
    {
        return m_endFrame;
    }
    
    public void setFrameRow(int frameRow)
    {
        m_frameRow = frameRow;
    }
    
    public int getFrameRow()
    {
        return m_frameRow;
    }
    
    public void setDelay(long delay)
    {
        m_delay = delay;
    }
    
    public void setFrame(int index)
    {
        m_currentFrame = index;
    }
    
    public void setName(String name)
    {
        m_name = name;
    }
    
    public String getName()
    {
        return m_name;
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
    
    public void play()
    {
        
    }
    
    public void pause()
    {
        
    }
    
    public void stop()
    {
        
    }
    
    public void reset()
    {
        
    }
}

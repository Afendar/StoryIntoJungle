package states;

import core.StateManager;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Abstract base state
 * @version %I%, %G%
 * @author Afendar
 */
public abstract class BaseState
{
    /** StateManager m_stateManager */
    protected StateManager m_stateManager;
    
    /** boolean m_transparent */
    protected boolean m_transparent;
    
    /** boolean m_transcendent */
    protected boolean m_transcendent;
    
    /**
     * 
     * @param stateManager 
     */
    public BaseState(StateManager stateManager)
    {
        m_stateManager = stateManager;
        m_transcendent = false;
        m_transparent = false;
    }
    
    /**
     * 
     */
    public abstract void onCreate();
    
    /**
     * 
     */
    public abstract void onDestroy();
    
    /**
     * 
     */
    public abstract void activate();
    
    /**
     * 
     */
    public abstract void desactivate();
    
    /**
     * 
     */
    public abstract void reloadLocales();
    
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
    
    /**
     * 
     * @param b 
     */
    public void setTransparent(boolean b)
    {
        m_transparent = b;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isTransparent()
    {
        return m_transparent;
    }
    
    /**
     * 
     * @param b 
     */
    public void setTranscendent(boolean b)
    {
        m_transcendent = b;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isTranscendent()
    {
        return m_transcendent;
    }
    
    /**
     * 
     * @return 
     */
    public StateManager getStateManager()
    {
        return m_stateManager;
    }
    
    /**
     * 
     * @param img
     * @param targetWidth
     * @param targetHeight
     * @param hint
     * @param higherQuality
     * @return 
     */
    public BufferedImage getScaledInstance(BufferedImage img,
                                       int targetWidth,
                                       int targetHeight,
                                       Object hint,
                                       boolean higherQuality)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
            BufferedImage ret = (BufferedImage)img;
        int w, h;
        if (higherQuality)
        {
            w = img.getWidth();
            h = img.getHeight();
        } 
        else
        {
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
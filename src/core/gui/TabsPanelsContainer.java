package core.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import states.BaseState;

/**
 * 
 */
public class TabsPanelsContainer extends GuiComponent
{
    private static final int TOP = 1;
    private static final int LEFT = 2;
    private static final int BOTTOM = 3;
    private static final int RIGHT = 4;

    private static final String componentID = "Tabs Panels Container";

    private int m_tabPlacement = TOP;

    private ArrayList<Page> m_pages;

    private int m_selectedIndex;
    
    private int m_tabPadding;
    
    private int m_tabWidth;
    private int m_tabHeight;

    /**
     * 
     * @param owner 
     */
    public TabsPanelsContainer(BaseState owner)
    {
        this(TabsPanelsContainer.TOP, owner);
    }

    /**
     * 
     * @param tabsPlacement
     * @param owner 
     */
    public TabsPanelsContainer(int tabsPlacement, BaseState owner)
    {
        super(owner);
        m_tabPlacement = tabsPlacement;
        m_pages = new ArrayList<>();
        m_selectedIndex = -1;
        m_tabWidth = 48;
        m_tabHeight = 48;
        m_tabPadding = 10;
    }

    @Override
    public void update(double dt)
    {
        m_pages.forEach((p) ->
        {
            p.update(dt);
        });
    }

    @Override
    public void render(Graphics2D g)
    {
        FontMetrics fm = g.getFontMetrics(m_font);
        int positionTab = m_position.m_x + 50;
        
        g.setColor(Color.BLACK);
        g.setFont(m_font);
        
        g.drawImage(m_owner.getStateManager().getContext().m_resourceManager.getSpritesheets("bgPause"), m_position.m_x, m_position.m_y, null);
        
        for(int i = 0 ; i < m_pages.size(); i++)
        {
            Page p = m_pages.get(i);
            
            if(m_pages.size() > 1)
            {
                String title = p.getTitle();
                p.setTitlePosition(new int[]{positionTab, m_position.m_y + 50});
                positionTab += m_tabWidth + 10;
            }
            g.setColor(Color.BLACK);
            p.renderTitle(g, fm.getAscent());
            
            if(p.isEnabled())
            {
                p.renderContent(g);
            }
        }
    }
    
    @Override
    public void onHover()
    {
    }
    
    @Override
    public void onClick(int mouseX, int mouseY)
    {
        for(int i = 0;i<m_pages.size();i++)
        {
            Page p = m_pages.get(i);
            p.setEnabled(false);
            if(mouseX >= m_position.m_x + 50 + i * (m_tabWidth + 10) &&
                mouseX <= m_position.m_x + 50 + i * (m_tabWidth + 10) + m_tabWidth &&
                mouseY >= m_position.m_y + 50 &&
                mouseY <= m_position.m_y + 50 + m_tabHeight)
                p.setEnabled(true);
        }
    }
    
    @Override
    public void onRelease()
    {
        
    }
    
    @Override
    public void onLeave()
    {
        
    }
    
    @Override
    public boolean isInside(int posX, int posY)
    {
        for(int i = 0 ; i < m_pages.size() ; i++)
        {
            Page p = m_pages.get(i);
            if(posX >= m_position.m_x + 50 + i * (m_tabWidth + 10) &&
                posX <= m_position.m_x + 50 + i * (m_tabWidth + 10) + m_tabWidth &&
                posY >= m_position.m_y + 50 &&
                posY <= m_position.m_y + 50 + m_tabHeight)
                return true;
        }
        return false;
    }
    
    /**
     * 
     */
    private class Page
    {
        private TabContent m_content;
        
        private boolean m_enabled = true;
        
        private int[] m_titlePosition;
        
        private int[] m_iconSize;
        
        private String m_title;
        
        private BufferedImage m_icon;
        
        /**
         * 
         * @param title
         * @param content 
         */
        protected Page(String title, BufferedImage icon, TabContent content)
        {
            m_enabled = false;
            m_title = title;
            m_icon = icon;
            
            if(m_icon  != null)
            {
                m_iconSize = new int[]{
                    m_icon.getWidth(),
                    m_icon.getHeight()
                };
            }
            else
            {
                m_iconSize = new int[2];
            }
            
            m_content = content;
            m_titlePosition = new int[]{0, 0};
        }
        
        /**
         * 
         * @return 
         */
        public TabContent getContent()
        {
            return m_content;
        }

        /**
         * 
         * @param content 
         */
        public void setContent(TabContent content)
        {
            m_content = content;
        }

        /**
         * 
         * @return 
         */
        public boolean isEnabled()
        {
            return m_enabled;
        }

        /**
         * 
         * @param enabled 
         */
        public void setEnabled(boolean enabled)
        {
            m_enabled = enabled;
        }

        /**
         * 
         * @return 
         */
        public String getTitle()
        {
            return m_title;
        }

        /**
         * 
         * @param title 
         */
        public void setTitle(String title)
        {
            m_title = title;
        }

        /**
         * 
         * @param size 
         */
        public void setIconSize(int[] size)
        {
            m_iconSize = size;
        }
        
        /**
         * 
         * @param w
         * @param h 
         */
        public void setIconSize(int w, int h)
        {
            m_iconSize = new int[]{w, h};
        }
        
        /**
         * 
         * @return 
         */
        public int[] getIconSize()
        {
            return m_iconSize;
        }
        
        /**
         * 
         * @return 
         */
        public BufferedImage getIcon()
        {
            return m_icon;
        }

        /**
         * 
         * @param icon 
         */
        public void setIcon(BufferedImage icon)
        {
            this.m_icon = icon;
        }
        
        public void update(double dt)
        {
            m_content.update(dt);
        }
        
        /**
         * 
         * @param g 
         */
        public void renderTitle(Graphics2D g, int textAscent)
        {
            if(m_icon != null)
            {            
                if(m_enabled)
                {
                    g.setColor(new Color(192, 179, 114));
                    g.fillRect(m_titlePosition[0], m_titlePosition[1], 48, 48);
                }
                g.drawImage(
                        m_icon, 
                        m_titlePosition[0] + ((m_tabWidth - m_iconSize[0]) / 2), 
                        m_titlePosition[1] + ((m_tabHeight - m_iconSize[1]) / 2), 
                        m_iconSize[0], 
                        m_iconSize[1], 
                        null
                );
            }
            g.drawString(
                    m_title, 
                    (m_icon != null) ? m_titlePosition[0] + m_iconSize[0] : m_titlePosition[0], 
                    m_titlePosition[1] + ((m_iconSize[1] + textAscent) / 2)
            );
        }
        
        /**
         * 
         * @param g 
         */
        public void renderContent(Graphics2D g)
        {
            m_content.render(g);
        }
        
        /**
         * 
         * @param pos 
         */
        public void setTitlePosition(int[] pos)
        {
            m_titlePosition = pos;
        }
        
        /**
         * 
         * @return 
         */
        public int[] getTitlePosition()
        {
            return m_titlePosition;
        }
    }
    
    /**
     * 
     * @return 
     */
    public int getSelectedIndex()
    {
        return m_selectedIndex;
    }
    
    /**
     * 
     * @param index 
     */
    public void setSelectedIndex(int index)
    {
        m_selectedIndex = index;
    }
    
    /**
     * 
     * @return 
     */
    public int getTabHeight()
    {
        return m_tabHeight; 
    }
    
    /**
     * 
     * @param title
     * @param content
     * @param index 
     */
    private void insertTab(String title, BufferedImage icon, TabContent content, int index)
    {
        if(title == null)
        {
            title = "";
        }
        
        Page p = new Page(title, icon, content);
        
        if(getSelectedIndex() == -1)
        {
            setSelectedIndex(0);
            p.setEnabled(true);
        }
        
        m_pages.add(index, p);
    }
    
    /**
     * 
     * @param title
     * @param content 
     */
    public void addTab(String title, BufferedImage icon, TabContent content)
    {
        insertTab(title, icon, content, m_pages.size());
    }
    
    /**
     * 
     * @param index
     * @param w
     * @param h 
     */
    public void setIconSize(int index, int w, int h)
    {
        if(index > m_pages.size() - 1 || index < 0)
        {
            return;
        }
        m_pages.get(index).setIconSize(w, h);
    }
    
    /**
     * 
     * @param index 
     */
    public void removeTabAt(int index)
    {
        
    }
}

package core.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TabsPanelsContainer
{
    private static final int TOP = 1;
    private static final int LEFT = 2;
    private static final int BOTTOM = 3;
    private static final int RIGHT = 4;

    private static final String componentID = "Tabs Panels Container";

    private int m_tabPlacement = TOP;

    private ArrayList<TabContent> m_tabsContents;
    private ArrayList<TabItem> m_tabsItems;

    private int m_currentIndex;

    public TabsPanelsContainer()
    {
        this(TabsPanelsContainer.TOP);
    }

    public TabsPanelsContainer(int tabsPlacement)
    {
        setTabsPlacement(tabsPlacement);

        m_tabsContents = new ArrayList<>();
        m_tabsItems = new ArrayList<>();
    }

    public void setTabsPlacement(int tabsPlacement)
    {
        if (tabsPlacement != TOP && tabsPlacement != LEFT && tabsPlacement != BOTTOM && tabsPlacement != RIGHT)
        {
            throw new IllegalArgumentException("Tab placement must be TOP, LEFT, BOTTOM or RIGHT");
        }

        m_tabPlacement = tabsPlacement;
    }

    public void addTab(BufferedImage icon, TabContent tabContent)
    {
        addTab("", icon, tabContent);
    }

    public void addTab(String label, TabContent tabContent)
    {
        addTab(label, null, tabContent);
    }

    public void addTab(String label, BufferedImage icon, TabContent tabContent)
    {
        m_tabsContents.add(tabContent);
        m_tabsItems.add(new TabItem(label != null ? label : "", icon));
    }

    public void setSelectedTab(int index)
    {
        if (index > m_tabsItems.size())
        {
            throw new IllegalArgumentException("index exceded tabs size container");
        }

        m_currentIndex = index;
    }

    public void update(double dt)
    {

    }

    public void render(Graphics2D g)
    {

    }
}

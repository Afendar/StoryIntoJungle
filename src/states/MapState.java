package states;

import core.Defines;
import core.Easing;
import core.ResourceManager;
import core.Screen;
import core.StateManager;
import core.StateType;
import core.gui.Button;
import core.gui.GuiComponent;
import core.gui.IconButton;
import entity.CageEntity;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

public class MapState extends BaseState
{
    public BufferedImage m_mapBg, m_panda, m_panel, m_emptyStar, m_plainStar;
    public Font m_font;
    public int m_currentLvl, m_currentScore, m_posY;
    private final ArrayList<GuiComponent> m_guiElements;
    
    private int[][] m_coordsPinsIcons = {
        {341, 400},
        {201, 308},
        {379, 252},
        {573, 371},
        {557, 184},
        {292, 110}
    };
    
    private int[][] m_coordsPins = {
        {350, 405},
        {210, 312},
        {388, 256},
        {583, 375},
        {567, 188},
        {301, 115}
    };
    private List<Point2D> m_points;
    private int m_index, m_displayStar;
    private Point2D m_pos;
    private CubicCurve2D[] m_curve;
    private boolean m_animated;
    private List<List<CageEntity>> m_cagesMap;
    private boolean[] m_unlockedLevels;
    private double m_time, m_scale;
    
    public MapState(StateManager stateManager)
    {
        super(stateManager);
        
        m_curve = new CubicCurve2D[5];
        m_animated = false;
        m_guiElements = new ArrayList<>();
        m_posY = 400;
        m_scale = 0;
        m_time = 0;
        m_displayStar = 0;
    }
    
    @Override
    public void onCreate()
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
        try
        {
            URL url = getClass().getResource("/bgmap2.png");
            m_mapBg = ImageIO.read(url);
            url = getClass().getResource("/littles_pandas.png");
            m_panda = ImageIO.read(url);
            m_panda = m_panda.getSubimage(0, 0, 64, 64);
        }
        catch(IOException e)
        {
            e.getMessage();
        }

        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        BufferedImage spritesheetGui2 = resourceManager.getSpritesheets("spritesheetGui2"); 
        
        IconButton ib = new IconButton(spritesheetGui2.getSubimage(243, 94, 42, 34), this);
        ib.setPosition(screenWidth/6 - 60, 480);
        ib.addCallback(GuiComponent.Status.CLICKED, "backToMain", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, spritesheetGui2.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
        ib = new IconButton(spritesheetGui2.getSubimage(760, 151, 34, 34), this);
        ib.setPosition(2* screenWidth/6 - 60, 480);
        ib.addCallback(GuiComponent.Status.CLICKED, "prevLevel", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, spritesheetGui2.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
        ib = new IconButton(spritesheetGui2.getSubimage(726, 151, 34, 36), this);
        ib.setPosition(3* screenWidth/6 - 60, 480);
        ib.addCallback(GuiComponent.Status.CLICKED, "nextLevel", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, spritesheetGui2.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
        ib = new IconButton(spritesheetGui2.getSubimage(176, 93, 34, 35), this);
        ib.setPosition(4* screenWidth/6 - 60, 480);
        ib.addCallback(GuiComponent.Status.CLICKED, "save", this);
        ib.addApearance(GuiComponent.Status.NEUTRAL, spritesheetGui2.getSubimage(370, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.FOCUSED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        ib.addApearance(GuiComponent.Status.CLICKED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        m_guiElements.add(ib);
        
        Button b = new Button("Go", this);
        b.setPosition(5* screenWidth/6 - 60, 480);
        b.setColor(new Color(44, 23, 4));
        b.setTextCenter(true);
        b.setFont(resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 22.0f));
        b.addCallback(GuiComponent.Status.CLICKED, "go", this);
        b.addApearance(GuiComponent.Status.NEUTRAL, spritesheetGui2.getSubimage(370, 1, 120, 99));
        b.addApearance(GuiComponent.Status.FOCUSED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        b.addApearance(GuiComponent.Status.CLICKED, spritesheetGui2.getSubimage(491, 1, 120, 99));
        m_guiElements.add(b);
        
        m_panel = spritesheetGui2.getSubimage(1, 680, 71, 43);
        m_emptyStar = spritesheetGui2.getSubimage(73, 685, 15, 14);
        m_plainStar = spritesheetGui2.getSubimage(73, 701, 15, 14);
        
        m_currentLvl = 1;
        m_currentScore = 0;
        
        m_unlockedLevels = new boolean[]{true, true, true, false, false, false};
        
        m_index = 0;
        
        m_curve[0] = new CubicCurve2D.Double(
                (double)m_coordsPins[0][0],(double)m_coordsPins[0][1],
                (double)350, (double)390,
                (double)285, (double)312,
                (double)m_coordsPins[1][0],(double)m_coordsPins[1][1]
        );
        m_curve[1] = new CubicCurve2D.Double(
                (double)m_coordsPins[1][0], (double)m_coordsPins[1][1],
                (double)220, (double)270,
                (double)360, (double)240,
                (double)m_coordsPins[2][0], (double)m_coordsPins[2][1]
        );
        m_curve[2] = new CubicCurve2D.Double(
                (double)m_coordsPins[2][0], (double)m_coordsPins[2][1],
                (double)460, (double)285,
                (double)550, (double)380,
                (double)m_coordsPins[3][0], (double)m_coordsPins[3][1]
        );
        m_curve[3] = new CubicCurve2D.Double(
                (double)m_coordsPins[3][0], (double)m_coordsPins[3][1],
                (double)500, (double)350,
                (double)425, (double)255,
                (double)m_coordsPins[4][0], (double)m_coordsPins[4][1]
        );
        m_curve[4] = new CubicCurve2D.Double(
                (double)m_coordsPins[4][0], (double)m_coordsPins[4][1],
                (double)495, (double)160,
                (double)215,(double)225,
                (double)m_coordsPins[5][0], (double)m_coordsPins[5][1]
        );
        m_pos = new Point2D.Double(m_coordsPins[m_currentLvl - 1][0], m_coordsPins[m_currentLvl - 1][1]);
        
        m_points = new ArrayList<>(25);
    }

    @Override
    public void onDestroy()
    {
    }

    @Override
    public void activate()
    {
    }

    @Override
    public void desactivate() 
    {
    }

    @Override
    public void reloadLocales()
    {
    }
    
    @Override
    public void update(double dt) 
    {
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        for(GuiComponent element : m_guiElements)
        {   
            element.update(dt);
            
            if(element.isInside(mouseX, mouseY))
            {
                if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount >= 1)
                {
                    element.onClick(mouseX, mouseY);
                }
                else if(!m_stateManager.getContext().m_inputsListener.mousePressed && element.getStatus() == GuiComponent.Status.CLICKED)
                {
                    element.onRelease();
                }
                
                if(element.getStatus() != GuiComponent.Status.NEUTRAL)
                {
                    continue;
                }

                element.onHover();
            }
            else if(element.getStatus() == GuiComponent.Status.FOCUSED)
            {
                element.onLeave();
            }
            else if(element.getStatus() == GuiComponent.Status.CLICKED)
            {
                element.onRelease();
            }
        }
        
        m_time += dt;

        if(m_posY > 330)
        {
            m_posY = Easing.cubicEaseIn(m_time, 400, -330, 80);
            m_scale = Easing.cubicEaseIn(m_time, 10, 200, 80) / 50.0;
        }
        else if(m_displayStar < 3)
        {
            m_displayStar = Easing.linearEase(m_time, 0, 1, 40);
        }
        
        if(!m_points.isEmpty())
        {
            if(m_index < m_points.size() - 1)
                m_index++;
            else
            {
                m_animated = false;
            }
        
            m_pos = m_points.get(m_index);
        }
    }

    @Override
    public void render(Graphics2D g)
    {   
        Screen screen = m_stateManager.getContext().m_screen;
        ResourceManager resourceManager = m_stateManager.getContext().m_resourceManager;
        int screenWidth = screen.getContentPane().getWidth();
        
        g.drawImage(m_mapBg, 0, 0, null);
        //draw curves lvl 1 to 2
        //g.setColor(Color.RED);
        //g.setStroke(new BasicStroke(3));
        //g.draw(m_curve[0]);
        
        //draw curve lvl 2 to 3
        //g.draw(m_curve[1]);
        
        //draw curve lvl 3 to 4
        //g.draw(m_curve[2]);
        
        //draw curve lvl 4 to 5
        //g.draw(m_curve[3]);
        
        //draw curve lvl 5 to 6
        //g.draw(m_curve[4]);
        
        BufferedImage spritesheetGui2 = m_stateManager.getContext().m_resourceManager.getSpritesheets("spritesheetGui2");
        
        for(int i=0 ; i < Defines.LEVEL_MAX - 1;i++)
        {
            if(m_unlockedLevels[i])
            {
                g.drawImage(spritesheetGui2.getSubimage(841, 65, 19, 8), m_coordsPinsIcons[i][0], m_coordsPinsIcons[i][1], null);
            }
            else
            {
                g.drawImage(spritesheetGui2.getSubimage(863, 65, 19, 8), m_coordsPinsIcons[i][0], m_coordsPinsIcons[i][1], null);
            }
        }
        
        AffineTransform at = new AffineTransform();
        at.rotate(0, 16, 16);
        g.drawImage(m_panda, null, (int)m_pos.getX() - 30, (int)m_pos.getY() - 30);
        
        m_guiElements.forEach((element) ->
        {
            element.render(g);
        });
        
        if(m_scale > 0)
        {
            BufferedImage panel = getScaledInstance(
                    m_panel, 
                    (int)(m_panel.getWidth() * m_scale), 
                    (int)(m_panel.getHeight() * m_scale), 
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC, 
                    false
            );
            g.drawImage(panel, 315 + ((m_panel.getWidth() - panel.getWidth()) / 2), m_posY, null);
            if(m_displayStar > 0)
                g.drawImage(m_plainStar, 325, m_posY + 12, null);
            if(m_displayStar > 1)
                g.drawImage(m_plainStar, 342, m_posY + 9, null);
            if(m_displayStar > 2)
                g.drawImage(m_plainStar, 360, m_posY + 10, null);
        }
        
        g.drawImage(m_panel, 175, 235, null);
        g.drawImage(m_plainStar, 185, 235 + 12, null);
        g.drawImage(m_plainStar, 202, 235 + 9, null);
        g.drawImage(m_emptyStar, 220, 235 + 10, null);
        
        g.drawImage(m_panel, 355, 186, null);
        g.drawImage(m_plainStar, 365, 186 + 12, null);
        g.drawImage(m_emptyStar, 382, 186 + 9, null);
        g.drawImage(m_emptyStar, 400, 186 + 10, null);
        
        //g.drawImage(m_panel, 545, 305, null);
        
        //g.drawImage(m_panel, 530, 118, null);
        
        //g.drawImage(m_panel, 265, 40, null);
        
        Font font = resourceManager.getFont("kaushanscriptregular").deriveFont(Font.PLAIN, 24.0f);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        String levelName = "Mystic Lagoon";
        int levelNameWidth = metrics.stringWidth(levelName);
        
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(screenWidth - levelNameWidth - 60, 30, levelNameWidth + 20, 40);
        g.setColor(new Color(255, 255, 255));
        g.drawString(levelName, screenWidth - 50 - levelNameWidth, 58);
    }
    
    /*
    public void processClick()
    {
        if(m_stateManager.getContext().m_inputsListener.mousePressed && m_stateManager.getContext().m_inputsListener.mouseClickCount == 1)
        {
            switch(m_selectedItem)
            {
                case 1:
                    m_stateManager.switchTo(StateType.MAIN_MENU);
                    break;
                case 2:
                    // TODO save
                    break;
                case 3:
                    if(m_currentLvl > 1 && m_unlockedLevels[m_currentLvl - 2])
                    {
                        m_animated = true;
                        m_currentLvl--;
                        followPrevious();
                    }
                    break;
                case 4:
                    if(m_currentLvl < 6 && m_unlockedLevels[m_currentLvl]){
                        m_animated = true;
                        m_currentLvl++;
                        followNext();
                    }
                    break;
                case 5:
                    
                    break;
            }
        }
    }*/
    
    /**
     * 
     */
    public void backToMain()
    {
        m_stateManager.switchTo(StateType.MAIN_MENU);
    }
    
    /**
     * 
     */
    public void prevLevel()
    {
        if(m_currentLvl > 1 && m_unlockedLevels[m_currentLvl - 2] && !m_animated)
        {
            m_animated = true;
            m_currentLvl--;
            followPrevious();
        }
    }
    
    /**
     * 
     */
    public void nextLevel()
    {
        if(m_currentLvl < 6 && m_unlockedLevels[m_currentLvl] && !m_animated)
        {
            m_animated = true;
            m_currentLvl++;
            followNext();
        }
    }
    
    /**
     * 
     */
    public void save()
    {
        //TODO save
    }
    
    /**
     * 
     */
    public void go()
    {
        if(!m_animated)
        {
            /*GameScene gs = new GameScene(Defines.SCREEN_WIDTH, this.h, this.game, this.currentLvl + 1, this.currentScore);
            gs.level.setUnlockedLevels(m_unlockedLevels);
            if(m_cagesMap.get(m_currentLvl).isEmpty())
                m_cagesMap.set(m_currentLvl, gs.level.cageEntity);
            gs.setLevelCagesMap(m_cagesMap);
            gs.cageToFree = gs.level.nbCages;*/
        }
    }
    
    /**
     * 
     */
    public void followNext()
    {
        PathIterator pi = m_curve[m_currentLvl - 2].getPathIterator(null, 0.01);
        m_points = new ArrayList<>(25);
        while(!pi.isDone())
        {
            double[] coordinates = new double[6];
            switch(pi.currentSegment(coordinates))
            {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    m_points.add(new Point2D.Double(coordinates[0], coordinates[1]));
                    break;
            }
            pi.next();
        }
        m_index = 0;
    }
    
    /**
     * 
     */
    public void followPrevious()
    {
        PathIterator pi = m_curve[m_currentLvl - 1].getPathIterator(null, 0.01);
        m_points = new ArrayList<>(25);
        while(!pi.isDone())
        {
            double[] coordinates = new double[6];
            switch(pi.currentSegment(coordinates))
            {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    m_points.add(new Point2D.Double(coordinates[0], coordinates[1]));
                    break;
            }
            pi.next();
        }
        
        Collections.reverse(m_points);
        
        m_index = 0;
    }
    
    /**
     * 
     * @param cagesMap 
     */
    public void setCagesMap(List<List<CageEntity>> cagesMap)
    {
        m_cagesMap = cagesMap;
    }
}

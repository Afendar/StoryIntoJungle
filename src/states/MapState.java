package states;

import audio.Sound;
import core.Defines;
import core.Screen;
import core.StateManager;
import core.StateType;
import entity.CageEntity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

public class MapState extends BaseState
{
    public BufferedImage m_mapBg, m_panda, m_homeIcon, m_previousIcon, m_nextIcon, m_saveIcon, m_bgBtnMediumNoSelect, m_bgBtnMediumSelect, m_spritesheetGui2;
    public Font m_font;
    public int[][] m_btnCoords;
    public int m_selectedItem, m_currentLvl, m_currentScore;
    
    private int[][] m_coordsPinsIcons = {
        {76, 337},
        {172, 397},
        {309, 407},
        {413, 285},
        {370, 161},
        {562, 315}
    };
    
    private int[][] m_coordsPins = {
        {86, 348},
        {182, 407},
        {320, 418},
        {423, 296},
        {381, 172},
        {571, 324}
    };
    private List<Point2D> m_points;
    private int m_index;
    private Point2D m_pos;
    private QuadCurve2D[] m_curve = new QuadCurve2D[5];
    private boolean m_animated = false;
    private List<List<CageEntity>> m_cagesMap;
    private boolean[] m_unlockedLevels;
    
    public MapState(StateManager stateManager)
    {
        super(stateManager);
    }
    
    @Override
    public void onCreate()
    {
        Screen screen = m_stateManager.getContext().m_screen;
        int screenWidth = screen.getContentPane().getWidth();
        
        try{
            URL url = getClass().getResource("/fonts/kaushanscriptregular.ttf");
            m_font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            m_font = m_font.deriveFont(Font.PLAIN, 22.0f);
            url = getClass().getResource("/bgmap.png");
            m_mapBg = ImageIO.read(url);
            url = getClass().getResource("/littles_pandas.png");
            m_panda = ImageIO.read(url);
            m_panda = m_panda.getSubimage(0, 0, 64, 64);
            
            url = getClass().getResource("/gui2.png");
            m_spritesheetGui2 = ImageIO.read(url);
            
            m_homeIcon = m_spritesheetGui2.getSubimage(243, 94, 42, 34);
            m_previousIcon = m_spritesheetGui2.getSubimage(760, 151, 34, 34);
            m_nextIcon = m_spritesheetGui2.getSubimage(726, 151, 34, 36);
            m_saveIcon = m_spritesheetGui2.getSubimage(176, 93, 34, 35);
            
            m_bgBtnMediumNoSelect = m_spritesheetGui2.getSubimage(370, 1, 120, 99);
            m_bgBtnMediumSelect = m_spritesheetGui2.getSubimage(491, 1, 120, 99);
        }
        catch(FontFormatException|IOException e)
        {
            e.getMessage();
        }
        
        int [][]coords = {
            {screenWidth/6 - 60, 480},
            {2* screenWidth/6 - 60, 480},
            {3* screenWidth/6 - 60, 480},
            {4* screenWidth/6 - 60, 480},
            {5* screenWidth/6 - 60, 480}
        };
        
        m_btnCoords = coords;
        m_selectedItem = 0;
        /*if(currentLvl > 1)
            currentLvl--;
        m_currentLvl = currentLvl;
        m_currentScore = currentScore;
        m_unlockedLevels = unlockedLevels;*/
        
        m_currentLvl = 1;
        m_currentScore = 0;
        
        m_index = 0;
        
        m_curve[0] = new QuadCurve2D.Double(
                (double)m_coordsPins[0][0],(double)m_coordsPins[0][1],
                (double)116, (double)411,
                (double)m_coordsPins[1][0],(double)m_coordsPins[1][1]
        );
        m_curve[1] = new QuadCurve2D.Double(
                (double)m_coordsPins[1][0], (double)m_coordsPins[1][1],
                (double)253, (double)391,
                (double)m_coordsPins[2][0], (double)m_coordsPins[2][1]
        );
        m_curve[2] = new QuadCurve2D.Double(
                (double)m_coordsPins[2][0], (double)m_coordsPins[2][1],
                (double)501, (double)446,
                (double)m_coordsPins[3][0], (double)m_coordsPins[3][1]
        );
        m_curve[3] = new QuadCurve2D.Double(
                (double)m_coordsPins[3][0], (double)m_coordsPins[3][1],
                (double)372, (double)211,
                (double)m_coordsPins[4][0], (double)m_coordsPins[4][1]
        );
        m_curve[4] = new QuadCurve2D.Double(
                (double)m_coordsPins[4][0], (double)m_coordsPins[4][1],
                (double)537, (double)202,
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
        System.out.println("Reload locales");
    }
    
    @Override
    public void update(double dt) 
    {
        processHover();
        
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
        
        processClick();
    }

    @Override
    public void render(Graphics2D g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(m_mapBg, 0, 0, null);
        
        g.setFont(m_font);
        
        FontMetrics metrics = g.getFontMetrics(m_font);
        
        if(m_selectedItem == 1)
        {
            g.drawImage(m_bgBtnMediumSelect, m_btnCoords[0][0], m_btnCoords[0][1], null);
        }
        else
        {
            g.drawImage(m_bgBtnMediumNoSelect, m_btnCoords[0][0], m_btnCoords[0][1], null);
        }
        
        if(m_selectedItem == 2)
        {
            g.drawImage(m_bgBtnMediumSelect, m_btnCoords[1][0], m_btnCoords[1][1], null);
        }
        else
        {
            g.drawImage(m_bgBtnMediumNoSelect, m_btnCoords[1][0], m_btnCoords[1][1], null);
        }
        
        if(m_selectedItem == 3)
        {
            g.drawImage(m_bgBtnMediumSelect, m_btnCoords[2][0], m_btnCoords[2][1], null);
        }
        else
        {
            g.drawImage(m_bgBtnMediumNoSelect, m_btnCoords[2][0], m_btnCoords[2][1], null);
        }
        
        if(m_selectedItem == 4)
        {
            g.drawImage(m_bgBtnMediumSelect, m_btnCoords[3][0], m_btnCoords[3][1], null);
        }
        else{
            g.drawImage(m_bgBtnMediumNoSelect, m_btnCoords[3][0], m_btnCoords[3][1], null);
        }
        
        if(m_selectedItem == 5)
        {
            g.drawImage(m_bgBtnMediumSelect, m_btnCoords[4][0], m_btnCoords[4][1], null);
        }
        else{
            g.drawImage(m_bgBtnMediumNoSelect, m_btnCoords[4][0], m_btnCoords[4][1], null);
        }
        
        g.drawImage(m_homeIcon, m_btnCoords[0][0] + 35, m_btnCoords[0][1] + 32, null);
        g.drawImage(m_saveIcon, m_btnCoords[1][0] + 42, m_btnCoords[1][1] + 32, null);
        g.drawImage(m_previousIcon, m_btnCoords[2][0] + 40, m_btnCoords[2][1] + 30, null);
        g.drawImage(m_nextIcon, m_btnCoords[3][0] + 40, m_btnCoords[3][1] + 30, null);
        
        g.setColor(new Color(44, 23, 4));
        g.drawString("GO", m_btnCoords[4][0] + 40, m_btnCoords[4][1] + 55);
        
        //draw curves lvl 1 to 2
        g.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(m_curve[0]);
        
        //draw curve lvl 2 to 3
        g2d.draw(m_curve[1]);
        
        //draw curve lvl 3 to 4
        g2d.draw(m_curve[2]);
        
        //draw curve lvl 4 to 5
        g2d.draw(m_curve[3]);
        
        //draw curve lvl 5 to 6
        g2d.draw(m_curve[4]);
        
        for(int i=0 ; i < Defines.LEVEL_MAX - 1;i++)
        {
            if(m_unlockedLevels[i])
            {
                g.drawImage(m_spritesheetGui2.getSubimage(840, 42, 20, 20), m_coordsPinsIcons[i][0], m_coordsPinsIcons[i][1], null);
            }
            else
            {
                g.drawImage(m_spritesheetGui2.getSubimage(862, 42, 20, 20), m_coordsPinsIcons[i][0], m_coordsPinsIcons[i][1], null);
            }
        }
        
        AffineTransform at = new AffineTransform();
        at.rotate(0, 16, 16);
        g2d.drawImage(m_panda, null, (int)m_pos.getX() - 24, (int)m_pos.getY() - 24);
    }
    
    public void processHover()
    {
        int mouseX = m_stateManager.getContext().m_inputsListener.mouseX;
        int mouseY = m_stateManager.getContext().m_inputsListener.mouseY;
        
        int oldSelected = m_selectedItem;
        if(mouseX > m_btnCoords[0][0] && mouseX < m_btnCoords[0][0] + 120 &&
                mouseY > m_btnCoords[0][1] && mouseY < m_btnCoords[0][1] + 99)
        {
            m_selectedItem = 1;
        }
        else if(mouseX > m_btnCoords[1][0] && mouseX < m_btnCoords[1][0] + 120 &&
                mouseY > m_btnCoords[1][1] && mouseY < m_btnCoords[1][1] + 99)
        {
            m_selectedItem = 2;
        }
        else if(mouseX > m_btnCoords[2][0] && mouseX < m_btnCoords[2][0] + 120 &&
                mouseY > m_btnCoords[2][1] && mouseY < m_btnCoords[2][1] + 99)
        {
            m_selectedItem = 3;
        }
        else if(mouseX > m_btnCoords[3][0] && mouseX < m_btnCoords[3][0] + 120 &&
                mouseY > m_btnCoords[3][1] && mouseY < m_btnCoords[3][1] + 99)
        {
            m_selectedItem = 4;
        }
        else if(mouseX > m_btnCoords[4][0] && mouseX < m_btnCoords[4][0] + 120 &&
                mouseY > m_btnCoords[4][1] && mouseY < m_btnCoords[4][1] + 99)
        {
            m_selectedItem = 5;
        }
        else
        {
            m_selectedItem = 0;
        }
        
        if(m_selectedItem != 0 && m_selectedItem != oldSelected)
        {
            new Thread(Sound.hover::play).start();
        }
    }
    
    /**
     * 
     * @return 
     */
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
                    if(!m_animated)
                    {
                        /*GameScene gs = new GameScene(Defines.SCREEN_WIDTH, this.h, this.game, this.currentLvl + 1, this.currentScore);
                        gs.level.setUnlockedLevels(m_unlockedLevels);
                        if(m_cagesMap.get(m_currentLvl).isEmpty())
                            m_cagesMap.set(m_currentLvl, gs.level.cageEntity);
                        gs.setLevelCagesMap(m_cagesMap);
                        gs.cageToFree = gs.level.nbCages;*/
                    }
                    break;
            }
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

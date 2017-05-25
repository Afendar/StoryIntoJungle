package ld34.scene;

import audio.Sound;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import core.Game;
import entity.CageEntity;
import java.util.Collections;

/**
 * MapScene class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class MapScene extends Scene {
    
    public BufferedImage mapBg, panda, homeIcon, previousIcon, nextIcon, saveIcon, bgBtnMediumNoSelect, bgBtnMediumSelect;
    public Font font;
    public int[][] btnCoords;
    public int selectedItem, currentLvl, currentScore;
    
    private int[][] coordsPinsIcons = {
        {76, 337},
        {172, 397},
        {309, 407},
        {413, 285},
        {370, 161},
        {562, 315}
    };
    
    private int[][] coordsPins = {
        {86, 348},
        {182, 407},
        {320, 418},
        {423, 296},
        {381, 172},
        {571, 324}
    };
    private final Rectangle rect;
    private List<Point2D> points;
    private int index;
    private Point2D pos;
    private QuadCurve2D[] curve = new QuadCurve2D[5];
    private boolean animated = false;
    private List<List<CageEntity>> cagesMap;
    private boolean[] unlockedLevels;
    
    /**
     * 
     * @param w
     * @param h
     * @param game
     * @param currentLvl
     * @param currentScore 
     * @param unlockedLevels 
     */
    public MapScene(int w, int h, Game game, int currentLvl, int currentScore, boolean[] unlockedLevels){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            url = runtimeClass.getResource("/bgmap.png");
            this.mapBg = ImageIO.read(url);
            url = runtimeClass.getResource("/littles_pandas.png");
            this.panda = ImageIO.read(url);
            this.panda = this.panda.getSubimage(0, 0, 64, 64);
            
            this.homeIcon = this.spritesheetGui2.getSubimage(243, 94, 42, 34);
            this.previousIcon = this.spritesheetGui2.getSubimage(760, 151, 34, 34);
            this.nextIcon = this.spritesheetGui2.getSubimage(726, 151, 34, 36);
            this.saveIcon = this.spritesheetGui2.getSubimage(176, 93, 34, 35);
            
            this.bgBtnMediumNoSelect = this.spritesheetGui2.getSubimage(370, 1, 120, 99);
            this.bgBtnMediumSelect = this.spritesheetGui2.getSubimage(491, 1, 120, 99);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        int [][]coords = {
            {this.w/6 - 60, 480},
            {2* this.w/6 - 60, 480},
            {3* this.w/6 - 60, 480},
            {4* this.w/6 - 60, 480},
            {5* this.w/6 - 60, 480}
        };
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        this.currentLvl = currentLvl;
        this.currentScore = currentScore;
        this.unlockedLevels = unlockedLevels;
        
        this.rect = new Rectangle(0, 0, 32, 32);
        this.index = 0;
        
        this.curve[0] = new QuadCurve2D.Double(
                (double)this.coordsPins[0][0],(double)this.coordsPins[0][1],
                (double)116, (double)411,
                (double)this.coordsPins[1][0],(double)this.coordsPins[1][1]
        );
        this.curve[1] = new QuadCurve2D.Double(
                (double)this.coordsPins[1][0], (double)this.coordsPins[1][1],
                (double)253, (double)391,
                (double)this.coordsPins[2][0], (double)this.coordsPins[2][1]
        );
        this.curve[2] = new QuadCurve2D.Double(
                (double)this.coordsPins[2][0], (double)this.coordsPins[2][1],
                (double)501, (double)446,
                (double)this.coordsPins[3][0], (double)this.coordsPins[3][1]
        );
        this.curve[3] = new QuadCurve2D.Double(
                (double)this.coordsPins[3][0], (double)this.coordsPins[3][1],
                (double)372, (double)211,
                (double)this.coordsPins[4][0], (double)this.coordsPins[4][1]
        );
        this.curve[4] = new QuadCurve2D.Double(
                (double)this.coordsPins[4][0], (double)this.coordsPins[4][1],
                (double)537, (double)202,
                (double)this.coordsPins[5][0], (double)this.coordsPins[5][1]
        );
        pos = new Point2D.Double(this.coordsPins[this.currentLvl - 1][0], this.coordsPins[this.currentLvl - 1][1]);
        
        points = new ArrayList<>(25);
    }
    
    @Override
    public Scene update(double dt) {
        processHover();
        
        if(!this.points.isEmpty())
        {
            if(this.index < this.points.size() - 1)
                this.index++;
            else{
                this.animated = false;
            }
        
            this.pos = this.points.get(index);
        }
        
        return processClick();
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.mapBg, 0, 0, null);
        
        g.setFont(this.font);
        
        FontMetrics metrics = g.getFontMetrics(this.font);
        
        if(this.selectedItem == 1)
        {
            g.drawImage(this.bgBtnMediumSelect, this.btnCoords[0][0], this.btnCoords[0][1], null);
        }
        else
        {
            g.drawImage(this.bgBtnMediumNoSelect, this.btnCoords[0][0], this.btnCoords[0][1], null);
        }
        
        if(this.selectedItem == 2)
        {
            g.drawImage(this.bgBtnMediumSelect, this.btnCoords[1][0], this.btnCoords[1][1], null);
        }
        else
        {
            g.drawImage(this.bgBtnMediumNoSelect, this.btnCoords[1][0], this.btnCoords[1][1], null);
        }
        
        if(this.selectedItem == 3)
        {
            g.drawImage(this.bgBtnMediumSelect, this.btnCoords[2][0], this.btnCoords[2][1], null);
        }
        else
        {
            g.drawImage(this.bgBtnMediumNoSelect, this.btnCoords[2][0], this.btnCoords[2][1], null);
        }
        
        if(this.selectedItem == 4){
            g.drawImage(this.bgBtnMediumSelect, this.btnCoords[3][0], this.btnCoords[3][1], null);
        }
        else{
            g.drawImage(this.bgBtnMediumNoSelect, this.btnCoords[3][0], this.btnCoords[3][1], null);
        }
        
        if(this.selectedItem == 5){
            g.drawImage(this.bgBtnMediumSelect, this.btnCoords[4][0], this.btnCoords[4][1], null);
        }
        else{
            g.drawImage(this.bgBtnMediumNoSelect, this.btnCoords[4][0], this.btnCoords[4][1], null);
        }
        
        g.drawImage(this.homeIcon, this.btnCoords[0][0] + 35, this.btnCoords[0][1] + 32, null);
        g.drawImage(this.saveIcon, this.btnCoords[1][0] + 42, this.btnCoords[1][1] + 32, null);
        g.drawImage(this.previousIcon, this.btnCoords[2][0] + 40, this.btnCoords[2][1] + 30, null);
        g.drawImage(this.nextIcon, this.btnCoords[3][0] + 40, this.btnCoords[3][1] + 30, null);
        
        g.setColor(new Color(44, 23, 4));
        g.drawString("GO", this.btnCoords[4][0] + 40, this.btnCoords[4][1] + 55);
        
        //draw curves lvl 1 to 2
        g.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(this.curve[0]);
        
        //draw curve lvl 2 to 3
        g2d.draw(this.curve[1]);
        
        //draw curve lvl 3 to 4
        g2d.draw(this.curve[2]);
        
        //draw curve lvl 4 to 5
        g2d.draw(this.curve[3]);
        
        //draw curve lvl 5 to 6
        g2d.draw(this.curve[4]);
        
        for(int i=0 ; i<this.coordsPinsIcons.length;i++){
            if(this.unlockedLevels[i]){
                g.drawImage(this.spritesheetGui2.getSubimage(840, 42, 20, 20), this.coordsPinsIcons[i][0], this.coordsPinsIcons[i][1], null);
            }
            else{
                g.drawImage(this.spritesheetGui2.getSubimage(862, 42, 20, 20), this.coordsPinsIcons[i][0], this.coordsPinsIcons[i][1], null);
            }
        }
        
        AffineTransform at = new AffineTransform();
        at.rotate(0, 16, 16);
        g2d.drawImage(this.panda, null, (int)this.pos.getX() - 24, (int)this.pos.getY() - 24);
    }
    
    /**
     * 
     */
    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        int oldSelected = this.selectedItem;
        if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 120 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 99){
            this.selectedItem = 1;
        }
        else if(mouseX > this.btnCoords[1][0] && mouseX < this.btnCoords[1][0] + 120 &&
                mouseY > this.btnCoords[1][1] && mouseY < this.btnCoords[1][1] + 99){
            this.selectedItem = 2;
        }
        else if(mouseX > this.btnCoords[2][0] && mouseX < this.btnCoords[2][0] + 120 &&
                mouseY > this.btnCoords[2][1] && mouseY < this.btnCoords[2][1] + 99){
            this.selectedItem = 3;
        }
        else if(mouseX > this.btnCoords[3][0] && mouseX < this.btnCoords[3][0] + 120 &&
                mouseY > this.btnCoords[3][1] && mouseY < this.btnCoords[3][1] + 99){
            this.selectedItem = 4;
        }
        else if(mouseX > this.btnCoords[4][0] && mouseX < this.btnCoords[4][0] + 120 &&
                mouseY > this.btnCoords[4][1] && mouseY < this.btnCoords[4][1] + 99){
            this.selectedItem = 5;
        }
        else{
            this.selectedItem = 0;
        }
        
        if(this.selectedItem != 0 && this.selectedItem != oldSelected){
            new Thread(Sound.hover::play).start();
        }
    }
    
    /**
     * 
     * @return 
     */
    public Scene processClick(){
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1)
        {
            switch(this.selectedItem){
                case 1:
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                case 2:
                    //save
                    break;
                case 3:
                    if(this.currentLvl > 1 && this.unlockedLevels[this.currentLvl - 2]){
                        this.animated = true;
                        this.currentLvl--;
                        this.followPrevious();
                    }
                    break;
                case 4:
                    if(this.currentLvl < 6 && this.unlockedLevels[this.currentLvl]){
                        this.animated = true;
                        this.currentLvl++;
                        this.followNext();
                    }
                    break;
                case 5:
                    if(!this.animated){
                        GameScene gs = new GameScene(this.w, this.h, this.game, this.currentLvl, this.currentScore);
                        gs.level.setUnlockedLevels(unlockedLevels);
                        gs.setLevelCagesMap(this.cagesMap);
                        currentScene = gs;
                    }
                    break;
            }
        }
        
        return currentScene;
    }
    
    /**
     * 
     */
    public void followNext(){
        PathIterator pi = curve[this.currentLvl - 2].getPathIterator(null, 0.01);
        this.points = new ArrayList<>(25);
        while(!pi.isDone())
        {
            double[] coordinates = new double[6];
            switch(pi.currentSegment(coordinates)) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    points.add(new Point2D.Double(coordinates[0], coordinates[1]));
                    break;
            }
            pi.next();
        }
        this.index = 0;
    }
    
    /**
     * 
     */
    public void followPrevious(){
        PathIterator pi = curve[this.currentLvl - 1].getPathIterator(null, 0.01);
        this.points = new ArrayList<>(25);
        while(!pi.isDone())
        {
            double[] coordinates = new double[6];
            switch(pi.currentSegment(coordinates)){
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    this.points.add(new Point2D.Double(coordinates[0], coordinates[1]));
                    break;
            }
            pi.next();
        }
        
        Collections.reverse(this.points);
        
        this.index = 0;
    }
    
    /**
     * 
     * @param cagesMap 
     */
    public void setCagesMap(List<List<CageEntity>> cagesMap){
        this.cagesMap = cagesMap;
    }
}

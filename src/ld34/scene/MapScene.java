package ld34.scene;

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
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import ld34.profile.Settings;
import core.Game;

/**
 *
 * @author Mickaël
 */
public class MapScene extends Scene {
    
    public String nextTxt ,saveTxt, btnBack;
    public BufferedImage mapBg, panda;
    public Font font;
    public int[][] btnCoords;
    public int selectedItem, currentLvl, currentScore;
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
    
    public MapScene(int w, int h, Game game, int currentLvl, int currentScore){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 22.0f);
            url = runtimeClass.getResource("/bgmap.png");
            this.mapBg = ImageIO.read(url);
            url = runtimeClass.getResource("/little_panda.png");
            this.panda = ImageIO.read(url);
            
        }catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        int [][]coords = {
            {25, 505},
            {(this.w/3) + 25, 505},
            {(2*this.w/3) + 25, 505}
        };
        
        this.btnCoords = coords;
        this.selectedItem = 0;
        this.currentLvl = currentLvl;
        this.currentScore = currentScore;
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        this.bundle = ResourceBundle.getBundle("lang.levelmap", this.game.langs[localeIndex]);
        
        this.btnBack = this.bundle.getString("backToMain");
        this.nextTxt = this.bundle.getString("next");
        this.saveTxt = this.bundle.getString("save");
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
        //draw back btn
        int backWidth = metrics.stringWidth(this.btnBack);
        g.drawImage(this.bgBtn, this.btnCoords[0][0], this.btnCoords[0][1], null);
        if(this.selectedItem == 1)
        {
            g2d.rotate(-0.1, 25 + 107, 535);
            g.setColor(this.darkGreen);
            g.drawString(this.btnBack, (25 + 107) - backWidth/2, 545);
            g2d.rotate(0.1, 25 + 107, 535);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.drawString(this.btnBack, (25 + 107) - backWidth/2, 545);
        }
        
        int saveWidth = metrics.stringWidth(this.saveTxt);
        g.drawImage(this.bgBtn, this.btnCoords[1][0], this.btnCoords[1][1], null);
        if(this.selectedItem == 2)
        {
            g2d.rotate(-0.1, (this.w/3) + 25 + 107, 535);
            g.setColor(this.darkGreen);
            g.drawString(this.saveTxt, ((this.w/3) + 25 + 107) - saveWidth/2, 545);
            g2d.rotate(0.1, (this.w/3) + 25 + 107, 535);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.drawString(this.saveTxt, ((this.w/3) + 25 + 107) - saveWidth/2, 545);
        }
        
        int nextWidth = metrics.stringWidth(this.nextTxt);
        g.drawImage(this.bgBtn, this.btnCoords[2][0], this.btnCoords[2][1], null);
        if(this.selectedItem == 3)
        {
            g2d.rotate(-0.1, (2*this.w/3) + 25 + 107, 535);
            g.setColor(this.darkGreen);
            g.drawString(this.nextTxt, ((2*this.w/3) + 25 + 107) - nextWidth/2, 545);
            g2d.rotate(0.1, (2*this.w/3) + 25 + 107, 535);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.drawString(this.nextTxt, ((2*this.w/3) + 25 + 107) - nextWidth/2, 545);
        }
        
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
        
        
        
        AffineTransform at = new AffineTransform();
        at.rotate(0, 16, 16);
        g2d.drawImage(this.panda, null, (int)this.pos.getX() - 24, (int)this.pos.getY() - 24);
    }
    
    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        if(mouseX > this.btnCoords[0][0] && mouseX < this.btnCoords[0][0] + 214 &&
                mouseY > this.btnCoords[0][1] && mouseY < this.btnCoords[0][1] + 70){
            this.selectedItem = 1;
        }
        else if(mouseX > this.btnCoords[1][0] && mouseX < this.btnCoords[1][0] + 214 &&
                mouseY > this.btnCoords[1][1] && mouseY < this.btnCoords[1][1] + 70){
            this.selectedItem = 2;
        }
        else if(mouseX > this.btnCoords[2][0] && mouseX < this.btnCoords[2][0] + 214 &&
                mouseY > this.btnCoords[2][1] && mouseY < this.btnCoords[2][1] + 70){
            this.selectedItem = 3;
        }
        else{
            this.selectedItem = 0;
        }
    }
    
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
                    this.animated = true;
                    this.currentLvl++;
                    this.followNext();
                    break;
            }
        }
        
        if(this.animated && 
                this.pos.getX() == this.coordsPins[this.currentLvl - 1][0] && 
                this.pos.getY() == this.coordsPins[this.currentLvl - 1][1])
        {
            currentScene = new GameScene(this.w, this.h, this.game, this.currentLvl, this.currentScore);
        }
        
        return currentScene;
    }
    
    public void followNext(){
        PathIterator pi = curve[this.currentLvl-2].getPathIterator(null, 0.01);
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
}

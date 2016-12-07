package particles;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

public class Leaf extends Particle {
    private int x, y, size, startX, startY;
    private int winW, winH;
    private double dx, dy, perturb, influence, speed, dt, theta, angle;
    private boolean genStartX, dead;
    private Random rnd = new Random();
    private BufferedImage spriteSheet, leafSprites[], sprite;
    
    public Leaf(int size, int startX, int startY , int winW, int winH){
        try{
            URL url = this.getClass().getResource("/leafs.png");
            this.spriteSheet = ImageIO.read(url);
            this.leafSprites = new BufferedImage[4];
            this.leafSprites[0] = this.spriteSheet.getSubimage(0, 0, 35, 22);
            this.leafSprites[1] = this.spriteSheet.getSubimage(0, 23, 22, 20);
            this.leafSprites[2] = this.spriteSheet.getSubimage(35, 0, 35, 22);
            this.leafSprites[3] = this.spriteSheet.getSubimage(23, 23, 22, 20);
            this.sprite = this.leafSprites[rnd.nextInt(4-0)+0];
        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.winW = winW;
        this.winH = winH;
        this.dt = 0;
        this.genStartX = false;
        this.startX = startX;
        this.startY = startY;
        this.genRandStartX();
    }
    
    public boolean isGenStartX(){
        return this.genStartX;
    }
    
    public void setZone(int startX, int startY, int winW, int winH){
        this.startX = startX;
        this.startY = startY;
        this.winW = winW;
        this.winH = winH;
    }
    
    public void genRandStartX(){
        this.x = this.rnd.nextInt((this.startX + this.winW) - this.startX) + this.startX;
        this.y = this.startY - this.sprite.getHeight();
        this.speed = Math.random() * 4 + 1;
        this.genStartX = true;
        this.perturb = 0;
        this.influence = Math.random() * 0.06 + 0.05;
        this.theta = rnd.nextInt(91-0)+0;
    }
    
    @Override
    public void update(double dt){
        this.dt += dt;
        if(this.dt > 1.5)
        {
            this.dt = 0;
            this.dy = speed * Math.sin(90 * Math.PI / 180);
            this.dx = speed * Math.cos(this.perturb);
            this.angle = Math.cos(this.perturb/1.5);
            this.x += this.dx;
            if (this.y <= this.startY + this.winH - this.sprite.getHeight()) {
                this.y += this.dy;
            } else {
                this.genStartX = false;
                this.dead = true;
            }
            this.perturb += this.influence;
        }
    }
    
    public boolean isDead(){
        return this.dead;
    }
    
    @Override
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(this.angle, this.x, this.y);
        g2d.drawImage(this.sprite , (int)this.x - (this.sprite.getWidth() / 2), (int)this.y - (this.sprite.getHeight() / 2), null);
        g2d.rotate(-this.angle, this.x, this.y);
        g2d.dispose();
    }
}

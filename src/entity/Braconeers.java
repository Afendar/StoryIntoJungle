package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import core.Defines;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import level.Level;
import particles.Bullet;

/**
 * Braconeers class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Braconeers extends Entity {
    
    protected BufferedImage spritesheet, sprite;
    protected Level level;
    protected boolean isMoving, isShooting, isStuck, isDeadAnim, isDead;
    protected double timeWalk, elapsedTime, timeAnim, timeAnimStuck, timeAnimDeath, timeAnimShot;
    protected int direction;
    protected int velX;
    protected Random rnd;
    protected int offset;
    protected static int LEFT = 0;
    protected static int RIGHT = 1;
    protected List<Bullet> bullets = new ArrayList<>();
    
    /**
     * 
     * @param level
     * @param posX
     * @param posY 
     */
    public Braconeers(Level level, int posX, int posY)
    {
        super(posX , posY - Defines.TILE_SIZE);
        this.level = level;
        
        try{
            URL url = this.getClass().getResource("/braconier_sheet.png");
            this.spritesheet = ImageIO.read(url);
            this.sprite = this.spritesheet.getSubimage(0, 0, 84, 128);
        }
        catch(IOException e){
            e.getMessage();
        }
        this.isMoving = false;
        this.timeWalk = 0;
        this.elapsedTime = 0;
        this.timeAnim = 0;
        this.velX = 0;
        this.offset = 0;
        this.direction = LEFT;
        this.rnd = new Random();
    }
    
    @Override
    public void update(double dt)
    {
        this.elapsedTime += dt;
        
        int playerX = (int)this.level.player.getPosX();
        int playerY = (int)this.level.player.getPosY();
        if(playerX >= this.posX - 200 && playerX <= this.posX + 200 && playerY >= this.posY && playerY <= this.posY + 128){
            //System.out.println("player detected !");
            this.isShooting = true;
        }
        else{
            this.isShooting = false;
        }
        
        if(!this.isMoving && this.timeWalk == 0 && !this.isShooting){
            this.isMoving = rnd.nextBoolean();
            this.timeWalk = rnd.nextInt(150 - 100) + 100;
        }
        
        if(this.isStuck){
            if(this.timeAnimStuck < 120){
                this.timeAnimStuck += dt;
            }
            else{
                this.isDeadAnim = true;
            }
        }
        
        if(this.isDeadAnim){
            if(this.timeAnimDeath < 120){
                this.timeAnimDeath += dt;
            }
            else{
                this.isDead = true;
            }
        }
        
        if(this.isMoving && !this.isShooting && !this.isStuck && !this.isDead){
            if(this.elapsedTime < timeWalk){
                if(this.direction == LEFT)
                    this.velX = -(Defines.SPEED/2);
                else if(this.direction == RIGHT){
                    this.velX = (Defines.SPEED/2);
                }
                else{
                    this.velX = 0;
                }
                move();
                
                this.timeAnim += dt;
                if(this.timeAnim >= 10){
                    this.timeAnim = 0;
                    this.offset++;
                    if(this.offset > 2)
                        this.offset = 0;
                    this.sprite = this.spritesheet.getSubimage(this.offset * 84, 0, 84, 128);
                }
            }
        }
        else{
            this.offset = 0;
            this.sprite = this.spritesheet.getSubimage(this.offset * 84, 0, 84, 128);
        }
        
        if(this.isShooting && !this.isStuck){
            if(this.timeAnimShot < 200){
                this.timeAnimShot += dt;
            }
            else{
                this.timeAnimShot = 0;
                this.fire();
            }
        }
        
        if(this.elapsedTime >= timeWalk){
            this.isMoving = false;
            this.elapsedTime = 0;
            this.timeWalk = 0;
        }
        
        for(int i = 0 ; i < this.bullets.size() ; i++){
            Bullet b = this.bullets.get(i);
            Rectangle playerDim = this.level.player.getBounds();
            
            if(b.x > playerDim.x && b.x < playerDim.x + playerDim.width &&
                    b.y > playerDim.y && b.y < playerDim.y + playerDim.height){
                this.level.player.die();
                this.bullets.remove(i);
            }
            else{
                b.update(dt);
                if(b.isDead()){
                    this.bullets.remove(i);
                }
            }
        }
    }
    
    /**
     * 
     */
    protected void move(){
        this.posX += this.velX;
        
        if(this.direction == RIGHT){
            int x = (int)(this.posX + 59) / Defines.TILE_SIZE;
            int y = (int)(this.posY + ((Defines.TILE_SIZE - 1) * 2)) / Defines.TILE_SIZE;
            if(this.level.getTile(x, y + 1) == 0){
                this.direction = LEFT;
            }
        }
        else if(this.direction == LEFT){
            int x = (int)(this.posX + 25) / Defines.TILE_SIZE;
            int y = (int)(this.posY + ((Defines.TILE_SIZE - 1) * 2)) / Defines.TILE_SIZE;
            if(this.level.getTile(x, y + 1) == 0){
                this.direction = RIGHT;
            }
        }
        
        if(this.posX < 0){
            this.posX = 0;
            this.direction = RIGHT;
        }
        else if(this.posX + 84 > this.level.w){
            this.posX = this.level.w - 84;
            this.direction = LEFT;
        }
    }
    
    /**
     * 
     */
    public void doStuck(){
        System.out.println("do stuck braconeer");
        this.isStuck = true;
    }
    
    @Override
    public Rectangle getBounds(){
        return new Rectangle((int)this.posX + 18, (int)this.posY, 55, 128);
    }
    
    @Override
    public void render(Graphics g, Boolean debug){
        for(int i = 0; i < this.bullets.size(); i++){
            this.bullets.get(i).render(g);
        }
        
        g.drawImage(this.sprite, (int)this.posX, (int) this.posY, null);
        
        Rectangle rect = this.getBounds();
        if(this.isShooting){
            g.setColor(Color.RED);
            g.drawRect((int)rect.x, (int)rect.y, (int)rect.getWidth(), (int)rect.getHeight());
        }
        if(this.isStuck){
            g.setColor(Color.MAGENTA);
            g.drawRect((int)rect.x, (int)rect.y, (int)rect.getWidth(), (int)rect.getHeight());
        }
        if(this.isDeadAnim){
            g.setColor(Color.BLACK);
            g.drawRect((int)rect.x, (int)rect.y, (int)rect.getWidth(), (int)rect.getHeight());
        }
        
        if(debug){
            this.renderHitbox(g);
        }
    }
    
    @Override
    public void renderHitbox(Graphics g){
        Rectangle rect = this.getBounds();
        g.setColor(Color.GREEN);
        g.drawRect((int)rect.x, (int)rect.y, (int)rect.getWidth(), (int)rect.getHeight());
    }
    
    /**
     * 
     * @return 
     */
    public boolean isDead(){
        return this.isDead;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isStuck(){
        return this.isStuck;
    }
    
    /**
     * 
     */
    public void fire(){
        int velX = 0;
        int velY = 2;
        if(this.posX <= this.level.player.posX){
            velX = 4;
        }
        if(this.posX > this.level.player.posX){
            velX = -4;
        }
        
        Bullet b = new Bullet(
            (velX > 0)?this.getBounds().x + this.getBounds().width:this.getBounds().x,
            (int)this.posY + 60,
            velX, 
            velY, 
            250
        );
        this.bullets.add(b);
    }
}

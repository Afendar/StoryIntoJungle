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
import level.Level;

public class Braconeers extends Entity {
    
    protected BufferedImage spritesheet, sprite;
    protected Level level;
    protected boolean isMoving;
    protected double timeWalk, elapsedTime, timeAnim;
    protected int direction;
    protected int velX;
    protected Random rnd;
    protected int offset;
    
    protected static int LEFT = 0;
    protected static int RIGHT = 1;
    
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
        if(!this.isMoving && this.timeWalk == 0){
            this.isMoving = rnd.nextBoolean();
            this.timeWalk = rnd.nextInt(150 - 100) + 100;
        }
        
        this.elapsedTime += dt;
        
        if(this.isMoving){
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
        
        if(this.elapsedTime >= timeWalk){
            this.isMoving = false;
            this.elapsedTime = 0;
            this.timeWalk = 0;
        }
    }
    
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
    
    @Override
    public Rectangle getBounds(){
        return new Rectangle((int)this.posX + 18, (int)this.posY, 55, 128);
    }
    
    @Override
    public void render(Graphics g, Boolean debug){
        g.drawImage(this.sprite, (int)this.posX, (int) this.posY, null);
        
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
}

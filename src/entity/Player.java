package entity;

import audio.Sound;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import ld34.Camera;
import ld34.Defines;
import ld34.InputsListeners;
import level.Level;
import level.tiles.TileAtlas;

public class Player extends Entity {
    
    public Level level;
    public float velX, velY;
    protected boolean isFalling;
    protected boolean isJumping;
    InputsListeners listener;
    BufferedImage spritesheet, sprite;
    public Camera cam;
    public boolean isDead, win;
    public int difficulty;
    public int score;
    private int animX, animY, timeAnim;
    public int PLAYER_SIZE;
    
    public Player(int posX, int posY, Level level, InputsListeners listener, Camera cam, int difficulty){
        super(posX, posY);
        
        this.velX = 0;
        this.velY = 0;
        this.isFalling = true;
        this.isJumping = false;
        this.isDead = false;
        this.level = level;
        this.win = false;
        this.listener = listener;
        this.cam = cam;
        this.difficulty = difficulty;
        this.score = 0;
        this.animX = 0;
        this.animY = 2;
        this.timeAnim = 5;
        this.PLAYER_SIZE = 32;
        
        try{
            URL url = this.getClass().getResource("/spritesheet.png");
            this.spritesheet = ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
        }
        
        this.sprite = this.spritesheet.getSubimage(this.animX, this.animY*this.PLAYER_SIZE, this.PLAYER_SIZE, this.PLAYER_SIZE);
    }

    @Override
    public void update() {

        if(this.isDead){
            this.sprite = this.spritesheet.getSubimage(3*this.PLAYER_SIZE, 10, this.PLAYER_SIZE, this.PLAYER_SIZE);
            return;
        }
        
        if(this.isFalling || this.isJumping){
            this.velY += Defines.GRAVITY;
            
            if(this.velY > Defines.MAX_SPEED){
                this.velY = Defines.MAX_SPEED;
            }
        }
        
        int x0 = (int)(posX+2)/ Defines.TILE_SIZE;
        int y0 = (int)(posY+2) / Defines.TILE_SIZE;
        int x1 = (int)(posX - 2 + this.PLAYER_SIZE) / Defines.TILE_SIZE;
        int y1 = (int)(posY - 2 + this.PLAYER_SIZE) / Defines.TILE_SIZE;

        if(listener.jump.enabled && !this.isJumping){
            Sound.jump.play();
            this.isJumping = true;
            this.velY = - 5;
        }
        
        //On the bridge
        if(y1 + 1 < this.level.nbTilesH - 1 && 
                TileAtlas.atlas.get(this.level.getTile(x1, y1+1)).ID == 3 && 
                !listener.slow.enabled){
            this.level.removeTile(x1, y1+1);
            this.isJumping = true;
            this.isFalling = true;
            this.velX = 0;
            this.velY = 8;
        }
        else if(y1 + 1 < this.level.nbTilesH - 1 && 
                TileAtlas.atlas.get(this.level.getTile(x0, y1+1)).ID == 3 && 
                !listener.slow.enabled){
            this.level.removeTile(x0, y1+1);
            this.isJumping = true;
            this.isFalling = true;
            this.velY = 8;  
            this.velX = 0; 
        }
        
        //bonus test
        if(TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 4
                || TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 5){
            this.score += TileAtlas.atlas.get(this.level.getTile(x1, y1)).bonus;
            this.level.removeTile(x1, y1);
            Sound.bonus.play();
        }
        else if(TileAtlas.atlas.get(this.level.getTile(x0, y1)).ID == 4
                || TileAtlas.atlas.get(this.level.getTile(x0, y1)).ID == 5){
            this.score += TileAtlas.atlas.get(this.level.getTile(x0, y1)).bonus;
            this.level.removeTile(x0, y1);
            Sound.bonus.play();  
        }
        
        //end level test
        if(TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 7){
            this.win = true;
            Sound.levelup.play();
        }
        
        //spikes
        if(y1 < this.level.nbTilesH - 1 && 
                TileAtlas.atlas.get(this.level.getTile(x0, y1)).ID == 6 ||
                TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 6){
            this.isDead = true;
            Sound.death.play();
        }
        
        if(listener.mouseExited){
            this.velX = 0;
        }else{
            if(listener.mouseX + this.cam.x < (int)this.posX){
                if(listener.slow.enabled)
                    this.velX = (-(Defines.SPEED + this.difficulty))/2;
                else
                    this.velX = -(Defines.SPEED + this.difficulty);
                if(this.timeAnim == 0){
                    this.animY = 0 * this.PLAYER_SIZE;
                    this.animX += this.PLAYER_SIZE;
                    if(this.animX > 2*this.PLAYER_SIZE){
                        this.animX = 0;
                    }
                    this.timeAnim = 5;
                }
                
                this.sprite = this.spritesheet.getSubimage(this.animX, this.animY, this.PLAYER_SIZE, this.PLAYER_SIZE);
                
                if(this.timeAnim > 0){this.timeAnim--;}
            }
            else if(listener.mouseX + this.cam.x > (int)this.posX + 32){
                if(listener.slow.enabled)
                    this.velX = (Defines.SPEED + this.difficulty)/2;
                else
                    this.velX = (Defines.SPEED + this.difficulty);
                
                if(this.timeAnim == 0){
                    this.animY = this.PLAYER_SIZE;
                    this.animX += this.PLAYER_SIZE;
                    if(this.animX > 2*this.PLAYER_SIZE){
                        this.animX = 0;
                    }
                    this.timeAnim = 5;
                }
                
                this.sprite = this.spritesheet.getSubimage(this.animX, this.animY, this.PLAYER_SIZE, this.PLAYER_SIZE);
                
                if(this.timeAnim > 0){this.timeAnim--;}
            }
            else if(listener.mouseX + this.cam.x < (int)this.posX + 32 && listener.mouseX + this.cam.x > (int)this.posX){
                this.velX = 0;
                
                if(this.timeAnim == 0){
                    this.animY = 2*this.PLAYER_SIZE;
                    this.animX += this.PLAYER_SIZE;
                    if(this.animX > 3*this.PLAYER_SIZE){
                        this.animX = 0;
                    }
                    this.timeAnim = 20;
                }
                
                this.sprite = this.spritesheet.getSubimage(this.animX, this.animY, this.PLAYER_SIZE, this.PLAYER_SIZE);
                
                if(this.timeAnim > 0){this.timeAnim--;}
            }
        }
        
        //LEFT
        if((int)(posX + velX) / Defines.TILE_SIZE > 0 &&
                (!TileAtlas.atlas.get(this.level.getTile((int)(posX + velX) / Defines.TILE_SIZE, y0)).canPass() || 
                !TileAtlas.atlas.get(this.level.getTile((int)(posX + velX) / Defines.TILE_SIZE, y1)).canPass())){
            this.velX = 0;
        }
        //RIGHT
        else if((int)(posX + this.PLAYER_SIZE + velX) / Defines.TILE_SIZE < this.level.nbTilesW - 1 &&
                (!TileAtlas.atlas.get(this.level.getTile((int)(posX + this.PLAYER_SIZE + velX) / Defines.TILE_SIZE, y0)).canPass() || 
                !TileAtlas.atlas.get(this.level.getTile((int)(posX + this.PLAYER_SIZE + velX) / Defines.TILE_SIZE, y1)).canPass())){
             this.velX = 0; 
        } 
        
        if(!this.move(velX, velY)){
            this.velX = 0;
            this.velY = 0;
            this.isFalling = false;
            this.isJumping = false;
        }
        else{
            this.isFalling = true;
        }
    }

    public boolean move(float x, float y){

        float posX0 = this.posX + x;
        float posY0 = this.posY + y;
        
        int x0 = (int)(posX0 + 1) / Defines.TILE_SIZE;
        int y0 = (int)(posY0 + 1) / Defines.TILE_SIZE;
        int x1 = (int)(posX0 - 1 + this.PLAYER_SIZE) / Defines.TILE_SIZE;
        int y1 = (int)(posY0 - 1 + this.PLAYER_SIZE) / Defines.TILE_SIZE;
        
        if(x1 >= this.level.nbTilesW)
            x1 = this.level.nbTilesW - 1;
        if(y1 >= this.level.nbTilesH)
            y1 = this.level.nbTilesH - 1;
        
        //DOWN 
        if(!TileAtlas.atlas.get(this.level.getTile(x0, y1)).canPass() ||
                !TileAtlas.atlas.get(this.level.getTile(x1, y1)).canPass()){
            this.isJumping = false;
            return false;
        }
        
        //UP
        
        this.posX += x;
        this.posY += y;
        
        if(this.posX < 0){
            this.posX = 0;
            return false;
        }
        if(this.posY < 0){
            this.posY = 0;
            return false;
        }
        if(this.posX + this.PLAYER_SIZE > this.level.w){
            this.posX = this.level.w - this.PLAYER_SIZE;
            return false;
        }
        if(this.posY + this.PLAYER_SIZE > this.level.h){
            this.posY = this.level.h - this.PLAYER_SIZE;
            this.isDead = true;
            this.sprite = this.spritesheet.getSubimage(3 * this.PLAYER_SIZE, 10, this.PLAYER_SIZE, this.PLAYER_SIZE);
            Sound.death.play();
            return false;
        }
        
        return true;
    }
    
    @Override
    public void render(Graphics g) {
        g.drawImage(this.sprite, (int)this.posX, (int)this.posY, null);
    }
    
    public void reloadSpritesheet(int lvl){
        try{
            URL url = this.getClass().getResource("/spritesheet"+lvl+".png");
            this.spritesheet = ImageIO.read(url);
        }catch(IOException e){e.printStackTrace();}
    }
}

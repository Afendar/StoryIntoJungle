package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import core.Camera;
import ld34.profile.Settings;
import core.Defines;
import core.InputsListeners;
import core.TimerThread;
import java.awt.Color;
import java.util.List;
import level.Level;
import level.tiles.TileAtlas;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Player class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class Player extends Entity
{
    public Level level;
    public float velX, velY;
    public InputsListeners listener;
    public BufferedImage spritesheet, sprite, spritefx, spritesheetfx, spritesheetfxend, spritefxend;
    public Camera cam;
    public boolean isDead, win;
    public int difficulty;
    public int score;
    public String sex, age, species;
    public int checkpointX, checkpointY;
    public int direction, timeJAnim, offset, oldposX, oldposY, offset2, timeJEndAnim;
    public Thread jumpParticles, endJumpParticles;

    public static final int SPECIES_PANDA = 0;
    public static final int SPECIES_REDPANDA = 1;
    public static final int SEX_BOY = 0;
    public static final int SEX_GIRL = 1;
    public static final int PLAYER_SIZE = 64;

    protected boolean isFalling, isRespawn;
    protected boolean isJumping, renderJump, renderJumpEnd;

    private double timeRespawn, timeLastBlink;
    private int animX, animY, timeAnim;
    private String name;

    /**
     *
     * @param posX
     * @param posY
     * @param level
     * @param listener
     * @param cam
     * @param difficulty
     */
    public Player(int posX, int posY, Level level, InputsListeners listener, Camera cam, int difficulty)
    {
        super(posX, posY);

        this.velX = 0;
        this.velY = 0;
        this.isFalling = true;
        this.isJumping = false;
        this.isRespawn = false;
        this.isDead = false;
        this.renderJump = this.renderJumpEnd = false;
        this.level = level;
        this.win = false;
        this.listener = listener;
        this.cam = cam;
        this.difficulty = difficulty;
        this.score = 0;
        this.animX = 0;
        this.animY = 3;
        this.timeAnim = 5;
        this.checkpointX = this.checkpointY = 0;
        this.direction = this.timeJEndAnim = 0;
        this.timeJAnim = offset = oldposX = oldposY = offset2 = 0;
        this.timeRespawn = 0;
        this.timeLastBlink = 0;

        setSpecies(Integer.parseInt(Settings.getInstance().getConfigValue("spicies")));

        switch (Integer.parseInt(Settings.getInstance().getConfigValue("sex")))
        {
            case Player.SEX_BOY:
                this.sex = "boy";
                break;
            case Player.SEX_GIRL:
                this.sex = "girl";
                break;
        }

        switch (this.level.nbLevel)
        {
            case 1:
            case 2:
            case 3:
                this.age = "baby";
                break;
            case 4:
            case 5:
                this.age = "teen";
                break;
            case 6:
            case 7:
                this.age = "adult";
                break;
        }

        try
        {
            URL url = this.getClass().getResource("/" + this.species + "_" + this.sex + "_" + this.age + ".png");
            this.spritesheet = ImageIO.read(url);
            url = this.getClass().getResource("/effects_jump.png");
            this.spritesheetfx = ImageIO.read(url);
            url = this.getClass().getResource("/effects_end.png");
            this.spritesheetfxend = ImageIO.read(url);
        }
        catch (IOException e)
        {
            e.getMessage();
        }
        this.spritefx = this.spritesheetfx.getSubimage(0, 0, 60, 32);
        this.spritefxend = this.spritesheetfxend.getSubimage(0, 0, 60, 32);
        this.sprite = this.spritesheet.getSubimage(this.animX, this.animY * Player.PLAYER_SIZE, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
        this.name = Settings.getInstance().getConfigValue("name");
    }

    /**
     *
     * @param isRespawning
     */
    public void setIsRespawning(boolean isRespawning)
    {
        this.isRespawn = isRespawning;
        this.sprite = this.spritesheet.getSubimage(0, 2 * Player.PLAYER_SIZE, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
    }

    @Override
    public void update(double dt)
    {

        if (this.isRespawn)
        {
            this.timeRespawn += dt;
            this.timeLastBlink += dt;
            if (this.timeRespawn > 200)
            {
                this.timeRespawn = 0;
                this.timeLastBlink = 0;
                this.isRespawn = false;
            }
            return;
        }

        if (this.isDead)
        {
            this.sprite = this.spritesheet.getSubimage(3 * Player.PLAYER_SIZE, 10, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
            return;
        }

        if (this.isFalling || this.isJumping)
        {
            this.velY += Defines.GRAVITY;

            if (this.velY > Defines.MAX_SPEED)
            {
                this.velY = Defines.MAX_SPEED;
            }
        }

        int x0 = (int) (this.getBounds().x) / Defines.TILE_SIZE;
        int y0 = (int) (this.getBounds().y) / Defines.TILE_SIZE;
        int x1 = (int) (this.getBounds().x - 1 + this.getBounds().width) / Defines.TILE_SIZE;
        int y1 = (int) (this.getBounds().y - 1 + this.getBounds().height) / Defines.TILE_SIZE;

        if (listener.jump.enabled && !this.isJumping && !this.isFalling)
        {
            List<Braconeers> bl = this.level.getBraconeersEntities(this.getBounds().x + (this.direction == 0 ? Defines.TILE_SIZE : -Defines.TILE_SIZE), this.getBounds().y, this.getBounds().width, this.getBounds().height, false);
            if (bl.size() > 0)
            {
                for (int i = 0; i < bl.size(); i++)
                {
                    Braconeers b = bl.get(i);
                    b.doStuck();
                }
            }
            else
            {
                //TODO change sound call
                //new Thread(Sound.jump::play).start();
                this.renderJump = true;
                this.offset = 0;
                this.spritefx = this.spritesheetfx.getSubimage(this.offset * 60, 0, 60, 32);
                this.oldposX = (int) this.posX;
                this.oldposY = (int) this.posY;
                this.timeJAnim = TimerThread.MILLI;
                this.isJumping = true;
                this.velY = - 6;
            }
        }

        //On the bridge        
        if (y1 + 1 <= this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x1, y1 + 1)).ID == 3
                && !listener.slow.enabled)
        {
            this.level.removeTile(x1, y1 + 1);
        }
        if (y1 + 1 <= this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x0, y1 + 1)).ID == 3
                && !listener.slow.enabled)
        {
            this.level.removeTile(x0, y1 + 1);
        }

        //cage
        if (y1 + 1 <= this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x1, y1 + 1)).ID == 10
                && this.isJumping && this.isFalling)
        {
            CageEntity ce = this.level.getCageEntity(x1, y1 + 1);
            if (ce != null && !ce.isBreak())
            {
                ce.hurt();
            }
        }
        else if (y1 + 1 <= this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x0, y1 + 1)).ID == 10
                && this.isJumping && this.isFalling)
        {
            CageEntity ce = this.level.getCageEntity(x0, y1 + 1);
            if (ce != null && !ce.isBreak())
            {
                ce.hurt();
            }
        }

        //Sand
        if (y1 + 1 <= this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x1, y1 + 1)).ID == 9)
        {
            if (this.level.getData(x1, y1 + 1) < 1)
            {
                this.level.setData(x1, y1 + 1, 1);
            }
        }
        else if (y1 + 1 <= this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x0, y1 + 1)).ID == 9)
        {
            if (this.level.getData(x0, y1 + 1) < 1)
            {
                this.level.setData(x0, y1 + 1, 1);
            }
        }

        //bonus test
        if (TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 4
                || TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 5)
        {
            this.score += TileAtlas.atlas.get(this.level.getTile(x1, y1)).bonus;
            this.level.removeTile(x1, y1);
            //TODO change sound call
            //new Thread(Sound.bonus::play).start();
        }
        else if (TileAtlas.atlas.get(this.level.getTile(x0, y1)).ID == 4
                || TileAtlas.atlas.get(this.level.getTile(x0, y1)).ID == 5)
        {
            this.score += TileAtlas.atlas.get(this.level.getTile(x0, y1)).bonus;
            this.level.removeTile(x0, y1);
            //TODO change sound call
            //new Thread(Sound.bonus::play).start();
        }

        //checkpoint
        if (TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 8)
        {
            TileAtlas.atlas.get(this.level.getTile(x1, y1)).update(this.level, x1, y1, dt);
            this.checkpointX = x1 * Defines.TILE_SIZE;
            this.checkpointY = y1 * Defines.TILE_SIZE;
        }

        //end level test
        if (TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 7)
        {
            this.win = true;
            //TODO change sound call
            //new Thread(Sound.levelup::play).start();
        }

        //spikes
        if (y1 < this.level.nbTilesH - 1
                && TileAtlas.atlas.get(this.level.getTile(x0, y1)).ID == 6
                || TileAtlas.atlas.get(this.level.getTile(x1, y1)).ID == 6)
        {
            this.isDead = true;
            //TODO change sound call
            //new Thread(Sound.death::play).start();
        }

        if (listener.mouseExited)
        {
            this.velX = 0;
        }
        else
        {
            if (listener.mouseX + this.cam.x < (int) this.getBounds().x)
            {
                //Left Pose
                if (this.direction == 0)
                {
                    this.timeAnim = 0;
                }
                this.direction = 1;
                this.animY = 0;

                if (listener.slow.enabled)
                {
                    this.velX = (-(Defines.SPEED + this.difficulty)) / 2;
                }
                else
                {
                    this.velX = -(Defines.SPEED + this.difficulty);
                }
                if (this.timeAnim == 0)
                {
                    this.animX += Player.PLAYER_SIZE;
                    if (this.animX > 2 * Player.PLAYER_SIZE)
                    {
                        this.animX = 0;
                    }
                    this.timeAnim = 5;
                }

                if (this.timeAnim > 0)
                {
                    this.timeAnim--;
                }

            }
            else if (listener.mouseX + this.cam.x > (int) this.getBounds().x + this.getBounds().width)
            {
                //Right Pose
                if (this.direction == 1)
                {
                    this.timeAnim = 0;
                }
                this.direction = 0;
                this.animY = Player.PLAYER_SIZE;

                if (listener.slow.enabled)
                {
                    this.velX = (Defines.SPEED + this.difficulty) / 2;
                }
                else
                {
                    this.velX = (Defines.SPEED + this.difficulty);
                }

                if (this.timeAnim == 0)
                {
                    this.animX += Player.PLAYER_SIZE;
                    if (this.animX > 2 * Player.PLAYER_SIZE)
                    {
                        this.animX = 0;
                    }
                    this.timeAnim = 5;
                }

                if (this.timeAnim > 0)
                {
                    this.timeAnim--;
                }

            }
            else if (listener.mouseX + this.cam.x < (int) this.getBounds().x + this.getBounds().width / 2 + 5 && listener.mouseX + this.cam.x > (int) this.getBounds().x + this.getBounds().width / 2 - 5)
            {
                //Stand Pose
                this.velX = 0;
                this.animY = 2 * Player.PLAYER_SIZE;
                if (this.timeAnim == 0)
                {
                    this.animX += Player.PLAYER_SIZE;
                    if (this.animX > 2 * Player.PLAYER_SIZE)
                    {
                        this.animX = 0;
                    }
                    this.timeAnim = 40;
                }

                if (this.timeAnim > 0)
                {
                    this.timeAnim--;
                }
            }
            this.sprite = this.spritesheet.getSubimage(this.animX, this.animY, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
        }

        //LEFT
        if ((int) (this.getBounds().x + this.velX) / Defines.TILE_SIZE > 0
                && (!TileAtlas.atlas.get(this.level.getTile(((int) (this.getBounds().x + velX) / Defines.TILE_SIZE), (this.getBounds().y + 2) / Defines.TILE_SIZE)).canPass(this.level, ((int) (this.getBounds().x + velX) / Defines.TILE_SIZE), (this.getBounds().y + 2) / Defines.TILE_SIZE)
                || !TileAtlas.atlas.get(this.level.getTile(((int) (this.getBounds().x + velX) / Defines.TILE_SIZE), (this.getBounds().y + this.getBounds().height - 2) / Defines.TILE_SIZE)).canPass(this.level, ((int) (this.getBounds().x + velX) / Defines.TILE_SIZE), (this.getBounds().y + this.getBounds().height - 2) / Defines.TILE_SIZE)))
        {
            this.velX = 0;
        }
        //RIGHT
        else if ((int) (this.getBounds().x + this.getBounds().width + velX) / Defines.TILE_SIZE < this.level.nbTilesW - 1
                && (!TileAtlas.atlas.get(this.level.getTile((int) (this.getBounds().x + this.getBounds().width + velX) / Defines.TILE_SIZE, (this.getBounds().y + 2) / Defines.TILE_SIZE)).canPass(this.level, (int) (this.getBounds().x + this.getBounds().width + velX) / Defines.TILE_SIZE, (this.getBounds().y + 2) / Defines.TILE_SIZE)
                || !TileAtlas.atlas.get(this.level.getTile((int) (this.getBounds().x + this.getBounds().width + velX) / Defines.TILE_SIZE, (this.getBounds().y + this.getBounds().height - 2) / Defines.TILE_SIZE)).canPass(this.level, (int) (this.getBounds().x + this.getBounds().width + velX) / Defines.TILE_SIZE, (this.getBounds().y + this.getBounds().height - 2) / Defines.TILE_SIZE)))
        {
            this.velX = 0;
        }

        //DOWN 
        int y1VelY = (int) ((this.getBounds().y + this.getBounds().height + 4) / Defines.TILE_SIZE);
        if (y1 + 1 < this.level.nbTilesH)
        {
            if ((!TileAtlas.atlas.get(this.level.getTile(x0, y1VelY)).canPass(this.level, x0, y1VelY)
                    || !TileAtlas.atlas.get(this.level.getTile(x1, y1VelY)).canPass(this.level, x1, y1VelY)) && this.velY >= 0)
            {
                this.isJumping = false;
                this.isFalling = false;
                this.renderJump = false;

                if (this.velY > 0)
                {
                    this.renderJumpEnd = true;
                    this.offset2 = 0;
                    this.spritefxend = this.spritesheetfxend.getSubimage(this.offset2 * 60, 0, 60, 32);
                    this.oldposX = (int) this.posX;
                    this.oldposY = (int) this.posY;
                    this.timeJEndAnim = TimerThread.MILLI;
                }
                this.velY = 0;
            }
            else if (!this.isFalling && !this.isJumping)
            {
                this.velY = Defines.GRAVITY;
            }
        }

        this.isFalling = this.velY > 0;

        if (this.renderJump && TimerThread.MILLI - this.timeJAnim > 80)
        {
            this.timeJAnim = TimerThread.MILLI;
            this.offset++;
            if (this.offset > 4)
            {
                this.offset = 0;
                this.renderJump = false;
            }
            else
            {
                this.spritefx = this.spritesheetfx.getSubimage(this.offset * 60, 0, 60, 32);
            }
        }

        if (this.renderJumpEnd && TimerThread.MILLI - this.timeJEndAnim > 80)
        {
            this.timeJEndAnim = TimerThread.MILLI;
            this.offset2++;
            if (this.offset2 > 3)
            {
                this.offset2 = 0;
                this.renderJumpEnd = false;
            }
            else
            {
                this.spritefxend = this.spritesheetfxend.getSubimage(this.offset2 * 60, 0, 60, 32);
            }
        }

        if (!this.move(velX, velY))
        {
            this.velX = 0;
            this.velY = 0;
            this.isFalling = false;
            this.isJumping = false;
            this.renderJump = false;
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public boolean move(float x, float y)
    {

        this.posX += x;
        this.posY += y;

        if (this.posX < 0)
        {
            this.posX = 0;
            return false;
        }
        if (this.posY < 0)
        {
            this.posY = 0;
            return false;
        }
        if (this.getBounds().x + this.getBounds().width > this.level.w)
        {
            this.posX = this.level.w - this.getBounds().width;
            return false;
        }
        if (this.getBounds().y + this.getBounds().height > this.level.h)
        {
            this.posY = this.level.h - this.getBounds().height;
            this.isDead = true;
            this.sprite = this.spritesheet.getSubimage(3 * Player.PLAYER_SIZE, 10, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
            //TODO change sound call
            //new Thread(Sound.death::play).start();
            return false;
        }
        return true;
    }

    @Override
    public void render(Graphics g, Boolean debug)
    {

        if (this.renderJump)
        {
            g.drawImage(this.spritefx, this.oldposX + 20, this.oldposY + this.getBounds().height, null);
        }
        if (this.renderJumpEnd)
        {
            g.drawImage(this.spritefxend, this.oldposX + 20, this.oldposY + this.getBounds().height, null);
        }

        if (this.timeLastBlink > 20)
        {
            if (this.timeLastBlink > 30)
            {
                this.timeLastBlink = 0;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
            g2d.drawImage(this.sprite, (int) this.posX, (int) this.posY, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
        else
        {
            g.drawImage(this.sprite, (int) this.posX, (int) this.posY, null);
        }

        if (debug)
        {
            this.renderHitbox(g);
        }
    }

    /**
     *
     * @param lvl
     */
    public void reloadSpritesheet(int lvl)
    {

        switch (lvl)
        {
            case 1:
            case 2:
                this.age = "baby";
                break;
            case 3:
            case 4:
                this.age = "teen";
                break;
            case 5:
            case 6:
                this.age = "adult";
                break;
        }

        try
        {
            URL url = this.getClass().getResource("/" + this.species + "_" + this.sex + "_" + this.age + ".png");
            this.spritesheet = ImageIO.read(url);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Rectangle getBounds()
    {
        Rectangle bounds;
        switch (this.age)
        {
            default:
            case "baby":
                bounds = new Rectangle((int) this.posX + 5, (int) this.posY + 20, Player.PLAYER_SIZE - 10, Player.PLAYER_SIZE - 20);
                break;
            case "teen":
                bounds = new Rectangle((int) this.posX + 2, (int) this.posY + 16, Player.PLAYER_SIZE - 6, Player.PLAYER_SIZE - 16);
                break;
            case "adult":
                bounds = new Rectangle((int) this.posX + 2, (int) this.posY + 8, Player.PLAYER_SIZE - 6, Player.PLAYER_SIZE - 8);
                break;
        }
        return bounds;
    }

    /**
     *
     * @return
     */
    public boolean isJumping()
    {
        return this.isJumping;
    }

    /**
     *
     * @return
     */
    public boolean isFalling()
    {
        return this.isFalling;
    }

    @Override
    public void renderHitbox(Graphics g)
    {
        Rectangle rect = this.getBounds();
        g.setColor(Color.BLUE);
        g.drawRect((int) rect.x, (int) rect.y, (int) rect.getWidth(), (int) rect.getHeight());
    }

    /**
     *
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @param species
     */
    public void setSpecies(int species)
    {
        switch (species)
        {
            case Player.SPECIES_PANDA:
                this.species = "panda";
                break;
            case Player.SPECIES_REDPANDA:
                this.species = "redpanda";
                break;
        }
    }

    /**
     *
     */
    public void die()
    {
        this.isDead = true;
    }
    
    public JSONObject toSave()
    {
        /*
        "difficulty": 0,
        "species": 0,
        "score": 3000,
        "sex": 0,
        "name": "pipouille",
        "coords": [1040, 100]
        */
        JSONObject data = new JSONObject();
        data.put("difficulty", difficulty);
        data.put("species", species);
        data.put("score", score);
        data.put("sex", sex);
        data.put("name", name);
        JSONArray coords = new JSONArray();
        coords.add(posX);
        coords.add(posY);
        data.put("coords", coords);
        return data;
    }
}

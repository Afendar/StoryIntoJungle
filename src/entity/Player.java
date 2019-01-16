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
import core.Context;
import ld34.profile.Settings;
import core.Defines;
import core.InputsListeners;
import core.TimerThread;
import java.awt.Color;
import java.util.ArrayList;
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
    public static final int SPECIES_PANDA = 0;
    public static final int SPECIES_REDPANDA = 1;
    
    public static final int SEX_BOY = 0;
    public static final int SEX_GIRL = 1;
    
    public static final int PLAYER_SIZE = 64;
    
    private Level m_level;
    private float m_velX, m_velY;
    private InputsListeners m_listener;
    private BufferedImage m_spritesheet, m_sprite, m_spritefx, m_spritesheetfx, m_spritesheetfxend, m_spritefxend;
    private Camera m_cam;
    private boolean m_isDead, m_win, m_splash;
    private int m_difficulty;
    private int m_score;
    private String m_sex, m_age, m_species;
    private int m_checkpointX, m_checkpointY;
    private int m_direction, m_timeJAnim, m_offset, m_oldposX, m_oldposY, m_offset2, m_timeJEndAnim;
    private Thread jumpParticles, endJumpParticles;

    private boolean m_isFalling, m_isRespawn;
    private boolean m_isJumping, m_renderJump, m_renderJumpEnd;

    private double m_timeRespawn, m_timeLastBlink;
    private int m_animX, m_animY, m_timeAnim;
    private String m_name;
    
    private ArrayList<Animation> m_animations;

    /**
     *
     * @param posX
     * @param posY
     * @param level
     * @param listener
     * @param cam
     * @param difficulty
     */
    public Player(int posX, int posY, Level level, InputsListeners listener, Camera cam, int difficulty, Context context)
    {
        super(posX, posY, context);

        m_velX = 0;
        m_velY = 0;
        m_isFalling = true;
        m_isJumping = false;
        m_isRespawn = false;
        m_splash = false;
        m_isDead = false;
        m_renderJump = m_renderJumpEnd = false;
        m_level = level;
        m_win = false;
        m_listener = listener;
        m_cam = cam;
        m_difficulty = difficulty;
        m_score = 0;
        m_animX = 0;
        m_animY = 3;
        m_timeAnim = 5;
        m_checkpointX = m_checkpointY = 0;
        m_direction = m_timeJEndAnim = 0;
        m_timeJAnim = m_offset = m_oldposX = m_oldposY = m_offset2 = 0;
        m_timeRespawn = 0;
        m_timeLastBlink = 0;

        setSpecies(Integer.parseInt(Settings.getInstance().getConfigValue("species")));

        switch (Integer.parseInt(Settings.getInstance().getConfigValue("sex")))
        {
            case Player.SEX_BOY:
                m_sex = "boy";
                break;
            case Player.SEX_GIRL:
                m_sex = "girl";
                break;
        }

        switch (m_level.getNumber())
        {
            case 1:
            case 2:
            case 3:
                m_age = "baby";
                break;
            case 4:
            case 5:
                m_age = "teen";
                break;
            case 6:
            case 7:
                m_age = "adult";
                break;
        }

        try
        {
            URL url = this.getClass().getResource("/" + m_species + "_" + m_sex + "_" + m_age + ".png");
            m_spritesheet = ImageIO.read(url);
            url = this.getClass().getResource("/effects_jump.png");
            m_spritesheetfx = ImageIO.read(url);
            url = this.getClass().getResource("/effects_end.png");
            m_spritesheetfxend = ImageIO.read(url);
        }
        catch (IOException e)
        {
            e.getMessage();
        }
        
        m_spritefx = m_spritesheetfx.getSubimage(0, 0, 60, 32);
        m_spritefxend = m_spritesheetfxend.getSubimage(0, 0, 60, 32);
        m_sprite = m_spritesheet.getSubimage(m_animX, m_animY * Player.PLAYER_SIZE, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
        m_name = Settings.getInstance().getConfigValue("name");
        
        m_animations = new ArrayList<>();
    }

    /**
     *
     * @param isRespawning
     */
    public void setIsRespawning(boolean isRespawning)
    {
        m_isRespawn = isRespawning;
        m_sprite = m_spritesheet.getSubimage(0, 2 * Player.PLAYER_SIZE, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
    }

    @Override
    public void update(double dt)
    {

        if(m_isRespawn)
        {
            m_timeRespawn += dt;
            m_timeLastBlink += dt;
            if (m_timeRespawn > 200)
            {
                m_timeRespawn = 0;
                m_timeLastBlink = 0;
                m_isRespawn = false;
            }
            return;
        }

        if(m_isDead)
        {
            m_sprite = m_spritesheet.getSubimage(3 * Player.PLAYER_SIZE, 10, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
            return;
        }

        if(m_isFalling || m_isJumping)
        {
            m_velY += Defines.GRAVITY;

            if(m_velY > Defines.MAX_SPEED)
            {
                m_velY = Defines.MAX_SPEED;
            }
        }

        int x0 = (int) (getBounds().x) / Defines.TILE_SIZE;
        int y0 = (int) (getBounds().y) / Defines.TILE_SIZE;
        int x1 = (int) (getBounds().x - 1 + getBounds().width) / Defines.TILE_SIZE;
        int y1 = (int) (getBounds().y - 1 + getBounds().height) / Defines.TILE_SIZE;

        if (m_listener.jump.enabled && !m_isJumping && !m_isFalling)
        {
            List<Braconeers> bl = m_level.getBraconeersEntities(this.getBounds().x + (m_direction == 0 ? Defines.TILE_SIZE : -Defines.TILE_SIZE), getBounds().y, getBounds().width, getBounds().height, false);
            if(bl.size() > 0)
            {
                for (int i = 0; i < bl.size(); i++)
                {
                    Braconeers b = bl.get(i);
                    b.doStuck();
                }
            }
            else
            {
                new Thread(m_context.m_resourceManager.getSound("jump")::play).start();
                m_renderJump = true;
                m_offset = 0;
                m_spritefx = m_spritesheetfx.getSubimage(m_offset * 60, 0, 60, 32);
                m_oldposX = (int) m_posX;
                m_oldposY = (int) m_posY;
                m_timeJAnim = TimerThread.MILLI;
                m_isJumping = true;
                m_velY = - 6;
            }
        }

        //On the bridge        
        if (y1 + 1 <= m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x1, y1 + 1)).ID == 3
                && !m_listener.slow.enabled)
        {
            m_level.removeTile(x1, y1 + 1);
        }
        if (y1 + 1 <= m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x0, y1 + 1)).ID == 3
                && !m_listener.slow.enabled)
        {
            m_level.removeTile(x0, y1 + 1);
        }

        //cage
        if (y1 + 1 <= m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x1, y1 + 1)).ID == 10
                && m_isJumping && m_isFalling)
        {
            CageEntity ce = m_level.getCageEntity(x1, y1 + 1);
            if (ce != null && !ce.isBreak())
            {
                ce.hurt();
            }
        }
        else if(y1 + 1 <= m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x0, y1 + 1)).ID == 10
                && m_isJumping && m_isFalling)
        {
            CageEntity ce = m_level.getCageEntity(x0, y1 + 1);
            if(ce != null && !ce.isBreak())
            {
                ce.hurt();
            }
        }

        //Sand
        if (y1 + 1 <= m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x1, y1 + 1)).ID == 9)
        {
            if (m_level.getData(x1, y1 + 1) < 1)
            {
                m_level.setData(x1, y1 + 1, 1);
            }
        }
        else if (y1 + 1 <= m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x0, y1 + 1)).ID == 9)
        {
            if (m_level.getData(x0, y1 + 1) < 1)
            {
                m_level.setData(x0, y1 + 1, 1);
            }
        }

        //bonus test
        if (TileAtlas.atlas.get(m_level.getTile(x1, y1)).ID == 4
                || TileAtlas.atlas.get(m_level.getTile(x1, y1)).ID == 5)
        {
            m_score += TileAtlas.atlas.get(m_level.getTile(x1, y1)).bonus;
            m_level.removeTile(x1, y1);
            new Thread(m_context.m_resourceManager.getSound("bonus")::play).start();
        }
        else if (TileAtlas.atlas.get(m_level.getTile(x0, y1)).ID == 4
                || TileAtlas.atlas.get(m_level.getTile(x0, y1)).ID == 5)
        {
            m_score += TileAtlas.atlas.get(m_level.getTile(x0, y1)).bonus;
            m_level.removeTile(x0, y1);
            new Thread(m_context.m_resourceManager.getSound("bonus")::play).start();
        }

        //checkpoint
        if (TileAtlas.atlas.get(m_level.getTile(x1, y1)).ID == 8)
        {
            TileAtlas.atlas.get(m_level.getTile(x1, y1)).update(m_level, x1, y1, dt);
            m_checkpointX = x1 * Defines.TILE_SIZE;
            m_checkpointY = y1 * Defines.TILE_SIZE;
        }

        //end level test
        if (TileAtlas.atlas.get(m_level.getTile(x1, y1)).ID == 7)
        {
            m_win = true;
            new Thread(m_context.m_resourceManager.getSound("levelup")::play).start();
        }

        //spikes
        if (y1 < m_level.getNbTilesH() - 1
                && TileAtlas.atlas.get(m_level.getTile(x0, y1)).ID == 6
                || TileAtlas.atlas.get(m_level.getTile(x1, y1)).ID == 6)
        {
            m_isDead = true;
            new Thread(m_context.m_resourceManager.getSound("death")::play).start();
        }

        if (m_listener.mouseExited)
        {
            m_velX = 0;
        }
        else
        {
            if (m_listener.mouseX + m_cam.m_x < (int) this.getBounds().x)
            {
                //Left Pose
                if (m_direction == 0)
                {
                    m_timeAnim = 0;
                }
                m_direction = 1;
                m_animY = 0;

                if (m_listener.slow.enabled)
                {
                    m_velX = (-(Defines.SPEED + m_difficulty)) / 2;
                }
                else
                {
                    m_velX = -(Defines.SPEED + m_difficulty);
                }
                if (m_timeAnim == 0)
                {
                    m_animX += Player.PLAYER_SIZE;
                    if (m_animX > 2 * Player.PLAYER_SIZE)
                    {
                        m_animX = 0;
                    }
                    m_timeAnim = 5;
                }

                if (m_timeAnim > 0)
                {
                    m_timeAnim--;
                }

            }
            else if (m_listener.mouseX + m_cam.m_x > (int) this.getBounds().x + this.getBounds().width)
            {
                //Right Pose
                if (m_direction == 1)
                {
                    m_timeAnim = 0;
                }
                m_direction = 0;
                m_animY = Player.PLAYER_SIZE;

                if(m_listener.slow.enabled)
                {
                    m_velX = (Defines.SPEED + m_difficulty) / 2;
                }
                else
                {
                    m_velX = (Defines.SPEED + m_difficulty);
                }

                if (m_timeAnim == 0)
                {
                    m_animX += Player.PLAYER_SIZE;
                    if (m_animX > 2 * Player.PLAYER_SIZE)
                    {
                        m_animX = 0;
                    }
                    m_timeAnim = 5;
                }

                if (m_timeAnim > 0)
                {
                    m_timeAnim--;
                }

            }
            else if (m_listener.mouseX + m_cam.m_x < (int) this.getBounds().x + this.getBounds().width / 2 + 5 && m_listener.mouseX + m_cam.m_x > (int) this.getBounds().x + this.getBounds().width / 2 - 5)
            {
                //Stand Pose
                m_velX = 0;
                m_animY = 2 * Player.PLAYER_SIZE;
                if (m_timeAnim == 0)
                {
                    m_animX += Player.PLAYER_SIZE;
                    if (m_animX > 2 * Player.PLAYER_SIZE)
                    {
                        m_animX = 0;
                    }
                    m_timeAnim = 40;
                }

                if (m_timeAnim > 0)
                {
                    m_timeAnim--;
                }
            }
            m_sprite = m_spritesheet.getSubimage(m_animX, m_animY, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
        }

        //LEFT
        if ((int) (getBounds().x + m_velX) / Defines.TILE_SIZE > 0
                && (!TileAtlas.atlas.get(m_level.getTile(((int) (getBounds().x + m_velX) / Defines.TILE_SIZE), (getBounds().y + 2) / Defines.TILE_SIZE)).canPass(m_level, ((int) (getBounds().x + m_velX) / Defines.TILE_SIZE), (getBounds().y + 2) / Defines.TILE_SIZE)
                || !TileAtlas.atlas.get(m_level.getTile(((int) (getBounds().x + m_velX) / Defines.TILE_SIZE), (getBounds().y + getBounds().height - 2) / Defines.TILE_SIZE)).canPass(m_level, ((int) (getBounds().x + m_velX) / Defines.TILE_SIZE), (getBounds().y + getBounds().height - 2) / Defines.TILE_SIZE)))
        {
            m_velX = 0;
        }
        //RIGHT
        else if ((int) (getBounds().x + getBounds().width + m_velX) / Defines.TILE_SIZE < m_level.getNbTilesW() - 1
                && (!TileAtlas.atlas.get(m_level.getTile((int) (getBounds().x + getBounds().width + m_velX) / Defines.TILE_SIZE, (getBounds().y + 2) / Defines.TILE_SIZE)).canPass(m_level, (int) (getBounds().x + getBounds().width + m_velX) / Defines.TILE_SIZE, (getBounds().y + 2) / Defines.TILE_SIZE)
                || !TileAtlas.atlas.get(m_level.getTile((int) (getBounds().x + getBounds().width + m_velX) / Defines.TILE_SIZE, (getBounds().y + getBounds().height - 2) / Defines.TILE_SIZE)).canPass(m_level, (int) (getBounds().x + getBounds().width + m_velX) / Defines.TILE_SIZE, (getBounds().y + getBounds().height - 2) / Defines.TILE_SIZE)))
        {
            m_velX = 0;
        }

        //DOWN 
        int y1VelY = (int) ((getBounds().y + getBounds().height + 4) / Defines.TILE_SIZE);
        if(y1 + 1 < m_level.getNbTilesH())
        {
            if ((!TileAtlas.atlas.get(m_level.getTile(x0, y1VelY)).canPass(m_level, x0, y1VelY)
                    || !TileAtlas.atlas.get(m_level.getTile(x1, y1VelY)).canPass(m_level, x1, y1VelY)) && m_velY >= 0)
            {
                m_isJumping = false;
                m_isFalling = false;
                m_renderJump = false;

                if(m_velY > 0)
                {
                    m_renderJumpEnd = true;
                    m_offset2 = 0;
                    m_spritefxend = m_spritesheetfxend.getSubimage(m_offset2 * 60, 0, 60, 32);
                    m_oldposX = (int) m_posX;
                    m_oldposY = (int) m_posY;
                    m_timeJEndAnim = TimerThread.MILLI;
                }
                m_velY = 0;
            }
            else if(!m_isFalling && !m_isJumping)
            {
                m_velY = Defines.GRAVITY;
            }
        }

        if(m_posY > 1050 - 20 && m_posY < 1050 + 5  && !m_splash)
        {
            //m_level.splash(m_posX + 32, 6.3f);
            m_splash = true;
        }
        else if((m_posY > m_level.getHeight() - 70 || m_posY < 1050 - 20) && m_splash)
        {
            //TODO Check if in water stop velocity and start to floating
            m_velY = 0;
            m_isJumping = false;
            m_isFalling = false;
            m_splash = false;
        }
        
        m_isFalling = m_velY > 0;

        if(m_renderJump && TimerThread.MILLI - m_timeJAnim > 80)
        {
            m_timeJAnim = TimerThread.MILLI;
            m_offset++;
            if (m_offset > 4)
            {
                m_offset = 0;
                m_renderJump = false;
            }
            else
            {
                m_spritefx = m_spritesheetfx.getSubimage(m_offset * 60, 0, 60, 32);
            }
        }

        if(m_renderJumpEnd && TimerThread.MILLI - m_timeJEndAnim > 80)
        {
            m_timeJEndAnim = TimerThread.MILLI;
            m_offset2++;
            if (m_offset2 > 3)
            {
                m_offset2 = 0;
                m_renderJumpEnd = false;
            }
            else
            {
                m_spritefxend = m_spritesheetfxend.getSubimage(m_offset2 * 60, 0, 60, 32);
            }
        }

        if(!this.move(m_velX, m_velY))
        {
            m_velX = 0;
            m_velY = 0;
            m_isFalling = false;
            m_isJumping = false;
            m_renderJump = false;
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
        m_posX += x;
        m_posY += y;

        if(m_posX < 0)
        {
            m_posX = 0;
            return false;
        }
        if(m_posY < 0)
        {
            m_posY = 0;
            return false;
        }
        if(getBounds().x + getBounds().width > m_level.getWidth())
        {
            m_posX = m_level.getWidth() - getBounds().width;
            return false;
        }
        if(getBounds().y + getBounds().height > m_level.getHeight())
        {
            m_posY = m_level.getHeight() - getBounds().height;
            m_isDead = true;
            m_sprite = m_spritesheet.getSubimage(3 * Player.PLAYER_SIZE, 10, Player.PLAYER_SIZE, Player.PLAYER_SIZE);
            new Thread(m_context.m_resourceManager.getSound("death")::play).start();
            return false;
        }
        return true;
    }

    @Override
    public void render(Graphics g, Boolean debug)
    {
        if (m_renderJump)
        {
            g.drawImage(m_spritefx, m_oldposX + 20, m_oldposY + getBounds().height, null);
        }
        if(m_renderJumpEnd)
        {
            g.drawImage(m_spritefxend, m_oldposX + 20, m_oldposY + getBounds().height, null);
        }

        if (m_timeLastBlink > 20)
        {
            if (m_timeLastBlink > 30)
            {
                m_timeLastBlink = 0;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
            g2d.drawImage(m_sprite, (int) m_posX, (int) m_posY, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
        else
        {
            g.drawImage(m_sprite, (int) m_posX, (int) m_posY, null);
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
                m_age = "baby";
                break;
            case 3:
            case 4:
                m_age = "teen";
                break;
            case 5:
            case 6:
                m_age = "adult";
                break;
        }

        try
        {
            URL url = this.getClass().getResource("/" + m_species + "_" + m_sex + "_" + m_age + ".png");
            m_spritesheet = ImageIO.read(url);
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
        switch (m_age)
        {
            default:
            case "baby":
                bounds = new Rectangle((int) m_posX + 5, (int) m_posY + 20, Player.PLAYER_SIZE - 10, Player.PLAYER_SIZE - 20);
                break;
            case "teen":
                bounds = new Rectangle((int) m_posX + 2, (int) m_posY + 16, Player.PLAYER_SIZE - 6, Player.PLAYER_SIZE - 16);
                break;
            case "adult":
                bounds = new Rectangle((int) m_posX + 2, (int) m_posY + 8, Player.PLAYER_SIZE - 6, Player.PLAYER_SIZE - 8);
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
        return m_isJumping;
    }

    /**
     * 
     * @return 
     */
    public boolean isDead()
    {
        return m_isDead;
    }
    
    /**
     *
     * @return
     */
    public boolean isFalling()
    {
        return m_isFalling;
    }

    @Override
    public void renderHitbox(Graphics g)
    {
        Rectangle rect = this.getBounds();
        g.setColor(Color.BLUE);
        g.drawRect((int) rect.x, (int) rect.y, (int) rect.getWidth(), (int) rect.getHeight());
    }
    
    @Override
    public void die()
    {
        
    }

    /**
     * 
     * @return 
     */
    public boolean hasWon()
    {
        return m_win;
    }
    
    /**
     *
     * @return
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * 
     * @return 
     */
    public String getSpecies()
    {
        return m_species;
    }
    
    /**
     * 
     * @return 
     */
    public int getCheckpointX()
    {
        return m_checkpointX;
    }
    
    /**
     * 
     * @return 
     */
    public int getCheckpointY()
    {
        return m_checkpointY;
    }
    
    /**
     * 
     * @return 
     */
    public int getScore()
    {
        return m_score;
    }
    
    /**
     * 
     * @param lvl 
     */
    public void setLevel(Level lvl)
    {
        m_level = lvl;
    }
    
    /**
     * 
     * @param pos 
     */
    public void setCheckpointX(int pos)
    {
        m_checkpointX = pos;
    }
    
    /**
     * 
     * @param score 
     */
    public void setScore(int score)
    {
        m_score = score;
    }
      
    /**
     * 
     * @param win 
     */
    public void setWin(boolean win)
    {
        m_win = win;
    }
    
    /**
     *
     * @param name
     */
    public void setName(String name)
    {
        m_name = name;
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
                m_species = "panda";
                break;
            case Player.SPECIES_REDPANDA:
                m_species = "redpanda";
                break;
        }
    }

    /**
     * 
     * @param die 
     */
    public void die(boolean die)
    {
        m_isDead = die;
    }
    
    /**
     * 
     * @return 
     */
    public JSONObject toSave()
    {
        JSONObject data = new JSONObject();
        data.put("difficulty", m_difficulty);
        data.put("species", m_species.equals("panda") ? Player.SPECIES_PANDA : Player.SPECIES_REDPANDA);
        data.put("score", m_score);
        data.put("sex", m_sex.equals("boy") ? Player.SEX_BOY : Player.SEX_GIRL);
        data.put("name", m_name);
        JSONArray coords = new JSONArray();
        coords.add(m_posX);
        coords.add(m_posY);
        data.put("coords", coords);
        return data;
    }
}

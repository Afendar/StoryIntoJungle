package entity;

import java.awt.Graphics;
import java.util.ArrayList;

public class EntityManager{
    
    private Player m_player;
    
    private ArrayList<Entity> m_entities;
    
    public EntityManager(Player player)
    {
        m_player = player;
        m_entities = new ArrayList<>();
        addEntity(player);
    }
    
    public void update(double dt)
    {
        m_entities.forEach((e) ->
        {
            e.update(dt);
        });
    }
    
    public void render(Graphics g)
    {
        m_entities.forEach((e) ->
        {
            e.render(g, false);
        });
    }
    
    public void addEntity(Entity e)
    {
        if(e instanceof Player)
        {
            setPlayer((Player)e);
        }

        m_entities.add(e);
    }
    
    public Player getPlayer()
    {
        return m_player;
    }
    
    public void setPlayer(Player player)
    {
        m_player = player;
    }
    
    public ArrayList<Entity> getEntities()
    {
        return m_entities;
    }
    
    public void setEntities(ArrayList<Entity> entities)
    {
        m_entities = entities;
    }
}

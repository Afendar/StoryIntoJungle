package entity;

import entity.components.EntityComponentBase;
import entity.components.EntityComponentBase.ComponentType;
import entity.components.EntityComponentMovable;
import entity.components.EntityComponentPosition;
import entity.components.EntityComponentSoundEmitter;
import entity.components.EntityComponentSpritesheet;
import entity.components.EntityComponentState;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityManager{
    
    public class EntityComponentContainer extends ArrayList<EntityComponentBase>
    {
        public EntityComponentContainer()
        {
            super();
        }
    }
    
    public class EntityComponentData extends HashMap<Bitmask, EntityComponentContainer>
    {  
        public EntityComponentData()
        {
            super();
        }
        
        public EntityComponentData(Bitmask bitmask, EntityComponentContainer entityComponentContainer)
        {
            this.put(bitmask, entityComponentContainer);
        }
    }
    
    public class EntityContainer extends HashMap<Integer, EntityComponentData>
    {
        public EntityContainer()
        {
            super();
        }
    }
    
    public class EntityComponentFactory
    {
        public EntityComponentFactory()
        {
            
        }
        
        public EntityComponentBase getComponent(ComponentType type)
        {
            EntityComponentBase component = null;
            switch(type)
            {
                case POSITION:
                    component =  new EntityComponentPosition();
                    break;
                case STATE:
                    component =  new EntityComponentState();
                    break;
                case MOVABLE:
                    component =  new EntityComponentMovable();
                    break;
                case SOUNDEMITTER:
                    component =  new EntityComponentSoundEmitter();
                    break;
                case SPRITESHEETS:
                    component =  new EntityComponentSpritesheet();
                    break;
            }
            return component;
        }
    }
    
    private int m_counter;
    private EntityContainer m_entities;
    
    public EntityManager()
    {
        m_counter = 0;
        m_entities = new EntityContainer();
    }
    
    public void update(double dt)
    {
    }
    
    public void render(Graphics g)
    {
    }
    
    public int addEntity(Bitmask bitmask)
    {
        int idEntity = m_counter;
        if(m_entities.put(m_counter, new EntityComponentData(new Bitmask(0), new EntityComponentContainer())) != null)
        {
            return -1;
        }
        
        ++m_counter;
        
        for(int i = 0 ; i < EntityComponentBase.NB_COMPONENT_TYPES ; ++i)
        {
            if(bitmask.getBit(i))
            {
                addEntityComponent(idEntity, ComponentType.values()[i]);
            }
        }
        return idEntity;
    }
    
    public boolean removeEntity(int entityId)
    {
        return true;
    }
    
    public boolean addEntityComponent(int idEntity, ComponentType type)
    {
        System.out.println("idEntity: " + idEntity + " ComponentType: " + type);
        EntityComponentData ecd = m_entities.get(idEntity);
        if(ecd == null)
        {
            return false;
        }
        Map.Entry<Bitmask,EntityComponentContainer> entry = ecd.entrySet().iterator().next();
        if(entry.getKey().getBit(type.getTypeID()))
        {
            return false;
        }
        EntityComponentFactory fact = new EntityComponentFactory();
        EntityComponentBase component = fact.getComponent(type);
        System.out.println(component);
        
        return true;
    }
    
    public EntityComponentBase getComponent()
    {
        return null;
    }
    
    public boolean removeComponent()
    {
        return true;
    }
    
    public static void main(String[] args)
    {
        EntityManager em = new EntityManager();
        //0 0 0 0 1 1 1 0
        em.addEntity(new Bitmask(15));
    }
}

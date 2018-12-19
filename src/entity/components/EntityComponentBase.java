package entity.components;

public abstract class EntityComponentBase
{
    public static int NB_COMPONENT_TYPES = 32;
    
    public enum ComponentType
    {
        POSITION(0),
        STATE(1),
        MOVABLE(2),
        SOUNDEMITTER(3),
        SPRITESHEETS(4);
        
        private final int m_typeID;
        
        private ComponentType(int typeID)
        {
            m_typeID = typeID;
        }
        
        public int getTypeID()
        {
            return m_typeID;
        }
        
        @Override
        public String toString()
        {
            return Integer.toString(m_typeID);
        }
    }
    
    public enum ComponentSystem {
        RENDERER(0),
        MOVEMENT(1),
        COLLISION(2),
        CONTROL(3),
        STATE(4),
        SHEETANIMATION(5),
        SOUND(6);
        
        private final int m_systemID;
        
        private ComponentSystem(int systemID)
        {
            m_systemID = systemID;
        }
        
        public int getSystemID()
        {
            return m_systemID;
        }
        
        @Override
        public String toString()
        {
            return Integer.toString(m_systemID);
        }
    }
    
    public EntityComponentBase(ComponentType type)
    {
        
    }
}

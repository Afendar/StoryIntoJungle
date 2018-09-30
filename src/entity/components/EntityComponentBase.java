package entity.components;

public abstract class EntityComponentBase
{
    public enum ComponentType
    {
        POSITION(0),
        STATE(1),
        MOVABLE(2),
        SOUNDEMITTER(3);
        
        private final int m_type;
        
        private ComponentType(int type)
        {
            m_type = type;
        }
        
        @Override
        public String toString()
        {
            return Integer.toString(m_type);
        }
    }
    
    public EntityComponentBase(ComponentType type)
    {
        
    }
}

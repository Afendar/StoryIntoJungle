package entity.components;

public class EntityComponentState extends EntityComponentBase
{   
    public enum EntityState
    {
        IDLE(0, "Idle"),
        WALKING(1, "Walking"),
        RUNNING(2, "Running"),
        SWIMMING(3, "Swimming"),
        DYING(4, "Dying");
        
        private final int m_state;
        private final String m_stateLabel;
        
        private EntityState(int state, String stateLabel)
        {
            m_state = state;
            m_stateLabel = stateLabel;
        }
        
        @Override
        public String toString()
        {
            return m_stateLabel;
        }
    }

    private EntityState m_state;
    
    public EntityComponentState()
    {
        super(ComponentType.STATE);
    }
    
    public void setState(EntityState state)
    {
        m_state = state;
    }
    
    public EntityState getState()
    {
        return m_state;
    }
}

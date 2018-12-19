package entity;

public class Bitmask
{
    private int m_bits;
    
    public Bitmask()
    {
        m_bits = 0;
    }
    
    public Bitmask(int bitset)
    {
        m_bits = bitset;
    }
    
    public int getMask()
    {
        return m_bits;
    }
    
    public void setMask(int bits)
    {
        m_bits = bits;
    }
    
    public boolean matches(Bitmask bits, int relevant)
    {
        return (relevant > 0 ? ((bits.getMask() & relevant) == (m_bits & relevant)) : (bits.getMask() == m_bits));
    }
    
    public boolean getBit(int pos)
    {
        return ((m_bits & (1 << pos)) != 0);
    }
    
    public void turnOnBit(int pos)
    {
        m_bits |= 1 << pos;
    }
    
    public void turnOnBits(int bits)
    {
        m_bits |= bits;
    }
    
    public void clearBit(int pos)
    {
        m_bits &= ~(1 << pos);
    }
    
    public void toggleBit(int pos)
    {
        m_bits ^= 1 << pos;
    }
    
    public void clear()
    {
        m_bits = 0;
    }
    
    public static void main(String[] args)
    {
        // 0  0  0  0  0 0 1 0
        //128 64 32 16 8 4 2 1 ( 2 = 2 )
        Bitmask b = new Bitmask(2);
        // 0  0  0  0  1 0 1 0
        //128 64 32 16 8 4 2 1 ( 8 + 2  = 10)
        b.turnOnBit(3);
        System.out.println("mask: " + b.getMask());
        // 0  0  0  0  1 0 0 0
        //128 64 32 16 8 4 2 1 ( 8 = 8 )
        b.clearBit(1);
        System.out.println("mask: " + b.getMask());
        // 0  0  0  0  1 0 1 0
        //128 64 32 16 8 4 2 1 ( 8 + 2 = 10 )
        b.toggleBit(1);
        System.out.println("mask: " + b.getMask());
        // 0  1  0  0  1 0 1 0
        //128 64 32 16 8 4 2 1 ( 64 + 8 + 2 = 74 )
        b.toggleBit(6);
        System.out.println("mask: " + b.getMask());
        // 0  0  0  0  0 0 0 0
        //128 64 32 16 8 4 2 1 ( 0 = 0 )
        b.clear();
        System.out.println("mask: " + b.getMask());
    }
}

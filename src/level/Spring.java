package level;

public class Spring
{
    public float posx, posy;
	
    public int fix_y;

    public float speed;

    private final float k = 0.02f;

    private final float fric = 0.05f;

    public Spring(float x, int y)
    {
        posx = x;
        posy = y;
        fix_y = (int)y;
        speed = 0;
    }

    public void update()
    {
        float y = fix_y - posy;

        y *= k;

        if(speed > 0)
                y -= fric;
        else
                y += fric;


        speed += y;

        posy += speed;
    }
}

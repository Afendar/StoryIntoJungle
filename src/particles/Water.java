package particles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Water extends Particle
{
    public float x, y;
    public float velx, vely;
    public float orientation;
    private Image texture;
    private int width, height;

    public Water(float x, float y, float velx, float vely, float orientation)
    {
        super();
        
        this.x = x;
        this.y = y;
        this.velx = velx;
        this.vely = vely;
        this.orientation = orientation;

        try
        {
            BufferedImage t = ImageIO.read(this.getClass().getResource("/Droplet.png"));
            width = t.getWidth();
            height = t.getHeight();

            texture = t.getScaledInstance(width / 2, height / 2,
                        BufferedImage.TYPE_INT_ARGB);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics g)
    {
        g.setColor(Color.BLUE);

        Graphics2D g2 = (Graphics2D)g;

        AffineTransform at = AffineTransform.getTranslateInstance(x, y);

        at.rotate(orientation - Math.PI / 2,
                        width / 4, height / 4);

        g2.drawImage(texture, at, null);
    }
}

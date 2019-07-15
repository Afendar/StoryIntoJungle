package level.tiles;

import java.awt.Graphics;
import java.util.ArrayList;
import core.Defines;
import java.awt.image.BufferedImage;
import level.Level;
import particles.Leaf;

/**
 * Tree class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Tree extends Tile{
    
    public static final int TALL = 1;
    public static final int SMALL = 2;
    
    public int size;
    public ArrayList<Leaf> leaves = new ArrayList<Leaf>(3);
    
    /**
     * 
     * @param tileset
     * @param size
     * @param imgX
     * @param imgY 
     */
    public Tree(BufferedImage tileset, int size, int imgX, int imgY){
        super(tileset, imgX, imgY, (size == Tree.TALL) ? 12 : 13);
        this.size = size;
        if(size == Tree.TALL)
            m_tile = m_tileset.getSubimage(4 * Defines.TILE_SIZE, 0, 5 * Defines.TILE_SIZE, 4 * Defines.TILE_SIZE);
        else
            m_tile = m_tileset.getSubimage(1 * Defines.TILE_SIZE, 4 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE, 2 * Defines.TILE_SIZE);
    
        for(int i=0;i<3;i++){
            this.leaves.add(new Leaf(5, 0, 0, 800, 600));
        }
    }
    
    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        for(int i=0; i< this.leaves.size(); i++){
            Leaf leaf = this.leaves.get(i);
            if(!leaf.isGenStartX())
            {
                leaf.genRandStartX();
            }
            leaf.update(dt);
        }
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        this.render(g, x, y, Tree.TALL);
    }
    
    /**
     * 
     * @param g
     * @param x
     * @param y
     * @param size 
     */
    public void render(Graphics g, int x, int y, int size){
        switch(size){
            case Tree.SMALL:
                g.drawImage(m_tile, x * Defines.TILE_SIZE, (y * Defines.TILE_SIZE) - Defines.TILE_SIZE, null);
                break;
            default:
                g.drawImage(m_tile, (x * Defines.TILE_SIZE) - Defines.TILE_SIZE - 5, (y * Defines.TILE_SIZE) - (3 * Defines.TILE_SIZE - 1 ), null);
                break;
        }
    }
}

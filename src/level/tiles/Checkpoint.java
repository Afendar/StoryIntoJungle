package level.tiles;

public class Checkpoint extends Tile{
    
    public Checkpoint(int imgX, int imgY){
        super(imgX, imgY, 8);
    }
    
    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update() {
        
    }
}

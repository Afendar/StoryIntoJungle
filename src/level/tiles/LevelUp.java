package level.tiles;

public class LevelUp extends Tile{

    public LevelUp(int imgX, int imgY){
        super(imgX, imgY, 7);
    }
    
    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update() {
        
    }
    
}

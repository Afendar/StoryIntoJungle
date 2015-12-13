package level.tiles;

public class Bridge extends Tile {
    
    public Bridge(int imgX, int imgY){
        super(imgX, imgY, 3);
    }

    @Override
    public boolean canPass() {
        return false;
    }

    @Override
    public void update() {
        
    }
    
}

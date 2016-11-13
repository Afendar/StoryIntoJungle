package level.tiles;

public class Pious extends Tile {
    
    public Pious(int imgX, int imgY){
        super(imgX, imgY, 6);
    }

    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update(double dt) {
        
    }
    
}

package level.tiles;

public class Leaves extends Tile {
    
    public Leaves(int imgX, int imgY){
        super(imgX, imgY, 5);
        this.bonus = 100;
    }

    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update() {
        
    }
    
}

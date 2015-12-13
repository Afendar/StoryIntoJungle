package level.tiles;

public class Apple extends Tile {
    
    public Apple(int imgX, int imgY){
        super(imgX, imgY, 4);
        this.bonus = 10;
    }

    @Override
    public boolean canPass() {
        return true;
    }

    @Override
    public void update() {
        
    }
}

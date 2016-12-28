package level.tiles;

import level.Level;

public class Sand extends Tile {
    
    public boolean startBreak;
    public double dt;
    
    private boolean isBroken;
    
    public Sand(int imgX, int imgY){
        super(imgX, imgY, 9);
        this.startBreak = false;
        this.dt = 0;
        this.isBroken = false;
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        return level.getData(x, y) == 2;
    }
    
    public boolean isBroken(){
        return this.isBroken;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
    }
}
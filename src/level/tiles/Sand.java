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
        if(level.getData(x, y) == 2)
            return true;
        return false;
    }
    
    public boolean isBroken(){
        return this.isBroken;
    }
    
    public void startBreak(){
        this.startBreak = true;
    }
    
    public void update(Level level, int x, int y, double dt){
        
        if(level.getData(x, y) == 1){
            this.dt += dt;
            if(this.dt % 2 > 1.5)
            {
                
            }
            
            if(this.dt > 70)
            {
                this.isBroken = true;
                level.setData(x, y, 2);
            }
        }
    }
}

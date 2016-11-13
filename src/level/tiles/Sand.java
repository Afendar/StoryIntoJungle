package level.tiles;

public class Sand extends Tile {
    
    public boolean startBreak;
    public double dt;
    
    public Sand(int imgX, int imgY){
        super(imgX, imgY, 9);
        this.startBreak = false;
        this.dt = 0;
    }
    
    @Override
    public boolean canPass(){
        return false;
    }
    
    public void startBreak()
    {
        this.startBreak = true;
    }
    
    public void update(double dt){
        
        if(this.startBreak){
            this.dt += dt;
            if(this.dt % 2 > 1.5)
            {
                
            }
        }
    }
}
